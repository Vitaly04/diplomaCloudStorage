package ru.netology.cloudstorage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloudstorage.exception.ErrorBadCredentialException;
import ru.netology.cloudstorage.models.User;
import ru.netology.cloudstorage.security.JwtTokenUtil;
import ru.netology.cloudstorage.service.JwtUserDetailsService;

import java.util.Collections;
import java.util.Map;

@RestController
@CrossOrigin(origins = "https://jd-homeworks.vercel.app/")
//@CrossOrigin(origins = "http://localhost:8081/")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping(value = "/login")
    public Map<String, Object> createAuthenticationToken(@RequestBody User user) throws Exception {
        authenticate(user.getLogin(), user.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(user.getLogin());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return Collections.singletonMap("auth-token", token);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> saveUser(@RequestBody User user) throws Exception {
        return ResponseEntity.ok(userDetailsService.save(user));
    }

    private void authenticate(String login, String password) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new ErrorBadCredentialException("INVALID_CREDENTIALS");
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ErrorBadCredentialException.class)
    public String errorBadCredentialExceptionHandle(ErrorBadCredentialException e) {
        return e.getMessage() + e.getId();
    }

}
