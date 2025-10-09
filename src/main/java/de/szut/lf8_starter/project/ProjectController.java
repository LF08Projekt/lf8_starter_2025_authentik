package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/project/")
public class ProjectController {
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
    public ProjectGetDto create(@RequestBody ProjectCreateDto projectCreateDto) {

        ProjectEntity projectEntity = this.projectMapper.mapCreateDtoToEntity(projectCreateDto);

        if (!employeeService.isEmployeeValid(projectEntity.getResponsibleEmployeeId())) {
            throw new ResourceNotFoundException("Employee with Id " + projectEntity.getResponsibleEmployeeId() + "doesnt exist");
        }
        try {
            projectEntity = this.projectService.create(projectEntity);
        }catch (ResourceNotFoundException exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
        return this.projectMapper.mapEntityToGetDto(projectEntity);
    }

}
