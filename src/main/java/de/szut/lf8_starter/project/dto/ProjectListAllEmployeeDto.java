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
public class ProjectListAllEmployeeDto {

    private Long employeeId;

    private String lastname;

    private String firstname;

    private List<SkillDto> skillSet;
}