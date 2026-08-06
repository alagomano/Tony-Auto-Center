package model.dao.impl;

import jakarta.persistence.EntityManager;
import model.dao.ServiceItemDao;
import model.entities.ServiceItem;

import java.util.List;

public class ServiceItemDaoJPA implements ServiceItemDao {
    private final EntityManager entityManager;
    public ServiceItemDaoJPA(EntityManager entityManager){
        this.entityManager = entityManager;
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
