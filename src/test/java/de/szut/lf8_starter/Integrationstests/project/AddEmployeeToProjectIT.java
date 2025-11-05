package de.szut.lf8_starter.Integrationstests.project;

import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.employee.dto.EmployeeDto;
import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.project.ProjectRepository;
import de.szut.lf8_starter.skill.SkillDto;
import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AddEmployeeToProjectIT extends AbstractIntegrationTest {

    @Autowired
    ProjectRepository projectRepository;

    @MockBean
    EmployeeService employeeService;

    @Transactional
    @Test
    void addEmployeeToProject_isOk() throws Exception {
        var project = new ProjectEntity();
        project.setName("Test Project");
        project.setProjectEmployeesIds(new ArrayList<>());
        project = projectRepository.save(project);

        Long projectId = project.getProjectId();
        Long employeeId = 42L;
        Long requiredQualificationId = 5L;

        when(employeeService.isEmployeeIdValid(eq(employeeId))).thenReturn(true);

        var dto = new EmployeeDto();
        dto.setId(employeeId);
        var skill1 = new SkillDto();
        skill1.setId(requiredQualificationId);

        var skill2 = new SkillDto();
        skill2.setId(99L);
        dto.setSkillSet(List.of(skill1, skill2));
        when(employeeService.getById(eq(employeeId))).thenReturn(dto);

        mockMvc.perform(post("/projects/{id}/add/{employeeId}/qualification/{qualificationId}",
                        projectId, employeeId, requiredQualificationId)
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        var reloaded = projectRepository.findById(projectId).orElseThrow();
        assertThat(reloaded.getProjectEmployeesIds()).contains(employeeId);

        verify(employeeService).isEmployeeIdValid(employeeId);
        verify(employeeService).getById(employeeId);

    }

    @Test
    void addEmployee_projectNotFound() throws Exception {
        Long projectId = 999L;
        Long employeeId = 42L;
        Long requiredQualificationId = 5L;

        when(employeeService.isEmployeeIdValid(eq(employeeId))).thenReturn(true);

        mockMvc.perform(post("/projects/{id}/add/{employeeId}/qualification/{qualificationId}",
                        projectId, employeeId, requiredQualificationId)
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void addEmployee_employeeNotFound() throws Exception {
        var project = new ProjectEntity();
        project.setName("Test Project");
        project.setProjectEmployeesIds(new ArrayList<>());
        project = projectRepository.save(project);

        Long projectId = project.getProjectId();
        Long employeeId = 6L;
        Long qualificationId = 3L;

        when(employeeService.isEmployeeIdValid(eq(employeeId))).thenReturn(false);

        mockMvc.perform(post("/projects/{id}/add/{employeeId}/qualification/{qualificationId}",
                        projectId, employeeId, qualificationId)
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }
}
