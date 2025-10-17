package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.exceptionHandling.EmployeeNotFoundException;
import de.szut.lf8_starter.exceptionHandling.ProjectNotFoundException;
import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;


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

        if (!employeeService.isEmployeeValid(projectEntity.getResponsibleEmployeeId())) {
            throw new EmployeeNotFoundException("Employee with Id " +
                    projectEntity.getResponsibleEmployeeId() + "doesnt exist");
        }
        if (projectEntity.getProjectEmployeesIds() != null) {
            for (Long employeeId : projectEntity.getProjectEmployeesIds()) {
                if (!employeeService.isEmployeeValid(employeeId)) {
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
}
