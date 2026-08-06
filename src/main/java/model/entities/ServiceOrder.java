package model.entities;

import jakarta.persistence.*;
import model.enums.OrderStatus;
import model.exception.DomainException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "service_orders")
public class ServiceOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime entryDate;
    private LocalDateTime exitDate;
    @Column(nullable = false)
    private String problemDescription;
    private String observations;
    @Column(nullable = false, length = 30)
    private OrderStatus status;
    private BigDecimal totalValue;

    @Transient
    private Vehicle vehicle;
    @Transient
    private List<ServiceItem> items = new ArrayList<>();


    public ServiceOrder(){

    }

    public ServiceOrder(String problemDescription, String observations, Vehicle vehicle){
        setProblemDescription(problemDescription);
        setObservations(observations);
        setVehicle(vehicle);
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
        if(problemDescription == null || problemDescription.isBlank()){
            throw new DomainException("Descrição do problema inválida.");
        }
        this.problemDescription = problemDescription;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        if(observations == null || observations.isBlank()){
            observations = "Sem observações.";
        }
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
        return Collections.unmodifiableList(items);
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        if(vehicle == null){
            throw new DomainException("Veículo inválido.");
        }
        this.vehicle = vehicle;
    }

    private void validateItem(ServiceItem item){
        if(item == null){
            throw new DomainException("Item inválido.");
        }
    }

    public void addItem(ServiceItem item){
        validateItem(item);
        item.setServiceOrder(this);
        items.add(item);
        totalValue = calculateTotal();
    }

    public void deleteItem(ServiceItem item){
        validateItem(item);
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
            throw new DomainException("Ordem não pode ser iniciada.");
        }

        status = OrderStatus.IN_PROGRESS;
    }

    public void deliver(){
        if(status != OrderStatus.FINISHED){
            throw new DomainException("Ordem não pode ser entregue.");
        }

        status = OrderStatus.DELIVERED;
    }

    public void close(){

        if(status != OrderStatus.IN_PROGRESS){
            throw new DomainException("A ordem precisa estar em andamento.");
        }

        this.status = OrderStatus.FINISHED;
        this.exitDate = LocalDateTime.now();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceOrder that)) return false;
        if(id == null || that.id ==null) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
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
