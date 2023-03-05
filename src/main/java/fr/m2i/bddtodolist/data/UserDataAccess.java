package fr.m2i.bddtodolist.data;

import fr.m2i.bddtodolist.exception.IdNotFoundException;
import fr.m2i.bddtodolist.model.User;

import java.sql.*;
import java.util.ArrayList;

public class UserDataAccess extends DataAccess implements AutoCloseable{


    //region QUERY STRINGS
    private final String SELECT_USER_QUERY = "SELECT * FROM User";
    private final String SELECT_USER_BY_ID = "SELECT * FROM User WHERE userId = ?";
    private final String INSERT_USER_QUERY = "INSERT INTO User (userName, userFirstName) VALUE (?, ?)";
    private final String UPDATE_USER_QUERY = "UPDATE user SET userName = ?, userFirstName = ? WHERE userId = ?";
    private final String DELETE_USER_QUERY = "DELETE FROM User WHERE userId = ?";
    private final String DELETE_USER_AND_TODOS_QUERY = "DELETE Todo.*, User.* FROM Todo LEFT JOIN User ON User.userId = Todo.userId WHERE Todo.userId = ?";
    private static final String CHECK_FOR_TODOS_QUERY = "SELECT * FROM User WHERE userId NOT IN (SELECT userId FROM Todo) AND userId = ?";
    //endregion

    public UserDataAccess() {
        super();
    }


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

    //region UPDATE QUERY
    public void updateUser(User user) throws IdNotFoundException, SQLException {
        if(isIdInDB(user.getId())) {
            try(PreparedStatement ps = this.connection.prepareStatement(UPDATE_USER_QUERY)) {
                ps.setString(1, user.getName());
                ps.setString(2, user.getFirstName());
                ps.setInt(3, user.getId());
                ps.execute();
            }
        } else {
            throw new IdNotFoundException("User ID NOT FOUND");
        }
    }

    //endregion

    //region DELETE QUERY

    public void deleteUser(int userId) throws IdNotFoundException, SQLException {
        final String QUERY = areThereTodosRemaining(userId) ? DELETE_USER_AND_TODOS_QUERY : DELETE_USER_QUERY;
        if(isIdInDB(userId)) {
            try(PreparedStatement ps = this.connection.prepareStatement(QUERY)) {
                ps.setInt(1, userId);
                ps.execute();
            }
        } else {
            throw new IdNotFoundException("User ID NOT FOUND");
        }
    }

    //endregion

    //region OTHER METHODS
    private boolean isIdInDB(int id) {
        try(PreparedStatement ps = this.connection.prepareStatement(SELECT_USER_BY_ID)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean areThereTodosRemaining(int userId) {
        try(PreparedStatement ps = this.connection.prepareStatement(CHECK_FOR_TODOS_QUERY)) {
            ps.setInt(1, userId);
            try(ResultSet rs = ps.executeQuery()) {
                return !rs.next();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return true;
        }
    }

    //endregion

}
