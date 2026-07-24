package model.entities;

import model.exception.ServiceException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Client {

    private Long id;
    private String name;
    private String cpf;
    private String phone;
    private String address;
    private Map<String, Vehicle> vehicles = new HashMap<>();

    public Client(){}
    public Client(String name, String cpf, String phone, String address){
        this.name = name;
        this.cpf = cpf;
        this.phone = phone;
        this.address = address;
    }

    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setVehicles(Map<String, Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void addVehicle(Vehicle vehicle){
        if(vehicle == null){
            throw new ServiceException("Veículo inválido!");
        }
        if(vehicles.containsKey(vehicle.getPlate())){
            throw new ServiceException("Veículo já cadastrado");
        }
        vehicles.put(vehicle.getPlate(), vehicle);
    }

    public Vehicle getVehicle(String plate){
        return vehicles.get(plate);
    }

    public Collection<Vehicle> getVehicles(){
        return new ArrayList<>(vehicles.values());
    }

    public void removeVehicle(String plate){
        if(!vehicles.containsKey(plate)) {
            throw new ServiceException("Veículo não encontrado");
        }

        vehicles.remove(plate);
    }


    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(name + " | ");
        sb.append(cpf + " ");
        return sb.toString();
    }

}
