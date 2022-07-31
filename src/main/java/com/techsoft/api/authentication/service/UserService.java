package com.techsoft.api.authentication.service;

import com.techsoft.api.authentication.domain.ApplicationUser;
import com.techsoft.api.authentication.repository.UserRepository;
import com.techsoft.api.common.AbstractService;
import com.techsoft.api.common.error.HttpResponseException;
import com.techsoft.api.config.jwt.JwtRequest;
import com.techsoft.api.config.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;

@Service
public class UserService extends AbstractService<ApplicationUser, Object> implements UserDetailsService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Lazy
    @Autowired
    UserService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        super(userRepository, ApplicationUser.class);

        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getRepository().findByUsername(username);
    }

    UserRepository getRepository() {
        return (UserRepository) repository;
    }


    public ApplicationUser signIn(JwtRequest authenticationRequest) {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final ApplicationUser userDetails = getRepository().findByUsername(authenticationRequest.getUsername());

        userDetails.setToken(jwtTokenUtil.generateToken(userDetails));

        return userDetails;
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new HttpResponseException(HttpStatus.UNAUTHORIZED, "User disabled!");
        } catch (BadCredentialsException e) {
            throw new HttpResponseException(HttpStatus.UNAUTHORIZED, "Credentials is incorrect!");
        }
    }
}
