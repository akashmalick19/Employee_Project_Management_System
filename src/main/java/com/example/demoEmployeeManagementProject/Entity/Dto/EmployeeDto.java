package com.example.demoEmployeeManagementProject.Entity.Dto;

import com.example.demoEmployeeManagementProject.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

    @NotEmpty(message = "Employee First Name should not be Empty or null.")
    private String firstName;

    @NotEmpty(message = "Employee Last Name should not be Empty or null.")
    private String lastName;

    @NotEmpty(message = "Employee Email Id should not be Empty or null.")
    @Email(message = "Employee email-Id should be proper given.")
    private String emailId;


    @NotEmpty(message = "Password should not be empty or null.")
    @Size(min=4,max=20,message = "Password should be minimum 4 characters and maximum 20 characters.")
    private String password;


    @NotEmpty(message = "Employee contract Number should not be Empty or null and should be minimum 10 numbers.")
    @Size(min = 10,max=12,message = "Employee contract Number minimum has at least 10 characters and maximum 12 characters.")
    private String phoneNumber;

    private String city;

    private String googleFirebaseKeyToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDate joiningDate;
}
