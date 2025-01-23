package services;

import dto.RegisterCustomerRequest;
import dto.RegisterCustomerResponse;
import dto.StartPaymentRequest;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

public class MerchantService {

    private static final String BASE_URL = "http://localhost:8080/merchants/";

    public RegisterCustomerResponse registerMerchant(RegisterCustomerRequest request) throws Exception {
        try(Client client = ClientBuilder.newClient()){
            WebTarget target = client.target(BASE_URL);

            try(Response response = target.request().post(Entity.entity(request, "application/json"))){
                int statusCode = response.getStatus();
                System.out.println("Response status code: " + statusCode);

                if (statusCode < 200 || statusCode > 300) {
                    throw new Exception("Failed to register customer");
                }

                return response.readEntity(RegisterCustomerResponse.class);
            }
        }
    }

    public String pay(String merchantId, StartPaymentRequest request){
        try(Client client = ClientBuilder.newClient()){
            WebTarget target = client.target(BASE_URL).path(merchantId + "/payments");

            try(Response response = target.request().post(Entity.entity(request, "application/json"))){
                int statusCode = response.getStatus();
                String responseString = response.readEntity(String.class);

                System.out.println("Response status code: " + statusCode);
                System.out.println("Response: " + responseString);

                System.out.println("Payment result: "+ responseString);

                 if(!(statusCode >= 200 && statusCode <= 300)){
                     return null;
                }
                return responseString;
            }
        }

    }
}
