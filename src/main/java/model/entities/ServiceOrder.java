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
        this.entryDate = LocalDateTime.now();
        this.status = OrderStatus.OPEN;
        this.totalValue = BigDecimal.ZERO;
        this.vehicle = vehicle;
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

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public List<ServiceItem> getItems() {
        return items;
    }

    public void setItems(List<ServiceItem> items) {
        this.items = items;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void addService(ServiceItem item){

        if(item == null){
            throw new ServiceException("Tipo de serviço inválido");
        }
        items.add(item);
        this.totalValue = calculateTotal();
    }

    public BigDecimal calculateTotal(){

        BigDecimal total = BigDecimal.ZERO;

        for(ServiceItem item : items){
            total = total.add(item.getSubtotal());
        }

        return total;
    }

    public void update(OrderStatus status){
        if(status == null){
            throw new ServiceException("Status inválido.");
        }

        if(this.status == OrderStatus.DELIVERED){
            throw new ServiceException("Ordem já entregue!");
        }

        if(status == OrderStatus.DELIVERED && this.status != OrderStatus.FINISHED){
            throw new ServiceException("A ordem de serviço precisa ser fechada antes de ser entregue.");
        }

        this.status = status;
    }

    public void close(){
        if (status == null){
            throw new ServiceException("Status inválido.");
        }

        if(status == OrderStatus.DELIVERED){
            throw new ServiceException("Ordem já entregue.");
        }

        if(status == OrderStatus.FINISHED){
            throw new ServiceException("Ordem de serviço já foi finalizada.");
        }

        this.status = OrderStatus.FINISHED;
        this.exitDate = LocalDateTime.now();

    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(ServiceItem i : items){
            sb.append(i.toString() + "\n");
        }
        sb.append("Total a pagar: R$ " + this.calculateTotal());

        return sb.toString();

    }



}
