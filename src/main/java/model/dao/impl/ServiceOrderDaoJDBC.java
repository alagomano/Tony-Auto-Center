package model.dao.impl;

import model.dao.ServiceOrderDao;
import model.database.DBConnection;
import model.entities.ServiceOrder;
import model.entities.Vehicle;
import model.enums.OrderStatus;
import model.exception.DbException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceOrderDaoJDBC implements ServiceOrderDao {
    private final Connection connection;

    public ServiceOrderDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    private void validateServiceOrder(ServiceOrder serviceOrder){

        if (serviceOrder.getVehicle() == null) {
            throw new DbException("A ordem de serviço deve estar associada a um veículo.");
        }

        if (serviceOrder.getVehicle().getId() == null) {
            throw new DbException("O veículo deve estar persistido antes da ordem de serviço.");
        }

        if(serviceOrder.getEntryDate() == null){
            throw new DbException("Data de entrada obrigatória.");
        }

        if(serviceOrder.getStatus() == null){
            throw new DbException("Status do serviço é obrigatório.");
        }

    }

    private Vehicle instantiateVehicle(ResultSet rs) throws SQLException{
        Vehicle vehicle = new Vehicle();
        vehicle.setId(rs.getLong("vehicleId"));
        vehicle.setPlate(rs.getString("vehiclePlate"));
        vehicle.setBrand(rs.getString("vehicleBrand"));
        vehicle.setModel(rs.getString("vehicleModel"));
        vehicle.setYear(rs.getInt("vehicleYear"));
        return vehicle;
    }

    private ServiceOrder instantiateServiceOrderWithVehicle(ResultSet rs, Vehicle vehicle) throws SQLException{
        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setId(rs.getLong("id"));
        serviceOrder.setEntryDate(toLocalDateTime(rs.getTimestamp("entry_date")));
        serviceOrder.setExitDate(toLocalDateTime(rs.getTimestamp("exit_date")));
        serviceOrder.setProblemDescription(rs.getString("problem_description"));
        serviceOrder.setObservations(rs.getString("observations"));
        serviceOrder.setStatus(OrderStatus.valueOf(rs.getString("status")));
        serviceOrder.setTotalValue(rs.getBigDecimal("total_value"));
        serviceOrder.setVehicle(vehicle);
        return serviceOrder;
    }

    private ServiceOrder instantiateServiceOrder(ResultSet rs) throws SQLException {
        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setId(rs.getLong("id"));
        serviceOrder.setEntryDate(toLocalDateTime(rs.getTimestamp("entry_date")));
        serviceOrder.setExitDate(toLocalDateTime(rs.getTimestamp("exit_date")));
        serviceOrder.setProblemDescription(rs.getString("problem_description"));
        serviceOrder.setObservations(rs.getString("observations"));
        serviceOrder.setStatus(OrderStatus.valueOf(rs.getString("status")));
        serviceOrder.setTotalValue(rs.getBigDecimal("total_value"));
        return serviceOrder;
    }

    private Timestamp toTimestamp(LocalDateTime dateTime) {
        return dateTime != null ? Timestamp.valueOf(dateTime) : null;
    }

    private LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    @Override
    public void insert(ServiceOrder serviceOrder) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{

            validateServiceOrder(serviceOrder);

            String sql = """
                    INSERT INTO service_orders
                    (entry_date, exit_date, problem_description, observations, status, total_value, vehicle_id)
                    VALUES
                    (?, ?, ?, ?, ?, ?, ?)
                    """;
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setTimestamp(1, toTimestamp(serviceOrder.getEntryDate()));
            ps.setTimestamp(2, toTimestamp(serviceOrder.getExitDate()));
            ps.setString(3, serviceOrder.getProblemDescription());
            ps.setString(4, serviceOrder.getObservations());
            ps.setString(5, serviceOrder.getStatus().name());
            ps.setBigDecimal(6, serviceOrder.getTotalValue());
            ps.setLong(7, serviceOrder.getVehicle().getId());

            int rowsAffected = ps.executeUpdate();

            if(rowsAffected == 0) {
                throw new DbException("A ordem de Serviço não foi inserida.");
            }
            rs = ps.getGeneratedKeys();

            if(rs.next()){
                serviceOrder.setId(rs.getLong(1));
            }


        }catch (SQLException e){
            throw new DbException("Erro ao inserir ordem de serviço.", e);
        }finally {
            DBConnection.closeStatement(ps);
            DBConnection.closeResultSet(rs);
        }
    }

    @Override
    public void update(ServiceOrder serviceOrder) {
        PreparedStatement ps = null;

        try{
            validateServiceOrder(serviceOrder);
            String sql = """
                    UPDATE service_orders
                    SET entry_date = ?, exit_date = ?, problem_description = ?, observations = ?,
                    status = ?, total_value = ?
                    WHERE id = ?
                    """;

            ps = connection.prepareStatement(sql);

            ps.setTimestamp(1, toTimestamp(serviceOrder.getEntryDate()));
            ps.setTimestamp(2, toTimestamp(serviceOrder.getExitDate()));
            ps.setString(3, serviceOrder.getProblemDescription());
            ps.setString(4, serviceOrder.getObservations());
            ps.setString(5, serviceOrder.getStatus().name());
            ps.setBigDecimal(6, serviceOrder.getTotalValue());
            ps.setLong(7, serviceOrder.getId());

            int rowsAffected = ps.executeUpdate();
            if(rowsAffected == 0){
                throw new DbException("Nenhuma ordem de serviço foi atualizada.");
            }
        }catch (SQLException e){
            throw new DbException("Erro ao atualizar ordem de serviço.", e);
        }finally {
            DBConnection.closeStatement(ps);
        }

    }

    @Override
    public void deleteById(Long id) {
        PreparedStatement ps = null;

        try{
            String sql = """
                    DELETE FROM service_orders
                    WHERE id = ?
                    """;

            ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected == 0){
                throw new DbException("Ordem de serviço não encontrada.");
            }

        }catch (SQLException e){
            throw new DbException("Erro ao deletar ordem de serviço.", e);
        }finally {
            DBConnection.closeStatement(ps);
        }

    }

    @Override
    public ServiceOrder findById(Long id) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            String sql = """
                    SELECT service_orders.*,
                     vehicles.id AS vehicleId,
                     vehicles.plate AS vehiclePlate,
                     vehicles.brand AS vehicleBrand,
                     vehicles.model AS vehicleModel,
                     vehicles.year AS vehicleYear
                     FROM service_orders
                     INNER JOIN vehicles
                     ON service_orders.vehicle_id = vehicles.id
                     WHERE service_orders.id = ?
                    """;

            ps = connection.prepareStatement(sql);
            ps.setLong(1, id);

            rs = ps.executeQuery();
            if(rs.next()){
                Vehicle vehicle = instantiateVehicle(rs);
                return instantiateServiceOrderWithVehicle(rs, vehicle);
            }
            return null;
        }catch (SQLException e){
            throw new DbException("Erro ao procurar Ordem de serviço.", e);
        }finally {
            DBConnection.closeStatement(ps);
            DBConnection.closeResultSet(rs);
        }
    }

    @Override
    public List<ServiceOrder> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = """
                    SELECT service_orders.*,
                     vehicles.id AS vehicleId,
                     vehicles.plate AS vehiclePlate,
                     vehicles.brand AS vehicleBrand,
                     vehicles.model AS vehicleModel,
                     vehicles.year AS vehicleYear
                     FROM service_orders
                     INNER JOIN vehicles
                     ON service_orders.vehicle_id = vehicles.id
                     ORDER BY entry_date DESC
                    """;

            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            List<ServiceOrder> serviceOrders = new ArrayList<>();
            while(rs.next()){
                Vehicle vehicle = instantiateVehicle(rs);
                ServiceOrder serviceOrder = instantiateServiceOrderWithVehicle(rs, vehicle);
                serviceOrders.add(serviceOrder);
            }

            return serviceOrders;

        }catch (SQLException e){
            throw new DbException("Erro ao retornar lista de serviços.", e);
        }finally {
            DBConnection.closeStatement(ps);
            DBConnection.closeResultSet(rs);
        }
    }

    @Override
    public List<ServiceOrder> findByVehicle(Long vehicleId) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = """
                    SELECT service_orders.*,
                     vehicles.id AS vehicleId,
                     vehicles.plate AS vehiclePlate,
                     vehicles.brand AS vehicleBrand,
                     vehicles.model AS vehicleModel,
                     vehicles.year AS vehicleYear
                     FROM service_orders
                     INNER JOIN vehicles
                     ON service_orders.vehicle_id = vehicles.id
                    WHERE service_orders.vehicle_id = ?
                    ORDER BY entry_date DESC
                    """;

            ps = connection.prepareStatement(sql);
            ps.setLong(1, vehicleId);

            rs = ps.executeQuery();

            List<ServiceOrder> serviceOrders = new ArrayList<>();
            while(rs.next()){
                Vehicle vehicle = instantiateVehicle(rs);
                ServiceOrder serviceOrder = instantiateServiceOrderWithVehicle(rs, vehicle);
                serviceOrders.add(serviceOrder);
            }
            return serviceOrders;
        }catch (SQLException e){
            throw new DbException("Erro ao buscar lista de serviços.", e);
        }finally {
            DBConnection.closeStatement(ps);
            DBConnection.closeResultSet(rs);
        }
    }
}


