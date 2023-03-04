package fr.m2i.bddtodolist.data;

import java.sql.*;

public class UserDataAccess implements AutoCloseable{
    private Connection connection;
    private static UserDataAccess instance;


    static {
        instance = null;
    }
    //region USER PASSWORD AND URL
    private final String USER = "root";
    private final String PASSWORD = "0628Cara*";
    private static final String URL = "jdbc:mysql://localhost:3306/todoList?connectTimeout=3000&useSSL=false&allowPublicKeyRetrieval=true";
    //endregion

    public Connection getConnection() {
        return connection;
    }

    private UserDataAccess() {
        this.createConnection();
        instance = this;
    }

    public static UserDataAccess getInstance() {
        if(instance == null) {
            return new UserDataAccess();
        } else {
            if(instance.connection == null) {
                instance.createConnection();
            }
        }
        return instance;
    }

    public void createConnection() {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Ferme la connexion à la base de données
     * @throws SQLException
     */
    @Override
    public void close() throws SQLException {
        if(this.connection != null) {
            this.connection.close();
        }
    }
}
