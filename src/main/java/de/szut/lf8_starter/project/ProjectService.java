package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.exceptionHandling.ProjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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

    public List<ProjectEntity> readAll() {
        return this.projectRepository.findAll();
    }

    public void delete(ProjectEntity entity) {
        this.projectRepository.delete(entity);
    }

    public ProjectEntity update(Long projectId, ProjectEntity updatedProject) {
        var existingOpt = projectRepository.findById(projectId);
        if (existingOpt.isEmpty()) {
            throw new ProjectNotFoundException(
                    "ProjectEntity not found on id = " + projectId);
        }

        var existing = existingOpt.get();
        existing.setName(updatedProject.getName());
        existing.setResponsibleEmployeeId(updatedProject.getResponsibleEmployeeId());
        existing.setCustomerId(updatedProject.getCustomerId());
        existing.setResponsibleCustomerName(updatedProject.getResponsibleCustomerName());
        existing.setComment(updatedProject.getComment());
        existing.setStartDate(updatedProject.getStartDate());
        existing.setPlannedEndDate(updatedProject.getPlannedEndDate());
        existing.setActualEndDate(updatedProject.getActualEndDate());

        return projectRepository.save(existing);
    }


    public boolean isEmployeeAvailable(Long projectId, Long employeeId) {
        // TODO: implementieren wenn das Ticket bearbeitet wurde
        return true;
    }

    public boolean isEmployeeQualified(Long requiredQualificationId, Long employeeId) {
        boolean isQualified = false;
        for (int i = 0; i < employeeService.getById(employeeId).getQualifications().size(); i++) {
            if (Objects.equals(requiredQualificationId, employeeService.getById(employeeId).getQualifications().get(i))) {
                isQualified = true;
            }
        }
        return isQualified;
    }

    public ProjectEntity addEmployeeToProject(Long projectId, Long employeeId, Long requiredQualificationId) {
        var optionalProject = projectRepository.findById(projectId);

        if (optionalProject.isEmpty()) {
            throw new ProjectNotFoundException(
                    "ProjectEntity not found on id = " + projectId);
        }
        var existingProject = optionalProject.get();

        final List<Long> employeeIds = existingProject.getProjectEmployeesIds();
        employeeIds.add(employeeId);
        existingProject.setProjectEmployeesIds(employeeIds);

        return projectRepository.save(existingProject);
    }
}
