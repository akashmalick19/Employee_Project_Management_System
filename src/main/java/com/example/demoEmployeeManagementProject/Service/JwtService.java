package com.example.demoEmployeeManagementProject.Service;

import com.example.demoEmployeeManagementProject.Entity.EmployeeEntity;
import com.example.demoEmployeeManagementProject.Entity.JwtEntity.JwtRequest;
import com.example.demoEmployeeManagementProject.Entity.JwtEntity.JwtResponse;
import com.example.demoEmployeeManagementProject.Entity.Role;
import com.example.demoEmployeeManagementProject.Repository.EmployeeRepository;
import com.example.demoEmployeeManagementProject.Util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class JwtService implements UserDetailsService {

    Logger log= LoggerFactory.getLogger(JwtService.class);
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String empEmailId) throws UsernameNotFoundException {
        EmployeeEntity employee = employeeRepository.findByEmpEmail(empEmailId);

        if (employee != null) {
            return new org.springframework.security.core.userdetails.User(
                    employee.getEmpEmailId(),
                    employee.getEmpPassword(),
                    getAuthority(employee)
            );
        } else {
            throw new UsernameNotFoundException("Employee not found with EmployeeEmailId: " + empEmailId);
        }
    }

    private Set getAuthority(EmployeeEntity employee) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        employee.getRoleEntitySet().forEach( roleEntity-> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleEntity.getRoleNameId()));
        });
        return authorities;
    }


    public void authenticate(String empEmail, String empPassword) throws Exception {
        Map<String,Object> map=new LinkedHashMap<>();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(empEmail, empPassword));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED",e);
        } catch (BadCredentialsException e) {
           throw new Exception("INVALID_CREDENTIALS",e);
        }
    }

    public Map<String, Object> createJwtToken(JwtRequest jwtRequest, HttpServletRequest request) throws Exception {
        Map<String,Object> map=new LinkedHashMap<>();
        try{
            authenticate(jwtRequest.getEmpEmailId(),jwtRequest.getEmpPassword());
            UserDetails userDetails=loadUserByUsername(jwtRequest.getEmpEmailId());
            String token=jwtUtil.generateToken(userDetails);
            EmployeeEntity employeeEntity=employeeRepository.findByEmpEmail(jwtRequest.getEmpEmailId());
            map.put("Timestamp",new Date());
            map.put("Status", HttpStatus.OK.value());
            map.put("Message","Successfully generated Token.");
            map.put("Object",new JwtResponse(employeeEntity.getEmpFirstName(),employeeEntity.getEmpLastName(),token));
            return map;
        }
        catch (Exception e){
            map.put("Timestamp",new Date());
            map.put("Status", HttpStatus.BAD_REQUEST.value());
            map.put("Message","Invalid EmailId Or Password.");
            return map;
        }
    }
}
