package de.szut.lf8_starter.Integrationstests.project;

import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostIT extends AbstractIntegrationTest {

    @Test
    void postProject() throws Exception {
        String project = """
                {
                  "name": "TestProject",
                  "responsibleEmployeeId": 1,
                  "customerId": 1,
                  "responsibleCustomerName": "Armin",
                  "comment": "nichts",
                  "startDate": "2025-12-12",
                  "plannedEndDate": "2027-12-12"
                }
                """;

        final var projectAsString = this.mockMvc.perform(
                        post("/projects")
                                .with(jwt()) // falls Security aktiv
                                .content(project)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("TestProject")))
                .andExpect(jsonPath("$.responsibleEmployeeId", is(1)))
                .andExpect(jsonPath("$.customerId", is(1)))
                .andExpect(jsonPath("$.responsibleCustomerName", is("Armin")))
                .andExpect(jsonPath("$.comment", is("nichts")))
                .andExpect(jsonPath("$.startDate", is("2025-12-12")))
                .andExpect(jsonPath("$.plannedEndDate", is("2027-12-12")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var id = Long.parseLong(new JSONObject(projectAsString).get("projectId").toString());

        final var loadedEntity = projectRepository.findById(id);
        assertThat(loadedEntity).isPresent();
        assertThat(loadedEntity.get().getName()).isEqualTo("TestProject");
        assertThat(loadedEntity.get().getResponsibleEmployeeId()).isEqualTo(1L);
        assertThat(loadedEntity.get().getCustomerId()).isEqualTo(1L);
        assertThat(loadedEntity.get().getResponsibleCustomerName()).isEqualTo("Armin");
        assertThat(loadedEntity.get().getComment()).isEqualTo("nichts");
        assertThat(loadedEntity.get().getStartDate()).isEqualTo(LocalDate.parse("2025-12-12"));
        assertThat(loadedEntity.get().getPlannedEndDate()).isEqualTo(LocalDate.parse("2027-12-12"));
    }
}
