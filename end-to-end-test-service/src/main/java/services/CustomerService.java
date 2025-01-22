package services;

import dto.GenerateTokenResponse;
import dto.RegisterCustomerRequest;
import dto.RegisterCustomerResponse;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

public class CustomerService {
    private static final String BASE_URL = "http://localhost:8080/customers/";

    public RegisterCustomerResponse registerCustomer(RegisterCustomerRequest request){
        try(Client client = ClientBuilder.newClient()){
            WebTarget target = client.target(BASE_URL);

            try(Response response = target.request().post(Entity.entity(request, "application/json"))){
               int statusCode = response.getStatus();
                System.out.println("Response status code: " + statusCode);

                if (statusCode < 200 || statusCode > 300) {
                    throw new RuntimeException("Failed to register customer");
                }

                return response.readEntity(RegisterCustomerResponse.class);
            }
        }

    }

    public void unregisterCustomer(String customerId){
        try(Client client = ClientBuilder.newClient()){
            WebTarget target = client.target(BASE_URL).path(customerId);

            try(Response response = target.request().delete()){
                int statusCode = response.getStatus();
                String responseString = response.readEntity(String.class);

                System.out.println("Response status code: " + statusCode);
                System.out.println("Response: " + responseString);

                if (statusCode < 200 || statusCode > 300) {
                    throw new RuntimeException("Failed to unregister customer");
                }
            }
        }
    }

    public GenerateTokenResponse generateToken(String customerId, int amount){
        try(Client client = ClientBuilder.newClient()){
            WebTarget target = client.target(BASE_URL).path(customerId + "/tokens").queryParam("amount", amount);

            try(Response response = target.request().post(Entity.entity("", "application/json"))){
                int statusCode = response.getStatus();
                System.out.println("Response status code: " + statusCode);

                if (statusCode < 200 || statusCode > 300) {
                    throw new RuntimeException("Failed to generate token");
                }

                return response.readEntity(GenerateTokenResponse.class);
            }
        }
    }


}
