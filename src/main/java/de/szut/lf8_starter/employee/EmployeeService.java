package de.szut.lf8_starter.employee;

import de.szut.lf8_starter.employee.dto.EmployeeDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeService {

    private final RestTemplate restTemplate;
    private final String baseUrl = "https://employee-api.szut.dev/";

    public EmployeeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isEmployeeValid(Long employeeId) {
        try {
            EmployeeDto employee = restTemplate.getForObject(
                    baseUrl + "employees/" + employeeId,
                    EmployeeDto.class
            );
            return employee == null;
        } catch (HttpClientErrorException.NotFound e) {
            return true;
        }
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
}

