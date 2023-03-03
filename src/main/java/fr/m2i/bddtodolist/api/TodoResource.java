package fr.m2i.bddtodolist.api;

import fr.m2i.bddtodolist.data.DataAccess;
import fr.m2i.bddtodolist.model.Todo;
import fr.m2i.bddtodolist.model.Urgence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.sql.Date;
import java.sql.SQLException;

@Path("/todo")
public class TodoResource {
    @GET
    public Response getTodoById(@QueryParam("id") int id) {
        try(DataAccess da = DataAccess.getInstance()) {
            return Response
                    .status(Response.Status.OK)
                    .entity(String.format("%s", da.getTodoFromDB(id)))
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
    public Response createTodoByUserId(@QueryParam("userId") int userId,
                                       @FormParam("todoName") String todoName,
                                       @FormParam("todoDesc") String todoDesc,
                                       @FormParam("dateTodo") Date dateTodo,
                                       @FormParam("todoUrgence") int urgence)
    {
        Todo todo = new Todo(todoName, todoDesc, dateTodo, Urgence.values()[urgence - 1], userId);
        try(DataAccess da = DataAccess.getInstance()) {
            da.addTodoToDB(todo);
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
