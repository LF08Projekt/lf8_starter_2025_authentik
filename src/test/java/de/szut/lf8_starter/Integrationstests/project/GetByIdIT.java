package de.szut.lf8_starter.Integrationstests.project;

import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class GetByIdIT extends AbstractIntegrationTest {

    @Test
    @WithMockUser(roles = "user")
    void findProjectById_HappyPath() throws Exception {
        ProjectEntity project = new ProjectEntity();
        project.setName("Test Project");
        project.setResponsibleEmployeeId(1L);
        project.setCustomerId(1L);
        project.setResponsibleCustomerName("Jan");
        project.setStartDate(LocalDate.of(2025, 12, 12));
        project.setPlannedEndDate(LocalDate.of(2027, 12, 12));
        ProjectEntity savedProject = projectRepository.save(project);

        this.mockMvc.perform(get("/projects/" + savedProject.getProjectId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId",
                        is(savedProject.getProjectId().intValue())))
                .andExpect(jsonPath("$.name", is("Test Project")))
                .andExpect(jsonPath("$.responsibleEmployeeId", is(1)))
                .andExpect(jsonPath("$.customerId", is(1)))
                .andExpect(jsonPath("$.responsibleCustomerName", is("Jan")))
                .andExpect(jsonPath("$.startDate", is("2025-12-12")))
                .andExpect(jsonPath("$.plannedEndDate", is("2027-12-12")));
    }

    @Test
    @WithMockUser(roles = "user")
    void findProjectById_NotFound() throws Exception {
        long nonExistentId = 99999L;

        this.mockMvc.perform(get("/projects/" + nonExistentId)
                        .with(csrf()))
                .andExpect(content().string(containsString("ProjectEntity " +
                        "not found on id = 99999")))
                .andExpect(status().isNotFound());
    }
}