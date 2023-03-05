package fr.m2i.bddtodolist.data;

import fr.m2i.bddtodolist.exception.IdNotFoundException;
import fr.m2i.bddtodolist.model.Todo;
import fr.m2i.bddtodolist.model.Urgence;
import fr.m2i.bddtodolist.model.User;

import java.sql.*;
import java.util.ArrayList;

public class TodoDataAccess extends DataAccess implements AutoCloseable{


    //region QUERY STRINGS
    private final String INSERT_TODO_QUERY = "INSERT INTO Todo (todoName, todoDesc, dateTodo, urgenceId, userId) VALUE (?, ?, ?, ?, ?)";
    private final String SELECT_TODO_QUERY = "SELECT todoId, todoName, todoDesc, dateTodo, Urgence.urgenceId,  Urgence.urgenceLevel, User.userName, User.userFirstName, User.userId FROM Todo INNER JOIN User ON Todo.userId = User.userId INNER JOIN Urgence ON Todo.urgenceId = Urgence.urgenceId ORDER BY todoId";
    private final String SELECT_TODO_BY_ID = "SELECT todoId, todoName, todoDesc, dateTodo, Urgence.urgenceId, Urgence.urgenceLevel, User.userName, User.userFirstName, User.userId FROM Todo INNER JOIN User ON Todo.userId = User.userId INNER JOIN Urgence ON Todo.urgenceId = Urgence.urgenceId WHERE todoId = ?";
    private final String SELECT_TODO_BY_USER_ID = "SELECT todoId, todoName, todoDesc, dateTodo, Urgence.urgenceId,  Urgence.urgenceLevel, User.userName, User.userFirstName, User.userId FROM Todo INNER JOIN User ON Todo.userId = User.userId INNER JOIN Urgence ON Todo.urgenceId = Urgence.urgenceId WHERE User.userId = ?";
    private final String SELECT_TODO_BY_URGENCE_ID = "SELECT todoId, todoName, todoDesc, dateTodo, Urgence.urgenceId,  Urgence.urgenceLevel, User.userName, User.userFirstName, User.userId FROM Todo INNER JOIN User ON Todo.userId = User.userId INNER JOIN Urgence ON Todo.urgenceId = Urgence.urgenceId WHERE Urgence.urgenceId = ?";
    private final String SELECT_TODO_ORDER_BY = "SELECT todoId, todoName, todoDesc, dateTodo, Urgence.urgenceId,  Urgence.urgenceLevel, User.userName, User.userFirstName, User.userId FROM Todo INNER JOIN User ON Todo.userId = User.userId INNER JOIN Urgence ON Todo.urgenceId = Urgence.urgenceId ORDER BY User.userId, Urgence.urgenceId";
    private final String UPDATE_TODO = "UPDATE todo SET todoName = ?, todoDesc = ?, dateTodo = ?, urgenceId = ?, userId = ? WHERE todoId = ?";
    private final String DELETE_TODO = "DELETE FROM Todo WHERE todoId = ?";
    //endregion

    //region CONSTRUCTOR

    public TodoDataAccess() {
        super();
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

    public Todo getTodoById(int id) throws IdNotFoundException {
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
                    throw new IdNotFoundException("Todo ID NOT FOUND");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
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

    public ArrayList<Todo> getTodoByUserId(int id) throws IdNotFoundException {
        ArrayList<Todo> todoList;
        try(PreparedStatement ps = this.connection.prepareStatement(SELECT_TODO_BY_USER_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    todoList = pushTodos(rs);
                } else {
                    throw new IdNotFoundException("User ID NOT FOUND OR USER HAS NO TODO");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return todoList;
    }

    public ArrayList<Todo> getTodoByUrgenceId(int id) throws IdNotFoundException {
        ArrayList<Todo> todoList;
        try(PreparedStatement ps = this.connection.prepareStatement(SELECT_TODO_BY_URGENCE_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    todoList = pushTodos(rs);
                } else {
                    throw new IdNotFoundException("Urgence ID NOT FOUND OR NO TODO WITH THIS LEVEL");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
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

    //region UPDATE QUERY
    public void updateTodo(Todo todo) throws SQLException, IdNotFoundException {
        if (isIdInDB(todo.getId())) {
            try(PreparedStatement ps = this.connection.prepareStatement(UPDATE_TODO)) {
                ps.setString(1, todo.getName());
                ps.setString(2, todo.getDescription());
                ps.setDate(3, todo.getDate());
                ps.setInt(4, todo.getUrgence().getUrgenceId());
                ps.setInt(5, todo.getUser().getId());
                ps.setInt(6, todo.getId());
                ps.execute();
            }
        } else {
            throw new IdNotFoundException("Todo ID NOT FOUND");
        }
    }
    //endregion

    //region DELETE QUERY

    public void deleteTodoFromDB(int todoId) throws IdNotFoundException, SQLException {
        if(isIdInDB(todoId)) {
            try (PreparedStatement ps = this.connection.prepareStatement(DELETE_TODO)) {
                ps.setInt(1, todoId);
                ps.execute();
            }
        } else {
            throw new IdNotFoundException("Todo ID NOT FOUND");
        }
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

    private boolean isIdInDB(int id) {
        try(PreparedStatement ps = this.connection.prepareStatement(SELECT_TODO_BY_ID)) {
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
