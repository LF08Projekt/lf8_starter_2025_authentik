package de.szut.lf8_starter.Integrationstests.project;

import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.project.ProjectService;
import de.szut.lf8_starter.project.ProjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private ProjectMapper projectMapper;

    @BeforeEach
    void setUp() {
        reset(employeeService, projectService, projectMapper);
    }

    @Test
    void findAllProjectsOfEmployee_shouldReturnProjectList_whenEmployeeExists() throws Exception {
        Long employeeId = 1L;

        ProjectEntity project1 = new ProjectEntity();
        project1.setProjectId(10L);
        project1.setName("Project Alpha");
        project1.setResponsibleEmployeeId(2L);
        project1.setCustomerId(100L);
        project1.setStartDate(LocalDate.of(2024, 1, 1));
        project1.setProjectEmployeesIds(Arrays.asList(1L, 2L, 3L));

        ProjectEntity project2 = new ProjectEntity();
        project2.setProjectId(20L);
        project2.setName("Project Beta");
        project2.setResponsibleEmployeeId(3L);
        project2.setCustomerId(101L);
        project2.setStartDate(LocalDate.of(2024, 2, 1));
        project2.setProjectEmployeesIds(Arrays.asList(1L, 4L));

        List<ProjectEntity> projectEntities = Arrays.asList(project1, project2);

        ProjectGetDto dto1 = new ProjectGetDto(
                10L, "Project Alpha", 2L, 100L, "Customer A",
                "Comment 1", LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31), null,
                Arrays.asList(1L, 2L, 3L)
        );

        ProjectGetDto dto2 = new ProjectGetDto(
                20L, "Project Beta", 3L, 101L, "Customer B",
                "Comment 2", LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 11, 30), null,
                Arrays.asList(1L, 4L)
        );

        when(employeeService.isEmployeeIdValid(employeeId)).thenReturn(true);
        when(projectService.listAllProjectsForEmployee(employeeId)).thenReturn(projectEntities);
        when(projectMapper.mapEntityToGetDto(project1)).thenReturn(dto1);
        when(projectMapper.mapEntityToGetDto(project2)).thenReturn(dto2);

        // Act & Assert
        mockMvc.perform(get("/projects/employee/{id}", employeeId)
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].projectId", is(10)))
                .andExpect(jsonPath("$[0].name", is("Project Alpha")))
                .andExpect(jsonPath("$[0].responsibleEmployeeId", is(2)))
                .andExpect(jsonPath("$[0].customerId", is(100)))
                .andExpect(jsonPath("$[0].responsibleCustomerName", is("Customer A")))
                .andExpect(jsonPath("$[0].projectEmployeesIds", hasSize(3)))
                .andExpect(jsonPath("$[1].projectId", is(20)))
                .andExpect(jsonPath("$[1].name", is("Project Beta")));
    }

    @Test
    void findAllProjectsOfEmployee_shouldReturnEmptyList_whenEmployeeHasNoProjects() throws Exception {
        Long employeeId = 5L;

        when(employeeService.isEmployeeIdValid(employeeId)).thenReturn(true);
        when(projectService.listAllProjectsForEmployee(employeeId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/projects/employee/{id}", employeeId)
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void findAllProjectsOfEmployee_shouldThrowNotFoundException_whenEmployeeDoesNotExist() throws Exception {
        Long employeeId = 999L;

        when(employeeService.isEmployeeIdValid(employeeId)).thenReturn(false);

        mockMvc.perform(get("/projects/employee/{id}", employeeId)
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllProjectsOfEmployee_shouldReturnOneProject_whenEmployeeHasOneProject() throws Exception {
        Long employeeId = 3L;

        ProjectEntity project = new ProjectEntity();
        project.setProjectId(15L);
        project.setName("Single Project");
        project.setResponsibleEmployeeId(3L);
        project.setCustomerId(102L);
        project.setStartDate(LocalDate.of(2024, 3, 1));
        project.setProjectEmployeesIds(Collections.singletonList(3L));

        ProjectGetDto dto = new ProjectGetDto(
                15L, "Single Project", 3L, 102L, "Customer C",
                "Solo project", LocalDate.of(2024, 3, 1),
                LocalDate.of(2024, 9, 30), null,
                Collections.singletonList(3L)
        );

        when(employeeService.isEmployeeIdValid(employeeId)).thenReturn(true);
        when(projectService.listAllProjectsForEmployee(employeeId)).thenReturn(Collections.singletonList(project));
        when(projectMapper.mapEntityToGetDto(project)).thenReturn(dto);

        mockMvc.perform(get("/projects/employee/{id}", employeeId)
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].projectId", is(15)))
                .andExpect(jsonPath("$[0].name", is("Single Project")))
                .andExpect(jsonPath("$[0].projectEmployeesIds", hasSize(1)))
                .andExpect(jsonPath("$[0].projectEmployeesIds[0]", is(3)));
    }

    @Test
    void findAllProjectsOfEmployee_shouldReturn401_whenNotAuthenticated() throws Exception {
        Long employeeId = 1L;

        mockMvc.perform(get("/projects/employee/{id}", employeeId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
