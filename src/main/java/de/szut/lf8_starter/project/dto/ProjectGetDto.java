package de.szut.lf8_starter.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectGetDto {

    private Long projectId;

    private String name;

    private Long responsibleEmployeeId;

    private Long customerId;

    private String responsibleCustomerName;

    private String comment;

    private LocalDate startDate;

    private LocalDate plannedEndDate;

    private LocalDate actualEndDate;

    private List<Long> projectEmployeesIds;
}