package com.example.demoEmployeeManagementProject.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@JsonPropertyOrder({"empId","adminId","empFirstName","empLastName",
        "EmpPhoneNumber","empEmailId","empPassword","empCity","roleEntitySet",
        "creationDate","joiningDate","projectEntityList","updateByAdminList","googleFirebaseKeyToken"})

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee_table",
        uniqueConstraints = {
                @UniqueConstraint(name = "phNo",columnNames = "EmpPhoneNumber"),
                @UniqueConstraint(name = "email",columnNames = "empEmailId")
        })
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empId;

    private String empFirstName;
    private String empLastName;
    private String empEmailId;
    private String empPassword;
    private String EmpPhoneNumber;
    private String empCity;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "employee_role_table",
    joinColumns = @JoinColumn(name = "emp_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roleEntitySet;


    @OneToMany(mappedBy = "assigningEmployee", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<AssignProjectToEmployeesEntity> assigningProject;

    private Long adminId;

    /* its return Admin Details */
//    public AdminDetailsDto getAdminDetailsDto(){
//        AdminDetailsDto adminDetailsDto=new AdminDetailsDto();
//        adminDetailsDto.setAdminId(this.getAdminId());
//        EmployeeEntity employeeEntity=employeeRepository.findById(this.getAdminId()).get();
//        adminDetailsDto.setAdminEmailId(this.getEmpEmailId());
//        adminDetailsDto.setAdminFirstName(this.getEmpFirstName());
//        adminDetailsDto.setAdminLastName(this.getEmpLastName());
//        return adminDetailsDto;
//    }

    @OneToMany(mappedBy = "employeeEntity", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<UpdateByAdmin>updateByAdminList;

//    public List<UpdateByAdmin>updateByAdminList(EmployeeEntity employeeEntity1){
//        UpdateByAdmin updateByAdmin=new UpdateByAdmin();
//        updateByAdmin.setUpdateById(employeeEntity1.getEmpId());
//        updateByAdmin.setUpdateByFirstName(this.getEmpFirstName());
//        updateByAdmin.setUpdateByLastName(this.getEmpLastName());
//        updateByAdmin.setUpdateByEmployeeEmailId(this.getEmpEmailId());
//        return (List<UpdateByAdmin>) updateByAdmin;
//    }

    @JsonIgnore
    @Column(name = "emp_enable_status",nullable = false,columnDefinition = "TINYINT(1)")
    private boolean enableEmployee=true;

    private String googleFirebaseKeyToken;

    @CreationTimestamp
    private LocalDateTime creationDate;

    private LocalDate joiningDate;

}
