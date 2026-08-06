package model.entities;

import jakarta.persistence.*;
import model.exception.DomainException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "service_item")
public class ServiceItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String description;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private BigDecimal unitValue;
    @ManyToOne
    @JoinColumn(name = "service_order_id", nullable = false)
    private ServiceOrder serviceOrder;

    public ServiceItem(){}

    public ServiceItem(String description, Integer quantity, BigDecimal unitValue, ServiceOrder serviceOrder){
        setDescription(description);
        setQuantity(quantity);
        setUnitValue(unitValue);
        this.serviceOrder = serviceOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if(description == null || description.isBlank()){
            throw new DomainException("Descrição do item inválida.");
        }
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0){
            throw new DomainException("A quantidade deve ser maior que zero.");
        }
        this.quantity = quantity;
    }

    public BigDecimal getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(BigDecimal unitValue) {
        if(unitValue == null || unitValue.compareTo(BigDecimal.ZERO) <= 0){
            throw new DomainException("O valor unitário deve ser maior que zero.");
        }
        this.unitValue = unitValue;
    }

    public ServiceOrder getServiceOrder() {
        return serviceOrder;
    }

    public void setServiceOrder(ServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    public BigDecimal getSubtotal() {
        return unitValue.multiply(
                BigDecimal.valueOf(quantity)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceItem that)) return false;
        if(id == null || that.id == null) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString(){
        return id + " | " + description + " | " + quantity + " | R$ " + unitValue + " | " + "SubTotal R$ " + getSubtotal();
    }
}
