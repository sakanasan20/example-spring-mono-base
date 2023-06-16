package tw.niq.example.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tw.niq.example.dto.UserDto;
import tw.niq.example.entity.UserEntity;
import tw.niq.example.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	
	private final ModelMapper modelMapper;

	public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDto userDto = findByUsername(username);
		return new User(
				userDto.getUsername(), 
				userDto.getPassword(), 
				userDto.getEnabled(), 
				userDto.getAccountNonExpired(), 
				userDto.getCredentialsNonExpired(), 
				userDto.getAccountNonLocked(), 
				userDto.getAuthorities());
	}

	@Override
	public UserDto findByUsername(String username) {
		UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
		UserDto userDto = modelMapper.map(userEntity, UserDto.class);
		return userDto;
	}
	
}
