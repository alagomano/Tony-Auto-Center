package model.services;

import model.dao.DaoFactory;
import model.dao.ServiceItemDao;
import model.dao.ServiceOrderDao;
import model.entities.ServiceItem;
import model.entities.ServiceOrder;
import model.entities.Vehicle;
import model.exception.ServiceException;

import java.util.List;

public class ServiceOrderService {
    private final ServiceOrderDao serviceOrderDao = DaoFactory.createServiceOrderDao();
    private final ServiceItemDao serviceItemDao = DaoFactory.createServiceItemDao();

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

    public void addItemToOrder(Long serviceOrderId, ServiceItem item){
        validateID(serviceOrderId);
        validateServiceItem(item);

        ServiceOrder serviceOrder = findServiceOrderById(serviceOrderId);

        serviceOrder.addItem(item);
        serviceItemDao.insert(item);
        serviceOrderDao.update(serviceOrder);

    }

    public ServiceOrder findServiceOrderById(Long serviceOrderId){
        validateID(serviceOrderId);
        ServiceOrder serviceOrder = serviceOrderDao.findById(serviceOrderId);
        validateServiceOrder(serviceOrder);

        List<ServiceItem> items = serviceItemDao.findByServiceOrder(serviceOrderId);
        items.forEach(serviceOrder::addItem);
        return serviceOrder;
    }

    public void updateServiceOrder(ServiceOrder serviceOrder){
        validateServiceOrder(serviceOrder);
        validateID(serviceOrder.getId());
        findServiceOrderById(serviceOrder.getId());

        serviceOrderDao.update(serviceOrder);
    }

    public void deleteServiceOrderById(Long serviceOrderId){
        validateID(serviceOrderId);
        findServiceOrderById(serviceOrderId);
        serviceOrderDao.deleteById(serviceOrderId);
    }

    public ServiceItem findServiceItemById(Long serviceOrderId, Long serviceItemId){
        validateID(serviceOrderId);
        validateID(serviceItemId);

        ServiceOrder order = findServiceOrderById(serviceOrderId);

        List<ServiceItem> items = order.getItems();

        ServiceItem item = items.stream().filter(i -> i.getId().equals(serviceItemId))
                .findFirst().orElseThrow(() -> new ServiceException("Item não pertence à ordem."));
        validateServiceItem(item);

        return item;
    }

    public void updateServiceItem(Long serviceOrderId, ServiceItem serviceItem){
        validateID(serviceOrderId);
        validateServiceItem(serviceItem);

        findServiceOrderById(serviceOrderId);
        findServiceItemById(serviceOrderId, serviceItem.getId());

        serviceItemDao.update(serviceItem);

    }

    public void deleteServiceItemById(Long serviceOrderId, Long serviceItemId){
        validateID(serviceItemId);
        ServiceOrder order = findServiceOrderById(serviceOrderId);
        ServiceItem item = findServiceItemById(serviceOrderId, serviceItemId);
        order.deleteItem(item);
        serviceItemDao.deleteById(serviceItemId);
    }

    public void startServiceOrder(Long serviceOrderId){
        ServiceOrder serviceOrder = findServiceOrderById(serviceOrderId);
        serviceOrder.start();
        serviceOrderDao.update(serviceOrder);
    }

    public void closeServiceOrder(Long serviceOrderId){
        ServiceOrder serviceOrder = findServiceOrderById(serviceOrderId);
        serviceOrder.close();
        serviceOrderDao.update(serviceOrder);
    }

    public void deliverServiceOrder(Long serviceOrderId){
        ServiceOrder serviceOrder = findServiceOrderById(serviceOrderId);
        serviceOrder.deliver();
        serviceOrderDao.update(serviceOrder);
    }

    public List<ServiceItem> getItemsByOrder(Long serviceOrderId){
        ServiceOrder order = findServiceOrderById(serviceOrderId);
        return order.getItems();
    }

    public List<ServiceOrder> findAll(){
        return serviceOrderDao.findAll();
    }

}
