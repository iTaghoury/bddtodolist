package fr.m2i.bddtodolist.data;

import java.sql.*;

public class DataAccess implements AutoCloseable{
    private Connection connection;
    private String connectionSuccess;
    private static DataAccess instance;

    public String isConnectionSuccess() {
        return connectionSuccess;
    }

    static {
        instance = null;
    }

    //region USER PASSWORD AND URL
    private final String USER = "root";
    private final String PASSWORD = "0628Cara*";
    private static final String URL = "jdbc:mysql://localhost:3306/todoList?useSSL=false&allowPublicKeyRetrieval=true";
    //endregion

    private final String SELECT_QUERY = "SELECT * FROM Urgence";
    public Connection getConnection() {
        return connection;
    }

    private DataAccess() {
        this.connectionSuccess = this.createConnection();
        instance = this;
    }

    public static DataAccess getInstance() {
        if(instance == null) {
            return new DataAccess();
        } else {
            return instance;
        }
    }

    public String createConnection() {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            return e.getMessage();
        }
        this.connectionSuccess = this.connection != null ? "true" : "false";
        return this.connectionSuccess;
    }
    @Override
    public void close() throws SQLException {
        if(this.connection != null) {
            this.connection.close();
        }
    }

    public StringBuilder getUrgence() {
        StringBuilder sb = new StringBuilder();
        try(PreparedStatement ps = this.connection.prepareStatement(SELECT_QUERY);
            ResultSet rs = ps.executeQuery())
        {
            while(rs.next()) {
                sb.append(String.format(
                        "Urgence Id : %d, Urgence Level : %s\n",
                        rs.getInt(1),
                        rs.getString(2)
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return sb;
    }
}
