package de.szut.lf8_starter.employee;

import de.szut.lf8_starter.employee.dto.EmployeeDto;
import de.szut.lf8_starter.security.AuthenticationService;
import lombok.Data;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
@Data
@Service
public class EmployeeService {

    private final RestTemplate restTemplate;

    private String url = "https://employee-api.szut.dev/employees";


    public EmployeeDto getById(Long employeeId) {
        HttpEntity<Void> entity = getHttpEntityWithToken();
        String url = this.url + "/" + employeeId;

        try {
            ResponseEntity<EmployeeDto> response = this.restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    EmployeeDto.class
            );
            return response.getBody();
        } catch (HttpClientErrorException.NotFound exception) {
            return null;
        }
    }


    private HttpEntity<Void> getHttpEntityWithToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Objects.requireNonNull(AuthenticationService.getCurrentJWT()));
        return new HttpEntity<>(headers);
    }

    public boolean isEmployeeIdValid(Long employeeId) {
        var dto = getById(employeeId);
        return dto != null && Objects.equals(dto.getId(), employeeId);
    }



   /* private final RestTemplate restTemplate;
    private String baseUrl = "https://employee-api.szut.dev/";

    public EmployeeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public EmployeeDto getById(Long employeeId) {
        try {
            return restTemplate.getForObject(
                    baseUrl + "employees/{employeeId}",
                    EmployeeDto.class,
                    employeeId
            );
        } catch (Exception e) {
            return null;
        }
    }

*//*    public boolean isEmployeeIdValid(Long employeeID) {
        return getById(employeeID).getId().equals(employeeID);
    }*//*

    public boolean isEmployeeIdValid(Long employeeId) {
        var dto = getById(employeeId);
        return dto != null && Objects.equals(dto.getId(), employeeId);
    }*/
}

