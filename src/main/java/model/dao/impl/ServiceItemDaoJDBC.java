package model.dao.impl;

import model.dao.ServiceItemDao;
import model.entities.ServiceItem;

import java.sql.Connection;
import java.util.List;

public class ServiceItemDaoJDBC implements ServiceItemDao {

    private Connection connection;

    public ServiceItemDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(ServiceItem entity) {

    }

    @Override
    public void update(ServiceItem entity) {

    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public ServiceItem findById(Long aLong) {
        return null;
    }

    @Override
    public List<ServiceItem> findAll() {
        return null;
    }

    @Override
    public List<ServiceItem> findByServiceOrder(Long serviceOrderId) {
        return null;
    }
}
