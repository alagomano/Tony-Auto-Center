package model.dao;

import model.entities.ServiceItem;

import java.util.List;

public interface ServiceItemDao extends Dao<ServiceItem, Long>{
    List<ServiceItem> findByServiceOrder(Long serviceOrderId);
}
