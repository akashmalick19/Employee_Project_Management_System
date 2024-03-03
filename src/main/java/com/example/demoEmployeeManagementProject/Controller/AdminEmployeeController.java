package com.example.demoEmployeeManagementProject.Controller;

import com.example.demoEmployeeManagementProject.Entity.Dto.EmployeeDto;
import com.example.demoEmployeeManagementProject.Entity.Notification.NotificationMessage;
import com.example.demoEmployeeManagementProject.Service.AdminEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

@Profile(value = {"test","prod"})
@RestController
@RequestMapping(value = "/api/Admin")
public class AdminEmployeeController {
    @Autowired
    private AdminEmployeeService adminEmployeeService;

    @PostMapping(value = "/adminRegister")
    public Map<String, Object> createAdminEmployeeRecord(@Valid @RequestBody EmployeeDto employeeDto, HttpServletRequest request) throws MessagingException {
        return adminEmployeeService.createAdminEmployeeRecord(employeeDto, request);
    }

    /* Employee/Manager/TeamLead both of all register by this url */
    @PostMapping(value = "/variousUserRegister")
    public Map<String, Object> createVariousEmployeeRecord(@Valid @RequestBody EmployeeDto employeeDto, HttpServletRequest request) throws MessagingException {
        return adminEmployeeService.createVariousEmployeeRecord(employeeDto, request);
    }

    @GetMapping(value = "/Employee/List")
    public Map<String, Object> employeeList(HttpServletRequest request) {
        return adminEmployeeService.employeeList(request);
    }
    @GetMapping(value = "/Employee/searchFirstName/{empFirstName}")
    public Map<String, Object> employeeSearchByFirstName(@PathVariable("empFirstName") String empFirstName, HttpServletRequest request) {
        return adminEmployeeService.employeeSearchByFirstName(empFirstName, request);
    }

    @GetMapping(value = "/Employee/searchEmailId")
    public Map<String, Object> employeeSearchByEmail(@RequestParam("empEmailId") String empEmailId, HttpServletRequest request) {
        return adminEmployeeService.employeeSearchByEmail(empEmailId, request);
    }
////////////////////////////////////////////
    @PutMapping(value = "/Employee/updateDetails")
    public Map<String, Object> updateEmployeeDetails(@RequestParam("empEmailId") String empEmailId, @Valid @RequestBody EmployeeDto employeeDto, HttpServletRequest request) {
        return adminEmployeeService.updateEmployeeDetails(empEmailId,employeeDto,request);
    }

    @DeleteMapping(value = "/Employee/deleteEmployee")
    public Map<String,Object> deleteEmployee(@RequestParam("empEmailId") String empEmailId, HttpServletRequest request){
        return adminEmployeeService.deleteEmployee(empEmailId,request);
    }
}
