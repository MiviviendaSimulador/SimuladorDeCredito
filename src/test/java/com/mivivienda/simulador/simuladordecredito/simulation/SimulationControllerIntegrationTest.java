package com.mivivienda.simulador.simuladordecredito.simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mivivienda.simulador.simuladordecredito.auth.dto.LoginRequest;
import com.mivivienda.simulador.simuladordecredito.auth.dto.RegisterRequest;
import com.mivivienda.simulador.simuladordecredito.simulation.dto.SimulationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SimulationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSimulationWithoutToken() throws Exception {
        SimulationRequest request = SimulationRequest.builder()
                .params("{\"amount\": 100000}")
                .build();

        mockMvc.perform(post("/api/v1/simulations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testSimulationWithToken() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("simulation@example.com")
                .password("password123")
                .fullName("Simulation User")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));

        LoginRequest loginRequest = LoginRequest.builder()
                .email("simulation@example.com")
                .password("password123")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseBody).get("accessToken").asText();

        SimulationRequest simulationRequest = SimulationRequest.builder()
                .params("{\"amount\": 100000, \"term\": 12}")
                .build();

        mockMvc.perform(post("/api/v1/simulations")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(simulationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.params").value("{\"amount\": 100000, \"term\": 12}"));

        mockMvc.perform(get("/api/v1/simulations")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}

