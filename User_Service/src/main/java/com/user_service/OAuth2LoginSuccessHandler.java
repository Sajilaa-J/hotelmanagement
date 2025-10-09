package com.user_service;

import com.shared_persistence.entity.User;
import com.user_service.repo.UserRepository;
import com.user_service.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        // Determine role based on the requested endpoint
        String path = request.getRequestURI(); // e.g., "/api/admin/booking"
        String role;
        if (path.startsWith("/api/admin")) {
            role = "ADMIN";
        } else if (path.startsWith("/api/user")) {
            role = "USER";
        } else {
            role = "USER"; // default fallback
        }

        // Check if user exists; if not, create a new user with dynamic role
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setName(name != null ? name : "OAuth User");
            newUser.setEmail(email);
            newUser.setRole(role);
            newUser.setPhone(oauthUser.getAttribute("phone_number"));

            return userRepository.save(newUser);
        });

        // Update role if it has changed
        if (!user.getRole().equals(role)) {
            user.setRole(role);
            userRepository.save(user);
        }

        // Generate JWT token with email + role
        String token = jwtUtil.generateToken(user);


        // Return token and role as JSON
        response.setContentType("application/json");
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("role", role);

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(tokenMap));
    }
}
