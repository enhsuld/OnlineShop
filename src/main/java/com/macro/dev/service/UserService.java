package com.macro.dev.service;

import com.macro.dev.models.LutUser;
import com.macro.dev.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public void save(LutUser user){
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    public LutUser getUser(String username){
        return userRepository.findByUsername(username);
    }

    public List<LutUser> getAllUsers() {
        return userRepository.findAll();
    }
}
