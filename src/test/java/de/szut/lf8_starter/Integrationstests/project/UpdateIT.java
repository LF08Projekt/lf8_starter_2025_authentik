package de.szut.lf8_starter.Integrationstests.project;

import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UpdateIT extends AbstractIntegrationTest {
    @Test
    @WithMockUser(roles = "user")
    void updateProjectSuccess() throws Exception {
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

        var response = this.mockMvc.perform(
                        post("/projects")
                                .with(jwt())
                                .content(project)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String createdBody = response.getResponse().getContentAsString();
        final var id =
                Long.parseLong(new JSONObject(createdBody).get("projectId").toString());


        String updatedProject = """
                {
                  "name": "UpdatedProject",
                  "responsibleEmployeeId": 2,
                  "customerId": 1,
                  "responsibleCustomerName": "Jan",
                  "comment": "nichts",
                  "startDate": "2025-12-10",
                  "plannedEndDate": "2027-12-13",
                  "actualEndDate": "2025-12-14"
                }
                """;

        this.mockMvc.perform(put("/projects/{id}", id)
                        .with(jwt())
                        .content(updatedProject)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("UpdatedProject")))
                .andExpect(jsonPath("$.responsibleEmployeeId", is(2)))
                .andExpect(jsonPath("$.customerId", is(1)))
                .andExpect(jsonPath("$.responsibleCustomerName", is("Jan")))
                .andExpect(jsonPath("$.comment", is("nichts")))
                .andExpect(jsonPath("$.startDate", is("2025-12-10")))
                .andExpect(jsonPath("$.plannedEndDate", is("2027-12-13")))
                .andExpect(jsonPath("$.actualEndDate", is("2025-12-14")))
                .andReturn();
    }


    @Test
    @WithMockUser(roles = "user")
    void updateProjectNotFoundWithWrongId() throws Exception {
        String updatedProject = """
                {
                  "name": "DoesNotExist",
                  "responsibleEmployeeId": 2,
                  "customerId": 1,
                  "responsibleCustomerName": "Jan",
                  "comment": "nichts",
                  "startDate": "2025-12-10",
                  "plannedEndDate": "2027-12-13",
                  "actualEndDate": "2025-12-14"
                }
                """;

        this.mockMvc.perform(put("/projects/9876")
                        .with(jwt())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProject))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(containsString("ProjectEntity not found on id = 9876")));
    }
}


