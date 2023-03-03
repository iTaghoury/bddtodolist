package fr.m2i.bddtodolist.api;

import fr.m2i.bddtodolist.data.DataAccess;
import fr.m2i.bddtodolist.model.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;

@Path("/user")
public class UserResource {
    @GET
    public Response getUserInfo(@QueryParam("id") int userId) {
       try(DataAccess da = DataAccess.getInstance()) {
           return Response
                   .status(Response.Status.OK)
                   .entity(String.format("%s", da.getUserInfoFromDB(userId)))
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
    public Response createUser(@FormParam("userName") String userName, @FormParam("userFirstName") String userFirstName) {
        try(DataAccess da = DataAccess.getInstance()){
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
}
