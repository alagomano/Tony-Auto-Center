package model.dao;

import model.entities.ServiceOrder;

import java.util.List;

public interface ServiceOrderDao extends Dao<ServiceOrder, Long>{
    List<ServiceOrder> findByVehicle(Long vehicleId);
}
