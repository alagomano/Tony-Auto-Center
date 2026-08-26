package model.dtos;

import model.entities.Client;

import java.io.Serializable;

public class VehicleUpdateDTO implements Serializable {
    private String plate;
    private String clientCpf;

    public VehicleUpdateDTO() {
    }

    public VehicleUpdateDTO(String plate, String clientCpf) {
        this.plate = plate;
        this.clientCpf = clientCpf;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getClientCpf() {
        return clientCpf;
    }

    public void setClientCpf(String clientCpf) {
        this.clientCpf = clientCpf;
    }
}
