package tw.niq.example.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import tw.niq.example.controller.HomeController;
import tw.niq.example.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurity {
	
	private final UserService userService;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		
		authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
		
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
		
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, userService);
		
		authenticationFilter.setFilterProcessesUrl("/login");
		
		http.authenticationManager(authenticationManager);
		
		http.authorizeHttpRequests((authorize) -> authorize
			.requestMatchers(PathRequest.toH2Console()).permitAll()
			.requestMatchers("/login", "/logout").permitAll()
			.requestMatchers("/webjars/**").permitAll()
			.requestMatchers(HttpMethod.GET, HomeController.REQUEST_PATH).permitAll()
			.anyRequest().authenticated())
			.csrf((csrf) -> csrf.disable())
			.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.disable()))
			.addFilter(authenticationFilter)
			.logout((logout) -> logout.logoutUrl("/logout").logoutSuccessUrl("/home"));
		
		return http.build();
	}
	
}
