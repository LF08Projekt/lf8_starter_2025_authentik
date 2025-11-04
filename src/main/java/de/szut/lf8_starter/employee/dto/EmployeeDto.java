package de.szut.lf8_starter.employee.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.szut.lf8_starter.skill.SkillDto;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeDto {

    private Long id;
    private List<SkillDto> skillSet;

    public List<Long> getQualifications() {
        if (skillSet == null) {
            return List.of();
        }
        return skillSet.stream().map(SkillDto::getId).collect(Collectors.toList());
    }
}
