package model.entities;

import model.enums.OrderStatus;
import model.exception.ServiceException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceOrder {
    private Long id;
    private LocalDateTime entryDate;
    private LocalDateTime exitDate;
    private String problemDescription;
    private String observations;
    private OrderStatus status;
    private BigDecimal totalValue;

    private Vehicle vehicle;


    private List<ServiceItem> items = new ArrayList<>();


    public ServiceOrder(){

    }

    public ServiceOrder(String problemDescription, String observations, Vehicle vehicle){
        this.problemDescription = problemDescription;
        this.observations = observations;
        this.vehicle = vehicle;
        this.entryDate = LocalDateTime.now();
        this.status = OrderStatus.OPEN;
        this.totalValue = BigDecimal.ZERO;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDateTime entryDate) {
        this.entryDate = entryDate;
    }

    public LocalDateTime getExitDate() {
        return exitDate;
    }

    public void setExitDate(LocalDateTime exitDate) {
        this.exitDate = exitDate;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public OrderStatus getStatus() {
        return status;
    }
    public void setStatus(OrderStatus status) {this.status = status;}

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public List<ServiceItem> getItems() {
        return items;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void addItem(ServiceItem item){

        if(item == null){
            throw new ServiceException("Item de serviço inválido");
        }
        item.setServiceOrder(this);
        items.add(item);
        totalValue = calculateTotal();
    }

    public void deleteItem(ServiceItem item){
        items.remove(item);
        totalValue = calculateTotal();
    }

    private BigDecimal calculateTotal(){
        return items.stream()
                .map(ServiceItem::getSubtotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    public void start(){
        if(status != OrderStatus.OPEN){
            throw new ServiceException("Ordem não pode ser iniciada.");
        }

        status = OrderStatus.IN_PROGRESS;
    }

    public void deliver(){
        if(status != OrderStatus.FINISHED){
            throw new ServiceException("Ordem não pode ser entregue.");
        }

        status = OrderStatus.DELIVERED;
    }

    public void close(){

        if(status != OrderStatus.IN_PROGRESS){
            throw new ServiceException("A ordem precisa estar em andamento.");
        }

        this.status = OrderStatus.FINISHED;
        this.exitDate = LocalDateTime.now();

    }

    @Override
    public String toString(){
        return "ID: " + id +
                " | Descrição:" + problemDescription +
                " | Observação: " + observations +
                " | " + status +
                " | Valor Total: " + totalValue;
    }



}
