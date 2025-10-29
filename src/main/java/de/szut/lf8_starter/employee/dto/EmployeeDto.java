package de.szut.lf8_starter.employee.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeDto {


    private Long id;
    private List<Long> qualifications;

}
