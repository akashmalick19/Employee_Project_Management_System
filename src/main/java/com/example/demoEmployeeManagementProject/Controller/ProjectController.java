package com.example.demoEmployeeManagementProject.Controller;

import com.example.demoEmployeeManagementProject.Entity.Dto.ProjectAssignToEmployeeDto;
import com.example.demoEmployeeManagementProject.Entity.Dto.ProjectDto;
import com.example.demoEmployeeManagementProject.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/Admin/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @PostMapping(value = "/projectListStore")
    public Map<String,Object> projectCreation(@Validated @RequestBody List<ProjectDto> projectDtoList, HttpServletRequest request){
        return projectService.projectCreation(projectDtoList,request);
    }

    @GetMapping(value = "/projectList")
    public Map<String,Object> projectListShow(HttpServletRequest request){
        return projectService.projectListShow(request);
    }

    @GetMapping(value = "/specificProjectShow/{projectId}")
    public Map<String,Object> specificProjectShow(@PathVariable("projectId") Long projectId, HttpServletRequest request){
        return projectService.specificProjectShow(projectId,request);
    }
///////////////////////////////////////
    @PostMapping(value = "/projectAssign")
    public Map<String,Object> assignProjectToEmployee(@Valid @RequestBody ProjectAssignToEmployeeDto projectAssignToEmployeeDto, HttpServletRequest request){
        return projectService.assignProjectToEmployee(projectAssignToEmployeeDto,request);
    }

    @DeleteMapping(value = "/projectFinishedStatus")
    public Map<String,Object> deactivatingProject(@Param("projectId") Long projectId,@Param("projectName") String projectName,HttpServletRequest request){
        return projectService.deactivatingProject(projectId,projectName,request);
    }

}
