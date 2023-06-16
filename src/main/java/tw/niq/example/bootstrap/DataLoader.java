package tw.niq.example.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import tw.niq.example.entity.UserEntity;
import tw.niq.example.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

	private final UserRepository userRepository;
	
	public DataLoader(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional
	@Override
	public void run(String... args) throws Exception {
		createAdmin();
	}

	private void createAdmin() {
		if (!userRepository.findByUsername("admin").isPresent()) {
			userRepository.save(UserEntity.builder().username("admin").password("admin").build());
		}
	}

}
