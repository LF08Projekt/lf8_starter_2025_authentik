package de.szut.lf8_starter.skill;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkillDto {
    private Long id;
    private String skill;
}
