package com.user_service.dto;
import lombok.Data;

@Data
public class AuthRequest {
    private String name;  // for registration
    private String email;
    private String password;
    private String phone;
    private String role;  // USER / ADMIN
}
