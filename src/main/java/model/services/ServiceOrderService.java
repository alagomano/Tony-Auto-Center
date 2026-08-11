package model.services;

import model.dao.ServiceItemDao;
import model.dao.ServiceOrderDao;
import model.entities.ServiceItem;
import model.entities.ServiceOrder;
import model.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Service
public class ServiceOrderService {
    private final ServiceOrderDao serviceOrderDao;
    private final ServiceItemDao serviceItemDao;

    public ServiceOrderService(ServiceOrderDao serviceOrderDao, ServiceItemDao serviceItemDao) {
        this.serviceOrderDao = serviceOrderDao;
        this.serviceItemDao = serviceItemDao;
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
    @Transactional
    public void addItemToOrder(Long serviceOrderId, ServiceItem item){
        validateID(serviceOrderId);
        validateServiceItem(item);
        ServiceOrder serviceOrder = findServiceOrderById(serviceOrderId);
        serviceOrder.addItem(item);
        serviceOrderDao.update(serviceOrder);
    }
    @Transactional
    public ServiceOrder findServiceOrderById(Long serviceOrderId){
        validateID(serviceOrderId);
        ServiceOrder serviceOrder = serviceOrderDao.findById(serviceOrderId);
        validateServiceOrder(serviceOrder);
        return serviceOrder;
    }
    @Transactional
    public void updateServiceOrder(ServiceOrder serviceOrder){
        validateServiceOrder(serviceOrder);
        validateID(serviceOrder.getId());
        findServiceOrderById(serviceOrder.getId());

        serviceOrderDao.update(serviceOrder);
    }
    @Transactional
    public void deleteServiceOrderById(Long serviceOrderId){
        validateID(serviceOrderId);
        findServiceOrderById(serviceOrderId);
        serviceOrderDao.deleteById(serviceOrderId);
    }
    @Transactional
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
    @Transactional
    public void updateServiceItem(Long serviceOrderId, ServiceItem serviceItem){
        validateID(serviceOrderId);
        validateServiceItem(serviceItem);

        ServiceOrder order = findServiceOrderById(serviceOrderId);
        findServiceItemById(serviceOrderId, serviceItem.getId());

        serviceItemDao.update(serviceItem);
        order.setTotalValue(order.getItems().stream()
                .map(ServiceItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        serviceOrderDao.update(order);
    }
    @Transactional
    public void deleteServiceItemById(Long serviceOrderId, Long serviceItemId){
        validateID(serviceItemId);
        ServiceOrder order = findServiceOrderById(serviceOrderId);
        ServiceItem item = findServiceItemById(serviceOrderId, serviceItemId);
        order.deleteItem(item);
        serviceOrderDao.update(order);
    }
    @Transactional
    public void startServiceOrder(Long serviceOrderId){
        ServiceOrder serviceOrder = findServiceOrderById(serviceOrderId);
        serviceOrder.start();
        serviceOrderDao.update(serviceOrder);
    }
    @Transactional
    public void closeServiceOrder(Long serviceOrderId){
        ServiceOrder serviceOrder = findServiceOrderById(serviceOrderId);
        serviceOrder.close();
        serviceOrderDao.update(serviceOrder);
    }
    @Transactional
    public void deliverServiceOrder(Long serviceOrderId){
        ServiceOrder serviceOrder = findServiceOrderById(serviceOrderId);
        serviceOrder.deliver();
        serviceOrderDao.update(serviceOrder);
    }
    @Transactional
    public List<ServiceItem> getItemsByOrder(Long serviceOrderId){
        ServiceOrder order = findServiceOrderById(serviceOrderId);
        return new ArrayList<>(order.getItems());
    }
    @Transactional
    public List<ServiceOrder> findAll(){
        return serviceOrderDao.findAll();
    }

}
