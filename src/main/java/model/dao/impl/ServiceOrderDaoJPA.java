package model.dao.impl;

import jakarta.persistence.EntityManager;
import model.dao.ServiceOrderDao;
import model.entities.ServiceOrder;

import java.util.List;

public class ServiceOrderDaoJPA implements ServiceOrderDao {

    private final EntityManager entityManager;
    public ServiceOrderDaoJPA(EntityManager entityManager){
        this.entityManager = entityManager;
    }
    @Override
    public void insert(ServiceOrder entity) {

    }

    @Override
    public void update(ServiceOrder entity) {

    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public ServiceOrder findById(Long aLong) {
        return null;
    }

    @Override
    public List<ServiceOrder> findAll() {
        return null;
    }

    @Override
    public List<ServiceOrder> findByVehicle(Long vehicleId) {
        return null;
    }
}
