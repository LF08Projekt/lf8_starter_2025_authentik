package de.szut.lf8_starter.employee;

import de.szut.lf8_starter.employee.dto.EmployeeDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class EmployeeService {

    private final RestTemplate restTemplate;
    private String baseUrl = "https://employee-api.szut.dev";

    public EmployeeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public EmployeeDto getById(Long employeeId) {
        try {
            return restTemplate.getForObject(
                    baseUrl + "/employees/{employeeId}",
                    EmployeeDto.class,
                    employeeId
            );
        } catch (Exception e) {
            return null;
        }
    }

/*    public boolean isEmployeeIdValid(Long employeeID) {
    return getById(employeeID).getId().equals(employeeID);
}*/
public boolean isEmployeeIdValid(Long employeeId) {
    var dto = getById(employeeId);
    return dto != null && Objects.equals(dto.getId(), employeeId);
}

}

