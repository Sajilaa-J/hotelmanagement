//package com.booking_service.security;
////
////import jakarta.servlet.FilterChain;
////import jakarta.servlet.ServletException;
////import jakarta.servlet.http.HttpServletRequest;
////import jakarta.servlet.http.HttpServletResponse;
////import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
////import org.springframework.security.core.authority.SimpleGrantedAuthority;
////import org.springframework.security.core.context.SecurityContextHolder;
////import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
////import org.springframework.web.filter.OncePerRequestFilter;
////
////import java.io.IOException;
////import java.util.Collections;
////
////public class JwtAuthFilter extends OncePerRequestFilter {
////
////    private final JwtUtil jwtUtil;
////
////    public JwtAuthFilter(JwtUtil jwtUtil) {
////        this.jwtUtil = jwtUtil;
////    }
////
////    @Override
////    protected void doFilterInternal(HttpServletRequest request,
////                                    HttpServletResponse response,
////                                    FilterChain filterChain)
////            throws ServletException, IOException {
////
////        String path = request.getRequestURI();
////
////        if (path.startsWith("/v3/api-docs") ||
////                path.startsWith("/swagger-ui") ||
////                path.startsWith("/swagger-resources") ||
////                path.startsWith("/webjars") ||
////                path.equals("/swagger-ui.html")) {
////
////            filterChain.doFilter(request, response);
////            return;
////        }
////
////        String authHeader = request.getHeader("Authorization");
////
////        if (authHeader != null && authHeader.startsWith("Bearer ")) {
////            String token = authHeader.substring(7);
////
////            if (jwtUtil.isTokenValid(token)) {
////
////                String email = jwtUtil.extractEmail(token);
////                String role = jwtUtil.extractRole(token);
////
////                UsernamePasswordAuthenticationToken authToken =
////                        new UsernamePasswordAuthenticationToken(
////                                email,
////                                null,
////                                Collections.singletonList(new SimpleGrantedAuthority(role))
////                        );
////                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
////
////                SecurityContextHolder.getContext().setAuthentication(authToken);
////            } else {
////                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
////                return;
////            }
////        } else {
////            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
////            return;
////        }
////
////        filterChain.doFilter(request, response);
////    }
////}
//
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Collections;
//
//public class JwtAuthFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//
//    public JwtAuthFilter(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String path = request.getRequestURI();
//
//        if (path.startsWith("/v3/api-docs") ||
//                path.startsWith("/swagger-ui") ||
//                path.startsWith("/swagger-resources") ||
//                path.startsWith("/webjars") ||
//                path.equals("/swagger-ui.html")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//
//            if (jwtUtil.isTokenValid(token)) {
//                String email = jwtUtil.extractEmail(token);
//                String role = jwtUtil.extractRole(token);
//
//                if (!role.startsWith("ROLE_")) {
//                    role = "ROLE_" + role;
//                }
//
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(
//                                email,
//                                null,
//                                Collections.singletonList(new SimpleGrantedAuthority(role))
//                        );
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("Invalid or expired token.");
//                return;
//            }
//        } else {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Authorization token is missing or invalid.");
//            return;
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
package com.booking_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // ✅ Allow Swagger, public endpoints, etc.
        if (path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/swagger-resources") ||
                path.startsWith("/webjars") ||
                path.equals("/swagger-ui.html")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Extract token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header.");
            return;
        }

        String token = authHeader.substring(7);

        // ✅ Validate token
        if (!jwtUtil.isTokenValid(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token.");
            return;
        }

        try {
            String email = jwtUtil.extractEmail(token);
            String role = jwtUtil.extractRole(token);
            Long tokenUserId = jwtUtil.extractUserId(token);

            if (!role.startsWith("ROLE_")) {
                role = "ROLE_" + role;
            }

            // ✅ If the endpoint contains a userId path variable, verify ownership
            if (path.matches(".*/bookings/\\d+.*")) {
                // Example paths: /api/user/bookings/5 , /api/user/bookings/5/cancel
                String[] segments = path.split("/");
                for (int i = 0; i < segments.length; i++) {
                    if (segments[i].equalsIgnoreCase("bookings") && i + 1 < segments.length) {
                        try {
                            Long userIdInPath = Long.parseLong(segments[i + 1]);
                            if (!tokenUserId.equals(userIdInPath)) {
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                response.getWriter().write("Access denied: You can only manage your own bookings.");
                                return;
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            }

            // ✅ Authentication setup for Spring Security
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority(role))
                    );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token parsing error: " + e.getMessage());
        }
    }
}
