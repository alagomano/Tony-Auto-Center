package model.dao.impl;

import jakarta.persistence.EntityManager;
import model.dao.VehicleDao;
import model.entities.Vehicle;

import java.util.List;

public class VehicleDaoJPA implements VehicleDao {
    private final EntityManager entityManager;
    public VehicleDaoJPA(EntityManager entityManager){
        this.entityManager = entityManager;
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
