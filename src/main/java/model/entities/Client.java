package model.entities;

import jakarta.persistence.*;
import model.exception.DomainException;

import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "clients")
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(unique = true, nullable = false, length = 14)
    private String cpf;
    @Column(length = 20)
    private String phone;
    @Column(length = 150)
    private String address;
    @Transient
    private Map<String, Vehicle> vehicles = new HashMap<>();

    public Client(){}
    public Client(String name, String cpf, String phone, String address){
        setName(name);
        setCpf(cpf);
        this.phone = phone;
        this.address = address;
    }

    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        if(cpf == null || cpf.isBlank()){
            throw new DomainException("CPF inválido.");
        }
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
        if(name == null || name.isBlank()){
            throw new DomainException("Nome inválido.");
        }
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client client)) return false;
        if (id == null || client.id == null) return false;
        return Objects.equals(getId(), client.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString(){
        return "ID: "+ id + " | Nome: " + name + " | CPF: " + cpf + " | Telefone " + phone + " | Endereço: " + address;
    }
}
