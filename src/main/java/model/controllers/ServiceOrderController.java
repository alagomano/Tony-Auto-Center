package model.controllers;

import model.dtos.ServiceItemRequestDTO;
import model.dtos.ServiceOrderRequestDTO;
import model.dtos.UpdateOrderStatusDTO;
import model.entities.ServiceItem;
import model.entities.ServiceOrder;
import model.exception.ServiceException;
import model.services.ServiceOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orders")
public class ServiceOrderController {
    private final ServiceOrderService serviceOrderService;

    public ServiceOrderController(ServiceOrderService serviceOrderService) {
        this.serviceOrderService = serviceOrderService;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ServiceOrder> findOrderById(@PathVariable Long orderId){
        ServiceOrder order = serviceOrderService.findServiceOrderById(orderId);
        return ResponseEntity.ok().body(order);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<ServiceOrder> updateOrder(@PathVariable Long orderId, @RequestBody ServiceOrderRequestDTO orderRequestDTO){
        ServiceOrder updateOrder = serviceOrderService.updateServiceOrder(orderId, orderRequestDTO);
        return ResponseEntity.ok().body(updateOrder);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long orderId, @RequestBody UpdateOrderStatusDTO statusDTO){
        switch (statusDTO.getStatus()){
            case IN_PROGRESS:
                serviceOrderService.startServiceOrder(orderId);
                break;
            case FINISHED:
                serviceOrderService.closeServiceOrder(orderId);
                break;
            case DELIVERED:
                serviceOrderService.deliverServiceOrder(orderId);
                break;
            default: throw new ServiceException("Status inválido para esta operação.");
        }
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId){
        serviceOrderService.deleteServiceOrderById(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ServiceOrder>> findAll(){
        List<ServiceOrder> orders = serviceOrderService.findAll();
        return ResponseEntity.ok().body(orders);
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<ServiceItem> insertItemToOrder(@PathVariable Long orderId, @RequestBody ServiceItemRequestDTO itemDTO){
        ServiceItem item = serviceOrderService.addItemToOrder(orderId, itemDTO);
        return ResponseEntity.ok().body(item);
    }

    @PutMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<ServiceItem> updateItem(@PathVariable Long orderId, @PathVariable Long itemId, @RequestBody ServiceItemRequestDTO itemDTO){
        ServiceItem item = serviceOrderService.updateServiceItem(orderId, itemId, itemDTO);
        return ResponseEntity.ok().body(item);
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long orderId, @PathVariable Long itemId){
        serviceOrderService.deleteServiceItemById(orderId, itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<ServiceItem>> getItemsByOrder(@PathVariable Long orderId){
        List<ServiceItem> items = serviceOrderService.getItemsByOrder(orderId);
        return ResponseEntity.ok().body(items);
    }

}
