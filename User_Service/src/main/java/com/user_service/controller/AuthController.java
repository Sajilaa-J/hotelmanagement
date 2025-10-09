package com.user_service.controller;
//
//import com.user_service.dto.AuthRequest;
//import com.user_service.dto.AuthResponse;
//import com.shared_persistence.entity.User;
//import com.user_service.dto.LoginRequest;
//import com.user_service.repo.UserRepository;
//import com.user_service.security.JwtUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtUtil jwtUtil;
//
//    @PostMapping("/register")
//    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
//        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
//            return ResponseEntity.badRequest().body(new AuthResponse("Email already exists"));
//
//        }
//
//        User user = new User();
//        user.setName(request.getName());
//        user.setEmail(request.getEmail());
//        user.setPhone(request.getPhone());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setRole("ADMIN".equalsIgnoreCase(request.getRole()) ? "ADMIN" : "USER");
//
//        userRepository.save(user);
//
//        String token = jwtUtil.generateToken(user);
//        return ResponseEntity.ok(new AuthResponse(token, "User registered successfully"));
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            return ResponseEntity.badRequest().body(new AuthResponse("Invalid password"));
//        }
//
//        String token = jwtUtil.generateToken(user);
//        return ResponseEntity.ok(new AuthResponse(token, "Login successful"));
//    }
//}

import com.shared_persistence.entity.User;
import com.user_service.dto.AuthRequest;
import com.user_service.dto.AuthResponse;
import com.user_service.dto.LoginRequest;
import com.user_service.repo.UserRepository;
import com.user_service.security.JwtUtil;
import com.user_service.assembler.UserModelAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserModelAssembler userModelAssembler;

    @PostMapping("/register")
    public ResponseEntity<EntityModel<AuthResponse>> register(@RequestBody AuthRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            AuthResponse response = new AuthResponse("Email already exists");
            return ResponseEntity.badRequest().body(userModelAssembler.toModel(response));
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ADMIN".equalsIgnoreCase(request.getRole()) ? "ADMIN" : "USER");
        userRepository.save(user);

        String token = jwtUtil.generateToken(user);
        AuthResponse response = new AuthResponse(token, "User registered successfully");

        return ResponseEntity.ok(userModelAssembler.toModel(response));
    }

    @PostMapping("/login")
    public ResponseEntity<EntityModel<AuthResponse>> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            AuthResponse response = new AuthResponse("Invalid password");
            return ResponseEntity.badRequest().body(userModelAssembler.toModel(response));
        }

        String token = jwtUtil.generateToken(user);
        AuthResponse response = new AuthResponse(token, "Login successful");

        return ResponseEntity.ok(userModelAssembler.toModel(response));
    }
}
