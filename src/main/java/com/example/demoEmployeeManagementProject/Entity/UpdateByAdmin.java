package com.example.demoEmployeeManagementProject.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;


/* This table contain the details of employee who update another employees Details */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "userDetails_updated_table")
public class UpdateByAdmin {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "emp_id")
    private EmployeeEntity employeeEntity;

    private Long updateById;
    private String updateByFirstName;
    private String updateByLastName;
    private String updateByEmployeeEmailId;
    @CreationTimestamp
    private LocalDateTime updateTiming;

}
