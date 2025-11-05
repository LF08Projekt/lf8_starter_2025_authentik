package de.szut.lf8_starter.Integrationstests.project;

import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UpdateIT extends AbstractIntegrationTest {

    @MockBean
    private EmployeeService employeeService;

    @Test
    void updateProjectSuccess() throws Exception {
        when(employeeService.isEmployeeIdValid(eq(1L))).thenReturn(true);
        when(employeeService.isEmployeeIdValid(eq(2L))).thenReturn(true);

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

        final var id =
                Long.parseLong(new JSONObject(response.getResponse().getContentAsString()).get("projectId").toString());

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
                .andExpect(jsonPath("$.actualEndDate", is("2025-12-14")));
    }

    @Test
    void updateProjectNotFoundWithWrongId() throws Exception {
        when(employeeService.isEmployeeIdValid(eq(2L))).thenReturn(true);

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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProject))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",
                        containsString("ProjectEntity not found on id = 9876")));
    }
}