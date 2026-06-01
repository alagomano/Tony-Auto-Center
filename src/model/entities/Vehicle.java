package model.entities;

import model.exception.ServiceException;
import model.services.ServiceItem;
import model.services.ServiceOrder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Vehicle {
    private Long id;
    private String plate;
    private String brand;
    private String model;
    private Integer year;
    private Integer mileage;


    private List<ServiceOrder> serviceOrders = new ArrayList<>();

    public Vehicle(String plate, String brand, String model, Integer year, Integer mileage) {
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
    }


    private void addServiceOrder(ServiceOrder order){
        if(order == null){
            throw new ServiceException("Ordem de serviço inválida.");
        }

        serviceOrders.add(order);
    }

    public ServiceOrder openServiceOrder(String problemDescription, String observation){
        ServiceOrder order = new ServiceOrder(problemDescription, observation);

        addServiceOrder(order);

        return order;
    }
    public List<ServiceOrder> getHistory(){
//        Manda uma cópia da lista.
        return new ArrayList<>(serviceOrders);
    }
}
