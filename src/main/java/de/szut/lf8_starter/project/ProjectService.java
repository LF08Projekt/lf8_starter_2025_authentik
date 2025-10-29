package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.exceptionHandling.EmployeeNotAvailableException;
import de.szut.lf8_starter.exceptionHandling.EmployeeNotFoundException;
import de.szut.lf8_starter.exceptionHandling.ProjectNotFoundException;
import de.szut.lf8_starter.exceptionHandling.QualificationNotMatchException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public boolean isEmployeeIdValid(Long employeeID) {
        // TODO(A.ribic): need to be implemented
        return true;
    }

    public boolean isEmployeeAvailable(Long projectId, Long employeeId) {
        return true;
    }

    public boolean isEmployeeQualified(String requiredQualification, Long employeeId) {
        return true;
    }

    public ProjectEntity addEmployeeToProject(Long projectId, Long employeeId, String requiredQualification) {
        var optionalProject = projectRepository.findById(projectId);

        if (optionalProject.isEmpty()) {
            throw new ProjectNotFoundException(
                    "ProjectEntity not found on id = " + projectId);
        }
        var existingProject = optionalProject.get();

        final LocalDate startDate = existingProject.getStartDate();
        final LocalDate endDate = existingProject.getPlannedEndDate();


        if (!isEmployeeIdValid(employeeId)) {
            throw new EmployeeNotFoundException("EmployeeEntity not found on id = " + employeeId);
        }

        if (!isEmployeeAvailable(projectId, employeeId)) {
            throw new EmployeeNotAvailableException(
                    "Employee is unavailable during the period " + startDate + " - " + endDate
            );
        }

        if (!isEmployeeQualified(requiredQualification, employeeId)) {
            throw new QualificationNotMatchException(
                    "Employee doesn't have the qualification " + requiredQualification
            );
        }
        final List<Long> employeeIds = existingProject.getProjectEmployeesIds();
        employeeIds.add(employeeId);
        existingProject.setProjectEmployeesIds(employeeIds);

        return projectRepository.save(existingProject);
    }
}
