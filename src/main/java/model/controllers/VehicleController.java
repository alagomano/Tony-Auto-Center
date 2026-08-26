package model.controllers;

import model.dtos.ServiceOrderRequestDTO;
import model.dtos.VehicleCreateDTO;
import model.dtos.VehicleUpdateDTO;
import model.entities.ServiceOrder;
import model.entities.Vehicle;
import model.services.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> findAll(){
        List<Vehicle> vehicles = vehicleService.getVehicles();
        return ResponseEntity.ok().body(vehicles);
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<Vehicle> findById(@PathVariable Long vehicleId){
        Vehicle vehicle = vehicleService.findVehicleById(vehicleId);
        return ResponseEntity.ok().body(vehicle);
    }

    @GetMapping("/plate/{plate}")
    public ResponseEntity<Vehicle> findByPlate(@PathVariable String plate){
        Vehicle vehicle = vehicleService.findVehicleByPlate(plate);
        return ResponseEntity.ok().body(vehicle);
    }

    @PutMapping("/{vehicleId}")
    public ResponseEntity<Vehicle> update(@PathVariable Long vehicleId, @RequestBody VehicleUpdateDTO vehicleUpdateDTO){
        Vehicle updateVehicle = vehicleService.updateVehicle(vehicleId, vehicleUpdateDTO);
        return ResponseEntity.ok().body(updateVehicle);
    }

    @PostMapping
    public ResponseEntity<Vehicle> insert(@RequestBody VehicleCreateDTO vehicleCreateDTO){
        Vehicle saveVehicle = vehicleService.registerVehicle(vehicleCreateDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{vehicleId}")
                .buildAndExpand(saveVehicle.getId())
                .toUri();
        return ResponseEntity.created(uri).body(saveVehicle);
    }

    @PostMapping("/{vehicleId}/orders")
    public ResponseEntity<ServiceOrder> openServiceOrder(@PathVariable Long vehicleId, @RequestBody ServiceOrderRequestDTO orderRequestDTO){
        ServiceOrder order = vehicleService.openServiceOrder(vehicleId, orderRequestDTO.getProblemDescription(), orderRequestDTO.getObservations());
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{orderId}")
                .buildAndExpand(order.getId())
                .toUri();

        return ResponseEntity.created(uri).body(order);
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> delete(@PathVariable Long vehicleId){
        vehicleService.removeVehicle(vehicleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{vehicleId}/orders")
    public ResponseEntity<List<ServiceOrder>> getOrdersByVehicle(@PathVariable Long vehicleId){
        List<ServiceOrder> orders = vehicleService.getOrders(vehicleId);
        return ResponseEntity.ok().body(orders);
    }
}
