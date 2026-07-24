package model.dao.impl;

import model.dao.ServiceOrderDao;
import model.entities.ServiceOrder;

import java.sql.Connection;
import java.util.List;

public class ServiceOrderDaoJDBC implements ServiceOrderDao {
    private Connection connection;

    public ServiceOrderDaoJDBC(Connection connection) {
        this.connection = connection;
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
