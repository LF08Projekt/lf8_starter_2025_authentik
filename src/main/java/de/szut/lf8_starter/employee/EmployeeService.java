package de.szut.lf8_starter.employee;

import de.szut.lf8_starter.employee.dto.EmployeeDto;
import de.szut.lf8_starter.security.AuthenticationService;
import lombok.Data;
import org.springframework.http.*;
import de.szut.lf8_starter.employee.dto.EmployeeInfoDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Data
@Service
public class EmployeeService {

    private final RestTemplate restTemplate;

    private String baseUrl = "https://employee-api.szut.dev";


    public EmployeeDto getById(Long employeeId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AuthenticationService.getCurrentJWT());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = baseUrl + "/employees/" + employeeId;

        try {
            ResponseEntity<EmployeeDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    EmployeeDto.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {

                return null;
            }
        } catch (Exception e) {

            return null;
        }
    }

    public EmployeeInfoDto getEmployeeInfoById(Long employeeId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AuthenticationService.getCurrentJWT());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = baseUrl + "/employees/" + employeeId;

        try {
            ResponseEntity<EmployeeInfoDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    EmployeeInfoDto.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (Exception e) {

            return null;
        }
    }


    private HttpEntity<Void> getHttpEntityWithToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AuthenticationService.getCurrentJWT());
        return new HttpEntity<>(headers);
    }

    public boolean isEmployeeIdValid(Long employeeId) {
        HttpEntity<Void> entity = getHttpEntityWithToken();
        String url = this.baseUrl + "/employees/" + employeeId;

        try {
            ResponseEntity<EmployeeDto> response = this.restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    EmployeeDto.class
            );
            return response.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException.NotFound exception) {
            return false;
        }


    }

}



