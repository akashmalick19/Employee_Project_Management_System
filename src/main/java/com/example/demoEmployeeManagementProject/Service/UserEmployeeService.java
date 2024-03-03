package com.example.demoEmployeeManagementProject.Service;

import com.example.demoEmployeeManagementProject.Configuration.JwtRequestFilter;
import com.example.demoEmployeeManagementProject.Entity.Dto.EmployeeDto;
import com.example.demoEmployeeManagementProject.Entity.EmployeeEntity;
import com.example.demoEmployeeManagementProject.Entity.Notification.NotificationMessage;
import com.example.demoEmployeeManagementProject.Entity.UpdateByAdmin;
import com.example.demoEmployeeManagementProject.Repository.EmployeeRepository;
import com.example.demoEmployeeManagementProject.Service.FireBaseMessagingService.FireBaseNotificationService;
import com.example.demoEmployeeManagementProject.Util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class UserEmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AdminEmployeeService adminEmployeeService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private FireBaseNotificationService fireBaseNotificationService;

    public Map<String, Object> selfEmployeeSearch(HttpServletRequest request) {
        Map<String,Object> map = new LinkedHashMap<>();
        try {
            String jwtToken = jwtRequestFilter.parseJwt(request);
            if (jwtToken == null) {
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.BAD_REQUEST.value());
                map.put("Message", "Please Provide JWT Token.");
                return map;
            }
            String empEmail = jwtUtil.getEmpEmailFromToken(jwtToken);
            EmployeeEntity employeeEntity1 = employeeRepository.findByEmpEmail(empEmail);
            if(employeeEntity1==null){
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.NOT_FOUND.value());
                map.put("Message", "Employee does not exist.");
            }
            else
            {
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.OK.value());
                map.put("Message", "Employee details successfully fetch.");
                map.put("Objects",employeeEntity1);
            }
        }
        catch (ExpiredJwtException e1)
        {
            map.put("Timestamp", new Date());
            map.put("Status", HttpStatus.BAD_REQUEST.value());
            map.put("Message", "JWT Token has expired.");
        }
        return map;
    }


    public Map<String, Object> selfUpdateEmployeeDetails(EmployeeDto employeeDto, HttpServletRequest request) {
        Map<String,Object> map = new LinkedHashMap<>();
        try {
            String jwtToken = jwtRequestFilter.parseJwt(request);
            if (jwtToken == null) {
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.BAD_REQUEST.value());
                map.put("Message", "Please Provide JWT Token.");
                return map;
            }
            String empEmail = jwtUtil.getEmpEmailFromToken(jwtToken);
            EmployeeEntity employeeEntity2 = employeeRepository.findByEmpEmail(empEmail);
            if(employeeEntity2==null){
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.NOT_FOUND.value());
                map.put("Message", empEmail +" EmailId does not exist.So Employee details does not modify.");
                return map;
            }
            int emailCount=0;
            emailCount=employeeRepository.findByEmpEmailId(employeeDto.getEmailId());
            int phCount=0;
            phCount=employeeRepository.findByEmpPhoneNumber(employeeDto.getPhoneNumber());

            if(emailCount != 0){
                if(!Objects.equals(employeeEntity2.getEmpEmailId(), employeeDto.getEmailId())){
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.BAD_REQUEST.value());
                    map.put("Message", employeeDto.getEmailId() +" Email Id is already exist.");
                    return map;
                }
            }
            if(phCount != 0) {
                if(!Objects.equals(employeeEntity2.getEmpPhoneNumber(), employeeDto.getPhoneNumber())){
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.BAD_REQUEST.value());
                    map.put("Message", employeeDto.getPhoneNumber() +" Phone Number is already exist.");
                    return map;
                }
            }
            employeeEntity2.setEmpEmailId(employeeDto.getEmailId());
            employeeEntity2.setEmpPhoneNumber(employeeDto.getPhoneNumber());
            employeeEntity2.setEmpCity(employeeDto.getCity());
            employeeEntity2.setEmpFirstName(employeeDto.getFirstName());
            employeeEntity2.setEmpLastName(employeeDto.getLastName());
            employeeEntity2.setEmpPassword(adminEmployeeService.getEncodedPassword(employeeDto.getPassword()));

            //***its return updatedBy Admin details
            List<UpdateByAdmin> updateByAdminList=new ArrayList<>();
            UpdateByAdmin updateByAdmin=new UpdateByAdmin();
            adminEmployeeService.updateByAdminTable(updateByAdminList,updateByAdmin,employeeEntity2,employeeEntity2);

            employeeRepository.save(employeeEntity2);
            try{
                NotificationMessage notificationMessage=new NotificationMessage();
                notificationMessage.setRecipientToken(employeeEntity2.getGoogleFirebaseKeyToken());
                notificationMessage.setTitle("Self_Update_Employee_Details.");
                notificationMessage.setBody("your Details is updated by yourself.\n"+
                        "Updated Details are reflect here.\n");
                Map<String,String> map1=new LinkedHashMap<>();
                map1.put("EmilId",employeeDto.getEmailId());
                map1.put("Password",employeeDto.getPassword());
                map1.put("Contact Number",employeeDto.getPhoneNumber());
                map1.put("City",employeeDto.getCity());
                map1.put("First Name",employeeDto.getFirstName());
                map1.put("Last Name",employeeDto.getLastName());
                notificationMessage.setData(map1);
                fireBaseNotificationService.sendNotificationByToken(notificationMessage);
            }
            catch (RuntimeException e1){
                e1.printStackTrace();
            }
            map.put("Timestamp", new Date());
            map.put("Status", HttpStatus.OK.value());
            map.put("Message", "Employee successfully modify his/her details.");
            map.put("Object",employeeEntity2);
        }
        catch (ExpiredJwtException e1)
        {
            map.put("Timestamp", new Date());
            map.put("Status", HttpStatus.BAD_REQUEST.value());
            map.put("Message", "JWT Token has expired.");
        }
        return map;
    }
}
