package fr.m2i.bddtodolist.api;

import fr.m2i.bddtodolist.data.TodoDataAccess;
import fr.m2i.bddtodolist.model.Todo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.sql.Date;
import java.sql.SQLException;

@Path("/todo")
public class TodoResource {
    @GET
    @Path("/{id}")
    public Response getTodoById(@PathParam("id") int id) {
        try(TodoDataAccess da = TodoDataAccess.getInstance()) {
            return Response
                    .status(Response.Status.OK)
                    .entity(da.getTodoById(id))
                    .build();
        } catch (SQLException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
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

    @POST
    @Path("/create")
    public Response createTodo(@FormParam("userId") int userId,
                               @FormParam("todoName") String todoName,
                               @FormParam("todoDesc") String todoDesc,
                               @FormParam("dateTodo") Date dateTodo,
                               @FormParam("urgenceId") int urgenceId)
    {
        Todo todo = new Todo(todoName, todoDesc, dateTodo);
        try(TodoDataAccess da = TodoDataAccess.getInstance()) {
            da.addTodoToDB(todo, urgenceId, userId);
        } catch (SQLException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
        return Response
                .status(Response.Status.CREATED)
                .entity(todo)
                .build();
    }

}
