package de.szut.lf8_starter.Integrationstests.project;

import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.project.ProjectRepository;
import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RemoveEmployeeFromProjectIT extends AbstractIntegrationTest {

    @Autowired
    ProjectRepository projectRepository;

    @MockBean
    EmployeeService employeeService;

    @Transactional
    @Test
    void removeEmployee_isOk() throws Exception {
        var project = new ProjectEntity();
        project.setName("Test Project");
        project.setProjectEmployeesIds(new ArrayList<>(List.of(1L, 23L, 4L)));
        project = projectRepository.save(project);

        Long projectId = project.getProjectId();
        Long employeeId = 23L;

        when(employeeService.isEmployeeIdValid(eq(employeeId))).thenReturn(true);

        mockMvc.perform(delete("/projects/{projectId}/remove/{employeeId}", projectId, employeeId)
                        .with(jwt()))
                .andExpect(status().isOk());

        var reloaded = projectRepository.findById(projectId).orElseThrow();
        assertThat(reloaded.getProjectEmployeesIds()).containsExactly(1L, 4L);
    }

    @Test
    void removeEmployee_projectNotFound() throws Exception {
        when(employeeService.isEmployeeIdValid(42L)).thenReturn(true);

        mockMvc.perform(delete("/projects/{projectId}/remove/{employeeId}", 9999L, 42L)
                        .with(jwt()))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeEmployee_employeeInvalid() throws Exception {
        var project = new ProjectEntity();
        project.setName("Test Project");
        project.setProjectEmployeesIds(new ArrayList<>(List.of(4L)));
        project = projectRepository.save(project);

        when(employeeService.isEmployeeIdValid(4L)).thenReturn(false);

        mockMvc.perform(delete("/projects/{projectId}/remove/{employeeId}", project.getProjectId(), 4L)
                        .with(jwt()))
                .andExpect(status().isNotFound());
    }
}
