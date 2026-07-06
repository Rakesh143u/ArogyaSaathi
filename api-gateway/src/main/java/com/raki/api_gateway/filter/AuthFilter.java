package com.raki.api_gateway.filter;


import com.raki.api_gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;



@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    @Autowired
    private JwtUtil jwtUtil;

    public AuthFilter(){
        super(Config.class);
    }
    public static class Config{}

    @Override
    public GatewayFilter apply(Config config){
        return (exchange,chain)->{
            if(exchange.getRequest().getURI().getPath().contains("/auth")||exchange.getRequest().getURI().getPath().contains("/users/register")){
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if(authHeader==null||!authHeader.startsWith("Bearer ")){
                throw new RuntimeException("Missing or Invalid Authorization header");
            }

            String token = authHeader.substring(7);
            try{
                jwtUtil.validateToken(token);
                System.out.println("HEADER: " + authHeader);
                System.out.println("TOKEN: " + token);
            }catch (Exception e){
                System.out.println("HEADER: " + authHeader);
                System.out.println("TOKEN: " + token);
                throw  new RuntimeException("Invalid Token");
            }
            Claims claims  = jwtUtil.getClaims(token);
            Long userId = claims.get("userId",Long.class);
            String email = claims.getSubject();
            String role = claims.get("role", String.class);
            String path = exchange.getRequest().getURI().getPath();
            if((path.contains("/avail")||path.contains("/appointments/doctor")||path.contains("/appointments/confirm"))&&!role.equals("DOCTOR")){
                throw new RuntimeException("Access Denied,Only Doctor");
            }
            if((path.contains("/appointments/book")||path.contains("/appointments/my"))&&!role.equals("PATIENT")){
                throw new RuntimeException("Access Denied,Only PATIENT");
            }
            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header("X-User-Id",String.valueOf(userId))
                    .header("X-User-Email",email)
                    .header("X-User-Role",role).build();

            System.out.println("HEADER: " + authHeader);
            System.out.println("TOKEN: " + token);
            return chain.filter(exchange.mutate().request(request).build());
        };
    }


}
