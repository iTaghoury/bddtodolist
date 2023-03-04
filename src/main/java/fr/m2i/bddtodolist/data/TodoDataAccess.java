package fr.m2i.bddtodolist.data;

import fr.m2i.bddtodolist.model.Todo;
import fr.m2i.bddtodolist.model.Urgence;
import fr.m2i.bddtodolist.model.User;

import java.sql.*;
import java.util.ArrayList;

public class TodoDataAccess implements AutoCloseable{
    private Connection connection;
    private static TodoDataAccess instance;


    static {
        instance = null;
    }

    //region USER PASSWORD AND URL
    private final String USER = "root";
    private final String PASSWORD = "Tassemanouche1";
    private static final String URL = "jdbc:mysql://localhost:3306/todoList?connectTimeout=3000&useSSL=false&allowPublicKeyRetrieval=true";
    //endregion

    //region QUERY STRINGS
    private final String INSERT_TODO_QUERY = "INSERT INTO Todo (todoName, todoDesc, dateTodo, urgenceId, userId) VALUE (?, ?, ?, ?, ?)";
    private final String SELECT_TODO_QUERY = "SELECT todoId, todoName, todoDesc, dateTodo, Urgence.urgenceId,  Urgence.urgenceLevel, User.userName, User.userFirstName, User.userId FROM Todo INNER JOIN User ON Todo.userId = User.userId INNER JOIN Urgence ON Todo.urgenceId = Urgence.urgenceId";
    private final String SELECT_TODO_BY_ID = "SELECT todoId, todoName, todoDesc, dateTodo, Urgence.urgenceId, Urgence.urgenceLevel, User.userName, User.userFirstName, User.userId FROM Todo INNER JOIN User ON Todo.userId = User.userId INNER JOIN Urgence ON Todo.urgenceId = Urgence.urgenceId WHERE todoId = ?";
    private final String SELECT_TODO_BY_USER_ID = "SELECT todoId, todoName, todoDesc, dateTodo, Urgence.urgenceId,  Urgence.urgenceLevel, User.userName, User.userFirstName, User.userId FROM Todo INNER JOIN User ON Todo.userId = User.userId INNER JOIN Urgence ON Todo.urgenceId = Urgence.urgenceId WHERE User.userId = ?";
    private final String SELECT_TODO_BY_URGENCE_ID = "SELECT todoId, todoName, todoDesc, dateTodo, Urgence.urgenceId,  Urgence.urgenceLevel, User.userName, User.userFirstName, User.userId FROM Todo INNER JOIN User ON Todo.userId = User.userId INNER JOIN Urgence ON Todo.urgenceId = Urgence.urgenceId WHERE Urgence.urgenceId = ?";
    private final String SELECT_TODO_ORDER_BY = "SELECT todoId, todoName, todoDesc, dateTodo, Urgence.urgenceId,  Urgence.urgenceLevel, User.userName, User.userFirstName, User.userId FROM Todo INNER JOIN User ON Todo.userId = User.userId INNER JOIN Urgence ON Todo.urgenceId = Urgence.urgenceId ORDER BY User.userId, Urgence.urgenceId";
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

    //region CREATE QUERY
    public void addTodoToDB(Todo todo, int urgenceId, int userId) throws SQLException {
        try(PreparedStatement ps = this.connection.prepareStatement(INSERT_TODO_QUERY)) {
            ps.setString(1, todo.getName());
            ps.setString(2, todo.getDescription());
            ps.setDate(3, todo.getDate());
            ps.setInt(4, urgenceId);
            ps.setInt(5, userId);
            ps.execute();
        }
    }
    //endregion

    //region READ QUERIES

    public Todo getTodoById(int id) throws SQLException {
        try(PreparedStatement ps = this.connection.prepareStatement(SELECT_TODO_BY_ID)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    User todoUser = new User(rs.getInt("userId"), rs.getString("userName"), rs.getString("userFirstName"));
                    Urgence todoUrgence = new Urgence(rs.getInt("urgenceId"), rs.getString("urgenceLevel"));
                    return new Todo(rs.getInt("todoId"),
                                    rs.getString("todoName"),
                                    rs.getString("todoDesc"),
                                    rs.getDate("dateTodo"),
                                    todoUrgence,
                                    todoUser);
                } else {
                    throw new SQLException("Todo ID NOT FOUND");
                }
            }
        }
    }

    public ArrayList<Todo> getTodoFromDB() throws SQLException {
        ArrayList<Todo> todoList;
        try(PreparedStatement ps = this.connection.prepareStatement(SELECT_TODO_QUERY);
            ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                todoList = pushTodos(rs);
            } else {
                throw new SQLException("Todolist is empty");
            }
        }
        return todoList;
    }

    public ArrayList<Todo> getTodoByUserId(int id) throws SQLException {
        ArrayList<Todo> todoList;
        try(PreparedStatement ps = this.connection.prepareStatement(SELECT_TODO_BY_USER_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    todoList = pushTodos(rs);
                } else {
                    throw new SQLException("User ID NOT FOUND");
                }

            }
        }
        return todoList;
    }

    public ArrayList<Todo> getTodoByUrgenceId(int id) throws SQLException {
        ArrayList<Todo> todoList;
        try(PreparedStatement ps = this.connection.prepareStatement(SELECT_TODO_BY_URGENCE_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    todoList = pushTodos(rs);
                } else {
                    throw new SQLException("Urgence ID NOT FOUND");
                }

            }
        }
        return todoList;
    }

    public ArrayList<Todo> getTodoOrderBy() throws SQLException {
        ArrayList<Todo> todoList;
        try(PreparedStatement ps = this.connection.prepareStatement(SELECT_TODO_ORDER_BY);
            ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                todoList = pushTodos(rs);
            } else {
                throw new SQLException("Todolist is empty");
            }
        }
        return todoList;
    }

    //endregion

    //region OTHER METHODS
    private ArrayList<Todo> pushTodos(ResultSet rs) {
        ArrayList<Todo> todoList = new ArrayList<>();
        try {
            do {
                User todoUser = new User(rs.getInt("userId"), rs.getString("userName"), rs.getString("userFirstName"));
                Urgence todoUrgence = new Urgence(rs.getInt("urgenceId"), rs.getString("urgenceLevel"));
                todoList.add(new Todo(rs.getInt("todoId"),
                        rs.getString("todoName"),
                        rs.getString("todoDesc"),
                        rs.getDate("dateTodo"),
                        todoUrgence,
                        todoUser));
            } while (rs.next());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return todoList;
    }

    //endregion


}
