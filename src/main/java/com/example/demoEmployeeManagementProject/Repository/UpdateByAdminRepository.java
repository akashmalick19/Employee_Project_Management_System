package com.example.demoEmployeeManagementProject.Repository;

import com.example.demoEmployeeManagementProject.Entity.UpdateByAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdateByAdminRepository extends JpaRepository<UpdateByAdmin,Long> {
}
