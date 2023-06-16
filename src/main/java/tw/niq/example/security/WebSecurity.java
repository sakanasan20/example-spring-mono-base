package tw.niq.example.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import tw.niq.example.controller.HomeController;

@Configuration
@EnableWebSecurity
public class WebSecurity {

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
		http.authenticationManager(authenticationManager);
		http.authorizeHttpRequests((authorize) -> authorize
			.requestMatchers(PathRequest.toH2Console()).permitAll()
			.requestMatchers("/webjars/**").permitAll()
			.requestMatchers(HttpMethod.GET, HomeController.REQUEST_PATH).permitAll()
			.anyRequest().authenticated());
		return http.build();
	}
	
}
