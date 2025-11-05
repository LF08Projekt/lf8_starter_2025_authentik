package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.employee.dto.EmployeeInfoDto;
import de.szut.lf8_starter.exceptionHandling.EmployeeNotFoundException;
import de.szut.lf8_starter.exceptionHandling.ProjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        var employee = employeeService.getById(employeeId);

        if (employee == null) {
            return false;
        }

        return employee.getQualifications().stream()
                .anyMatch(q -> q.equals(requiredQualificationId));
    }


    public ProjectEntity addEmployeeToProject(Long projectId, Long employeeId) {
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

    public ProjectEntity removeEmployeeFromProject(Long projectId, Long employeeId) {

        var optProject = projectRepository.findById(projectId);
        if (optProject.isEmpty()) {
            throw new ProjectNotFoundException("ProjectEntity not found on id = " + projectId);
        }
        var existingProject = optProject.get();


        boolean employeeIsValid = employeeService.isEmployeeIdValid(employeeId);
        if (!employeeIsValid) {
            throw new EmployeeNotFoundException("Employee not found on id = " + employeeId);
        }



        List<Long> employeeIds = existingProject.getProjectEmployeesIds();
        if (!employeeIds.contains(employeeId)) {
            throw new EmployeeNotFoundException("The employee with id" + employeeId + " is not working on this project.");
        }


        employeeIds.remove(employeeId);
        existingProject.setProjectEmployeesIds(employeeIds);

        return projectRepository.save(existingProject);
    }

    public List<EmployeeInfoDto> listAllEmployeesForProject(Long projectId) {
        var optionalProject = projectRepository.findById(projectId);

        if (optionalProject.isEmpty()) {
            throw new ProjectNotFoundException(
                    "ProjectEntity not found on id = " + projectId);
        }

        var existingProject = optionalProject.get();
        List<Long> employeeIds = existingProject.getProjectEmployeesIds();

        return employeeIds.stream()
                .map(employeeService::getEmployeeInfoById) // Holt
                // EmployeeInfoDto
                .filter(Objects::nonNull)
                .toList();
    }

    public List<ProjectEntity> listAllProjectsForEmployee(Long employeeId){
        List<ProjectEntity> allProjectsOfEmployee = new ArrayList<>();
        List<ProjectEntity> completeProjectList = readAll();
        Long currentProjectId;
        List<Long> employeeIdList;
        for (int i = 0; completeProjectList.size() > i; i++){
            currentProjectId = completeProjectList.get(i).getProjectId();
            employeeIdList =
                    readById(currentProjectId).getProjectEmployeesIds();
            for(int j = 0; employeeIdList.size() > j; j++){
                if(employeeIdList.get(j).equals(employeeId)){
                    allProjectsOfEmployee.add(readById(currentProjectId));
                }
            }
        }
        return allProjectsOfEmployee;
    }

    public boolean isEmployeeAlreadyInProject(long employeeId, long projectId){
        var project = readById(projectId);
        var employeeList = project.getProjectEmployeesIds();
        for (Long aLong : employeeList) {
            if (aLong == employeeId) {
                return false;
            }
        }
        return true;
    }
}
