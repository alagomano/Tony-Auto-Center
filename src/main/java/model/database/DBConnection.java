package model.database;

import model.exception.DbException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBConnection {
    private static Connection connection = null;

    public static Connection getConnection(){
        if(connection == null){
            try {
                Properties properties = loadProperties();

                String url = properties.getProperty("dburl");

                connection = DriverManager.getConnection(url, properties);
            } catch (SQLException e){
                throw new DbException(e.getMessage());
            }
        }

        return connection;
    }

//    Carregar propriedades do db.properties
private static Properties loadProperties() {
    try {
        Properties properties = new Properties();

        var input = DBConnection.class
                .getClassLoader()
                .getResourceAsStream("db.properties");

        if (input == null) {
            throw new DbException("Arquivo db.properties não encontrado.");
        }

        properties.load(input);

        return properties;

    } catch (IOException e) {
        throw new DbException("Erro ao carregar db.properties.", e);
    }
}

    public static void closeStatement(Statement st){
        if(st != null){
            try {
                st.close();
            }catch (SQLException e){
                throw new DbException(e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet rs){
        if(rs != null){
            try {
                rs.close();
            }catch (SQLException e){
                throw new DbException(e.getMessage());
            }
        }
    }
}
