package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import de.szut.lf8_starter.project.dto.ProjectUpdateDto;
import org.springframework.stereotype.Service;

@Service
public class ProjectMapper {

    public ProjectEntity mapCreateDtoToEntity(ProjectCreateDto dto){
        ProjectEntity newProject = new ProjectEntity();
        newProject.setName(dto.getName());
        newProject.setCustomerId(dto.getCustomerId());
        newProject.setResponsibleCustomerName(dto.getResponsibleCustomerName());
        newProject.setResponsibleEmployeeId(dto.getResponsibleEmployeeId());
        newProject.setStartDate(dto.getStartDate());
        newProject.setPlannedEndDate(dto.getPlannedEndDate());
        newProject.setActualEndDate(dto.getActualEndDate());
        newProject.setComment(dto.getComment());
        newProject.setProjectEmployeesIds(dto.getProjectEmployeesIds());
        return newProject;
    }

    public ProjectGetDto mapEntityToGetDto(ProjectEntity entity){
        ProjectGetDto newDto = new ProjectGetDto();
        newDto.setProjectId(entity.getProjectId());
        newDto.setName(entity.getName());
        newDto.setResponsibleEmployeeId(entity.getResponsibleEmployeeId());
        newDto.setComment(entity.getComment());
        newDto.setCustomerId(entity.getCustomerId());
        newDto.setResponsibleCustomerName(entity.getResponsibleCustomerName());
        newDto.setStartDate(entity.getStartDate());
        newDto.setPlannedEndDate(entity.getPlannedEndDate());
        newDto.setActualEndDate(entity.getActualEndDate());
        newDto.setProjectEmployeesIds(entity.getProjectEmployeesIds());
        return newDto;
    }
    public ProjectEntity mapUpdateDtoToEntity(ProjectUpdateDto updateDto,Long projectId){
        ProjectEntity projectToUpdate = new ProjectEntity();
        projectToUpdate.setProjectId(projectId);
        projectToUpdate.setName(updateDto.getName());
        projectToUpdate.setCustomerId(updateDto.getCustomerId());
        projectToUpdate.setResponsibleCustomerName(updateDto.getResponsibleCustomerName());
        projectToUpdate.setResponsibleEmployeeId(updateDto.getResponsibleEmployeeId());
        projectToUpdate.setStartDate(updateDto.getStartDate());
        projectToUpdate.setPlannedEndDate(updateDto.getPlannedEndDate());
        projectToUpdate.setActualEndDate(updateDto.getActualEndDate());
        projectToUpdate.setComment(updateDto.getComment());
        projectToUpdate.setProjectEmployeesIds(updateDto.getProjectEmployeesIds());
        return projectToUpdate;
    }

}
