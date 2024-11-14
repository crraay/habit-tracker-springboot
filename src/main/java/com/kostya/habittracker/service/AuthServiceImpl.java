package com.kostya.habittracker.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kostya.habittracker.config.PasswordEncoderComponent;
import com.kostya.habittracker.entity.User;
import com.kostya.habittracker.exception.UnauthorizedException;
import com.kostya.habittracker.model.LoginRequest;
import com.kostya.habittracker.model.LoginResponse;
import com.kostya.habittracker.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoderComponent passwordEncoder;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.token.expires.in}")
    private Integer TOKEN_EXPIRES_IN;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = this.userRepository.findByUsername(loginRequest.getUsername());
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid username or password"); 
        }
      
        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + this.TOKEN_EXPIRES_IN))
                .signWith(SignatureAlgorithm.HS256, this.SECRET_KEY)
                .compact();
        
        return new LoginResponse(token, this.TOKEN_EXPIRES_IN);
    }
    
}
