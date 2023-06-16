package tw.niq.example.security;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tw.niq.example.dto.UserDto;
import tw.niq.example.model.LoginModel;
import tw.niq.example.service.UserService;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final UserService userService;

	public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService) {
		super(authenticationManager);
		this.userService = userService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		try {

			LoginModel loginModel = new ObjectMapper().readValue(request.getInputStream(), LoginModel.class);
			
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
					new UsernamePasswordAuthenticationToken(loginModel.getUsername(), loginModel.getPassword(), new ArrayList<>());

			return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		String username = ((User) authResult.getPrincipal()).getUsername();
		
		UserDto userDto = userService.findByUsername(username);
		
		Instant now = Instant.now();
		
		String tokenSecret = "secretsecretsecretsecretsecretsecretsecretsecretsecretsecret";
		
		byte[] tokenSecretBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
		
		SecretKey secretKey = new SecretKeySpec(tokenSecretBytes, SignatureAlgorithm.HS512.getJcaName());
		
		String expirationTime = "86400000";
		
		String token = Jwts.builder()
			.setSubject(userDto.getUsername())
			.setExpiration(Date.from(now.plusMillis(Long.parseLong(expirationTime))))
			.setIssuedAt(Date.from(now))
			.signWith(secretKey, SignatureAlgorithm.HS512)
			.compact();
		
		response.addHeader("token", token);
		response.addHeader("username", userDto.getUsername());
	}
	
}
