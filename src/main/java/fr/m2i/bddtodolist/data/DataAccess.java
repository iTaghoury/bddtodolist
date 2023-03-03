package fr.m2i.bddtodolist.data;

import fr.m2i.bddtodolist.model.Todo;
import fr.m2i.bddtodolist.model.User;

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
    private static final String URL = "jdbc:mysql://localhost:3306/todoList?connectTimeout=3000&useSSL=false&allowPublicKeyRetrieval=true";
    //endregion

    private final String SELECT_QUERY = "SELECT * FROM Urgence";
    private final String SELECT_USER_BY_ID = "SELECT * FROM User WHERE userId = ?";
    private final String SELECT_TODO_BY_ID = "SELECT todoId, todoName, todoDesc, dateTodo, urgenceId, User.userName, User.userFirstName FROM Todo INNER JOIN User ON Todo.userId = User.userId WHERE todoId = ?";
    private final String INSERT_TODO_QUERY = "INSERT INTO Todo (todoName, todoDesc, dateTodo, urgenceId, userId) VALUE (?, ?, ?, ?, ?)";
    private final String INSERT_USER_QUERY = "INSERT INTO User (userName, userFirstName) VALUE (?, ?)";
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
        instance = null;
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

    public void addTodoToDB(Todo todo) throws SQLException {
        try(PreparedStatement ps = this.connection.prepareStatement(INSERT_TODO_QUERY)) {
            ps.setString(1, todo.getName());
            ps.setString(2, todo.getDescription());
            ps.setDate(3, todo.getDate());
            ps.setInt(4, todo.getUrgence().ordinal()+1);
            ps.setInt(5, todo.getUserId());
            ps.execute();
        }
    }

    public StringBuilder getTodoFromDB(int todoId) throws SQLException {
        StringBuilder sb = new StringBuilder();
        try(PreparedStatement ps = this.connection.prepareStatement(SELECT_TODO_BY_ID))
        {
            ps.setInt(1, todoId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                sb.append(String.format("Todo %d\nTodo Name : %s\nTodo Description : %s\nTodo date : %s\nUrgence : %d\nUser Name : %s\nUser first name : %s",
                                        rs.getInt("todoId"),
                                        rs.getString("todoName"),
                                        rs.getString("todoDesc"),
                                        rs.getDate("dateTodo").toString(),
                                        rs.getInt("urgenceId"),
                                        rs.getString("userName"),
                                        rs.getString("userFirstName")));
            } else {
                throw new SQLException("TODO ID NOT FOUND");
            }
            rs.close();
        }
        return sb;
    }

    public void addUserToDB(User user) throws SQLException {
        try(PreparedStatement ps = this.connection.prepareStatement(INSERT_USER_QUERY)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getFirstName());
            ps.execute();
        }
    }

    public StringBuilder getUserInfoFromDB(int userId) throws SQLException{
        StringBuilder sb = new StringBuilder();
        try(PreparedStatement ps = this.connection.prepareStatement(SELECT_USER_BY_ID))
        {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                sb.append(String.format("User name : %s\n User first name : %s", rs.getString("userName"), rs.getString("userFirstName")));
            } else {
                throw new SQLException("USER ID NOT FOUND");
            }
            rs.close();
        }
        return sb;
    }

}
