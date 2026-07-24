package model.dao;

import model.dao.impl.ClientDaoJDBC;
import model.dao.impl.ServiceItemDaoJDBC;
import model.dao.impl.ServiceOrderDaoJDBC;
import model.dao.impl.VehicleDaoJDBC;
import model.database.DBConnection;


public class DaoFactory {
    public static ClientDao createClientDao(){
        return new ClientDaoJDBC(DBConnection.getConnection());
    }

    public static VehicleDao createVehicleDao(){
        return new VehicleDaoJDBC(DBConnection.getConnection());
    }

    public static ServiceOrderDao createServiceOrderDao(){
        return new ServiceOrderDaoJDBC(DBConnection.getConnection());
    }

    public static ServiceItemDao createServiceItemDao(){
        return new ServiceItemDaoJDBC(DBConnection.getConnection());
    }
}
