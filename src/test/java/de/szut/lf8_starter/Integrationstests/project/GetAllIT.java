package de.szut.lf8_starter.Integrationstests.project;

import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


public class GetAllIT extends AbstractIntegrationTest {

    @Test
    @WithMockUser(roles = "user")
    void findAllProjects() throws Exception {

        ProjectEntity project1 = new ProjectEntity();
        project1.setName("Test 1");
        project1.setResponsibleEmployeeId(1L);
        project1.setCustomerId(1L);
        project1.setResponsibleCustomerName("Jan");
        projectRepository.save(project1);

        ProjectEntity project2 = new ProjectEntity();
        project2.setName("Test 2");
        project2.setResponsibleEmployeeId(2L);
        project2.setCustomerId(2L);
        project2.setResponsibleCustomerName("Yana");
        projectRepository.save(project2);

        this.mockMvc.perform(get("/projects")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Test 1")))
                .andExpect(jsonPath("$[1].name", is("Test 2")));
    }

    @Test
    @WithMockUser(roles = "user")
    void findAllProjects_EmptyList() throws Exception {

        this.mockMvc.perform(get("/projects")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
