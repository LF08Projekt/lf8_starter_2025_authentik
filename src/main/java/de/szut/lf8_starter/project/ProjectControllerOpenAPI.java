package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import de.szut.lf8_starter.project.dto.ProjectUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface ProjectControllerOpenAPI {

    @Operation(
            summary = "creates a new project with its id and project information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "created project",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ProjectGetDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "invalid JSON posted",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content)})
    ProjectGetDto create(ProjectCreateDto projectCreateDto);


    @Operation(summary = "deletes an existing project by id " +
            "information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "delete " +
                    "successful"),
            @ApiResponse(responseCode = "401", description = "not authorized" +
                    "posted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "resource not " +
                    "found",
                    content = @Content)})
    void deleteProjectById(long id);


    @Operation(summary = "updates a project by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "project updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectUpdateDto.class))}),
            @ApiResponse(responseCode = "404", description = "project not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "invalid JSON posted",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content)})
    ProjectGetDto updateProject(long id, ProjectUpdateDto projectUpdateDto);

}
