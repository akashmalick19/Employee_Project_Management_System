package com.example.demoEmployeeManagementProject.Entity.JwtEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String empFirstName;
    private String empLastName;
    private String jwtToken;
}
