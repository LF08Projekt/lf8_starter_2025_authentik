package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.employee.dto.EmployeeInfoDto;
import de.szut.lf8_starter.exceptionHandling.EmployeeNotAvailableException;
import de.szut.lf8_starter.exceptionHandling.EmployeeNotFoundException;
import de.szut.lf8_starter.exceptionHandling.ProjectNotFoundException;
import de.szut.lf8_starter.exceptionHandling.QualificationNotMatchException;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import de.szut.lf8_starter.project.dto.ProjectUpdateDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping(value = "/projects")
public class ProjectController implements ProjectControllerOpenAPI {
    private final ProjectMapper projectMapper;
    private final ProjectService projectService;
    private final EmployeeService employeeService;

    public ProjectController(ProjectMapper projectMapper, ProjectService projectService, EmployeeService employeeService) {
        this.projectMapper = projectMapper;
        this.projectService = projectService;
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectGetDto create(
            @RequestBody @Valid ProjectCreateDto projectCreateDto) {

        ProjectEntity projectEntity =
                this.projectMapper.mapCreateDtoToEntity(projectCreateDto);

        if (!employeeService.isEmployeeIdValid(projectEntity.getResponsibleEmployeeId())) {
            throw new EmployeeNotFoundException("Employee with Id " +
                    projectEntity.getResponsibleEmployeeId() + "doesnt exist");
        }
        try {
            projectEntity = this.projectService.create(projectEntity);
        } catch (EmployeeNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }

        return this.projectMapper.mapEntityToGetDto(projectEntity);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjectById(@PathVariable long id) {
        var entity = this.projectService.readById(id);
        if (entity == null) {
            throw new ProjectNotFoundException("ProjectEntity not found on " +
                    "id = " + id);
        } else {
            this.projectService.delete(entity);
        }
    }

    @PutMapping("/{id}")
    public ProjectGetDto updateProject(
            @PathVariable("id") long projectId,
            @RequestBody @Valid ProjectUpdateDto projectUpdateDto) {
        ProjectEntity projectEntity =
                this.projectMapper.mapUpdateDtoToEntity(projectUpdateDto, projectId);

        if (projectEntity == null) {
            throw new ProjectNotFoundException("ProjectEntity not found on " +
                    "id = " + projectId);
        }
        if (!employeeService.isEmployeeIdValid(projectEntity.getResponsibleEmployeeId())) {
            throw new EmployeeNotFoundException("Employee with Id " +
                    projectEntity.getResponsibleEmployeeId() + "doesnt exist");
        }
        try {
            ProjectEntity updatedProjectEntity =
                    projectMapper.mapUpdateDtoToEntity(projectUpdateDto,
                            projectId);
            ProjectEntity finalProjectEntity =
                    projectService.update(projectId, updatedProjectEntity);
            return projectMapper.mapEntityToGetDto(finalProjectEntity);
        } catch (ProjectNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    exception.getMessage());
        }
    }

    @GetMapping
    public List<ProjectGetDto> findAllProjects() {
        return this.projectService.readAll()
                .stream()
                .map(this.projectMapper::mapEntityToGetDto)
                .toList();
    }

    @GetMapping("/employee/{id}")
    public List<ProjectGetDto> findAllProjectsOfEmployee(
            @PathVariable("id") long employeeId) {

        boolean employeeValid = employeeService.isEmployeeIdValid(employeeId);
        if (!employeeValid) {
            throw new EmployeeNotFoundException(
                    "Employee with ID = " + employeeId + " not found.");
        }

        return this.projectService.listAllProjectsForEmployee(employeeId)
                .stream()
                .map(this.projectMapper::mapEntityToGetDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ProjectGetDto findProjectById(@PathVariable("id") long projectId) {
        ProjectEntity projectEntity = this.projectService.readById(projectId);

        if (projectEntity == null) {

            throw new ProjectNotFoundException("ProjectEntity not found on id" +
                    " = " + projectId);
        }

        return this.projectMapper.mapEntityToGetDto(projectEntity);
    }

    @PostMapping("/{id}/add/{employeeId}/qualification/{qualificationId}")
    public ProjectGetDto addEmployeesToProject(
            @PathVariable("id") long projectId,
            @PathVariable("employeeId") long employeeId,
            @PathVariable("qualificationId") long qualificationId) {


        ProjectEntity project = projectService.readById(projectId);
        if (project == null) {
            throw new ProjectNotFoundException(
                    "Project with ID = " + projectId + " not found.");
        }

        boolean employeeValid = employeeService.isEmployeeIdValid(employeeId);
        if (!employeeValid) {
            throw new EmployeeNotFoundException(
                    "Employee with ID = " + employeeId + " not found.");
        }

        boolean available =
                projectService.isEmployeeAvailable(projectId, employeeId);
        if (!available) {
            throw new EmployeeNotAvailableException(
                    "Employee is unavailable during the project period " +
                            project.getStartDate() + " - " +
                            project.getPlannedEndDate()
            );
        }

        boolean qualified =
                projectService.isEmployeeQualified(qualificationId, employeeId);
        if (!qualified) {
            throw new QualificationNotMatchException(
                    "Employee does not have the required qualification " +
                            qualificationId
            );
        }

        ProjectEntity updatedProject =
                projectService.addEmployeeToProject(projectId, employeeId);

        return projectMapper.mapEntityToGetDto(updatedProject);
    }

    @GetMapping("/{id}/employees")
    public List<EmployeeInfoDto> listEmployeesForProject(
            @PathVariable("id") long projectId) {
        try {
            return projectService.listAllEmployeesForProject(projectId);
        } catch (ProjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }


}
