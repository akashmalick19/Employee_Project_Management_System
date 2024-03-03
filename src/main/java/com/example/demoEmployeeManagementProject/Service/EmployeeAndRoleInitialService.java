package com.example.demoEmployeeManagementProject.Service;

import com.example.demoEmployeeManagementProject.Entity.*;
import com.example.demoEmployeeManagementProject.Repository.EmployeeRepository;
import com.example.demoEmployeeManagementProject.Repository.ProjectRepository;
import com.example.demoEmployeeManagementProject.Repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class EmployeeAndRoleInitialService {

    Logger log= LoggerFactory.getLogger(EmployeeAndRoleInitialService.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AdminEmployeeService adminEmployeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostConstruct
    public void initRoleAndEmployeeAdd()
    {
        Set<RoleEntity> roleEntitySet=new HashSet<>();
        RoleEntity roleEntity1=new RoleEntity();
        roleEntity1.setRoleId(1L);
        roleEntity1.setRoleNameId(String.valueOf(Role.Admin));
        roleEntitySet.add(roleEntity1);

        RoleEntity roleEntity2=new RoleEntity();
        roleEntity2.setRoleId(2L);
        roleEntity2.setRoleNameId(String.valueOf(Role.Manager));
        roleEntitySet.add(roleEntity2);

        RoleEntity roleEntity3=new RoleEntity();
        roleEntity3.setRoleId(3L);
        roleEntity3.setRoleNameId(String.valueOf(Role.Teamlead));
        roleEntitySet.add(roleEntity3);

        RoleEntity roleEntity4=new RoleEntity();
        roleEntity4.setRoleId(4L);
        roleEntity4.setRoleNameId(String.valueOf(Role.Employee));
        roleEntitySet.add(roleEntity4);
        roleRepository.saveAll(roleEntitySet);

        EmployeeEntity employeeEntity=new EmployeeEntity();
        employeeEntity.setEmpId(1L);
        employeeEntity.setAdminId(1L);
        employeeEntity.setEmpFirstName("Akash");
        employeeEntity.setEmpLastName("Malick");
        employeeEntity.setEmpPhoneNumber("9836679918");
        employeeEntity.setEmpCity("Kolkata");
        Set<RoleEntity> roleEntitySet1=new HashSet<>();
        roleEntitySet1.add(roleEntity1);
        employeeEntity.setRoleEntitySet(roleEntitySet1);
        employeeEntity.setEmpEmailId("akashmalick14@gmail.com");
        employeeEntity.setEmpPassword(adminEmployeeService.getEncodedPassword("1234"));
        employeeEntity.setGoogleFirebaseKeyToken("AAAAxQK-rcI:APA91bG-50I-uYZ43oFl4_QsemaEeSmIuB4aY" +
                "-CDy-V2f0hmnndUU1625gJHWvb-FnF0lxdeWUoVAgWCbtJ6xal14TOW7Z7dh8vnif-PtKw6yHLQQwV8Ms" +
                "CXaGE0u7xfcU246QTJ8scD");
        employeeEntity.setCreationDate(LocalDateTime.now());
        employeeEntity.setJoiningDate(LocalDate.now());
       ///////////////////////////////////////////////////////////
//        List<AdminDetails> adminDetailsList=new ArrayList<>();
//        AdminDetails adminDetails=new AdminDetails();
//        adminDetails.setAdminId(1L);
//        adminDetails.setEmployeeEntity(employeeEntity);
//        adminDetailsList.add(adminDetails);
//        employeeEntity.setAdminDetailsList(adminDetailsList);
        ////////////////////////////////////////////
        employeeRepository.save(employeeEntity);

        System.out.println("Role Table Details:");
        for(RoleEntity roleEntity:roleRepository.findAll()){
            log.info("Role-Table: {}",roleEntity);
        }
        System.out.println("Employee Table Details:");
            log.info("Employee-Table: {}",employeeEntity);

    }
}
