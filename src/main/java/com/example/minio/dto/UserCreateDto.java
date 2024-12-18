package com.example.minio.dto;

import com.example.minio.utils.ApplicationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserCreateDto extends UserUpdateDto {

    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = ApplicationConstants.PASSWORD_PATTERN_REGEX,
            message = "Password must contain at least one uppercase letter, one lowercase letter, one special character, and be at least 8 characters long")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
