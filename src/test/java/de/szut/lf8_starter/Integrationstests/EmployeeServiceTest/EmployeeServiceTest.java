package de.szut.lf8_starter.Integrationstests.EmployeeServiceTest;

import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.employee.dto.EmployeeDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    EmployeeService employeeService;

    @Test
    void testGetById_ok() {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(1L);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(EmployeeDto.class)
        )).thenReturn(ResponseEntity.ok(dto));

        EmployeeDto result = employeeService.getById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());

        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(EmployeeDto.class));
    }

    @Test
    void testGetById_notFound() {
        when(restTemplate.exchange(anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(EmployeeDto.class)))
                .thenThrow(HttpClientErrorException.create(
                        HttpStatus.NOT_FOUND, "not found", null, null, null

                ));
        EmployeeDto result = employeeService.getById(999L);

        Assertions.assertNull(result);
    }

    @Test
    void testIsEmployeeIdValid_true() {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(10L);

        when(restTemplate.exchange(
                anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(EmployeeDto.class)
        )).thenReturn(ResponseEntity.ok(dto));

        boolean result = employeeService.isEmployeeIdValid(10L);
        Assertions.assertTrue(result);

    }


    @Test
    void testIsEmployeeIdValid_notFound() {

        when(restTemplate.exchange(
                anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(EmployeeDto.class)

        )).thenThrow(HttpClientErrorException.create(
                HttpStatusCode.valueOf(404),
                "not found",
                null,
                null,
                null
        ));
        boolean result = employeeService.isEmployeeIdValid(10L);
        Assertions.assertFalse(result);
    }
}
