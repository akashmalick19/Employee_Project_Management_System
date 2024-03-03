package com.example.demoEmployeeManagementProject.Controller;

import com.example.demoEmployeeManagementProject.Entity.Dto.EmployeeDto;
import com.example.demoEmployeeManagementProject.Service.UserEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Profile(value = {"test","prod"})
@RestController
@RequestMapping(value = "/api/User")
public class UserEmployeeController {

    @Autowired
    private UserEmployeeService userEmployeeService;

    @GetMapping(value = "/Employee/selfSearch")
    public Map<String, Object> selfEmployeeSearch(HttpServletRequest request) {
        return userEmployeeService.selfEmployeeSearch(request);
    }

    @PutMapping(value = "/Employee/selfUpdateDetails")
    public Map<String, Object> selfUpdateEmployeeDetails(@Valid @RequestBody EmployeeDto employeeDto, HttpServletRequest request) {
        return userEmployeeService.selfUpdateEmployeeDetails(employeeDto,request);
    }

}
