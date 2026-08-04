package model.entities;

import model.enums.OrderStatus;
import model.exception.DomainException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Vehicle {
    private Long id;
    private String plate;
    private String brand;
    private String model;
    private Integer year;

    private Client client;


    private List<ServiceOrder> serviceOrders = new ArrayList<>();

    public Vehicle(){}
    public Vehicle(String plate, String brand, String model, Integer year, Client client) {
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.client = client;
    }

    public String getPlate(){
        return plate;
    }
    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<ServiceOrder> getServiceOrders() {
        return serviceOrders;
    }

    public void addServiceOrder(ServiceOrder order){
        if(order == null){
            throw new DomainException("Ordem de serviço inválida.");
        }

        serviceOrders.add(order);
    }

    public ServiceOrder openServiceOrder(String problemDescription, String observation){
        ServiceOrder order = new ServiceOrder(problemDescription, observation, this);

        addServiceOrder(order);

        return order;
    }

    public boolean hasActiveServiceOrder() {
        return serviceOrders.stream().anyMatch(order -> order.getStatus() == OrderStatus.OPEN || order.getStatus() == OrderStatus.IN_PROGRESS);
    }
    public List<ServiceOrder> getHistory(){
        return Collections.unmodifiableList(serviceOrders);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", plate='" + plate + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                '}';
    }
}
