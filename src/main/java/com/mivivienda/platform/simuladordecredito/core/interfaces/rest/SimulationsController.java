package com.mivivienda.platform.simuladordecredito.core.interfaces.rest;

import com.mivivienda.platform.simuladordecredito.core.domain.model.queries.GetAllSimulationsQuery;
import com.mivivienda.platform.simuladordecredito.core.domain.model.queries.GetSimulationByIdQuery;
import com.mivivienda.platform.simuladordecredito.core.domain.services.SimulationCommandService;
import com.mivivienda.platform.simuladordecredito.core.domain.services.SimulationQueryService;
import com.mivivienda.platform.simuladordecredito.core.interfaces.rest.resources.CreateSimulationResource;
import com.mivivienda.platform.simuladordecredito.core.interfaces.rest.resources.SimulationResource;
import com.mivivienda.platform.simuladordecredito.core.interfaces.rest.transform.CreateSimulationCommandFromResourceAssembler;
import com.mivivienda.platform.simuladordecredito.core.interfaces.rest.transform.SimulationResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/simulations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Simulations", description = "Available Simulation Endpoints")
public class SimulationsController {
    
    private final SimulationCommandService simulationCommandService;
    private final SimulationQueryService simulationQueryService;
    
    public SimulationsController(SimulationCommandService simulationCommandService, SimulationQueryService simulationQueryService) {
        this.simulationCommandService = simulationCommandService;
        this.simulationQueryService = simulationQueryService;
    }
    
    @PostMapping
    @Operation(summary = "Create a new simulation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Simulation created"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    public ResponseEntity<SimulationResource> createSimulation(@RequestBody CreateSimulationResource resource) {
        var command = CreateSimulationCommandFromResourceAssembler.toCommandFromResource(resource);
        var simulation = simulationCommandService.handle(command);
        
        if (simulation.isEmpty()) return ResponseEntity.badRequest().build();
        
        var createdSimulation = simulation.get();
        var simulationResource = SimulationResourceFromEntityAssembler.toResourceFromEntity(createdSimulation);
        return new ResponseEntity<>(simulationResource, HttpStatus.CREATED);
    }
    
    @GetMapping("/{simulationId}")
    @Operation(summary = "Get a simulation by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Simulation found"),
            @ApiResponse(responseCode = "404", description = "Simulation not found")})
    public ResponseEntity<SimulationResource> getSimulationById(@PathVariable Long simulationId) {
        var query = new GetSimulationByIdQuery(simulationId);
        var simulation = simulationQueryService.handle(query);
        
        if (simulation.isEmpty()) return ResponseEntity.notFound().build();
        
        var simulationResource = SimulationResourceFromEntityAssembler.toResourceFromEntity(simulation.get());
        return ResponseEntity.ok(simulationResource);
    }
    
    @GetMapping
    @Operation(summary = "Get all simulations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Simulations found"),
            @ApiResponse(responseCode = "404", description = "Simulations not found")})
    public ResponseEntity<List<SimulationResource>> getAllSimulations() {
        var simulations = simulationQueryService.handle(new GetAllSimulationsQuery());
        
        if (simulations.isEmpty()) return ResponseEntity.notFound().build();
        
        var simulationResources = simulations.stream()
                .map(SimulationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        
        return ResponseEntity.ok(simulationResources);
    }
}