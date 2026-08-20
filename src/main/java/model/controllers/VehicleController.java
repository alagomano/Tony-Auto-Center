package model.controllers;

import model.entities.ServiceOrder;
import model.entities.Vehicle;
import model.services.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Vehicle> update(@PathVariable Long vehicleId, @RequestBody Vehicle vehicle){
        Vehicle updateVehicle = vehicleService.updateVehicle(vehicleId, vehicle);
        return ResponseEntity.ok().body(updateVehicle);
    }

    @PostMapping("clients/{clientId}")
    public ResponseEntity<Vehicle> insert(@PathVariable Long clientId, @RequestBody Vehicle vehicle){
        Vehicle saveVehicle = vehicleService.registerVehicle(clientId, vehicle);
        return ResponseEntity.ok().body(saveVehicle);
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
