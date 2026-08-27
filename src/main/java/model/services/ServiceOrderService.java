package model.services;

import model.dtos.ServiceItemRequestDTO;
import model.dtos.ServiceOrderRequestDTO;
import model.entities.ServiceItem;
import model.entities.ServiceOrder;
import model.exception.ServiceException;
import model.repositories.ServiceItemRepository;
import model.repositories.ServiceOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceOrderService {
    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceItemRepository serviceItemRepository;

    public ServiceOrderService(ServiceOrderRepository serviceOrderRepository, ServiceItemRepository serviceItemRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.serviceItemRepository = serviceItemRepository;
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

    private void updateData(ServiceOrder orderBefore, ServiceOrderRequestDTO orderRequestDTOAfter){
        orderBefore.setProblemDescription(orderRequestDTOAfter.getProblemDescription());
        orderBefore.setObservations(orderRequestDTOAfter.getObservations());
    }
    @Transactional
    public ServiceItem addItemToOrder(Long serviceOrderId, ServiceItemRequestDTO dto){
        validateID(serviceOrderId);
        ServiceItem item = new ServiceItem();
        item.setDescription(dto.getDescription());
        item.setQuantity(dto.getQuantity());
        item.setUnitValue(dto.getUnitValue());

        ServiceOrder serviceOrder = findServiceOrderById(serviceOrderId);
        item.setServiceOrder(serviceOrder);
        serviceOrder.addItem(item);
        serviceOrderRepository.save(serviceOrder);
        return serviceOrder.getItems().get(serviceOrder.getItems().size() - 1);
    }
    @Transactional
    public ServiceOrder findServiceOrderById(Long serviceOrderId){
        validateID(serviceOrderId);
        Optional<ServiceOrder> serviceOrder = serviceOrderRepository.findById(serviceOrderId);
        return serviceOrder.orElseThrow(() -> new ServiceException("Ordem de serviço não encontrada."));
    }
    @Transactional
    public ServiceOrder updateServiceOrder(Long serviceId, ServiceOrderRequestDTO orderRequestDTO){
        validateID(serviceId);
        ServiceOrder entityOrder = findServiceOrderById(serviceId);
        entityOrder.validateState();

        updateData(entityOrder, orderRequestDTO);
        serviceOrderRepository.save(entityOrder);
        return entityOrder;
    }
    @Transactional
    public void deleteServiceOrderById(Long serviceOrderId){
        validateID(serviceOrderId);
        findServiceOrderById(serviceOrderId);
        serviceOrderRepository.deleteById(serviceOrderId);
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
    public ServiceItem updateServiceItem(Long serviceOrderId, Long itemId, ServiceItemRequestDTO itemDTO){
        validateID(serviceOrderId);

        ServiceOrder order = findServiceOrderById(serviceOrderId);
        ServiceItem item = findServiceItemById(serviceOrderId, itemId);

        item.setDescription(itemDTO.getDescription());
        item.setQuantity(itemDTO.getQuantity());
        item.setUnitValue(itemDTO.getUnitValue());

        serviceItemRepository.save(item);
        order.setTotalValue(order.getItems().stream()
                .map(ServiceItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        serviceOrderRepository.save(order);

        return item;
    }
    @Transactional
    public void deleteServiceItemById(Long serviceOrderId, Long serviceItemId){
        validateID(serviceItemId);
        ServiceOrder order = findServiceOrderById(serviceOrderId);
        ServiceItem item = findServiceItemById(serviceOrderId, serviceItemId);
        order.deleteItem(item);
        serviceOrderRepository.save(order);
    }
    @Transactional
    public void startServiceOrder(Long serviceOrderId){
        ServiceOrder serviceOrder = findServiceOrderById(serviceOrderId);
        serviceOrder.start();
        serviceOrderRepository.save(serviceOrder);
    }
    @Transactional
    public void closeServiceOrder(Long serviceOrderId){
        ServiceOrder serviceOrder = findServiceOrderById(serviceOrderId);
        serviceOrder.close();
        serviceOrderRepository.save(serviceOrder);
    }
    @Transactional
    public void deliverServiceOrder(Long serviceOrderId){
        ServiceOrder serviceOrder = findServiceOrderById(serviceOrderId);
        serviceOrder.deliver();
        serviceOrderRepository.save(serviceOrder);
    }
    @Transactional
    public List<ServiceItem> getItemsByOrder(Long serviceOrderId){
        ServiceOrder order = findServiceOrderById(serviceOrderId);
        return new ArrayList<>(order.getItems());
    }
    @Transactional
    public List<ServiceOrder> findAll(){
        return serviceOrderRepository.findAll();
    }

}
