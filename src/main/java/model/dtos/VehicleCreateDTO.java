package model.dtos;

import java.io.Serializable;

public class VehicleCreateDTO implements Serializable {
    private String plate;
    private String brand;
    private String model;
    private Integer year;
    private String clientCpf;

    public VehicleCreateDTO() {
    }

    public VehicleCreateDTO(String plate, String brand, String model, Integer year, String clientCpf) {
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.clientCpf = clientCpf;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
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

    public String getClientCpf() {
        return clientCpf;
    }

    public void setClientCpf(String clientCpf) {
        this.clientCpf = clientCpf;
    }
}
