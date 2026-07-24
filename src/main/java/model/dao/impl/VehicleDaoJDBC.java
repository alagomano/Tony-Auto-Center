package model.dao.impl;

import model.dao.VehicleDao;
import model.entities.Vehicle;

import java.sql.Connection;
import java.util.List;

public class VehicleDaoJDBC implements VehicleDao {
    private Connection connection;

    public VehicleDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Vehicle entity) {

    }

    @Override
    public void update(Vehicle entity) {

    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public Vehicle findById(Long aLong) {
        return null;
    }

    @Override
    public List<Vehicle> findAll() {
        return null;
    }

    @Override
    public Vehicle findByPlate(String plate) {
        return null;
    }

    @Override
    public List<Vehicle> findByClient(Long clientId) {
        return null;
    }
}
