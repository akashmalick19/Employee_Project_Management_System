package com.example.demoEmployeeManagementProject.Entity.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.executable.ValidateOnExecution;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidateOnExecution
public class ProjectDto {

    @NotEmpty(message = "Project Name should not be Empty or null.")
    private String projectName;

    @NotEmpty(message = "Project Description should not be Empty or null.")
    private String projectDescription;

    @NotEmpty(message = "Project's Client first name should not be Empty or null.")
    private String projectClientFirstName;

    @NotEmpty(message = "Project's Client last name should not be Empty or null.")
    private String projectClientLastName;

    @NotEmpty(message = "projectDurationMonth field should be filled with Integer value")
    private Integer projectDurationMonth;

}
