package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.exceptionHandling.ProjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final EmployeeService employeeService;

    public ProjectService(ProjectRepository projectRepository, EmployeeService employeeService) {
        this.projectRepository = projectRepository;
        this.employeeService = employeeService;
    }

    public ProjectEntity create(ProjectEntity projectEntity) {
        return projectRepository.save(projectEntity);
    }

    public ProjectEntity readById(long id) {
        Optional<ProjectEntity> optionalQualifications =
                this.projectRepository.findById(id);
        return optionalQualifications.orElse(null);
    }

    public void delete(ProjectEntity entity) {
        this.projectRepository.delete(entity);
    }
    public ProjectEntity update(Long id, ProjectEntity incoming) {
        var existingOpt = projectRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new ProjectNotFoundException(
                    "ProjectEntity not found on id = " + id);
        }

        var existing = existingOpt.get();
        existing.setName(incoming.getName());
        existing.setResponsibleEmployeeId(incoming.getResponsibleEmployeeId());
        existing.setCustomerId(incoming.getCustomerId());
        existing.setResponsibleCustomerName(incoming.getResponsibleCustomerName());
        existing.setComment(incoming.getComment());
        existing.setStartDate(incoming.getStartDate());
        existing.setPlannedEndDate(incoming.getPlannedEndDate());
        existing.setActualEndDate(incoming.getActualEndDate());

        return projectRepository.save(existing);
    }

    /*public ProjectEntity update(ProjectEntity project) {
        ProjectEntity updatedProject = readById(project.getProjectId());

        updatedProject.setName(project.getName());
        updatedProject.setResponsibleEmployeeId(project.getResponsibleEmployeeId());
        updatedProject.setCustomerId(project.getCustomerId());
        updatedProject.setResponsibleCustomerName(project.getResponsibleCustomerName());
        updatedProject.setComment(project.getComment());
        updatedProject.setStartDate(project.getStartDate());
        updatedProject.setPlannedEndDate(project.getPlannedEndDate());
        updatedProject.setActualEndDate(project.getActualEndDate());
        updatedProject.setProjectEmployeesIds(project.getProjectEmployeesIds());

        updatedProject = this.projectRepository.save(updatedProject);
        return updatedProject;
    }*/

}
