package com.raki.api_gateway;


import com.raki.api_gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String,String> login(@RequestBody Map<String,String> request){
        String username = request.get("username");
        String password = request.get("password");
        if("rakesh".equals(username)&&"1234".equals(password)){
            String token = jwtUtil.generateToken(username);
            return Map.of("token",token);
        }
        throw new RuntimeException("Invalid Credentials");
    }
}
