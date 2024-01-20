package com.project.ecommerce;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.project.ecommerce.entities.ERole;
import com.project.ecommerce.entities.Role;
import com.project.ecommerce.repositories.RoleRepository;

@SpringBootApplication
public class EcommerceApplication implements CommandLineRunner{
	@Autowired
	private RoleRepository roleRepository;
	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			Role adminRole = new Role();
			adminRole.setId(101);
			adminRole.setName(ERole.ROLE_ADMIN);

			Role userRole = new Role();
			userRole.setId(102);
			userRole.setName(ERole.ROLE_USER);

			List<Role> roles = List.of(adminRole, userRole);

			List<Role> savedRoles = roleRepository.saveAll(roles);

			savedRoles.forEach(System.out::println);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
