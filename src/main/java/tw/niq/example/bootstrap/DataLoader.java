package tw.niq.example.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import tw.niq.example.entity.UserEntity;
import tw.niq.example.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

	private final UserRepository userRepository;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public DataLoader(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Transactional
	@Override
	public void run(String... args) throws Exception {
		createAdmin();
	}

	private void createAdmin() {
		if (!userRepository.findByUsername("admin").isPresent()) {
			userRepository.save(UserEntity.builder().username("admin").password(bCryptPasswordEncoder.encode("admin")).build());
		}
	}

}
