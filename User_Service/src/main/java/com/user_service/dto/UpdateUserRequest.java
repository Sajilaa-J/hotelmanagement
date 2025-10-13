package com.user_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private String oldEmail;
    private String oldPassword;
    private String newEmail;
}
