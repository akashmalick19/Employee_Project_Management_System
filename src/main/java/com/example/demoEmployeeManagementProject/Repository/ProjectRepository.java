package com.example.demoEmployeeManagementProject.Repository;

import com.example.demoEmployeeManagementProject.Entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity,Long> {
    @Query(value = "select * from project_table p where p.project_id=?1 and p.project_status='Ongoing'",
            nativeQuery = true
    )
    Optional<ProjectEntity> findByProjectId(Long projectId);
}
