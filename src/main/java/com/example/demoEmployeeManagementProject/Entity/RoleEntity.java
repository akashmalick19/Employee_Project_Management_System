package com.example.demoEmployeeManagementProject.Entity;

import com.example.demoEmployeeManagementProject.Entity.Dto.ProjectDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.*;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.example.demoEmployeeManagementProject.Entity.ProjectStatus.Ongoing;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "role_table")
public class RoleEntity {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY,)
    private Long roleId;

    private String roleNameId;

}
