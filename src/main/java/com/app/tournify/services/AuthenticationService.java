package com.app.tournify.services;

import com.app.tournify.database.entities.User;
import com.app.tournify.database.repositories.UserRepository;
import com.app.tournify.dtos.UserDto;
import com.app.tournify.exceptions.errors.BadRequestException;
import com.app.tournify.exceptions.errors.ConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> register(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.email())) {
            throw new ConflictException("Email already exists");
        }

        if (!userDto.password().equals(userDto.confirmPassword())) {
            throw new BadRequestException("Password and confirmation do not match");
        }

        var encryptedPassword = passwordEncoder.encode(userDto.password());
        var user = new User(userDto, encryptedPassword);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }
}
