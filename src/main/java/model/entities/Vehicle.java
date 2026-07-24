package model.entities;

import model.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {
    private Long id;
    private String plate;
    private String brand;
    private String model;
    private Integer year;


    private List<ServiceOrder> serviceOrders = new ArrayList<>();

    public Vehicle(String plate, String brand, String model, Integer year) {
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.year = year;
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

    public List<ServiceOrder> getServiceOrders() {
        return serviceOrders;
    }

    public void setServiceOrders(List<ServiceOrder> serviceOrders) {
        this.serviceOrders = serviceOrders;
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
