package com.user_service.controller;
//
//import com.shared_persistence.entity.User;
//import com.user_service.dto.AuthRequest;
//import com.user_service.dto.AuthResponse;
//import com.user_service.dto.LoginRequest;
//import com.user_service.repo.UserRepository;
//import com.user_service.security.JwtUtil;
//import com.user_service.assembler.UserModelAssembler;
//import lombok.RequiredArgsConstructor;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtUtil jwtUtil;
//    private final UserModelAssembler userModelAssembler;
//
//    @PostMapping("/register")
//    public ResponseEntity<EntityModel<AuthResponse>> register(@RequestBody AuthRequest request) {
//        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
//            AuthResponse response = new AuthResponse("Email already exists");
//            return ResponseEntity.badRequest().body(userModelAssembler.toModel(response));
//        }
//
//        User user = new User();
//        user.setName(request.getName());
//        user.setEmail(request.getEmail());
//        user.setPhone(request.getPhone());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setRole("ADMIN".equalsIgnoreCase(request.getRole()) ? "ADMIN" : "USER");
//        userRepository.save(user);
//
//        String token = jwtUtil.generateToken(user);
//        AuthResponse response = new AuthResponse(token, "User registered successfully");
//
//        return ResponseEntity.ok(userModelAssembler.toModel(response));
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<EntityModel<AuthResponse>> login(@RequestBody LoginRequest request) {
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            AuthResponse response = new AuthResponse("Invalid password");
//            return ResponseEntity.badRequest().body(userModelAssembler.toModel(response));
//        }
//
//        String token = jwtUtil.generateToken(user);
//        AuthResponse response = new AuthResponse(token, "Login successful");
//
//        return ResponseEntity.ok(userModelAssembler.toModel(response));
//    }
//
//
//
//}


import com.shared_persistence.entity.User;
import com.user_service.dto.*;
import com.user_service.kafka.UserProducer;
import com.user_service.repo.UserRepository;
import com.user_service.security.JwtUtil;
import com.user_service.assembler.UserModelAssembler;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;



import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserModelAssembler userModelAssembler;
    private final JavaMailSender mailSender;
    private final UserProducer userProducer;


    private final Map<String, String> otpStorage = new HashMap<>();

    @PostMapping("/register")
    public ResponseEntity<EntityModel<AuthResponse>> register(@Valid @RequestBody AuthRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            AuthResponse response = new AuthResponse("Email already exists");
            return ResponseEntity.badRequest().body(userModelAssembler.toModel(response));
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        //user.setRole("ADMIN".equalsIgnoreCase(request.getRole()) ? "ADMIN" : "USER");

        String role = request.getRole();
        if (!"ADMIN".equalsIgnoreCase(role) && !"USER".equalsIgnoreCase(role)) {
            throw new IllegalArgumentException("Role must be either ADMIN or USER");
        }
        user.setRole(role.toUpperCase());

        userRepository.save(user);

        userProducer.sendUserCreatedEvent(user.getName(), user.getEmail());

        //return ResponseEntity.ok("✅ User created: " + user.getName() + " (" + user.getEmail() + ")");
        String token = jwtUtil.generateToken(user);
        AuthResponse response = new AuthResponse(token, "User registered successfully");
        return ResponseEntity.ok(userModelAssembler.toModel(response));
    }

    @PostMapping("/login")
    public ResponseEntity<EntityModel<AuthResponse>> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            AuthResponse response = new AuthResponse("Wrong password");
            return ResponseEntity.badRequest().body(userModelAssembler.toModel(response));
        }

        String token = jwtUtil.generateToken(user);
        AuthResponse response = new AuthResponse(token, "Login successful");
        return ResponseEntity.ok(userModelAssembler.toModel(response));
    }

    @PutMapping("/update-email")
    public ResponseEntity<EntityModel<AuthResponse>> updateEmail(
            @RequestBody UpdateUserRequest request) {

        Optional<User> optionalUser = userRepository.findByEmail(request.getOldEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(userModelAssembler.toModel(new AuthResponse("User not found")));
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest()
                    .body(userModelAssembler.toModel(new AuthResponse("Wrong password")));
        }

        if (request.getNewEmail() != null && !request.getNewEmail().isEmpty()) {
            if (userRepository.findByEmail(request.getNewEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(userModelAssembler.toModel(new AuthResponse("Email already exists")));
            }
            user.setEmail(request.getNewEmail());
        }

        userRepository.save(user);

        return ResponseEntity.ok(userModelAssembler.toModel(
                new AuthResponse("Email updated successfully")));
    }



    @PutMapping("/change-password")
    public ResponseEntity<EntityModel<AuthResponse>> changePassword(@RequestBody ChangePasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            AuthResponse response = new AuthResponse("Email not found");
            return ResponseEntity.badRequest().body(userModelAssembler.toModel(response));
        }

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            AuthResponse response = new AuthResponse("Current password is incorrect");
            return ResponseEntity.badRequest().body(userModelAssembler.toModel(response));
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        AuthResponse response = new AuthResponse("Password changed successfully");
        return ResponseEntity.ok(userModelAssembler.toModel(response));
    }
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<EntityModel<AuthResponse>> deleteUser(@PathVariable Long userId) {

        Optional<User> userToDelete = userRepository.findById(userId);
        if (userToDelete.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(userModelAssembler.toModel(new AuthResponse("User not found")));
        }

        userRepository.delete(userToDelete.get());
        return ResponseEntity.ok(userModelAssembler.toModel(
                new AuthResponse("User deleted successfully")));
    }



    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody PasswordResetRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }


        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
        otpStorage.put(request.getEmail(), otp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("Password Reset OTP");
        message.setText("Your OTP to reset password is: " + otp);
        mailSender.send(message);

        return ResponseEntity.ok("OTP sent to email");
    }

//    @PutMapping("/reset-password")
//    public ResponseEntity<?> resetPassword(@RequestBody PasswordUpdateRequest request) {
//        if (!otpStorage.containsKey(request.getEmail()) ||
//                !otpStorage.get(request.getEmail()).equals(request.getOtp())) {
//            return ResponseEntity.badRequest().body("Invalid OTP");
//        }
//
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
//        userRepository.save(user);
//
//        otpStorage.remove(request.getEmail());
//        return ResponseEntity.ok("Password updated successfully");
//    }

//    @PutMapping("/reset-password")
//    @Transactional
//    public ResponseEntity<?> resetPassword(@RequestBody PasswordUpdateRequest request) {
//        try {
//            // 1️⃣ Validate OTP
//            String storedOtp = otpStorage.get(request.getEmail());
//            if (storedOtp == null || !storedOtp.equals(request.getOtp())) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body(Map.of("error", "Invalid or expired OTP"));
//            }
//
//            // 2️⃣ Validate new password
//            if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body(Map.of("error", "New password cannot be empty"));
//            }
//
//            // 3️⃣ Find user by email
//            User user = userRepository.findByEmail(request.getEmail())
//                    .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()));
//
//
//
//            // 4️⃣ Update password (encode it before saving)
//            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
//            userRepository.save(user);
//
//            // 5️⃣ Remove OTP after successful reset
//            otpStorage.remove(request.getEmail());
//
//            return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "Failed to reset password: " + e.getMessage()));
//        }
//    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordUpdateRequest request) {
        String email = request.getEmail();

        // Check OTP
        if (!otpStorage.containsKey(email) ||
                !otpStorage.get(email).equals(request.getOtp())) {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }

        // Find user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update encoded password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Remove used OTP
        otpStorage.remove(email);

        return ResponseEntity.ok("Password reset successful");
    }


}


