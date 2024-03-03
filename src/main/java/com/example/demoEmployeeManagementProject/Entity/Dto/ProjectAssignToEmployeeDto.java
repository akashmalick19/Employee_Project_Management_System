package com.example.demoEmployeeManagementProject.Entity.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAssignToEmployeeDto {
    @NotNull(message = "Employee Id should not be empty or null.")
    private Long employeeId;

    @NotEmpty(message = "Employee First name should not be empty or null.")
    private  String employeeFirstName;

    @NotEmpty(message = "Employee Last name should not be empty or null.")
    private  String employeeLastName;

    @NotNull(message = "Project Id should not be empty or null.")
    private Long projectId;

    @NotEmpty(message = "Project name should not be empty or null.")
    private String projectName;

}
