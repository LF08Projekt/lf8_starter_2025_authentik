package de.szut.lf8_starter.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectAddEmployeeDto {
    @NotNull(message = "An Employee is mandatory")
     private Long newEmployeeId;

    @NotNull(message = "QualificationId is mandatory")
    private Long qualificationId;
}
