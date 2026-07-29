package model.services;

import model.dao.DaoFactory;
import model.dao.ServiceOrderDao;
import model.entities.ServiceItem;
import model.entities.ServiceOrder;
import model.entities.Vehicle;
import model.enums.OrderStatus;
import model.exception.ServiceException;

import java.util.List;

public class ServiceOrderService {
    private final ServiceOrderDao serviceOrderDao = DaoFactory.createServiceOrderDao();

    private void validateVehicle(Vehicle vehicle){
        if(vehicle == null) {
            throw new ServiceException("Veículo inválido.");
        }
    }
    private void validateServiceOrder(ServiceOrder serviceOrder){
        if (serviceOrder == null) {
            throw new ServiceException("Ordem de serviço inválida.");
        }
    }

    private void validateID(Long id){
        if (id == null || id < 0){
            throw new ServiceException("Id inválido.");
        }
    }

    public ServiceOrder createServiceOrder(Vehicle vehicle, ServiceOrder serviceOrder){

        validateVehicle(vehicle);
        validateServiceOrder(serviceOrder);

        boolean verificationOS = vehicle.getHistory().stream().anyMatch(os -> os.getStatus() == OrderStatus.OPEN || os.getStatus() == OrderStatus.IN_PROGRESS);

        if (verificationOS){
            throw new ServiceException("Veículo já possui uma ordem de serviço ativa.");
        }

        serviceOrder.setVehicle(vehicle);
        serviceOrderDao.insert(serviceOrder);

        return serviceOrder;
    }

    public void addItemToOrder(Long serviceOrderId, ServiceItem item){
        validateID(serviceOrderId);
        if(item == null){
            throw new ServiceException("Item inválido.");
        }

        ServiceOrder serviceOrder = serviceOrderDao.findById(serviceOrderId);

        if (serviceOrder == null){
            throw new ServiceException("Ordem de serviço inválida.");
        }

        serviceOrder.addItem(item);
        serviceOrderDao.update(serviceOrder);

    }

    public ServiceOrder findById(Long serviceOrderId){
        validateID(serviceOrderId);
        ServiceOrder serviceOrder = serviceOrderDao.findById(serviceOrderId);
        validateServiceOrder(serviceOrder);
        return serviceOrder;
    }

    public void startServiceOrder(Long serviceOrderId){
        ServiceOrder serviceOrder = findById(serviceOrderId);
        serviceOrder.start();
        serviceOrderDao.update(serviceOrder);
    }

    public void closeServiceOrder(Long serviceOrderId){
        ServiceOrder serviceOrder = findById(serviceOrderId);
        serviceOrder.close();
        serviceOrderDao.update(serviceOrder);
    }

    public void deliverServiceOrder(Long serviceOrderId){
        ServiceOrder serviceOrder = findById(serviceOrderId);
        serviceOrder.deliver();
        serviceOrderDao.update(serviceOrder);
    }

    public List<ServiceOrder> findAll(){
        return serviceOrderDao.findAll();
    }

}
