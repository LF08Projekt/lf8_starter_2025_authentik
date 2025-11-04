package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.exceptionHandling.EmployeeNotAvailableException;
import de.szut.lf8_starter.exceptionHandling.EmployeeNotFoundException;
import de.szut.lf8_starter.exceptionHandling.ProjectNotFoundException;
import de.szut.lf8_starter.exceptionHandling.QualificationNotMatchException;
import de.szut.lf8_starter.project.dto.ProjectAddEmployeeDto;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import de.szut.lf8_starter.project.dto.ProjectUpdateDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

import java.time.LocalDate;
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
        /*if (!employeeService.isEmployeeIdValid(projectEntity.getResponsibleEmployeeId())) {
            throw new EmployeeNotFoundException("Employee with Id " +
                    projectEntity.getResponsibleEmployeeId() + "doesnt exist");
        }*/
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
      /*  @PutMapping("/{id}")
        public ProjectGetDto updateProject(@PathVariable("id") long projectId,
        @RequestBody @Valid ProjectUpdateDto projectUpdateDto) {
            if (!employeeService.isEmployeeIdValid(projectUpdateDto.getResponsibleEmployeeId())) {
                throw new EmployeeNotFoundException("Employee with ID " +
                        projectUpdateDto.getResponsibleEmployeeId() + " does not exist");
            }

            try {
                ProjectEntity updatedProject = projectMapper.mapUpdateDtoToEntity(projectUpdateDto, projectId);
                ProjectEntity savedProject = projectService.update(projectId, updatedProject);
                return projectMapper.mapEntityToGetDto(savedProject);
            } catch (ProjectNotFoundException exception) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
            }
        }*/
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

  /*  @PostMapping("/{id}/add/{employeeId}/qualification/{qualificationId}")
    public ProjectGetDto addEmployeesToProject(
            @PathVariable("id") long projectId,
            @PathVariable("employeeId") long employeeId,
            @PathVariable("qualificationId") long qualificationId,
            @RequestBody @Valid ProjectAddEmployeeDto projectAddEmployeeDto) {
        ProjectEntity currentProject = projectService.readById(projectId);
        ProjectEntity projectEntity =
                this.projectMapper.mapProjectAddEmployeeDtoToEntity(projectAddEmployeeDto, currentProject);

        if (projectEntity == null) {
            throw new ProjectNotFoundException("ProjectEntity not found on " +
                    "id = " + projectId);
        }

        final LocalDate startDate = projectEntity.getStartDate();
        final LocalDate endDate = projectEntity.getPlannedEndDate();

        if (!employeeService.isEmployeeIdValid(employeeId)) {
            throw new EmployeeNotFoundException("EmployeeEntity not found on id = " + employeeId);
        }

        if (!projectService.isEmployeeAvailable(projectId, employeeId)) {
            throw new EmployeeNotAvailableException(
                    "Employee is unavailable during the period " + startDate + " - " + endDate
            );
        }

        if (!projectService.isEmployeeQualified(qualificationId, employeeId)) {
            throw new QualificationNotMatchException(
                    "Employee doesn't have the qualification " + qualificationId
            );
        }
        try {
            ProjectEntity finalProjectEntity =
                    projectService.addEmployeeToProject(projectId, employeeId, qualificationId);
            return projectMapper.mapEntityToGetDto(finalProjectEntity);
        } catch (ProjectNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    exception.getMessage());
        }
    }*/
    @PostMapping("/{id}/add/{employeeId}/qualification/{qualificationId}")
    public ProjectGetDto addEmployeesToProject(
            @PathVariable("id") long projectId,
            @PathVariable("employeeId") long employeeId,
            @PathVariable("qualificationId") long qualificationId) {

        ProjectEntity project = projectService.readById(projectId);
        if (project == null) {
            throw new ProjectNotFoundException("Project with ID = " + projectId + " not found.");
        }

        if (!employeeService.isEmployeeIdValid(employeeId)) {
            throw new EmployeeNotFoundException("Employee with ID = " + employeeId + " not found.");
        }

        if (!projectService.isEmployeeAvailable(projectId, employeeId)) {
            throw new EmployeeNotAvailableException(
                    "Employee is unavailable during the project period " +
                            project.getStartDate() + " - " + project.getPlannedEndDate()
            );
        }

        if (!projectService.isEmployeeQualified(qualificationId, employeeId)) {
            throw new QualificationNotMatchException(
                    "Employee does not have the required qualification " + qualificationId
            );
        }

        ProjectEntity updatedProject = projectService.addEmployeeToProject(projectId, employeeId, qualificationId);
        return projectMapper.mapEntityToGetDto(updatedProject);
    }

}
