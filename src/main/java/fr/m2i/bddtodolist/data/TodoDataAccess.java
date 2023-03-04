package fr.m2i.bddtodolist.data;

import java.sql.*;

public class TodoDataAccess implements AutoCloseable{
    private Connection connection;
    private static TodoDataAccess instance;


    static {
        instance = null;
    }

    //region USER PASSWORD AND URL
    private final String USER = "root";
    private final String PASSWORD = "0628Cara*";
    private static final String URL = "jdbc:mysql://localhost:3306/todoList?connectTimeout=3000&useSSL=false&allowPublicKeyRetrieval=true";
    //endregion

    //region QUERY STRINGS
    private final String SELECT_TODO_QUERY = "SELECT todoId, todoName, todoDesc, dateTodo, urgenceId, User.userName, User.userFirstName FROM Todo INNER JOIN User ON Todo.userId = User.userId";
    private final String SELECT_TODO_BY_ID = "SELECT todoId, todoName, todoDesc, dateTodo, urgenceId, User.userName, User.userFirstName FROM Todo INNER JOIN User ON Todo.userId = User.userId WHERE todoId = ?";
    private final String INSERT_TODO_QUERY = "INSERT INTO Todo (todoName, todoDesc, dateTodo, urgenceId, userId) VALUE (?, ?, ?, ?, ?)";
    //endregion

    //region COMMON METHODS
    public Connection getConnection() {
        return connection;
    }

    private TodoDataAccess() {
        this.createConnection();
        instance = this;
    }

    public static TodoDataAccess getInstance() {
        if(instance == null) {
            return new TodoDataAccess();
        } else {
            try {
                if(instance.connection.isClosed()) {
                    instance.createConnection();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
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
    //endregion
}
