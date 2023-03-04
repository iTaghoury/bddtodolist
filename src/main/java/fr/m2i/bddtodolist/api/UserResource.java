package fr.m2i.bddtodolist.api;

import fr.m2i.bddtodolist.data.UserDataAccess;
import fr.m2i.bddtodolist.exception.IdNotFoundException;
import fr.m2i.bddtodolist.model.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;

@Path("/user")
public class UserResource {

    @POST
    @Path("/create")
    public Response createUser(@FormParam("userName") String userName, @FormParam("userFirstName") String userFirstName) {
        try(UserDataAccess da = UserDataAccess.getInstance()){
            User user = new User(userName, userFirstName);
            da.addUserToDB(user);
            return Response
                    .status(Response.Status.CREATED)
                    .entity(user)
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") int userId) {
       try(UserDataAccess da = UserDataAccess.getInstance()) {
           return Response
                   .status(Response.Status.OK)
                   .entity(da.getUserById(userId))
                   .build();
       } catch (SQLException e) {
           return Response
                   .status(Response.Status.NOT_FOUND)
                   .entity(e.getMessage())
                   .build();
       }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersList() {
        try(UserDataAccess da = UserDataAccess.getInstance()) {
            return Response
                    .status(Response.Status.OK)
                    .entity(da.getUserFromDB())
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
    public Response updateUser(@QueryParam("id") int id, @FormParam("userName") String userName, @FormParam("userFirstName") String userFirstName) {
        User user = new User(id, userName, userFirstName);
        try(UserDataAccess da= UserDataAccess.getInstance()) {
            da.updateUser(user);
            return Response
                    .status(Response.Status.OK)
                    .entity(user)
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
