package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.dto.EmployeeInfoDto;
import de.szut.lf8_starter.project.dto.ProjectAddEmployeeDto;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import de.szut.lf8_starter.project.dto.ProjectUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ProjectControllerOpenAPI {

    @Operation(
            summary = "creates a new project with its id and project information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "created project",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ProjectGetDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "invalid JSON posted",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content)})
    ProjectGetDto create(ProjectCreateDto projectCreateDto);


    @Operation(summary = "deletes an existing project by id " +
            "information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "delete " +
                    "successful"),
            @ApiResponse(responseCode = "401", description = "not authorized" +
                    "posted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "resource not " +
                    "found",
                    content = @Content)})
    void deleteProjectById(long id);


    @Operation(summary = "updates a project by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "project updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ProjectUpdateDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "project not found",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "invalid JSON posted",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content)})
    ProjectGetDto updateProject(long id, ProjectUpdateDto projectUpdateDto);

    @Operation(summary = "finds a project by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "project found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ProjectGetDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "project not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content)})
    ProjectGetDto findProjectById(long projectId);

    @Operation(summary = "lists all projects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "projects listed" +
                    " successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ProjectGetDto.class))}),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content)})
    List<ProjectGetDto> findAllProjects();

    @Operation(summary = "Add employee to project with qualification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Employee added to project successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ProjectGetDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Project not found, Employee not found, or Employee doesn't have the required qualification",
                    content = @Content),
            @ApiResponse(responseCode = "409",
                    description = "Employee is not available during the project period",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content)})
    ProjectGetDto addEmployeesToProject(long projectId, long employeeId, long qualificationId);

    @Operation(
            summary = "List all employees assigned to a project with their details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Employees listed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = EmployeeInfoDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Project not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content)})
    List<EmployeeInfoDto> listEmployeesForProject(long projectId);

    @Operation(
            summary = "Lists all projects assigned to a specific employee",
            description = "Returns a list of all projects in which the specified employee is currently involved.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Projects for employee listed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectGetDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Employee not found or no projects assigned",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Not authorized",
                    content = @Content)
    })
    List<ProjectGetDto> findAllProjectsOfEmployee(long employeeId);

}
