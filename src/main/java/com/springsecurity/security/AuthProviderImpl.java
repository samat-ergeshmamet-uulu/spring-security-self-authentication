package com.springsecurity.security;

import com.springsecurity.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AuthProviderImpl implements AuthenticationProvider {
    private final PersonDetailsService personDetailsService;

    @Autowired
    public AuthProviderImpl(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
       //Получили имя пользователя
        String username = authentication.getName();

        //Передаем username которое пришло из формы
        UserDetails personDetails = personDetailsService.loadUserByUsername(username);

        //Получаем пароль из формы, в getCredentials ведется пароль
        String password = authentication.getCredentials().toString();

        if (!password.equals(personDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect password!!!");
        }                                              //personDetail это principal
        return new UsernamePasswordAuthenticationToken(personDetails, password, Collections.emptyList());
        // Этот возвращаемый объект будем помещен в сессию
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
