package com.kostya.habittracker.service.impl;

import java.security.Key;
import java.util.Date;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kostya.habittracker.config.PasswordEncoderComponent;
import com.kostya.habittracker.dto.LoginRequest;
import com.kostya.habittracker.dto.LoginResponse;
import com.kostya.habittracker.entity.User;
import com.kostya.habittracker.enums.UserRole;
import com.kostya.habittracker.enums.UserStatus;
import com.kostya.habittracker.exception.UnauthorizedException;
import com.kostya.habittracker.repository.UserRepository;
import com.kostya.habittracker.service.AuthService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthServiceImpl implements AuthService {

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
        if (user == null) {
            throw new UnauthorizedException("Invalid username or password");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            userRepository.save(user);
            throw new UnauthorizedException("Invalid username or password"); 
        }
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UnauthorizedException("User is not active");
        }
        if (user.getRole() != UserRole.USER) {
            throw new UnauthorizedException("User has incorrect role");
        }

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
      
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.SECRET_KEY)); 
        String token = Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + this.TOKEN_EXPIRES_IN))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        
        return LoginResponse.builder()
            .token(token)
            .tokenExpiresIn(this.TOKEN_EXPIRES_IN)
            .build();
    }
    
}
