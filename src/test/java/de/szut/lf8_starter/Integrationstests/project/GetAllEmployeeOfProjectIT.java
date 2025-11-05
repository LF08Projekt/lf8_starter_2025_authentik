package de.szut.lf8_starter.Integrationstests.project;

import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.employee.dto.EmployeeInfoDto;
import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.project.ProjectRepository;
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
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetAllEmployeeOfProjectIT extends AbstractIntegrationTest {

    @Autowired
    ProjectRepository projectRepository;

    @MockBean
    EmployeeService employeeService;

    @Transactional
    @Test
    void listEmployeesForProject_returnsEmployeeInfoList() throws Exception {
        var project = new ProjectEntity();
        project.setName("Project With Employees");
        project.setProjectEmployeesIds(new ArrayList<>());
        project.getProjectEmployeesIds().addAll(List.of(101L, 102L));
        project = projectRepository.save(project);

        Long projectId = project.getProjectId();

        var info1 = new EmployeeInfoDto();
        info1.setId(101L);
        info1.setFirstName("Alice");
        info1.setLastName("A");

        var info2 = new EmployeeInfoDto();
        info2.setId(102L);
        info2.setFirstName("Bob");
        info2.setLastName("B");

        when(employeeService.getEmployeeInfoById(eq(101L))).thenReturn(info1);
        when(employeeService.getEmployeeInfoById(eq(102L))).thenReturn(info2);

        var mvcResult = mockMvc.perform(get("/projects/{id}/employees", projectId)
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        assertThat(json).contains("\"id\":101");
        assertThat(json).contains("\"firstName\":\"Alice\"");
        assertThat(json).contains("\"id\":102");
        assertThat(json).contains("\"firstName\":\"Bob\"");

        verify(employeeService).getEmployeeInfoById(101L);
        verify(employeeService).getEmployeeInfoById(102L);
    }

    @Test
    void listEmployeesForProject_projectNotFound_returns404() throws Exception {
        Long nonExistingProjectId = 9999L;

        mockMvc.perform(get("/projects/{id}/employees", nonExistingProjectId)
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Transactional
    @Test
    void listEmployeesForProject_ignoresMissingEmployees() throws Exception {
        var project = new ProjectEntity();
        project.setName("Project With Some Missing Employees");
        project.setProjectEmployeesIds(new ArrayList<>());
        project.getProjectEmployeesIds().addAll(List.of(201L, 202L));
        project = projectRepository.save(project);

        Long projectId = project.getProjectId();

        var info201 = new EmployeeInfoDto();
        info201.setId(201L);
        info201.setFirstName("Carol");

        when(employeeService.getEmployeeInfoById(eq(201L))).thenReturn(info201);
        when(employeeService.getEmployeeInfoById(eq(202L))).thenReturn(null); // missing employee

        var mvcResult = mockMvc.perform(get("/projects/{id}/employees", projectId)
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        assertThat(json).contains("\"id\":201");
        assertThat(json).doesNotContain("\"id\":202");

        verify(employeeService).getEmployeeInfoById(201L);
        verify(employeeService).getEmployeeInfoById(202L);
    }
}
