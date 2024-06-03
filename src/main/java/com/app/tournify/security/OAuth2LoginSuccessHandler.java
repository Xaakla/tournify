package com.app.tournify.security;

import com.app.tournify.database.entities.User;
import com.app.tournify.database.repositories.UserRepository;
import com.app.tournify.dtos.UserDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final DbUserDetailsService dbUserDetailsService;

    public OAuth2LoginSuccessHandler(UserRepository userRepository, DbUserDetailsService dbUserDetailsService) {
        this.userRepository = userRepository;
        this.dbUserDetailsService = dbUserDetailsService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String googleName = oAuth2User.getAttribute("name");
        String googleEmail = oAuth2User.getAttribute("email");

        UserDto userDto = new UserDto(googleName, googleEmail, "", "");
        User userToCheck = new User(userDto, "");

        DbUserDetails registeredUser = null;

        try {
            registeredUser = (DbUserDetails) dbUserDetailsService.loadUserByUsername(googleEmail);
        } catch (UsernameNotFoundException ex) {
            userRepository.save(userToCheck);
        }
        response.sendRedirect("/home");
    }
}