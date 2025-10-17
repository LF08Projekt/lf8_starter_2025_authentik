package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final EmployeeService employeeService;

    public ProjectService(ProjectRepository projectRepository, EmployeeService employeeService){
        this.projectRepository = projectRepository;
        this.employeeService = employeeService;
    }
    public ProjectEntity create(ProjectEntity projectEntity){
        return  projectRepository.save(projectEntity);
    }
    public ProjectEntity readById(long id){
        Optional<ProjectEntity> optionalQualifications =
                this.projectRepository.findById(id);
        return optionalQualifications.orElse(null);
    }
    public void delete(ProjectEntity entity) {
        this.projectRepository.delete(entity);
    }


}
