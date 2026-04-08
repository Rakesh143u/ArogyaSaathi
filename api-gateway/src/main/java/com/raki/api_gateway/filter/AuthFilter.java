package com.raki.api_gateway.filter;


import com.raki.api_gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
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
            if(exchange.getRequest().getURI().getPath().contains("/auth")){
                return chain.filter(exchange);
            }
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if(authHeader==null||!authHeader.startsWith("Bearer ")){
                throw new RuntimeException("Missing or Invalid Authorization header");
            }
            String token = authHeader.substring(7);
            try{
                jwtUtil.validateToken(token);
            }catch (Exception e){
                throw  new RuntimeException("Invalid Token");
            }
            return chain.filter(exchange);
        };
    }


}
