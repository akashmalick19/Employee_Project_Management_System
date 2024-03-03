package com.example.demoEmployeeManagementProject.Repository;

import com.example.demoEmployeeManagementProject.Entity.AssignProjectToEmployeesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignProjectToEmployeesRepository extends JpaRepository<AssignProjectToEmployeesEntity,Long> {

    @Query(value = "select count(*) from assign_project_to_employee a where a.project_status='Ongoing' and a.emp_id=:empId and a.project_id=:projectId",
    nativeQuery = true
    )
    int findEmployeeAndProject(Long empId,Long projectId);

    @Query(value = "select * from assign_project_to_employee p where p.project_id=?1 and p.project_status='Ongoing'",
            nativeQuery = true
    )
    List<AssignProjectToEmployeesEntity> findAllByProjectId(Long projectId);

    @Query(value = "select * from assign_project_to_employee p where p.emp_id=?1 and p.project_status='Ongoing'",
            nativeQuery = true
    )
    List<AssignProjectToEmployeesEntity> findAllByEmployeeId(Long empId);

    @Query(value = "select p.project_id from assign_project_to_employee p where p.emp_id=?1 and p.project_status='Ongoing'",
            nativeQuery = true
    )
    Long getProjectIdByEmployeeId(Long empId);


    @Query(value = "select * from assign_project_to_employee p where p.project_id=?1 and p.emp_id<>?2 and p.project_status='Ongoing'",
            nativeQuery = true
    )
    List<AssignProjectToEmployeesEntity> findAllByProjectIdExceptSpecificEmployeeId(Long projectID, Long empId);
}
