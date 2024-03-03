package com.example.demoEmployeeManagementProject.Entity.JwtEntity;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class JwtRequest {

    @NotEmpty(message = "Employee Email Id should not be Empty or null.")
    private String empEmailId;

    @NotEmpty(message = "Password should not be empty or null.")
    private  String empPassword;
}
