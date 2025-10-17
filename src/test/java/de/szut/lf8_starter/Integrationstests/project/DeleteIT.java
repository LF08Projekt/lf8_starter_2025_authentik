package de.szut.lf8_starter.Integrationstests.project;

import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.containsString;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class DeleteIT extends AbstractIntegrationTest {

    @Test
    @WithMockUser(roles = "user")
    void deleteProjectSuccess() throws Exception {
        ProjectEntity project = projectRepository.save(new ProjectEntity());

        this.mockMvc.perform(
                delete("/projects/" +
                        project.getProjectId()).with(csrf()))
                .andExpect(status().isOk());
        assertThat(projectRepository.findById(project.getProjectId()).isPresent()).isFalse();
    }

    @Test
    @WithMockUser(roles = "user")
    void deleteProject() throws Exception {
        this.mockMvc.perform(delete("/projects" +
                        "/9876")
                .with(csrf()))
                .andExpect(content().string(containsString("ProjectEntity " +
                        "not found on id = 9876")))
                .andExpect(status().isNotFound());
    }
}
