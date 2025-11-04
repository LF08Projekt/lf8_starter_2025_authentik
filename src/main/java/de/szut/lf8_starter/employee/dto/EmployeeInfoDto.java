package de.szut.lf8_starter.employee.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.szut.lf8_starter.skill.SkillDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeInfoDto {

    private Long id;

    private String lastName;

    private String firstName;

    private List<SkillDto> skillSet;
}