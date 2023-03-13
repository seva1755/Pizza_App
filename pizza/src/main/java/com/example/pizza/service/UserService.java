package com.example.pizza.service;

import com.example.pizza.entity.User;
import com.example.pizza.repository.RoleRepository;
import com.example.pizza.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {userRepository.deleteById(id);}

    public User updateUser(User user) {return userRepository.save(user);}


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not exist!");
        }
        return user.get();
    }
}

