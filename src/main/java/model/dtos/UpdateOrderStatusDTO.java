package model.dtos;

import model.enums.OrderStatus;

public class UpdateOrderStatusDTO {
    private OrderStatus status;

    public UpdateOrderStatusDTO() {
    }

    public UpdateOrderStatusDTO(OrderStatus status) {
        this.status = status;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
