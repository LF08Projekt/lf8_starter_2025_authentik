package de.szut.lf8_starter.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectCreateDto {
    @NotBlank(message = "Name cannot be empty!")
    private String name;

    @NotNull(message = "ResponsibleEmployeeId is mandatory!")
    private Long responsibleEmployeeId;

    @NotNull(message = "CustomerId is mandatory!")
    private Long customerId;

    @NotBlank(message = "ResponsibleCustomerName is mandatory!")
    private String responsibleCustomerName;

    @NotBlank(message = "Comment cannot be empty!")
    private String comment;

    @NotNull(message ="Startdate is mandatory!")
    private LocalDate startDate;

    @NotNull(message = "PlannedEndDate is mandatory!")
    private LocalDate plannedEndDate;
}
