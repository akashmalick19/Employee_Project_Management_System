package com.example.demoEmployeeManagementProject.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import javax.validation.executable.ValidateOnExecution;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "project_table")
@ValidateOnExecution
public class ProjectEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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
  private Integer projectDurationMonth=0;

  @Column(name = "involved_persons_in_project")
  private Integer involvedPersons=0;

  @Column(name = "stored_by_empid")
  private Long projectStoreByEmpId;

  @CreationTimestamp
  @Column(name = "project_create_timing")
  private LocalDateTime creationTime;

}
