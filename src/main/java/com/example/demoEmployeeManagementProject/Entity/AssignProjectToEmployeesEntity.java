package com.example.demoEmployeeManagementProject.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assign_project_to_employee")
public class AssignProjectToEmployeesEntity {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long project_assigned_id;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "emp_id")
    private EmployeeEntity assigningEmployee;

    private Long projectId;
    private String projectName;
    private String projectDescription;

    @Column(name = "client_fname")
    private String projectClientFirstName;

    @Column(name = "client_lname")
    private String projectClientLastName;

    @Column(name = "project_status")
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Column(name ="duration_month")
    private Integer projectDurationMonth;

    @Column(name = "involved_persons_in_project")
    private Integer involvedPersons;

    @Column(name = "stored_by_empid")
    private Long projectStoreByEmpId;

    @Column(name = "assigned_by_empid")
    private Long projectAssignByEmpId;

    @CreationTimestamp
    private LocalDateTime projectAssigningTime;

    @Column(name = "project_create_timing")
    private LocalDateTime creationTime;

}
