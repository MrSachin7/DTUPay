package services;

import dto.GenerateReportsResponse;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;


public class ReportService {
    private static final String BASE_URL = "http://localhost:8080/reports/";

    public GenerateReportsResponse generateReports() throws Exception {
        try(Client client = ClientBuilder.newClient()){
            WebTarget target = client.target(BASE_URL);

            try(Response response = target.request().get()){
                int statusCode = response.getStatus();
                System.out.println("Response status code: " + statusCode);

                if (statusCode < 200 || statusCode > 300) {
                    throw new Exception("Failed to register customer");
                }

                return response.readEntity(GenerateReportsResponse.class);
            }
        }
    }
}