package ru.netology.cloudstorage.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ru.netology.cloudstorage.entity.UserEntity;
import ru.netology.cloudstorage.models.User;
import ru.netology.cloudstorage.repository.UserRepository;


@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByLogin(login);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with login: " + login);
        }
        return new org.springframework.security.core.userdetails.User(userEntity.getLogin(), userEntity.getPassword(),
                new ArrayList<>());
    }

    public UserEntity save(User user) {
        UserEntity newUser = new UserEntity();
        newUser.setLogin(user.getLogin());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));

        return userRepository.save(newUser);
    }
}
