package model.dao.impl;

import model.dao.ServiceItemDao;
import model.database.DBConnection;
import model.entities.ServiceItem;
import model.entities.ServiceOrder;
import model.enums.OrderStatus;
import model.exception.DbException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceItemDaoJDBC implements ServiceItemDao {

    private final Connection connection;

    public ServiceItemDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    private ServiceOrder instantiateServiceOrder(ResultSet rs) throws SQLException{
        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setId(rs.getLong("serviceOrderId"));
        serviceOrder.setEntryDate(toLocalDateTime(rs.getTimestamp("serviceOrderEntryDate")));
        serviceOrder.setExitDate(toLocalDateTime(rs.getTimestamp("serviceOrderExitDate")));
        serviceOrder.setProblemDescription(rs.getString("serviceOrderProblemDescription"));
        serviceOrder.setObservations(rs.getString("serviceOrderObservations"));
        serviceOrder.setStatus(OrderStatus.valueOf(rs.getString("serviceOrderStatus")));
        serviceOrder.setTotalValue(rs.getBigDecimal("serviceOrderTotalValue"));
        return serviceOrder;
    }

    private ServiceItem instantiateServiceItemWithServiceOrder(ResultSet rs, ServiceOrder serviceOrder) throws SQLException{
        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setId(rs.getLong("id"));
        serviceItem.setDescription(rs.getString("description"));
        serviceItem.setQuantity(rs.getInt("quantity"));
        serviceItem.setUnitValue(rs.getBigDecimal("unit_value"));
        serviceItem.setServiceOrder(serviceOrder);

        return serviceItem;

    }

    private LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    private void validateServiceItem(ServiceItem serviceItem) {

        if (serviceItem.getServiceOrder() == null) {
            throw new DbException("O item deve estar associado a uma ordem de serviço.");
        }

        if (serviceItem.getServiceOrder().getId() == null) {
            throw new DbException("A ordem de serviço deve estar persistida antes de adicionar itens.");
        }

        if (serviceItem.getDescription() == null || serviceItem.getDescription().isBlank()) {
            throw new DbException("Descrição do item é obrigatória.");
        }

        if (serviceItem.getQuantity() == null) {
            throw new DbException("Quantidade do item é obrigatória.");
        }

        if (serviceItem.getUnitValue() == null) {
            throw new DbException("Valor unitário é obrigatório.");
        }
    }

    @Override
    public void insert(ServiceItem serviceItem) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{

            validateServiceItem(serviceItem);

            String sql = """
                    INSERT INTO service_items
                    (description, quantity, unit_value, service_order_id)
                    VALUES
                    (?, ?, ?, ?)
                    """;
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, serviceItem.getDescription());
            ps.setInt(2, serviceItem.getQuantity());
            ps.setBigDecimal(3, serviceItem.getUnitValue());
            ps.setLong(4, serviceItem.getServiceOrder().getId());

            int rowsAffected = ps.executeUpdate();

            if(rowsAffected == 0) {
                throw new DbException("A ordem de item não foi inserida.");
            }
            rs = ps.getGeneratedKeys();

            if(rs.next()){
                serviceItem.setId(rs.getLong(1));
            }


        }catch (SQLException e){
            throw new DbException("Erro ao inserir ordem de item.", e);
        }finally {
            DBConnection.closeStatement(ps);
            DBConnection.closeResultSet(rs);
        }
    }

    @Override
    public void update(ServiceItem serviceItem) {
        PreparedStatement ps = null;

        try{
            validateServiceItem(serviceItem);
            String sql = """
                    UPDATE service_items
                    SET description = ?, quantity = ?, unit_value = ?
                    WHERE id = ?
                    """;

            ps = connection.prepareStatement(sql);

            ps.setString(1, serviceItem.getDescription());
            ps.setInt(2, serviceItem.getQuantity());
            ps.setBigDecimal(3, serviceItem.getUnitValue());
            ps.setLong(4, serviceItem.getId());

            int rowsAffected = ps.executeUpdate();
            if(rowsAffected == 0){
                throw new DbException("Nenhuma ordem de item foi atualizada.");
            }
        }catch (SQLException e){
            throw new DbException("Erro ao atualizar ordem de item.", e);
        }finally {
            DBConnection.closeStatement(ps);
        }
    }

    @Override
    public void deleteById(Long id) {
        PreparedStatement ps = null;

        try{
            String sql = """
                    DELETE FROM service_items
                    WHERE id = ?
                    """;

            ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected == 0){
                throw new DbException("Item de serviço não encontrada.");
            }

        }catch (SQLException e){
            throw new DbException("Erro ao deletar item de serviço.", e);
        }finally {
            DBConnection.closeStatement(ps);
        }
    }

    @Override
    public ServiceItem findById(Long id) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            String sql = """
                    SELECT service_items.*,
                     service_orders.id AS serviceOrderId,
                     service_orders.entry_date AS serviceOrderEntryDate,
                     service_orders.exit_date AS serviceOrderExitDate,
                     service_orders.problem_description AS serviceOrderProblemDescription,
                     service_orders.observations AS serviceOrderObservations,
                     service_orders.status AS serviceOrderStatus,
                     service_orders.total_value AS serviceOrderTotalValue
                     FROM service_items
                     INNER JOIN service_orders
                     ON service_items.service_order_id = service_orders.id
                     WHERE service_items.id = ?
                    """;

            ps = connection.prepareStatement(sql);
            ps.setLong(1, id);

            rs = ps.executeQuery();
            if(rs.next()){
                ServiceOrder serviceOrder = instantiateServiceOrder(rs);
                return instantiateServiceItemWithServiceOrder(rs, serviceOrder);
            }
            return null;
        }catch (SQLException e){
            throw new DbException("Erro ao procurar Ordem de item.", e);
        }finally {
            DBConnection.closeStatement(ps);
            DBConnection.closeResultSet(rs);
        }
    }

    @Override
    public List<ServiceItem> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = """
                    SELECT service_items.*,
                     service_orders.id AS serviceOrderId,
                     service_orders.entry_date AS serviceOrderEntryDate,
                     service_orders.exit_date AS serviceOrderExitDate,
                     service_orders.problem_description AS serviceOrderProblemDescription,
                     service_orders.observations AS serviceOrderObservations,
                     service_orders.status AS serviceOrderStatus,
                     service_orders.total_value AS serviceOrderTotalValue
                     FROM service_items
                     INNER JOIN service_orders
                     ON service_items.service_order_id = service_orders.id
                     ORDER BY description DESC
                    """;

            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            List<ServiceItem> serviceItems = new ArrayList<>();
            while(rs.next()){
                ServiceOrder serviceOrder = instantiateServiceOrder(rs);
                ServiceItem serviceItem = instantiateServiceItemWithServiceOrder(rs, serviceOrder);
                serviceItems.add(serviceItem);
            }

            return serviceItems;

        }catch (SQLException e){
            throw new DbException("Erro ao retornar lista de itens.", e);
        }finally {
            DBConnection.closeStatement(ps);
            DBConnection.closeResultSet(rs);
        }
    }

    @Override
    public List<ServiceItem> findByServiceOrder(Long serviceOrderId) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = """
                    SELECT service_items.*,
                     service_orders.id AS serviceOrderId,
                     service_orders.entry_date AS serviceOrderEntryDate,
                     service_orders.exit_date AS serviceOrderExitDate,
                     service_orders.problem_description AS serviceOrderProblemDescription,
                     service_orders.observations AS serviceOrderObservations,
                     service_orders.status AS serviceOrderStatus,
                     service_orders.total_value AS serviceOrderTotalValue
                     FROM service_items
                     INNER JOIN service_orders
                     ON service_items.service_order_id = service_orders.id
                     WHERE service_items.service_order_id = ?
                     ORDER BY description DESC
                    """;

            ps = connection.prepareStatement(sql);
            ps.setLong(1, serviceOrderId);

            rs = ps.executeQuery();

            List<ServiceItem> serviceItems = new ArrayList<>();
            while(rs.next()){
                ServiceOrder serviceOrder = instantiateServiceOrder(rs);
                ServiceItem serviceItem = instantiateServiceItemWithServiceOrder(rs, serviceOrder);
                serviceItems.add(serviceItem);
            }
            return serviceItems;
        }catch (SQLException e){
            throw new DbException("Erro ao buscar lista de itens.", e);
        }finally {
            DBConnection.closeStatement(ps);
            DBConnection.closeResultSet(rs);
        }
    }
}
