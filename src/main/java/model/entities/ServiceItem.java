package model.entities;

import java.math.BigDecimal;

public class ServiceItem {
    private Long id;
    private String description;
    private Integer quantity;
    private BigDecimal unitValue;

    private ServiceOrder serviceOrder;

    public ServiceItem(){}

    public ServiceItem(String description, Integer quantity, BigDecimal unitValue, ServiceOrder serviceOrder){
        this.description = description;
        this.quantity = quantity;
        this.unitValue = unitValue;
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
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(BigDecimal unitValue) {
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
    public String toString(){
        return id + " | " + description + " | " + quantity + " | R$ " + unitValue + " | " + "SubTotal R$ " + getSubtotal();
    }
}
