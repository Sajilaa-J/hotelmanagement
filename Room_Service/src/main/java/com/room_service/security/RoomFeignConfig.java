//package com.room_service.security;//package com.booking_service.security;
//
//import feign.RequestInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RoomFeignConfig {
//
//    @Bean
//    public RequestInterceptor requestInterceptor(JwtUtil jwtUtil) {
//        return template -> {
//            String token = jwtUtil.generateToken(); // token must have ROLE_ADMIN
//            template.header("Authorization", "Bearer " + token); // correct format
//        };
//    }
//
//}
