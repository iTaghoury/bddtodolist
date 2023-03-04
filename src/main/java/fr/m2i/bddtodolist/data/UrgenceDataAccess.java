package fr.m2i.bddtodolist.data;

import fr.m2i.bddtodolist.exception.IdNotFoundException;
import fr.m2i.bddtodolist.model.Urgence;

import java.sql.*;
import java.util.ArrayList;

public class UrgenceDataAccess implements AutoCloseable  {
    private Connection connection;
    private static UrgenceDataAccess instance;

    static {
        instance = null;
    }

    //region USER PASSWORD AND URL
    private final String USER = "root";
    private final String PASSWORD = "0628Cara*";
    private static final String URL = "jdbc:mysql://localhost:3306/todoList?connectTimeout=3000&useSSL=false&allowPublicKeyRetrieval=true";
    //endregion

    //region QUERY STRINGS
    private final String SELECT_URGENCE_QUERY = "SELECT * FROM Urgence";
    private final String SELECT_URGENCE_BY_ID = "SELECT * FROM Urgence WHERE urgenceId = ?";
    private final String INSERT_URGENCE_QUERY = "INSERT INTO Urgence (urgenceLevel) VALUE (?)";
    private final String UPDATE_URGENCE_QUERY = "UPDATE urgence SET urgenceLevel = ? WHERE urgenceId = ?";
    //endregion

    //region COMMON METHODS
    public Connection getConnection() {
        return connection;
    }

    private UrgenceDataAccess() {
        this.createConnection();
        instance = this;
    }

    public static UrgenceDataAccess getInstance() {
        if(instance == null) {
            return new UrgenceDataAccess();
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

    //endregion

}
