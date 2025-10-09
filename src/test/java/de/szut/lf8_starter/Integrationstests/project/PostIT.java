package de.szut.lf8_starter.Integrationstests.project;

import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

public class PostIT extends AbstractIntegrationTest {
    @Test
    void postProject() throws Exception {
        String projectAsString = """
                {"name": "TestProject",
                "responsibleEmployeeId": "1",
                "customerId": "1",
                "responsibleCustomerName":"Armin",
                "comment":"nichts",
                "startDate": "2025,12,12",
                "plannedEndDate":"2027,12,12",
                "projectEmployeesIds":{2,3}
                }""";

    }
}
