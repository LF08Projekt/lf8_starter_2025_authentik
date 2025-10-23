package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.exceptionHandling.EmployeeNotFoundException;
import de.szut.lf8_starter.exceptionHandling.ProjectNotFoundException;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import de.szut.lf8_starter.project.dto.ProjectUpdateDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

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

        if (employeeService.isEmployeeValid(projectEntity.getResponsibleEmployeeId())) {
            throw new EmployeeNotFoundException("Employee with Id " +
                    projectEntity.getResponsibleEmployeeId() + "doesnt exist");
        }
        if (projectEntity.getProjectEmployeesIds() != null) {
            for (Long employeeId : projectEntity.getProjectEmployeesIds()) {
                if (employeeService.isEmployeeValid(employeeId)) {
                    throw new EmployeeNotFoundException(
                            "Employee with Id " + employeeId +
                                    " doesn't exist");
                }
            }
        }
        try {
            projectEntity = this.projectService.create(projectEntity);
        } catch (EmployeeNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }

        return this.projectMapper.mapEntityToGetDto(projectEntity);
    }

    @DeleteMapping("/{id}")
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
        if (employeeService.isEmployeeValid(projectEntity.getResponsibleEmployeeId())) {
            throw new EmployeeNotFoundException("Employee with Id " +
                    projectEntity.getResponsibleEmployeeId() + "doesnt exist");
        }
        if (projectEntity.getProjectEmployeesIds() != null) {
            for (Long employeeId : projectEntity.getProjectEmployeesIds()) {
                if (employeeService.isEmployeeValid(employeeId)) {
                    throw new EmployeeNotFoundException(
                            "Employee with Id " + employeeId +
                                    " doesn't exist");
                }
            }
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

    @GetMapping("/{id}")
    public ProjectGetDto findProjectById(@PathVariable("id") long projectId) {
        ProjectEntity projectEntity = this.projectService.readById(projectId);

        if(projectEntity == null) {
            throw new ProjectNotFoundException("ProjectEntity not found on id" +
                    " = " + projectId);
        }

        return this.projectMapper.mapEntityToGetDto(projectEntity);
    }
    @PostMapping("/{projectId}/employees/{employeeId}")
    public ProjectGetDto addEmployeeToProject(
            @PathVariable Long projectId,
            @PathVariable Long employeeId) {
        try {
            ProjectEntity updatedProject = this.projectService.addEmployeeToProject(projectId, employeeId);
            return this.projectMapper.mapEntityToGetDto(updatedProject);
        } catch (ProjectNotFoundException | EmployeeNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    @DeleteMapping("/{projectId}/employees/{employeeId}")
    public ProjectGetDto removeEmployeeFromProject(
            @PathVariable Long projectId,
            @PathVariable Long employeeId) {
        try {
            ProjectEntity updatedProject = this.projectService.removeEmployeeFromProject(projectId, employeeId);
            return this.projectMapper.mapEntityToGetDto(updatedProject);
        } catch (ProjectNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }
}
