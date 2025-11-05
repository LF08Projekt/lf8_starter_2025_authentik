package de.szut.lf8_starter.employee;

import de.szut.lf8_starter.employee.dto.EmployeeDto;
import de.szut.lf8_starter.employee.dto.EmployeeInfoDto;
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

    private String baseUrl = "https://employee-api.szut.dev";


    public EmployeeDto getById(Long employeeId) {
        String url = baseUrl + "/employees/" + employeeId;


        ResponseEntity<EmployeeDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                getHttpEntityWithToken(),
                EmployeeDto.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }

        return null;


    }

    public EmployeeInfoDto getEmployeeInfoById(Long employeeId) {
        String url = baseUrl + "/employees/" + employeeId;


        ResponseEntity<EmployeeInfoDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                getHttpEntityWithToken(),
                EmployeeInfoDto.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        return null;


    }


    private HttpEntity<Void> getHttpEntityWithToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Objects.requireNonNull(AuthenticationService.getCurrentJWT()));
        return new HttpEntity<>(headers);
    }

    public boolean isEmployeeIdValid(Long employeeId) {
        String url = this.baseUrl + "/employees/" + employeeId;

        try {
            ResponseEntity<EmployeeDto> response = this.restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    getHttpEntityWithToken(),
                    EmployeeDto.class
            );
            return response.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException.NotFound exception) {
            return false;
        }


    }

}



