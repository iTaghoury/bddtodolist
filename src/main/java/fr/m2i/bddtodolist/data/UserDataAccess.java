package fr.m2i.bddtodolist.data;

import fr.m2i.bddtodolist.model.User;

import java.sql.*;
import java.util.ArrayList;

public class UserDataAccess implements AutoCloseable{
    private Connection connection;
    private static UserDataAccess instance;


    static {
        instance = null;
    }
    //region USER PASSWORD AND URL
    private final String USER = "root";
    private final String PASSWORD = "Tassemanouche1";
    private static final String URL = "jdbc:mysql://localhost:3306/todoList?connectTimeout=3000&useSSL=false&allowPublicKeyRetrieval=true";
    //endregion

    //region QUERY STRINGS
    private final String SELECT_USER_QUERY = "SELECT * FROM User";
    private final String SELECT_USER_BY_ID = "SELECT * FROM User WHERE userId = ?";
    private final String INSERT_USER_QUERY = "INSERT INTO User (userName, userFirstName) VALUE (?, ?)";

    //endregion

    //region COMMON METHODS
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

    //region CREATE QUERY
    public void addUserToDB(User user) throws SQLException {
        try(PreparedStatement ps = this.connection.prepareStatement(INSERT_USER_QUERY)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getFirstName());
            ps.execute();
        }
    }

    //endregion

    //region READ QUERIES

    public User getUserById(int userId) throws SQLException{
        try(PreparedStatement ps = this.connection.prepareStatement(SELECT_USER_BY_ID))
        {
            ps.setInt(1, userId);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    User user = new User(rs.getInt("userId"), rs.getString("userName"), rs.getString("userFirstName"));
                    return user;
                } else {
                    throw new SQLException("USER ID NOT FOUND");
                }
            }
        }
    }

    public ArrayList<User> getUserFromDB() {
        ArrayList<User> userList = new ArrayList<>();
        try(PreparedStatement ps = this.connection.prepareStatement(SELECT_USER_QUERY);
            ResultSet rs = ps.executeQuery())
        {
            while(rs.next()) {
                userList.add(new User(rs.getInt("userId"), rs.getString("userName"), rs.getString("userFirstName")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userList;
    }

    //endregion

}
