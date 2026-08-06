package model.dao;

import model.entities.Vehicle;

import java.util.List;

public interface VehicleDao extends Dao<Vehicle, Long>{
    Vehicle findByPlate(String plate);
    List<Vehicle> findByClient(Long clientId);
}
