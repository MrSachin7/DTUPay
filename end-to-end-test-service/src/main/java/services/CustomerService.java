package services;

import dto.RegisterCustomerRequest;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

public class CustomerService {

    private static final String BASE_URL = "http://localhost:8080";

    public void registerCustomer(RegisterCustomerRequest request){
        try(Client client = ClientBuilder.newClient()){
            WebTarget target = client.target(BASE_URL).path("/customers");

            try(Response response = target.request().post(Entity.entity(request, "application/json"))){
               int statusCode = response.getStatus();
               String responseString = response.readEntity(String.class);

                System.out.println("Response status code: " + statusCode);
                System.out.println("Response: " + responseString);

                if (statusCode > 200 && statusCode < 300){
                    return;
                } else {
                    throw new RuntimeException("Failed to register customer");
                }
            }
        }

    }
}
