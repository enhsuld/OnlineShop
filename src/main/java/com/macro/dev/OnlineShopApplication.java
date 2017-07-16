package com.macro.dev;

import com.macro.dev.config.CustomUserDetails;
import com.macro.dev.models.LutRole;
import com.macro.dev.models.LutUser;
import com.macro.dev.repositories.UserRepository;
import com.macro.dev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
@EntityScan(basePackageClasses = {OnlineShopApplication.class, Jsr310JpaConverters.class})
public class OnlineShopApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(OnlineShopApplication.class, args);
	}

	/**
	 * Password grants are switched on by injecting an AuthenticationManager.
	 * Here, we setup the builder so that the userDetailsService is the one we coded.
	 * @param builder
	 * @param repository
	 * @throws Exception
	 */
	@Autowired
	public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repository, UserService userService) throws Exception {
		if (repository.count()==0)
			userService.save(new LutUser("admin", "adminPassword", Arrays.asList(new LutRole("USER"), new LutRole("ACTUATOR") , new LutRole("ADMIN"))));
		builder.userDetailsService(userDetailsService(repository)).passwordEncoder(passwordEncoder);
	}

	/**
	 * We return an istance of our CustomUserDetails.
	 * @param repository
	 * @return
	 */
	private UserDetailsService userDetailsService(final UserRepository repository) {
		return username -> new CustomUserDetails(repository.findByUsername(username));
	}
}
