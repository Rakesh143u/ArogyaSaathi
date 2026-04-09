package com.raki.a_user_service.controller;


import com.raki.a_user_service.dto.LoginRequest;
import com.raki.a_user_service.model.User;
import com.raki.a_user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> userRegister(@RequestBody User user){
        return userService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest request){
        return userService.login(request.getEmail(),request.getPassword());
    }
}
