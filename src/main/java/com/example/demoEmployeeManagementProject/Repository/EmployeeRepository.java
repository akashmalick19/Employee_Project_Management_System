package com.example.demoEmployeeManagementProject.Repository;

import com.example.demoEmployeeManagementProject.Entity.EmployeeEntity;
import com.example.demoEmployeeManagementProject.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity,Long> {


    @Query(value = "select * from employee_table e where e.emp_enable_status='1'",
    nativeQuery = true
    )
    List<EmployeeEntity> findAll();

    @Query(value = "select count(*) from employee_table e where e.emp_email_id= ?1 and e.emp_enable_status='1'",
    nativeQuery = true
    )
    int findByEmpEmailId(String emailId);

    @Query(value = "select count(*) from employee_table e where e.emp_phone_number= ?1 and e.emp_enable_status='1'",
            nativeQuery = true
    )
    int findByEmpPhoneNumber(String phoneNumber);

    @Query(value = "select * from employee_table e where e.emp_email_id= ?1 and e.emp_enable_status='1'",
            nativeQuery = true
    )
    EmployeeEntity findByEmpEmail(String empEmailId);

    @Query(value = "select r1.role_name_id from role_table r1,employee_role_table r2,employee_table e where r1.role_id=r2.role_id and e.emp_id=r2.emp_id and e.emp_id=:empId",
        nativeQuery = true
    )
    Role getRoleName(Long empId);

    @Query(value = "select * from employee_table e where e.emp_first_name= ?1 and e.emp_enable_status='1'",
    nativeQuery = true
    )
    List<EmployeeEntity> findByEmpFirstName(String empFirstName);

    @Query(value = "select * from employee_table e where e.emp_id=?1 and e.emp_enable_status='1'",
            nativeQuery = true
    )
    Optional<EmployeeEntity> findByEmpId(Long empId);

}
