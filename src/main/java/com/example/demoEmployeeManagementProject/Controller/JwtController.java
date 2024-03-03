package com.example.demoEmployeeManagementProject.Controller;

import com.example.demoEmployeeManagementProject.Entity.JwtEntity.JwtRequest;
import com.example.demoEmployeeManagementProject.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
public class JwtController {

    @Autowired
    private JwtService jwtService;

    @GetMapping(value = "api/createToken")
    public Map<String,Object>createJwtToken(@Valid @RequestBody JwtRequest jwtRequest, HttpServletRequest request) throws Exception {
       return jwtService.createJwtToken(jwtRequest,request);
    }
}
