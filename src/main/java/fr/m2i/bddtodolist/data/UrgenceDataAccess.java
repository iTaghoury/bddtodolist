package fr.m2i.bddtodolist.data;

import fr.m2i.bddtodolist.exception.IdNotFoundException;
import fr.m2i.bddtodolist.exception.TodosRemainingException;
import fr.m2i.bddtodolist.model.Urgence;

import java.sql.*;
import java.util.ArrayList;

public class UrgenceDataAccess extends DataAccess implements AutoCloseable  {

    //region QUERY STRINGS
    private final String INSERT_URGENCE_QUERY = "INSERT INTO Urgence (urgenceLevel) VALUE (?)";
    private final String SELECT_URGENCE_QUERY = "SELECT * FROM Urgence";
    private final String SELECT_URGENCE_BY_ID = "SELECT * FROM Urgence WHERE urgenceId = ?";
    private final String UPDATE_URGENCE_QUERY = "UPDATE urgence SET urgenceLevel = ? ";
    private final String DELETE_URGENCE_QUERY = "DELETE FROM Urgence WHERE urgenceId NOT IN (SELECT urgenceId FROM Todo) AND urgenceId = ?";
    private final String CHECK_FOR_TODOS_QUERY = "SELECT * FROM Urgence WHERE urgenceId NOT IN (SELECT urgenceId FROM Todo) AND urgenceId = ?";
    //endregion

    //region CONSTRUCTOR

    public UrgenceDataAccess() {
        super();
    }

    //endregion

    //region QUERY METHODS

    //region CREATE QUERY

    public void addUrgenceToDB(Urgence urgence) throws SQLException {
        try(PreparedStatement ps = this.connection.prepareStatement(INSERT_URGENCE_QUERY)) {
            ps.setString(1, urgence.getUrgenceLevel());
            ps.execute();
        }
    }
    //endregion

    //region READ QUERIES
    public ArrayList<Urgence> getUrgenceFromDB() {
        ArrayList<Urgence> urgenceList = new ArrayList<>();
        try(PreparedStatement ps = this.connection.prepareStatement(SELECT_URGENCE_QUERY);
            ResultSet rs = ps.executeQuery())
        {
            while(rs.next()) {
                urgenceList.add(new Urgence(rs.getInt("urgenceId"), rs.getString("urgenceLevel")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return urgenceList;
    }

    public Urgence getUrgenceById(int id) throws SQLException {
        try(PreparedStatement ps = this.connection.prepareStatement(SELECT_URGENCE_BY_ID))
        {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    Urgence urgence = new Urgence(rs.getInt("urgenceId"), rs.getString("urgenceLevel"));
                    return urgence;
                } else {
                    throw new SQLException("URGENCE ID NOT FOUND");
                }
            }
        }
    }

    //endregion


    //region UPDATE QUERY

    public void updateUrgence(Urgence urgence) throws IdNotFoundException, SQLException {
        if(isIdInDB(urgence.getUrgenceId())) {
            try(PreparedStatement ps = this.connection.prepareStatement(UPDATE_URGENCE_QUERY)) {
                ps.setString(1, urgence.getUrgenceLevel());
                ps.setInt(2, urgence.getUrgenceId());
                ps.execute();
            }
        } else {
            throw new IdNotFoundException("Urgence ID NOT FOUND");
        }
    }

    //endregion

    //region DELETE QUERY

    public void deleteUrgence(int urgenceId) throws TodosRemainingException, IdNotFoundException, SQLException {
        if(isIdInDB(urgenceId)) {
            if(areThereTodosRemaining(urgenceId)) {
                throw new TodosRemainingException("Cannot delete Urgence level, todos still remaining");
            } else {
                try(PreparedStatement ps = this.connection.prepareStatement(DELETE_URGENCE_QUERY)) {
                    ps.setInt(1, urgenceId);
                    ps.execute();
                }
            }
        } else {
            throw new IdNotFoundException("Urgence ID NOT FOUND");
        }
    }

    //endregion

    //endregion

    //region OTHER METHODS

    private boolean isIdInDB(int id) {
        try(PreparedStatement ps = this.connection.prepareStatement(SELECT_URGENCE_BY_ID)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean areThereTodosRemaining(int urgenceId) {
        try(PreparedStatement ps = this.connection.prepareStatement(CHECK_FOR_TODOS_QUERY)) {
            ps.setInt(1, urgenceId);
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
