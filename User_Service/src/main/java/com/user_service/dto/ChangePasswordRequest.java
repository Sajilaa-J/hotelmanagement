package com.user_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private String email;
    private String currentPassword;
    private String newPassword;
}
