package model.entities;

import java.math.BigDecimal;

public class ServiceItem {
    private Long id;
    private String description;
    private Integer quantity;
    private BigDecimal unitValue;


    public ServiceItem(String description, Integer quantity, BigDecimal unitValue){
        this.description = description;
        this.quantity = quantity;
        this.unitValue = unitValue;
    }

    public BigDecimal getSubtotal() {
        return unitValue.multiply(
                BigDecimal.valueOf(quantity)
        );
    }
}
