package com.example.demoEmployeeManagementProject.Service;

import com.example.demoEmployeeManagementProject.Configuration.JwtRequestFilter;
import com.example.demoEmployeeManagementProject.EmailConfiguration.EmailService;
import com.example.demoEmployeeManagementProject.Entity.*;
import com.example.demoEmployeeManagementProject.Entity.Dto.EmployeeDto;
import com.example.demoEmployeeManagementProject.Entity.Notification.NotificationMessage;
import com.example.demoEmployeeManagementProject.Repository.*;
import com.example.demoEmployeeManagementProject.Service.FireBaseMessagingService.FireBaseNotificationService;
import com.example.demoEmployeeManagementProject.Util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;

@Service
public class AdminEmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private FireBaseNotificationService fireBaseNotificationService;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AssignProjectToEmployeesRepository assignProjectToEmployeesRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UpdateByAdminRepository updateByAdminRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public String getEncodedPassword(String password){
        return passwordEncoder.encode(password);
    }

    public Map<String,Object> createAdminEmployeeRecord(EmployeeDto employeeDto, HttpServletRequest request) throws MessagingException {
        EmployeeEntity employeeEntity=new EmployeeEntity();
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
            if (Role.Admin.equals(employeeRepository.getRoleName(employeeEntity1.getEmpId()))) {
                if (employeeDto.getRole().equals(Role.Admin) || employeeDto.getRole().equals(Role.ADMIN)) {
                    return employeeDetailsStore(employeeEntity, employeeDto, employeeEntity1);
                }
                else {
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.BAD_REQUEST.value());
                    map.put("Message", " Employee Role should be Admin.");
                }
            }
            else {
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.BAD_REQUEST.value());
                map.put("Message", "Only Admin can Authorized to register another Admin-Employee,Contract to organization!");
            }
        }
        catch (ExpiredJwtException e2)
        {
            map.put("Timestamp", new Date());
            map.put("Status", HttpStatus.BAD_REQUEST.value());
            map.put("Message", "JWT Token has expired.");
        }
        return map;
    }


    public Map<String, Object> createVariousEmployeeRecord(EmployeeDto employeeDto, HttpServletRequest request) {
        EmployeeEntity employeeEntity=new EmployeeEntity();
        Map<String,Object> map = new LinkedHashMap<>();
        try{
            String jwtToken=jwtRequestFilter.parseJwt(request);
            if(jwtToken==null){
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.BAD_REQUEST.value());
                map.put("Message", "Please Provide JWT Token.");
                return map;
            }
            String empEmail=jwtUtil.getEmpEmailFromToken(jwtToken);
            EmployeeEntity employeeEntity1=employeeRepository.findByEmpEmail(empEmail);
            if(Role.Admin.equals(employeeRepository.getRoleName(employeeEntity1.getEmpId())))
            {
                if (employeeDto.getRole().equals(Role.Employee) || employeeDto.getRole().equals(Role.EMPLOYEE)
                        || employeeDto.getRole().equals(Role.Manager) || employeeDto.getRole().equals(Role.MANAGER)
                        || employeeDto.getRole().equals(Role.Teamlead) || employeeDto.getRole().equals(Role.TEAMLEAD)) {

                    return employeeDetailsStore(employeeEntity,employeeDto,employeeEntity1);
                }
                else{
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.BAD_REQUEST.value());
                    map.put("Message", "Role should be among Employee/Manager/Teamlead.");
                }
            }
            else {
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.BAD_REQUEST.value());
                map.put("Message", "Only Admin can Authorized to register another Employee(Manager/Teamlead/Employee),Contract to organization!");
            }
        }
        catch (ExpiredJwtException | MessagingException e2)
        {
            map.put("Timestamp", new Date());
            map.put("Status", HttpStatus.BAD_REQUEST.value());
            map.put("Message", "JWT Token has expired.");
        }
        return map;
    }


    public Map<String, Object> employeeDetailsStore(EmployeeEntity employeeEntity, EmployeeDto employeeDto, EmployeeEntity employeeEntity1) throws MessagingException {
        Map<String, Object> map = new LinkedHashMap<>();
        int emailCount=0;
        emailCount=employeeRepository.findByEmpEmailId(employeeDto.getEmailId());
        int phCount=0;
        phCount=employeeRepository.findByEmpPhoneNumber(employeeDto.getPhoneNumber());

        if(emailCount!=0){
            map.put("Timestamp", new Date());
            map.put("Status", HttpStatus.BAD_REQUEST.value());
            map.put("Message", "Email Id is already exist.");
        }
        else {
            if (phCount != 0) {
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.BAD_REQUEST.value());
                map.put("Message", "Phone Number is already exist.");
            }
            else {
                employeeCommonDetailsStore(employeeEntity,employeeDto,employeeEntity1);

                if(employeeDto.getRole().equals(Role.Admin) || employeeDto.getRole().equals(Role.ADMIN)){
                    adminEmployeeRoleSet(employeeEntity);
                }
                else if(employeeDto.getRole().equals(Role.Employee) || employeeDto.getRole().equals(Role.EMPLOYEE)) {
                    userEmployeeRoleSet(employeeEntity);
                }
                else if(employeeDto.getRole().equals(Role.Manager) || employeeDto.getRole().equals(Role.MANAGER)) {
                    managerEmployeeRoleSet(employeeEntity);
                }
                else if(employeeDto.getRole().equals(Role.Teamlead) || employeeDto.getRole().equals(Role.TEAMLEAD)) {
                    teamLeadEmployeeRoleSet(employeeEntity);
                }
                employeeRepository.save(employeeEntity);
                emailServiceToEmployee(employeeDto);
                try{
                    NotificationMessage notificationMessage=new NotificationMessage();
                    notificationMessage.setRecipientToken(employeeEntity.getGoogleFirebaseKeyToken());
                    notificationMessage.setTitle("Registration_Conformation_Notification.");
                    notificationMessage.setBody("your Registration is SuccessFull.\n"+
                            "Registration Details are reflect here.\n");
                    Map<String,String> map1=new LinkedHashMap<>();
                    map1.put("First Name",employeeDto.getFirstName());
                    map1.put("Last Name",employeeDto.getLastName());
                    map1.put("EmilId",employeeDto.getEmailId());
                    map1.put("Password",employeeDto.getPassword());
                    map1.put("Contact Number",employeeDto.getPhoneNumber());
                    map1.put("City",employeeDto.getCity());
                    map1.put("Role", String.valueOf(employeeDto.getRole()));
                    map1.put("Joining Date", String.valueOf(employeeDto.getJoiningDate()));
                    notificationMessage.setData(map1);
                    fireBaseNotificationService.sendNotificationByToken(notificationMessage);
                }
                catch (RuntimeException e1){
                    e1.printStackTrace();
                }
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.OK.value());
                map.put("Message", "Employee Add Successfully.");
                map.put("Object", employeeEntity);
            }
        }
        return map;
    }

    public void employeeCommonDetailsStore(EmployeeEntity employeeEntity, EmployeeDto employeeDto, EmployeeEntity employeeEntity1) {
        //employeeEntity.getAdminDetailsDto(); //its return Admin Details
        employeeEntity.setAdminId(employeeEntity1.getEmpId());
        employeeEntity.setEmpFirstName(employeeDto.getFirstName());
        employeeEntity.setEmpLastName(employeeDto.getLastName());
        employeeEntity.setEmpPhoneNumber(employeeDto.getPhoneNumber());
        employeeEntity.setEmpCity(employeeDto.getCity());
        employeeEntity.setEmpEmailId(employeeDto.getEmailId());
        employeeEntity.setEmpPassword(getEncodedPassword(employeeDto.getPassword()));
        employeeEntity.setGoogleFirebaseKeyToken(employeeDto.getGoogleFirebaseKeyToken());
        try {
            if (employeeDto.getJoiningDate() == null) {
                throw new Exception();
            } else {
                employeeEntity.setJoiningDate(employeeDto.getJoiningDate());
            }
        } catch (Exception e1) {
            employeeEntity.setJoiningDate(LocalDate.now());
        }
    }


    public void adminEmployeeRoleSet(EmployeeEntity employeeEntity)
    {
        Set<RoleEntity> roleEntitySet=new HashSet<>();
        RoleEntity roleEntity=roleRepository.findById(1L).get();
        roleEntitySet.add(roleEntity);
        employeeEntity.setRoleEntitySet(roleEntitySet);
    }

    public void managerEmployeeRoleSet(EmployeeEntity employeeEntity)
    {
        Set<RoleEntity> roleEntitySet=new HashSet<>();
        RoleEntity roleEntity=roleRepository.findById(2L).get();
        roleEntitySet.add(roleEntity);
        employeeEntity.setRoleEntitySet(roleEntitySet);
    }

    public void teamLeadEmployeeRoleSet(EmployeeEntity employeeEntity)
    {
        Set<RoleEntity> roleEntitySet=new HashSet<>();
        RoleEntity roleEntity=roleRepository.findById(3L).get();
        roleEntitySet.add(roleEntity);
        employeeEntity.setRoleEntitySet(roleEntitySet);
    }

    public void userEmployeeRoleSet(EmployeeEntity employeeEntity)
    {
        Set<RoleEntity> roleEntitySet=new HashSet<>();
        RoleEntity roleEntity=roleRepository.findById(4L).get();
        roleEntitySet.add(roleEntity);
        employeeEntity.setRoleEntitySet(roleEntitySet);
    }

    public void emailServiceToEmployee(EmployeeDto employeeDto) throws MessagingException {

        //                     emailService.sendTextEmail(employeeDto.getEmailId(),
//                            "Congrats "+employeeDto.getFirstName()+"!\n"+
//                            "You are the new Employee in our Company.your Username and Password are shared with you at below.\n"+
//                            "User Name: "+employeeDto.getEmailId()+"\n"+
//                            "Password: "+employeeDto.getPassword()+"\n"+
//                            "Thanks a lot for being a part of our company.",
//                            "Registration_Confirmation_Email");

        emailService.sendTextAndAttachmentEmail(employeeDto.getEmailId(),
                "Congrats "+employeeDto.getFirstName()+"!\n"+
                        "You are the new Employee in our Company.your Username and Password are shared with you at below.\n"+
                        "User Name : "+employeeDto.getEmailId()+"\n"+
                        "Password : "+employeeDto.getPassword()+"\n"+
                        "Thank You for being a part of our Organization.",
                "Registration_Confirmation_Email",
                "C:\\Users\\AKASH\\Pictures\\Saved Pictures\\Minions.jpg");

    }

    public Map<String, Object> employeeList(HttpServletRequest request) {
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
            if (Role.Admin.equals(employeeRepository.getRoleName(employeeEntity1.getEmpId()))) {
                List<EmployeeEntity> employeeEntityList=employeeRepository.findAll().stream().toList();
                if(employeeEntityList.isEmpty()){
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.OK.value());
                    map.put("Message", "Employee List is Empty.");
                }
                else
                {
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.OK.value());
                    map.put("Message", "Employee List.");
                    map.put("Objects",employeeEntityList);
                }
            }
            else {
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.BAD_REQUEST.value());
                map.put("Message", "Only Admin can Authorized to Show the List of all Employees.");
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


    public Map<String, Object> employeeSearchByFirstName(String empFirstName, HttpServletRequest request) {
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
            if (Role.Admin.equals(employeeRepository.getRoleName(employeeEntity1.getEmpId()))) {
                List<EmployeeEntity> employeeEntityList=employeeRepository.findByEmpFirstName(empFirstName);
                if(employeeEntityList.isEmpty()){
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.NOT_FOUND.value());
                    map.put("Message", empFirstName +" name dose not exist.");
                }
                else
                {
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.OK.value());
                    map.put("Message", "Specific Employee Details is exist.");
                    map.put("Objects",employeeEntityList);
                }
            }
            else {
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.BAD_REQUEST.value());
                map.put("Message", "Only Admin can Authorized to Show Specific Employee Details.");
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


    public Map<String, Object> employeeSearchByEmail(String empEmailId, HttpServletRequest request) {
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
            if (Role.Admin.equals(employeeRepository.getRoleName(employeeEntity1.getEmpId()))) {
                EmployeeEntity employeeEntity=employeeRepository.findByEmpEmail(empEmailId);
                if(employeeEntity==null){
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.NOT_FOUND.value());
                    map.put("Message", empEmailId +" EmailId does not exist.");
                }
                else
                {
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.OK.value());
                    map.put("Message", "Specific Employee Details is exist.");
                    map.put("Objects",employeeEntity);
                }
            }
            else {
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.BAD_REQUEST.value());
                map.put("Message", "Only Admin can Authorized to Show Specific Employee Details.");
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


    public Map<String, Object> updateEmployeeDetails(String empEmailId, EmployeeDto employeeDto, HttpServletRequest request) {
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
            if (Role.Admin.equals(employeeRepository.getRoleName(employeeEntity1.getEmpId()))) {
                EmployeeEntity employeeEntity2 = employeeRepository.findByEmpEmail(empEmailId);
                if(employeeEntity2==null){
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.NOT_FOUND.value());
                    map.put("Message", empEmailId +" EmailId does not exist.So Employee details does not modify.");
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
               employeeEntity2.setEmpPassword(getEncodedPassword(employeeDto.getPassword()));
               try {
                   if (employeeDto.getJoiningDate() == null) {
                       throw new Exception();
                   }
                   else {
                       employeeEntity2.setJoiningDate(employeeDto.getJoiningDate());
                   }
               }
               catch (Exception e1) {
                   employeeEntity2.setJoiningDate(employeeEntity2.getJoiningDate());
               }

               /* Role update by role-hierarchy maintains */
               Role role=employeeRepository.getRoleName(employeeEntity2.getEmpId());
               if(employeeDto.getRole().equals(Role.Admin) || employeeDto.getRole().equals(Role.ADMIN)){
                   if(role.equals(Role.Manager) || role.equals(Role.Admin)){
                       adminEmployeeRoleSet(employeeEntity2);
                   }
                   else{
                       map.put("Timestamp", new Date());
                       map.put("Status", HttpStatus.BAD_REQUEST.value());
                       map.put("Message", "can not change TeamLead role to Admin role OR Employee role to Admin role directly.");
                       return map;
                   }
               }
               else if(employeeDto.getRole().equals(Role.Manager) || employeeDto.getRole().equals(Role.MANAGER)){
                   if(role.equals(Role.Teamlead) || role.equals(Role.Manager) || role.equals(Role.Admin)){
                       managerEmployeeRoleSet(employeeEntity2);
                   }
                   else{
                       map.put("Timestamp", new Date());
                       map.put("Status", HttpStatus.BAD_REQUEST.value());
                       map.put("Message", "can not change Employee role to Manager role directly or vice-versa.");
                       return map;
                   }
               }
               else if(employeeDto.getRole().equals(Role.Teamlead) || employeeDto.getRole().equals(Role.TEAMLEAD)){
                   if(role.equals(Role.Employee) || role.equals(Role.Teamlead) || role.equals(Role.Manager) ){
                       teamLeadEmployeeRoleSet(employeeEntity2);
                   }
                   else{
                       map.put("Timestamp", new Date());
                       map.put("Status", HttpStatus.BAD_REQUEST.value());
                       map.put("Message", "can not change Admin role to TeamLead role directly or vice-versa.");
                       return map;
                   }
               }
               else if(employeeDto.getRole().equals(Role.Employee) || employeeDto.getRole().equals(Role.EMPLOYEE)){
                   if(role.equals(Role.Teamlead) || role.equals(Role.Employee)){
                       userEmployeeRoleSet(employeeEntity2);
                   }
                   else{
                       map.put("Timestamp", new Date());
                       map.put("Status", HttpStatus.BAD_REQUEST.value());
                       map.put("Message", "can not change Admin role to Employee role OR Manager role to Employee role directly.");
                       return map;
                   }
               }

                //***its return updatedBy Admin details
               List<UpdateByAdmin> updateByAdminList=new ArrayList<>();
               UpdateByAdmin updateByAdmin=new UpdateByAdmin();
               updateByAdminTable(updateByAdminList,updateByAdmin,employeeEntity1,employeeEntity2);

               employeeRepository.save(employeeEntity2);
               try{
                   NotificationMessage notificationMessage=new NotificationMessage();
                   notificationMessage.setRecipientToken(employeeEntity2.getGoogleFirebaseKeyToken());
                   notificationMessage.setTitle("Update_Employee_Details_By_Admin.");
                   notificationMessage.setBody("your Details is updated by "+ employeeEntity1.getEmpFirstName()+
                           " Who is the Admin and Employee-Id is "+ employeeEntity1.getEmpId()+"\n"+
                           "Updated Details are reflect here.\n");
                   Map<String,String> map1=new LinkedHashMap<>();
                   map1.put("EmilId",employeeDto.getEmailId());
                   map1.put("Password",employeeDto.getPassword());
                   map1.put("Contact Number",employeeDto.getPhoneNumber());
                   map1.put("City",employeeDto.getCity());
                   map1.put("First Name",employeeDto.getFirstName());
                   map1.put("Last Name",employeeDto.getLastName());
                   map1.put("Role", String.valueOf(employeeDto.getRole()));
                   map1.put("Joining Date", String.valueOf(employeeDto.getJoiningDate()));
                   notificationMessage.setData(map1);
                   fireBaseNotificationService.sendNotificationByToken(notificationMessage);
               }
                catch (RuntimeException e1){
                   e1.printStackTrace();
                }
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.OK.value());
                map.put("Message", "Employee Details successfully modify.");
                map.put("Object",employeeEntity2);
            }
            else{
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.BAD_REQUEST.value());
                map.put("Message", "Only Admin can Authorized to Modify Specific Employee Details.");
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


    public Map<String, Object> deleteEmployee(String empEmailId, HttpServletRequest request) {
        Map<String, Object> map = new LinkedHashMap<>();
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
            if (Role.Admin.equals(employeeRepository.getRoleName(employeeEntity1.getEmpId()))) {
                EmployeeEntity employeeEntity2 = employeeRepository.findByEmpEmail(empEmailId);
                if (employeeEntity2 == null) {
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.NOT_FOUND.value());
                    map.put("Message", empEmailId + " EmailId does not exist.");
                }
                else {
                    employeeEntity2.setEnableEmployee(false);
                    employeeRepository.save(employeeEntity2);

                    //*** update the involved_person field of ProjectEntity table
                    Long projectID = assignProjectToEmployeesRepository.getProjectIdByEmployeeId(employeeEntity2.getEmpId());
                    try {
                        ProjectEntity projectEntity = projectRepository.findByProjectId(projectID).get();
                        projectEntity.setInvolvedPersons(projectEntity.getInvolvedPersons() - 1);
                        projectRepository.save(projectEntity);
                    }
                    catch (NoSuchElementException e2) {
                    }

                        //*** update total involved_person field without judge the emp_Id field of AssignProjectToEmployeesEntity table
                    try {
                        List<AssignProjectToEmployeesEntity> assignProjectToEmployeesList
                                = assignProjectToEmployeesRepository.findAllByProjectIdExceptSpecificEmployeeId(projectID, employeeEntity2.getEmpId());
                        for (AssignProjectToEmployeesEntity assignProjectToEmployees : assignProjectToEmployeesList) {
                            assignProjectToEmployees.setInvolvedPersons(assignProjectToEmployees.getInvolvedPersons() - 1);
                        }
                    }
                    catch (NoSuchElementException e2) {

                    }
                    //*** update the particular involved_person and emp_id field of AssignProjectToEmployeesEntity table
                    List<AssignProjectToEmployeesEntity> assignProjectToEmployeesEntityList
                            = assignProjectToEmployeesRepository.findAllByEmployeeId(employeeEntity2.getEmpId());
                    for (AssignProjectToEmployeesEntity assignProjectToEmployeesEntity : assignProjectToEmployeesEntityList) {
                        assignProjectToEmployeesEntity.setAssigningEmployee(null);
                        assignProjectToEmployeesEntity.setInvolvedPersons(assignProjectToEmployeesEntity.getInvolvedPersons() - 1);
                        assignProjectToEmployeesRepository.save(assignProjectToEmployeesEntity);
                    }

                    //*** FireBase Notification is generated to the employee who was Deactivated from the organization
                    try{
                        NotificationMessage notificationMessage=new NotificationMessage();
                        notificationMessage.setRecipientToken(employeeEntity2.getGoogleFirebaseKeyToken());
                        notificationMessage.setTitle("Deactivate_Employment.");
                        notificationMessage.setBody("You are Deactivated by "+ employeeEntity1.getEmpFirstName()+
                                " Who is Admin and Employee-Id is "+ employeeEntity1.getEmpId()+"\n"+
                                "Updated Details are reflect here.\n");
                        fireBaseNotificationService.sendNotificationByToken(notificationMessage);
                    }
                    catch (RuntimeException e1){
                        e1.printStackTrace();
                    }
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.OK.value());
                    map.put("Message", "Employee Deleted Successful.");
                }
            } else {
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.BAD_REQUEST.value());
                map.put("Message", "Only Admin can Authorized to Delete Specific Employee.");
            }
        }
        catch (ExpiredJwtException e1) {
            map.put("Timestamp", new Date());
            map.put("Status", HttpStatus.BAD_REQUEST.value());
            map.put("Message", "JWT Token has expired.");
        }
        return map;
    }

    /* **its return updatedBy Admin details */
   public void updateByAdminTable(List<UpdateByAdmin> updateByAdminList, UpdateByAdmin updateByAdmin, EmployeeEntity employeeEntity1,EmployeeEntity employeeEntity2) {
        updateByAdmin.setUpdateById(employeeEntity1.getEmpId());
        updateByAdmin.setUpdateByEmployeeEmailId(employeeEntity1.getEmpEmailId());
        updateByAdmin.setUpdateByFirstName(employeeEntity1.getEmpFirstName());
        updateByAdmin.setUpdateByLastName(employeeEntity1.getEmpLastName());
        updateByAdmin.setEmployeeEntity(employeeEntity2);
        updateByAdminList.add(updateByAdmin);
        updateByAdminRepository.saveAll(updateByAdminList);
        employeeEntity2.setUpdateByAdminList(updateByAdminList);
    }

}


