package com.raki.api_gateway;


import com.raki.api_gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private WebClient.Builder webClientBuilder;
    @PostMapping("/login")
    public Mono<Map<String,String>> login(@RequestBody Map<String,String> request){
        String email = request.get("email");
        String password = request.get("password");
        if(email==null||password==null){
            throw new RuntimeException("Email or Password Missing");
        }
        return webClientBuilder.build()
                .post()
                .uri("lb://A-USER-SERVICE/users/login")
                .bodyValue(Map.of("email",email,"password",password))
                .retrieve()
                .bodyToMono(LoginResponse.class)
                .map(user ->{
                    String token = jwtUtil.generateToken(user.getEmail());
                    return Map.of("token",token);
                });
    }
}
