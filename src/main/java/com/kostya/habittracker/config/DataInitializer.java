package com.kostya.habittracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.kostya.habittracker.entity.User;
import com.kostya.habittracker.repository.UserRepository;

@Configuration
public class DataInitializer {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner init() {
		return args -> {
			if (userRepository.findByUsername("testuser") == null) {
				User user = new User();
				user.setUsername("testuser");
				user.setPassword(passwordEncoder.encode("testpassword"));
				userRepository.save(user);
			}
		};
	}
}
