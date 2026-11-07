
package com.klef.soa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class SupplierIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DiscoveryClient discoveryClient;


    // =========================================================
    // COMMON METHOD
    // LOGIN AND GET JWT TOKEN
    // =========================================================

    private String getJWTToken() throws Exception {

        String email = "admin@gmail.com";
        String password = "admin";

        String loginRequest =
                "{"
                + "\"email\":\"" + email + "\","
                + "\"password\":\"" + password + "\""
                + "}";

        MvcResult result =
                mockMvc.perform(
                        post("/supplier/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginRequest)
                )
                .andExpect(status().isOk())
                .andReturn();

        String response =
                result.getResponse().getContentAsString();

        JsonNode jsonNode =
                objectMapper.readTree(response);

        JsonNode tokenNode = jsonNode.get("token");

        assertNotNull(tokenNode, "JWT token was not generated");

        String token = tokenNode.asText();

        assertFalse(token.isBlank(), "JWT token is empty");

        return token;
    }


    // =========================================================
    // TEST CASE 1
    // VALID WORKFLOW
    //
    // Login -> JWT -> Protected API -> Success
    // =========================================================

    @Test
    void testValidWorkflow() throws Exception {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("TEST CASE 1 : VALID WORKFLOW");
        System.out.println("==========================================");

        String token = getJWTToken();

        System.out.println("JWT generated successfully");
        System.out.println("Token : " + token);

        MvcResult result =
                mockMvc.perform(
                        get("/supplier/displayall")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk())
                .andReturn();

        String response =
                result.getResponse().getContentAsString();

        System.out.println();
        System.out.println("Protected API : /supplier/displayall");
        System.out.println("Status Code : "
                + result.getResponse().getStatus());
        System.out.println("Response : " + response);

        assertNotNull(response);
        assertFalse(response.isBlank());

        System.out.println();
        System.out.println("TC-1 PASSED");
        System.out.println(
                "Valid credentials -> JWT -> Protected API -> Success");
    }


    // =========================================================
    // TEST CASE 2
    // INVALID JWT
    //
    // Expected : 401 Unauthorized
    // =========================================================

    @Test
    void testInvalidJWT() throws Exception {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("TEST CASE 2 : INVALID JWT");
        System.out.println("==========================================");

        String invalidToken = "invalid-jwt-token";

        MvcResult result =
                mockMvc.perform(
                        get("/supplier/displayall")
                                .header(
                                        "Authorization",
                                        "Bearer " + invalidToken
                                )
                )
                .andReturn();

        int status =
                result.getResponse().getStatus();

        String response =
                result.getResponse().getContentAsString();

        System.out.println("Status Code : " + status);
        System.out.println("Response : " + response);

        assertEquals(401, status);

        System.out.println();
        System.out.println("TC-2 PASSED : INVALID JWT -> 401");
    }


    // =========================================================
    // TEST CASE 3
    // NO JWT TOKEN
    //
    // Expected : 401 Unauthorized
    // =========================================================

    @Test
    void testNoJWTToken() throws Exception {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("TEST CASE 3 : NO JWT TOKEN");
        System.out.println("==========================================");

        MvcResult result =
                mockMvc.perform(
                        get("/supplier/displayall")
                )
                .andReturn();

        int status =
                result.getResponse().getStatus();

        String response =
                result.getResponse().getContentAsString();

        System.out.println("Status Code : " + status);
        System.out.println("Response : " + response);

        assertEquals(401, status);

        System.out.println();
        System.out.println("TC-3 PASSED : NO JWT -> 401");
    }


    // =========================================================
    // TEST CASE 4
    // SERVICE-TO-SERVICE COMMUNICATION
    //
    // SupplierService -> Feign -> Eureka -> ProductService
    // =========================================================

    @Test
    void testServiceToServiceCommunication() throws Exception {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("TEST CASE 4 : SERVICE COMMUNICATION");
        System.out.println("==========================================");

        String token = getJWTToken();

        MvcResult result =
                mockMvc.perform(
                        get("/supplier/product/test")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk())
                .andReturn();

        String response =
                result.getResponse().getContentAsString();

        System.out.println("Supplier -> ProductService");
        System.out.println("Response : " + response);

        assertEquals(
                "Product Service Communication Successful",
                response
        );

        System.out.println(
                "TC-4 PASSED : SERVICE-TO-SERVICE COMMUNICATION");
    }


    // =========================================================
    // TEST CASE 5
    // LOAD BALANCING CHECK
    //
    // Multiple ProductService instances must be running.
    //
    // SupplierService
    //       |
    //     Feign
    //       |
    //     Eureka
    //       |
    //  LoadBalancer
    //       |
    // ProductService instances
    // =========================================================

    @Test
    void testLoadBalancing() throws Exception {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("TEST CASE 5 : LOAD BALANCING CHECK");
        System.out.println("==========================================");

        List<ServiceInstance> instances =
                discoveryClient.getInstances("ProductService");

        System.out.println();
        System.out.println(
                "ProductService instances registered in Eureka:");

        for (ServiceInstance instance : instances) {

            System.out.println(
                    "Instance : " + instance.getInstanceId());

            System.out.println(
                    "Host : " + instance.getHost());

            System.out.println(
                    "Port : " + instance.getPort());

            System.out.println(
                    "URI : " + instance.getUri());

            System.out.println("--------------------------------");
        }

        assertTrue(
                instances.size() >= 2,
                "At least two ProductService instances must be running"
        );

        String token = getJWTToken();

        Set<String> visitedInstances = new HashSet<>();

        for (int i = 1; i <= 10; i++) {

            MvcResult result =
                    mockMvc.perform(
                            get("/supplier/product/instance")
                                    .header(
                                            "Authorization",
                                            "Bearer " + token
                                    )
                    )
                    .andExpect(status().isOk())
                    .andReturn();

            String response =
                    result.getResponse().getContentAsString();

            JsonNode jsonNode =
                    objectMapper.readTree(response);

            String instance =
                    jsonNode.get("instance").asText();

            visitedInstances.add(instance);

            System.out.println();
            System.out.println("Request " + i);
            System.out.println("Response : " + response);
            System.out.println("Served by : " + instance);
        }

        assertTrue(
                visitedInstances.size() >= 2,
                "Requests were not distributed across multiple instances"
        );

        System.out.println();
        System.out.println("TC-5 PASSED : LOAD BALANCING");
        System.out.println("Instances reached : " + visitedInstances);
    }


    // =========================================================
    // TEST CASE 6
    // SERVICE DISCOVERY / INSTANCE FAILURE
    //
    // MANUAL SETUP:
    // 1. Start Eureka
    // 2. Start ProductService instance 1
    // 3. Start ProductService instance 2
    // 4. Verify both are registered
    // 5. Stop ONE ProductService instance
    // 6. Wait for Eureka to update
    // 7. Run this test
    //
    // Expected:
    // Remaining ProductService instance handles request
    // =========================================================

    @Test
    void testServiceDiscoveryFailure() throws Exception {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("TEST CASE 6 : SERVICE DISCOVERY FAILURE");
        System.out.println("==========================================");

        List<ServiceInstance> instances =
                discoveryClient.getInstances("ProductService");

        System.out.println();
        System.out.println(
                "Currently available ProductService instances:");

        for (ServiceInstance instance : instances) {

            System.out.println(
                    instance.getInstanceId()
                    + " -> "
                    + instance.getUri()
            );
        }

        assertTrue(
                instances.size() >= 1,
                "At least one ProductService instance must be available"
        );

        String token = getJWTToken();

        MvcResult result =
                mockMvc.perform(
                        get("/supplier/product/test")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andReturn();

        int status =
                result.getResponse().getStatus();

        String response =
                result.getResponse().getContentAsString();

        System.out.println();
        System.out.println("Status Code : " + status);
        System.out.println("Response : " + response);

        assertEquals(200, status);

        System.out.println();
        System.out.println(
                "TC-6 PASSED : SYSTEM CONTINUED USING AVAILABLE INSTANCE");
    }


    // =========================================================
    // TEST CASE 7
    // PUBLIC SUPPLIER API
    // =========================================================

    @Test
    void testPublicSupplierAPI() throws Exception {

        mockMvc.perform(
                get("/supplier/")
        )
        .andExpect(status().isOk());

        System.out.println(
                "TC-7 PASSED : PUBLIC SUPPLIER API");
    }

}
