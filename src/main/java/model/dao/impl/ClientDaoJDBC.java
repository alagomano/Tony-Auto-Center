package model.dao.impl;

import model.dao.ClientDao;
import model.database.DBConnection;
import model.entities.Client;
import model.exception.DbException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDaoJDBC implements ClientDao {
    private final Connection connection;
    public ClientDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    private Client instantiateClient(ResultSet rs) throws SQLException{
        Client client = new Client();
        client.setId(rs.getLong("id"));
        client.setName(rs.getString("name"));
        client.setCpf(rs.getString("cpf"));
        client.setPhone(rs.getString("phone"));
        client.setAddress(rs.getString("address"));
        return client;
    }

    @Override
    public Client findByCpf(String cpf) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = """
                    SELECT * FROM clients
                    WHERE cpf = ?
                    """;

            ps = connection.prepareStatement(sql);
            ps.setString(1, cpf);

            rs = ps.executeQuery();

            if(rs.next()){
                return instantiateClient(rs);
            }
            return null;
        }catch (SQLException e){
            throw new DbException("Erro ao buscar cliente.", e);
        }finally {
            DBConnection.closeStatement(ps);
            DBConnection.closeResultSet(rs);
        }


    }

    @Override
    public void insert(Client client) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String sql = """
                    INSERT INTO clients
                    (name, cpf, phone, address)
                    VALUES
                    (?, ?, ?, ?)
                    """;
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, client.getName());
            ps.setString(2, client.getCpf());
            ps.setString(3, client.getPhone());
            ps.setString(4, client.getAddress());

            int rowsAffected = ps.executeUpdate();
            if(rowsAffected > 0){
                rs = ps.getGeneratedKeys();
                if(rs.next()){
                    client.setId(rs.getLong(1));
                }
            }else {
                throw new DbException("O cliente não foi inserido.");
            }

        }catch (SQLException e){
            throw new DbException("Erro ao inserir cliente.", e);
        }finally {
            DBConnection.closeStatement(ps);
            DBConnection.closeResultSet(rs);
        }

    }

    @Override
    public void update(Client client) {
        PreparedStatement ps = null;

        try{
            String sql = """
                    UPDATE clients
                    SET name = ?, cpf = ?, phone = ?, address = ?
                    WHERE id = ?
                    """;

            ps = connection.prepareStatement(sql);

            ps.setString(1, client.getName());
            ps.setString(2, client.getCpf());
            ps.setString(3, client.getPhone());
            ps.setString(4, client.getAddress());
            ps.setLong(5, client.getId());

            int rowsAffected = ps.executeUpdate();
            if(rowsAffected == 0){
                throw new DbException("Nenhum cliente foi atualizado.");
            }
        }catch (SQLException e){
            throw new DbException("Erro ao atualizar dados do cliente.", e);
        }finally {
            DBConnection.closeStatement(ps);
        }

    }

    @Override
    public void deleteById(Long id) {
        PreparedStatement ps = null;

        try{
            String sql = """
                    DELETE FROM clients
                    WHERE id = ?
                    """;

            ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected == 0){
                throw new DbException("Cliente não encontrado.");
            }

        }catch (SQLException e){
            throw new DbException("Erro ao deletar cliente.", e);
        }finally {
            DBConnection.closeStatement(ps);
        }

    }

    @Override
    public Client findById(Long id) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            String sql = """
                    SELECT * FROM clients WHERE id = ?
                    """;

            ps = connection.prepareStatement(sql);
            ps.setLong(1, id);

            rs = ps.executeQuery();
            if(rs.next()){
                return instantiateClient(rs);
            }
            return null;
        }catch (SQLException e){
            throw new DbException("Erro ao procurar cliente.", e);
        }finally {
            DBConnection.closeStatement(ps);
            DBConnection.closeResultSet(rs);
        }
    }

    @Override
    public List<Client> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = """
                    SELECT * FROM clients ORDER BY NAME
                    """;

            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            List<Client> clients = new ArrayList<>();
            while(rs.next()){
                clients.add(instantiateClient(rs));
            }

            return clients;

        }catch (SQLException e){
            throw new DbException("Erro ao retornar lista de clientes.", e);
        }finally {
            DBConnection.closeStatement(ps);
            DBConnection.closeResultSet(rs);
        }

    }
}
