package tw.niq.example.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
public class UserDto implements Serializable {

	private static final long serialVersionUID = 2315694471760063305L;

	private Long id;
	
	private String username;
	
	private String password;
	
	private Boolean enabled = true;
	
	private Boolean accountNonExpired;
	
	private Boolean credentialsNonExpired;

	private Boolean accountNonLocked;
	
	Collection<? extends GrantedAuthority> authorities = new HashSet<>();
	
}
