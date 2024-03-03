package com.example.demoEmployeeManagementProject.Repository;

import com.example.demoEmployeeManagementProject.Entity.Role;
import com.example.demoEmployeeManagementProject.Entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,Long> {

}
