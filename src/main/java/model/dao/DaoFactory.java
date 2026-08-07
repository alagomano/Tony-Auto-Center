package model.dao;


import jakarta.persistence.EntityManager;
import model.dao.impl.ClientDaoJPA;
import model.dao.impl.ServiceItemDaoJPA;
import model.dao.impl.ServiceOrderDaoJPA;
import model.dao.impl.VehicleDaoJPA;
import model.infrastructure.JPAUtil;

public class DaoFactory {

    private static final EntityManager entityManager = JPAUtil.getEntityManager();

    public static ClientDao createClientDao(){
        return new ClientDaoJPA(entityManager);
    }

    public static VehicleDao createVehicleDao(){
        return new VehicleDaoJPA(entityManager);
    }

    public static ServiceOrderDao createServiceOrderDao(){
        return new ServiceOrderDaoJPA(entityManager);
    }

    public static ServiceItemDao createServiceItemDao(){
        return new ServiceItemDaoJPA(entityManager);
    }
}
