package com.example.demoEmployeeManagementProject.Service;

import com.example.demoEmployeeManagementProject.Configuration.JwtRequestFilter;
import com.example.demoEmployeeManagementProject.Entity.*;
import com.example.demoEmployeeManagementProject.Entity.Dto.ProjectAssignToEmployeeDto;
import com.example.demoEmployeeManagementProject.Entity.Dto.ProjectDto;
import com.example.demoEmployeeManagementProject.Entity.Notification.NotificationMessage;
import com.example.demoEmployeeManagementProject.Repository.AssignProjectToEmployeesRepository;
import com.example.demoEmployeeManagementProject.Repository.EmployeeRepository;
import com.example.demoEmployeeManagementProject.Repository.ProjectRepository;
import com.example.demoEmployeeManagementProject.Service.FireBaseMessagingService.FireBaseNotificationService;
import com.example.demoEmployeeManagementProject.Util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import static com.example.demoEmployeeManagementProject.Entity.ProjectStatus.Ongoing;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AssignProjectToEmployeesRepository assignProjectToEmployeesRepository;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private FireBaseNotificationService fireBaseNotificationService;


    public Map<String, Object> projectCreation(List<ProjectDto> projectDtoList, HttpServletRequest request) {
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
                List<ProjectEntity> projectEntityList=projectRepository.findAll();
                
                if(projectEntityList.isEmpty()){
                    for(ProjectDto projectDto : projectDtoList)
                    {
                        ProjectEntity projectEntity=getProjectEntity(projectDto,employeeEntity1);
                        projectEntityList.add(projectEntity);
                    }
                    projectRepository.saveAll(projectEntityList);
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.OK.value());
                    map.put("Message", "Projects Store Successfully.");
                    map.put("Object",projectEntityList);
                }
                else{
                    String name= null;
                    int flag=0;
                    for(ProjectDto projectDto : projectDtoList) {
                        for (ProjectEntity projectEntity1 : projectEntityList) {
                            if (Objects.equals(projectEntity1.getProjectName(), projectDto.getProjectName())) {
                                name=projectDto.getProjectName();
                                flag = 1;
                                break;
                            }
                        }
                        if(flag==1){
                            break;
                        }
                    }
                    if(flag==0){
                        List<ProjectEntity> projectEntityList1=new ArrayList<>();
                        for(ProjectDto projectDto : projectDtoList){
                            ProjectEntity projectEntity=getProjectEntity(projectDto,employeeEntity1);
                            projectEntityList1.add(projectEntity);
                        }
                        projectRepository.saveAll(projectEntityList1);
                        map.put("Timestamp", new Date());
                        map.put("Status", HttpStatus.OK.value());
                        map.put("Message", "Projects Store Successfully.");
                        map.put("Object",projectEntityList1);
                    }
                    else{
                        map.put("Timestamp", new Date());
                        map.put("Status", HttpStatus.BAD_REQUEST.value());
                        map.put("Message", name +" project is already exist.");
                    }
                }
            }
            else{
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.BAD_REQUEST.value());
                map.put("Message", "Only Admin can Store Projects.");
            }
        } catch (ExpiredJwtException e1) {
            map.put("Timestamp", new Date());
            map.put("Status", HttpStatus.BAD_REQUEST.value());
            map.put("Message", "JWT Token has expired.");
        }
        return map;
    }

    private ProjectEntity getProjectEntity(ProjectDto projectDto, EmployeeEntity employeeEntity1) {
        ProjectEntity projectEntity=new ProjectEntity();
        projectEntity.setProjectName(projectDto.getProjectName());
        projectEntity.setProjectDescription(projectDto.getProjectDescription());
        projectEntity.setProjectClientFirstName(projectDto.getProjectClientFirstName());
        projectEntity.setProjectClientLastName(projectDto.getProjectClientLastName());
        projectEntity.setProjectDurationMonth(projectDto.getProjectDurationMonth());
        projectEntity.setStatus(Ongoing);
        projectEntity.setProjectStoreByEmpId(employeeEntity1.getEmpId());
        return projectEntity;
    }

    public Map<String, Object> projectListShow(HttpServletRequest request) {
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
            if (Role.Admin.equals(employeeRepository.getRoleName(employeeEntity1.getEmpId()))){
                List<ProjectEntity> projectEntityList=projectRepository.findAll().stream().toList();
                if(projectEntityList.isEmpty()){
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.OK.value());
                    map.put("Message", "Project List is Empty.");
                }
                else {
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.BAD_REQUEST.value());
                    map.put("Message", "Project List Successfully Fetch.");
                    map.put("Object",projectEntityList);
                }
            }
            else{
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.BAD_REQUEST.value());
                map.put("Message", "Only Admin can fetch Project List.");
            }
        }
        catch (ExpiredJwtException e1) {
            map.put("Timestamp", new Date());
            map.put("Status", HttpStatus.BAD_REQUEST.value());
            map.put("Message", "JWT Token has expired.");
        }
        return map;
    }

    public Map<String, Object> specificProjectShow(Long projectId,HttpServletRequest request) {
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
            if (Role.Admin.equals(employeeRepository.getRoleName(employeeEntity1.getEmpId()))){
                ProjectEntity projectEntity=projectRepository.findByProjectId(projectId).orElse(null);
                if(projectEntity==null){
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.NOT_FOUND.value());
                    map.put("Message", "Project does not exist.");
                }
                else {
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.BAD_REQUEST.value());
                    map.put("Message", "Project Successfully Fetch.");
                    map.put("Object",projectEntity);
                }
            }
            else{
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.BAD_REQUEST.value());
                map.put("Message", "Only Admin can Fetch Specific Projects Details.");
            }
        }
        catch (ExpiredJwtException e1) {
            map.put("Timestamp", new Date());
            map.put("Status", HttpStatus.BAD_REQUEST.value());
            map.put("Message", "JWT Token has expired.");
        }
        return map;
    }


    public Map<String, Object> assignProjectToEmployee(ProjectAssignToEmployeeDto projectAssignToEmployeeDto, HttpServletRequest request) {
        Map<String, Object> map = new LinkedHashMap<>();
        try{
            String jwtToken = jwtRequestFilter.parseJwt(request);
            if (jwtToken == null) {
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.BAD_REQUEST.value());
                map.put("Message", "Please Provide JWT Token.");
                return map;
            }
            String empEmail = jwtUtil.getEmpEmailFromToken(jwtToken);
            EmployeeEntity employeeEntity1 = employeeRepository.findByEmpEmail(empEmail);
            if (Role.Admin.equals(employeeRepository.getRoleName(employeeEntity1.getEmpId()))){
                EmployeeEntity employeeEntity=employeeRepository.findByEmpId(projectAssignToEmployeeDto.getEmployeeId()).orElse(null);
                ProjectEntity projectEntity=projectRepository.findByProjectId(projectAssignToEmployeeDto.getProjectId()).orElse(null);
                if(employeeEntity==null){
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.BAD_REQUEST.value());
                    map.put("Message", "Employee Id does not exist.");
                }
                else if(!Objects.equals(employeeEntity.getEmpFirstName(), projectAssignToEmployeeDto.getEmployeeFirstName()))
                {
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.BAD_REQUEST.value());
                    map.put("Message", "Employee First name does not match.");
                }
                else if(!(employeeEntity.getEmpLastName().equals(projectAssignToEmployeeDto.getEmployeeLastName())))
                {
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.BAD_REQUEST.value());
                    map.put("Message", "Employee Last name does not match.");
                }
                else if(projectEntity==null){
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.BAD_REQUEST.value());
                    map.put("Message", "Project Id does not exist.");
                }
                else if(!(projectEntity.getProjectName().equals(projectAssignToEmployeeDto.getProjectName())))
                {
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.BAD_REQUEST.value());
                    map.put("Message", "Project name does not match.");
                }
                else if(assignProjectToEmployeesRepository.findEmployeeAndProject(projectAssignToEmployeeDto.getEmployeeId(),
                        projectAssignToEmployeeDto.getProjectId())>0){

                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.BAD_REQUEST.value());
                    map.put("Message", "Project is already assigned to the same Employee.");

                }
                else {
                    projectEntity.setInvolvedPersons(projectEntity.getInvolvedPersons()+1);
                    projectRepository.save(projectEntity);

                    List<AssignProjectToEmployeesEntity>assignProjectToEmployeesEntityList=new ArrayList<>();
                    AssignProjectToEmployeesEntity assignProjectToEmployeesEntity=new AssignProjectToEmployeesEntity();
                    assignProjectToEmployeesEntity.setProjectId(projectEntity.getProjectId());
                    assignProjectToEmployeesEntity.setProjectName(projectEntity.getProjectName());
                    assignProjectToEmployeesEntity.setProjectDescription(projectEntity.getProjectDescription());
                    assignProjectToEmployeesEntity.setInvolvedPersons(projectEntity.getInvolvedPersons());
                    assignProjectToEmployeesEntity.setProjectClientFirstName(projectEntity.getProjectClientFirstName());
                    assignProjectToEmployeesEntity.setProjectClientLastName(projectEntity.getProjectClientLastName());
                    assignProjectToEmployeesEntity.setCreationTime(projectEntity.getCreationTime());
                    assignProjectToEmployeesEntity.setProjectDurationMonth(projectEntity.getProjectDurationMonth());
                    assignProjectToEmployeesEntity.setProjectStoreByEmpId(projectEntity.getProjectStoreByEmpId());
                    assignProjectToEmployeesEntity.setProjectAssignByEmpId(employeeEntity1.getEmpId());
                    assignProjectToEmployeesEntity.setStatus(Ongoing);
                    List<EmployeeEntity> employeeEntityList=new ArrayList<>();
                    employeeEntity.setAssigningProject(assignProjectToEmployeesEntityList);

                    employeeEntityList.add(employeeEntity);
                    assignProjectToEmployeesEntity.setAssigningEmployee(employeeEntity);
                    assignProjectToEmployeesEntityList.add(assignProjectToEmployeesEntity);
                    assignProjectToEmployeesRepository.saveAll(assignProjectToEmployeesEntityList);
                    employeeRepository.save(employeeEntity);

                    //***for show same number of involve persons inside any specific project
                    List<AssignProjectToEmployeesEntity> assignProjectToEmployeesEntityList1
                            =assignProjectToEmployeesRepository.findAllByProjectId(projectEntity.getProjectId());
                    for(AssignProjectToEmployeesEntity assignProjectToEmployeesEntity1 : assignProjectToEmployeesEntityList1)
                    {
                        assignProjectToEmployeesEntity1.setInvolvedPersons(projectEntity.getInvolvedPersons());
                        assignProjectToEmployeesRepository.save(assignProjectToEmployeesEntity1);
                    }

                    try{
                        NotificationMessage notificationMessage=new NotificationMessage();
                        notificationMessage.setRecipientToken(employeeEntity.getGoogleFirebaseKeyToken());
                        notificationMessage.setTitle("Project_assigned.");
                        notificationMessage.setBody("your are assigned to the Project by "+ employeeEntity1.getEmpFirstName()+
                                " Who is the Admin and Employee-Id is "+ employeeEntity1.getEmpId()+".\n"+
                                "Project Details is reflect here.");
                        Map<String,String> map1=new LinkedHashMap<>();
                        map1.put("EmilId", String.valueOf(projectEntity.getProjectId()));
                        map1.put("Password",projectEntity.getProjectName());
                        map1.put("Contact Number",projectEntity.getProjectDescription());
                        map1.put("City",projectEntity.getProjectClientFirstName());
                        map1.put("First Name",projectEntity.getProjectClientLastName());
                        map1.put("Last Name", String.valueOf(projectEntity.getInvolvedPersons()));
                        map1.put("Role", String.valueOf(projectEntity.getProjectDurationMonth()));
                        map1.put("Joining Date", String.valueOf(projectEntity.getCreationTime()));
                        map1.put("Project Status", String.valueOf(projectEntity.getStatus()));
                        notificationMessage.setData(map1);
                        fireBaseNotificationService.sendNotificationByToken(notificationMessage);
                    }
                    catch (RuntimeException e1){
                        e1.printStackTrace();
                    }
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.BAD_REQUEST.value());
                    map.put("Message", projectAssignToEmployeeDto.getProjectName()+" Project is Successfully assigned to "+ projectAssignToEmployeeDto.getEmployeeFirstName()+" whose Employee Id is "+ projectAssignToEmployeeDto.getEmployeeId());
                    map.put("Object",employeeEntity);
                }
            }
            else{
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.BAD_REQUEST.value());
                map.put("Message", "Only Admin can Assign the particular Project to particular Employee.");
            }
        }
        catch (ExpiredJwtException e1) {
            map.put("Timestamp", new Date());
            map.put("Status", HttpStatus.BAD_REQUEST.value());
            map.put("Message", "JWT Token has expired.");
        }
        return map;
    }

    public Map<String, Object> deactivatingProject(Long projectId, String projectName, HttpServletRequest request) {
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
                ProjectEntity projectEntity=projectRepository.findByProjectId(projectId).orElse(null);
                if(projectEntity==null){
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.BAD_REQUEST.value());
                    map.put("Message", "Project Id does not exist.");
                }
                else if(!Objects.equals(projectEntity.getProjectName(),projectName))
                {
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.BAD_REQUEST.value());
                    map.put("Message", "Project name does not match.");
                }
                else{
                    projectEntity.setInvolvedPersons(0);
                    projectEntity.setStatus(ProjectStatus.Finished);
                    projectRepository.save(projectEntity);

                    //*** update the involved_person, project status and emp_id field of AssignProjectToEmployeesEntity table

                    List<AssignProjectToEmployeesEntity> assignProjectToEmployeesEntityList
                            =assignProjectToEmployeesRepository.findAllByProjectId(projectId);
                    for(AssignProjectToEmployeesEntity assignProjectToEmployeesEntity : assignProjectToEmployeesEntityList)
                    {
                        assignProjectToEmployeesEntity.setInvolvedPersons(0);
                        assignProjectToEmployeesEntity.setStatus(ProjectStatus.Finished);
                        assignProjectToEmployeesEntity.setAssigningEmployee(null);
                        assignProjectToEmployeesRepository.save(assignProjectToEmployeesEntity);
                    }

                    //*** Firebase Notification are generated who was worked the particular Project
                    try{
                        for(AssignProjectToEmployeesEntity assignProjectToEmployeesEntity : assignProjectToEmployeesEntityList){
                            NotificationMessage notificationMessage=new NotificationMessage();
                            notificationMessage.setRecipientToken(assignProjectToEmployeesEntity.getAssigningEmployee().getGoogleFirebaseKeyToken());
                            notificationMessage.setTitle("Project_Deactivated_Notification.");
                            notificationMessage.setBody("your are Released from the " +projectEntity.getProjectName()
                                    +" project.the project-Id is "+ projectEntity.getProjectId()+".\n");
                            fireBaseNotificationService.sendNotificationByToken(notificationMessage);
                        }
                    }
                    catch (RuntimeException e1){
                        e1.printStackTrace();
                    }
                    map.put("Timestamp", new Date());
                    map.put("Status", HttpStatus.OK.value());
                    map.put("Message", "Project is Deactivated successfully.");
                    map.put("Object",projectEntity);
                }
            }
            else{
                map.put("Timestamp", new Date());
                map.put("Status", HttpStatus.BAD_REQUEST.value());
                map.put("Message", "Only Admin can Authorized to Modify(Deactivate) Specific Project Details.");
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
}
