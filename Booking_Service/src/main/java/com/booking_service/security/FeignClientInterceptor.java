//package com.booking_service.security;
//import feign.RequestInterceptor;
//import feign.RequestTemplate;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import jakarta.servlet.http.HttpServletRequest;
//@Configuration
//@Component
//public class FeignClientInterceptor implements RequestInterceptor {
//
//    @Override
//    public void apply(RequestTemplate template) {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//
//        if (requestAttributes instanceof ServletRequestAttributes) {
//            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
//            String authHeader = request.getHeader("Authorization");
//
//            if (authHeader != null && !authHeader.isEmpty()) {
//                template.header("Authorization", authHeader);
//            }
//        }
//    }
//}

