package com.jobportal.jobconnect.dto.response;

import com.jobportal.jobconnect.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthResponseDTO {

    private String accesstoken;
    private String   refreshToken;
    private String type ="Bearer";
    private int id;
    private String name;
    private String email;
    private UserRole role;
}
