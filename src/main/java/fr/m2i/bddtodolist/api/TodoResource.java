package fr.m2i.bddtodolist.api;

import fr.m2i.bddtodolist.data.TodoDataAccess;
import fr.m2i.bddtodolist.exception.IdNotFoundException;
import fr.m2i.bddtodolist.model.Todo;
import fr.m2i.bddtodolist.model.Urgence;
import fr.m2i.bddtodolist.model.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.Date;
import java.sql.SQLException;

@Path("/todo")
public class TodoResource {

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTodo(@FormParam("userId") int userId,
                               @FormParam("todoName") String todoName,
                               @FormParam("todoDesc") String todoDesc,
                               @FormParam("dateTodo") Date dateTodo,
                               @FormParam("urgenceId") int urgenceId)
    {
        Todo todo = new Todo(todoName, todoDesc, dateTodo);
        try(TodoDataAccess da = TodoDataAccess.getInstance()) {
            da.addTodoToDB(todo, urgenceId, userId);
            return Response
                    .status(Response.Status.CREATED)
                    .entity(todo)
                    .build();
        } catch (SQLException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodoById(@PathParam("id") int id) {
        try(TodoDataAccess da = TodoDataAccess.getInstance()) {
            return Response
                    .status(Response.Status.OK)
                    .entity(da.getTodoById(id))
                    .build();
        } catch (IdNotFoundException e1) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(e1.getMessage())
                    .build();
        } catch (SQLException e2) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e2.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodoList() {
        try(TodoDataAccess da = TodoDataAccess.getInstance()) {
            return Response
                    .status(Response.Status.OK)
                    .entity(da.getTodoFromDB())
                    .build();
        } catch (SQLException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodoListByUser(@QueryParam("id") int userId) {
        try(TodoDataAccess da = TodoDataAccess.getInstance()) {
            return Response
                    .status(Response.Status.OK)
                    .entity(da.getTodoByUserId(userId))
                    .build();
        } catch (IdNotFoundException e1) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(e1.getMessage())
                    .build();
        } catch (SQLException e2) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(e2.getMessage())
                .build();
        }
    }

    @GET
    @Path("/urgence")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodoListByUrgence(@QueryParam("id") int urgenceId) {
        try(TodoDataAccess da = TodoDataAccess.getInstance()) {
            return Response
                    .status(Response.Status.OK)
                    .entity(da.getTodoByUrgenceId(urgenceId))
                    .build();
        } catch (IdNotFoundException e1) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(e1.getMessage())
                    .build();
        } catch (SQLException e2) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e2.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/order")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderTodoList() {
        try(TodoDataAccess da = TodoDataAccess.getInstance()) {
            return Response
                    .status(Response.Status.OK)
                    .entity(da.getTodoOrderBy())
                    .build();
        } catch (SQLException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/update")
    public Response updateTodo( @QueryParam("id") int todoId,
                                @FormParam("userId") int userId,
                                @FormParam("todoName") String todoName,
                                @FormParam("todoDesc") String todoDesc,
                                @FormParam("dateTodo") Date dateTodo,
                                @FormParam("urgenceId") int urgenceId) {
        Todo todo = new Todo(todoId, todoName, todoDesc, dateTodo, new Urgence(urgenceId), new User(userId));
        try(TodoDataAccess da = TodoDataAccess.getInstance()) {
            da.updateTodo(todo);
            return Response
                    .status(Response.Status.OK)
                    .entity(todo)
                    .build();
        } catch (IdNotFoundException e1) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(e1.getMessage())
                    .build();
        } catch (SQLException e2) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(e2.getMessage())
                .build();
        }
    }

    @DELETE
    @Path("/delete")
    public Response deleteTodo(@QueryParam("id") int todoId) {
        try(TodoDataAccess da = TodoDataAccess.getInstance()) {
            da.deleteTodoFromDB(todoId);
            return Response
                    .status(Response.Status.OK)
                    .entity(String.format("Deleted Todo with id %d", todoId))
                    .build();
        } catch (IdNotFoundException e1) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(e1.getMessage())
                    .build();
        } catch (SQLException e2) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e2.getMessage())
                    .build();
        }
    }

}
