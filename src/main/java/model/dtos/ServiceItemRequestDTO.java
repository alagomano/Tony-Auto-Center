package model.dtos;

import java.io.Serializable;
import java.math.BigDecimal;

public class ServiceItemRequestDTO implements Serializable {
    private String description;
    private Integer quantity;
    private BigDecimal unitValue;

    public ServiceItemRequestDTO() {
    }

    public ServiceItemRequestDTO(String description, Integer quantity, BigDecimal unitValue, Long serviceOrderId) {
        this.description = description;
        this.quantity = quantity;
        this.unitValue = unitValue;
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

}
