package com.example.ecommerce.service;

import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private  UserRepository userRepository;

    public User registerUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public String login(Map<String, String>srcMap){
        String userName = srcMap.get("username");
        User user = null;
        if(userName != null){
            user = findByUsername(userName);
        }
        if(user!=null && Objects.equals(user.getPassword(), srcMap.get("password"))){
            return "login successfull";
        }
        return "wrong username and password";
    }

    public User findByUsername(String username) {
        Optional<User> optionalUser= userRepository.findByUsername(username);
        if(optionalUser.isPresent()){
            return optionalUser.get();
        }
        return new User();
    }
}
