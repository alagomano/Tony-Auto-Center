package model.services;

import model.dao.DaoFactory;
import model.dao.ServiceItemDao;
import model.dao.ServiceOrderDao;
import model.entities.ServiceItem;
import model.entities.ServiceOrder;
import model.entities.Vehicle;
import model.enums.OrderStatus;
import model.exception.ServiceException;

import java.util.List;

public class ServiceOrderService {
    private final ServiceOrderDao serviceOrderDao = DaoFactory.createServiceOrderDao();
    private final ServiceItemDao serviceItemDao = DaoFactory.createServiceItemDao();

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
    private void validateServiceItem(ServiceItem serviceItem){
        if (serviceItem == null) {
            throw new ServiceException("Item inválido.");
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
        serviceItemDao.insert(item);
        serviceOrderDao.update(serviceOrder);

    }

    public ServiceOrder findByIdServiceOrder(Long serviceOrderId){
        validateID(serviceOrderId);
        ServiceOrder serviceOrder = serviceOrderDao.findById(serviceOrderId);
        List<ServiceItem> items = serviceItemDao.findByServiceOrder(serviceOrderId);
        items.forEach(serviceOrder::addItem);
        validateServiceOrder(serviceOrder);
        return serviceOrder;
    }

    public void updateServiceOrder(ServiceOrder serviceOrder){
        validateServiceOrder(serviceOrder);
        validateID(serviceOrder.getId());
        ServiceOrder order = findByIdServiceOrder(serviceOrder.getId());

        if(order == null){
            throw new ServiceException("Ordem de serviço não encontrada.");
        }

        serviceOrderDao.update(serviceOrder);
    }

    public void deleteByIdServiceOrder(Long serviceOrderId){
        validateID(serviceOrderId);
        ServiceOrder order = serviceOrderDao.findById(serviceOrderId);
        validateServiceOrder(order);
        serviceOrderDao.deleteById(serviceOrderId);
    }

    public ServiceItem findByIdServiceItem(Long serviceOrderId, Long serviceItemId){
        validateID(serviceOrderId);
        validateID(serviceItemId);

        ServiceOrder order = findByIdServiceOrder(serviceOrderId);
        validateServiceOrder(order);

        List<ServiceItem> items = order.getItems();

        ServiceItem item = items.stream().filter(i -> i.getId().equals(serviceItemId)).findFirst().orElse(null);
        if(item == null){
            throw new ServiceException("Item não pertence à ordem.");
        }
        return item;
    }

    public void updateServiceItem(Long serviceOrderId, ServiceItem serviceItem){
        validateID(serviceOrderId);
        validateServiceItem(serviceItem);

        ServiceOrder order = findByIdServiceOrder(serviceOrderId);
        validateServiceOrder(order);

        boolean exists = order.getItems().stream().anyMatch(i -> i.getId().equals(serviceItem.getId()));
        if(!exists){
            throw new ServiceException("Item não pertence à ordem.");
        }

        serviceItemDao.update(serviceItem);

    }

    public void deleteByIdServiceItem(Long serviceItemId){
        validateID(serviceItemId);
        ServiceItem item = serviceItemDao.findById(serviceItemId);
        validateServiceItem(item);
        serviceItemDao.deleteById(serviceItemId);
    }

    public void startServiceOrder(Long serviceOrderId){
        ServiceOrder serviceOrder = findByIdServiceOrder(serviceOrderId);
        serviceOrder.start();
        serviceOrderDao.update(serviceOrder);
    }

    public void closeServiceOrder(Long serviceOrderId){
        ServiceOrder serviceOrder = findByIdServiceOrder(serviceOrderId);
        serviceOrder.close();
        serviceOrderDao.update(serviceOrder);
    }

    public void deliverServiceOrder(Long serviceOrderId){
        ServiceOrder serviceOrder = findByIdServiceOrder(serviceOrderId);
        serviceOrder.deliver();
        serviceOrderDao.update(serviceOrder);
    }

    public List<ServiceOrder> findAll(){
        return serviceOrderDao.findAll();
    }

}
