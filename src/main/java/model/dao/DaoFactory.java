package model.dao;


import model.dao.impl.ClientDaoJPA;
import model.dao.impl.ServiceItemDaoJPA;
import model.dao.impl.ServiceOrderDaoJPA;
import model.dao.impl.VehicleDaoJPA;
import model.infrastructure.JPAUtil;

public class DaoFactory {

    public static ClientDao createClientDao(){
        return new ClientDaoJPA(JPAUtil.getEntityManager());
    }

    public static VehicleDao createVehicleDao(){
        return new VehicleDaoJPA(JPAUtil.getEntityManager());
    }

    public static ServiceOrderDao createServiceOrderDao(){
        return new ServiceOrderDaoJPA(JPAUtil.getEntityManager());
    }

    public static ServiceItemDao createServiceItemDao(){
        return new ServiceItemDaoJPA(JPAUtil.getEntityManager());
    }
}
