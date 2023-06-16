package tw.niq.example.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import tw.niq.example.dto.UserDto;

public interface UserService extends UserDetailsService {
	
	UserDto findByUsername(String username);
	
}
