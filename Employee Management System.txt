Project Name: Employee Management Platform with Spring Boot and Java.

Objective: Build an Employee management platform that allows employees to manage their Projects using Spring Boot and Java. 

Description of the project:This is Employee Management System. An Employee belongs to different roles(Admin,Manager,Teamlead,Employeee) based on role an employee can Register their information, also update and Delete their information. 
But there is a Admin who can add,update and delete an employee information. An employe can show his/her details and update details by himself/herself.
projects can add,show and delete by the Admin. Many Projects are assigned to the particular employee and also relese him/her from the particular project by the Admin accordingly.
In this project two main features are added i.e. PUSH NOTIFICATION (Using Google_FireBase Application) and EMAIL GENERATION. These two features are worked according to many activities of the employee. 

How to Run this Project in Locally:

1.Clone this project into local. Then goto the src/main file of the project.
2.After that Open it any code editor like Intellij Idea.
3.First add the dependencies- i. Spring Web ii. Spring Data JPA iii. Spring Security iv. Lombok Developer Tools dependency v. MySQL Diver vi. JSON WebToken(jjwt) vii. Spring Validation I/O .
4.Add the Database configuration inside 'application.properties' file. 
5.You can use API testing tool POSTMAN. 6.You can use Database MySQL Workbench 8.0 version.

Requirements for Project: 1. Set up a new Spring Boot project using the Spring Initializr (https://start.spring.io/) and select the necessary dependencies i.e. Spring Web, Spring Data JPA, Spring Security, Lombok Developer Tools, MySQL Diver, JSON WebToken(jjwt), Google firebase(firebase-admin),Spring boot Starter-mail, Spring Validation I/O.

2. Created the necessary domain models and their relationships:
	a. EmployeeEntit: Represented a employeeEntity with properties such as  "empId","adminId","empFirstName","empLastName","EmpPhoneNumber","empEmailId","empPassword","empCity","roleEntitySet","creationDate","joiningDate","projectEntityList","updateByAdminList","googleFirebaseKeyToken","enableEmployee"(i.e. 'emp_enable_status' field).
	   Each employee should be associated with many assigingProjects i.e. One_To_Many Bidirectional Relation Ship.
	   Each employee should be associated with many updateByAdminList i.e. One_To_Many Bidirectional Relation Ship.

	b. ProjectEntity: Represented an project with properties such as "projectId","projectName","projectDescription","projectClientFirstName","projectClientLastName","status"(Project_status),"projectDurationMonth","involvedPersons","projectStoreByEmpId","projectAssignByEmpId","projectAssigningTime","creationTime".
	   

	c. AssignProjectToEmployeesEntity: Represented an AssignProjectToEmployeesEntity with properties such as "project_assigned_id","assigningEmployee","projectId","projectName","projectDescription","projectClientFirstName","projectClientLastName","status"(Project_status),"projectDurationMonth","involvedPersons","projectStoreByEmpId","creationTime".
	   Many AssignProjectToEmployeesEntity should be associated with one assigingEmployee i.e. Many_to_One Bidirectional Relation Ship.

	d. Role: Represented an Role with properties such as roleId and roleName. there have four Roles i.e. Admin,Employee,Manager and Teamlead. Each Role should be associated with a employeeEmtity and vice versa i.e. Many_To_Many Relation Ship.
	
	e. UpdateByAdmin: Represented an UpdateByAdmin with properties such as "id","employeeEntity","updateById","updateByFirstName","updateByLastName","updateByEmployeeEmailId","updateTiming".
	   Many UpdateByAdmin should be associated with one employeeEntity i.e. Many_To_One Bidirectional Relation Ship.


3. Used Spring Data JPA to create repositories for the domain models and interact with the embedded H2 Database and MySQL Database.

4. Implemented exception handling and validation to ensure proper API usage and data integrity for each method of controller and service classes.

5. Used PostMan application for Unit tests of the APIs and services purpose.

6. Added the JWT-based authentication to secure the APIs of the project.

7. There have Two sub application.properties in this project. 
	
	i. application-test.properties: Inside this properties MySQL Database configuration is defined.
	
	ii. application-prod.properties: Inside this properties MySQL Database configuration is defined. 

8. Implemented RESTful APIs for the following actions:

  a. Token Generate:
	i. @GetMapping(value = "api/createToken"):By this API we can generate JWT token of any employee by passing through employee emailId and Pasword as a Json Request.

  b. Employee:
	i. @PostMapping(value = "/api/Admin/adminRegister"): Create all Admin employee by only Admin and also pass the Jwt Token to the headers of specific employee(i.e. jwt token of Admin).

	ii. @PostMapping(value = "/api/Admin/variousUserRegister"): Create all managers,employees,Teamleads by only Admin and also pass the Jwt Token to the headers of specific employee(i.e. jwt token of Admin).

	iii. @GetMapping(value = "/api/Admin/Employee/List"): Fetch all employees List by only Admin and also pass the Jwt Token to the headers of specific employee(i.e. jwt token of Admin).

	iv.  @GetMapping(value = "/api/Admin/Employee/searchFirstName/{empFirstName}"): Fetch a specific employee by pass any specific employee's firstName as parameter with the API and also pass the Jwt Token to the headers of specific employee(i.e. jwt token of Admin).

	v. @GetMapping(value = "/api/Admin/Employee/searchEmailId"): Fetch a specific employee by pass any specific employee's emailId as parameter with the API and also pass the Jwt Token to the headers of specific employee(i.e. jwt token of Admin).

	vi. @PutMapping(value = "/api/Admin/Employee/updateDetails"): Update an employee's information by pass any specific employee emailId as parameter with the API and also pass the Jwt Token to the headers of any specific employee(i.e. jwt token of Admin).

	vii.  @DeleteMapping(value = "/api/Admin/Employee/deleteEmployee"): Delete an employee information by pass any specific employee emailId as parameter with the API and also pass the Jwt Token to the headers of any specific employee(i.e. jwt token of Admin).

	viii. @GetMapping(value = "/api/User/Employee/selfSearch"): Fetch a specific employee by pass the Jwt Token to the headers of specific employee.
	
	ix. @PutMapping(value = "/api/User/Employee/selfUpdateDetails"): Update an employee's information by pass any specific Jwt Token to the headers of any specific employee.


  b. Project:
	i. @PostMapping(value = "/api/Admin/project/projectListStore"):create List of project by only Admin and also pass the Jwt Token to the headers of specific employee(i.e. jwt token of Admin).

	ii. @GetMapping(value = "/api/Admin/project/projectList"): Fetch all project List by only Admin and also pass the Jwt Token to the headers of specific employee(i.e. jwt token of Admin).
	
	iii. @GetMapping(value = "/api/Admin/project/specificProjectShow/{projectId}"): Fetch a specific project by pass any specific projectId as parameter with the API and also pass the Jwt Token to the headers of specific employee(i.e. jwt token of Admin).

	iv. @PostMapping(value = "/api/Admin/project/projectAssign"):Assiging project to a particular employee by only Admin and also pass the Jwt Token to the headers of specific employee(i.e. jwt token of Admin).

	v.  @DeleteMapping(value = "/api/Admin/project/projectFinishedStatus"): Delete a specific project information by pass any specific projectId and projectName as parameter with the API and also pass the Jwt Token to the headers of any specific employee(i.e. jwt token of Admin).
	
