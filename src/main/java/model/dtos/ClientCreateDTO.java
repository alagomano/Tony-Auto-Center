package model.dtos;

import java.io.Serializable;

public class ClientCreateDTO implements Serializable {
    private String name;
    private String cpf;
    private String phone;
    private String address;

    public ClientCreateDTO() {
    }

    public ClientCreateDTO(String name, String cpf, String phone, String address) {
        this.name = name;
        this.cpf = cpf;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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
}
