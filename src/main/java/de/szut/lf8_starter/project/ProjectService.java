package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.exceptionHandling.ProjectNotFoundException;
import de.szut.lf8_starter.hello.HelloEntity;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
