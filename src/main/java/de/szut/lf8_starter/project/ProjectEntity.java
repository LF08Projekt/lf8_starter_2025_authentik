package de.szut.lf8_starter.project;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
@Data
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long projectId;

    @NotBlank(message = "Name cannot be empty!")
    private String name;

    @NotNull
    private Long responsibleEmployeeId;

    @NotNull
    private Long customerId;

    @NotBlank
    private String responsibleCustomerName;

    private String comment;

    @NotBlank(message = "Start date is mandatory!")
    private Date startDate;

    @NotNull(message = "End date is mandatory!")
    private Date plannedEndDate;

    private Date actualEndDate;

    @ElementCollection
    private List<Long> projectEmployeesIds;


}
