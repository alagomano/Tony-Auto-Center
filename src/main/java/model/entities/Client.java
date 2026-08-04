package model.entities;

import model.exception.DomainException;

import java.util.*;

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

    private void validatePlate(String plate){
        if (plate == null || plate.isBlank()){
            throw new DomainException("Placa inválida.");
        }
    }

    private void validateVehicle(Vehicle vehicle){
        if(vehicle == null){
            throw new DomainException("Veículo inválido!");
        }
    }
    public void addVehicle(Vehicle vehicle){
        validateVehicle(vehicle);
        validatePlate(vehicle.getPlate());
        if(vehicles.containsKey(vehicle.getPlate())){
            throw new DomainException("Veículo já cadastrado");
        }
        vehicles.put(vehicle.getPlate(), vehicle);
    }

    public void removeVehicle(String plate){
        validatePlate(plate);

        if(!vehicles.containsKey(plate)) {
            throw new DomainException("Veículo não encontrado");
        }

        vehicles.remove(plate);
    }

    public Vehicle getVehicle(String plate){
        validatePlate(plate);
        return vehicles.get(plate);
    }
    public Collection<Vehicle> getVehicles(){
        return Collections.unmodifiableCollection(vehicles.values());
    }

    @Override
    public String toString(){
        return "ID: "+ id + " | Nome: " + name + " | CPF: " + cpf + " | Telefone " + phone + " | Endereço: " + address;
    }
}
