package model.dao.impl;

import model.dao.VehicleDao;
import model.database.DBConnection;
import model.entities.Client;
import model.entities.Vehicle;
import model.exception.DbException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDaoJDBC implements VehicleDao {
    private final Connection connection;

    public VehicleDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    private Vehicle instantiateVehicle(ResultSet rs) throws SQLException {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(rs.getLong("id"));
        vehicle.setPlate(rs.getString("plate"));
        vehicle.setBrand(rs.getString("brand"));
        vehicle.setModel(rs.getString("model"));
        vehicle.setYear(rs.getInt("year"));
        return vehicle;
    }

    private Vehicle instantiateVehicleWithClient(ResultSet rs, Client client) throws SQLException {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(rs.getLong("id"));
        vehicle.setPlate(rs.getString("plate"));
        vehicle.setBrand(rs.getString("brand"));
        vehicle.setModel(rs.getString("model"));
        vehicle.setYear(rs.getInt("year"));
        vehicle.setClient(client);
        return vehicle;
    }

    private Client instantiateClient(ResultSet rs) throws SQLException{
        Client client = new Client();
        client.setId(rs.getLong("clientId"));
        client.setName(rs.getString("clientName"));
        client.setCpf(rs.getString("clientCpf"));
        client.setPhone(rs.getString("clientPhone"));
        client.setAddress(rs.getString("clientAddress"));
        return client;

    }

    private void validateVehicleClient(Vehicle vehicle){
        if(vehicle.getClient() == null){
            throw new DbException("O veículo precisa estar associado a um cliente.");
        }

        if(vehicle.getClient().getId() == null){
            throw new DbException("O cliente do veículo precisa possuir ID.");
        }
    }

    @Override
    public void insert(Vehicle vehicle) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{

            validateVehicleClient(vehicle);

            String sql = """
                    INSERT INTO vehicles
                    (plate, brand, model, year, client_id)
                    VALUES
                    (?, ?, ?, ?, ?)
                    """;
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, vehicle.getPlate());
            ps.setString(2, vehicle.getBrand());
            ps.setString(3, vehicle.getModel());
            ps.setInt(4, vehicle.getYear());

            ps.setLong(5, vehicle.getClient().getId());

            int rowsAffected = ps.executeUpdate();

            if(rowsAffected == 0) {
                throw new DbException("O veículo não foi inserido.");
            }
            rs = ps.getGeneratedKeys();

            if(rs.next()){
                vehicle.setId(rs.getLong(1));
            }




        }catch (SQLException e){
            throw new DbException("Erro ao inserir veículo.", e);
        }finally {
            DBConnection.closeStatement(ps);
            DBConnection.closeResultSet(rs);
        }
    }

    @Override
    public void update(Vehicle vehicle) {
        PreparedStatement ps = null;

        try{
            validateVehicleClient(vehicle);
            String sql = """
                    UPDATE vehicles
                    SET plate = ?, brand = ?, model = ?, year = ?, client_id = ?
                    WHERE id = ?
                    """;

            ps = connection.prepareStatement(sql);

            ps.setString(1, vehicle.getPlate());
            ps.setString(2, vehicle.getBrand());
            ps.setString(3, vehicle.getModel());
            ps.setInt(4, vehicle.getYear());
            ps.setLong(5, vehicle.getClient().getId());
            ps.setLong(6, vehicle.getId());

            int rowsAffected = ps.executeUpdate();
            if(rowsAffected == 0){
                throw new DbException("Nenhum veículo foi atualizado.");
            }
        }catch (SQLException e){
            throw new DbException("Erro ao atualizar dados do veículo.", e);
        }finally {
            DBConnection.closeStatement(ps);
        }

    }

    @Override
    public void deleteById(Long id) {
        PreparedStatement ps = null;

        try{
            String sql = """
                    DELETE FROM vehicles
                    WHERE id = ?
                    """;

            ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected == 0){
                throw new DbException("Veículo não encontrado.");
            }

        }catch (SQLException e){
            throw new DbException("Erro ao deletar veículo.", e);
        }finally {
            DBConnection.closeStatement(ps);
        }
    }

    @Override
    public Vehicle findById(Long id) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            String sql = """
                    SELECT vehicles.*,
                     clients.id AS clientId,
                     clients.name AS clientName,
                     clients.cpf AS clientCpf,
                     clients.phone AS clientPhone,
                     clients.address AS clientAddress
                     FROM vehicles
                     INNER JOIN clients
                     ON vehicles.client_id = clients.id
                     WHERE vehicles.id = ?
                    """;

            ps = connection.prepareStatement(sql);
            ps.setLong(1, id);

            rs = ps.executeQuery();
            if(rs.next()){
                Client client = instantiateClient(rs);
                return instantiateVehicleWithClient(rs, client);
            }
            return null;
        }catch (SQLException e){
            throw new DbException("Erro ao procurar veículo.", e);
        }finally {
            DBConnection.closeStatement(ps);
            DBConnection.closeResultSet(rs);
        }
    }

    @Override
    public List<Vehicle> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = """
                    SELECT * FROM vehicles
                    """;

            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            List<Vehicle> vehicles = new ArrayList<>();
            while(rs.next()){
                vehicles.add(instantiateVehicle(rs));
            }

            return vehicles;

        }catch (SQLException e){
            throw new DbException("Erro ao retornar lista de veículos.", e);
        }finally {
            DBConnection.closeStatement(ps);
            DBConnection.closeResultSet(rs);
        }
    }

    @Override
    public Vehicle findByPlate(String plate) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = """
                    SELECT * FROM vehicles
                    WHERE plate = ?
                    """;

            ps = connection.prepareStatement(sql);
            ps.setString(1, plate);

            rs = ps.executeQuery();

            if(rs.next()){
                return instantiateVehicle(rs);
            }
            return null;
        }catch (SQLException e){
            throw new DbException("Erro ao buscar veículo.", e);
        }finally {
            DBConnection.closeStatement(ps);
            DBConnection.closeResultSet(rs);
        }
    }

    @Override
    public List<Vehicle> findByClient(Long clientId) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = """
                    SELECT * FROM vehicles
                    WHERE client_id = ?
                    """;

            ps = connection.prepareStatement(sql);
            ps.setLong(1, clientId);

            rs = ps.executeQuery();

            List<Vehicle> vehicles = new ArrayList<>();
            while(rs.next()){
                Vehicle vehicle = instantiateVehicle(rs);
                vehicles.add(vehicle);
            }
            return vehicles;
        }catch (SQLException e){
            throw new DbException("Erro ao buscar lista de veículos.", e);
        }finally {
            DBConnection.closeStatement(ps);
            DBConnection.closeResultSet(rs);
        }
    }
}
