package com.raki.a_user_service.service;

import com.raki.a_user_service.model.User;
import com.raki.a_user_service.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    public ResponseEntity<User> register(User user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userRepository.save(user);
        return new ResponseEntity<>(user,HttpStatus.CREATED);
    }


    public ResponseEntity<User> login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();
        if(!user.getPassword().equals(password)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(user,HttpStatus.OK);
    }
}
