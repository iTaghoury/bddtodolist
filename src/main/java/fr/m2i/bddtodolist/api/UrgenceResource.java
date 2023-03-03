package fr.m2i.bddtodolist.api;

import fr.m2i.bddtodolist.data.DataAccess;
import fr.m2i.bddtodolist.model.Urgence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;

@Path("/urgence")
public class UrgenceResource {

    @GET
    @Path("/{id}")
    public Response getUrgenceById(@PathParam("id") int id) {
        try(DataAccess da = DataAccess.getInstance()) {
            return Response
                    .status(Response.Status.OK)
                    .entity(String.format("%s", da.getUrgenceById(id)))
                    .build();
        } catch (SQLException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    public Response getUrgence() {
        try(DataAccess da = DataAccess.getInstance()) {
            return Response
                    .status(Response.Status.OK)
                    .entity(String.format("%s", da.getUrgenceFromDB()))
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
    public Response createUrgence(@FormParam("urgenceLevel") String urgenceLevel) {
        Urgence urgence = new Urgence(urgenceLevel);
        try(DataAccess da = DataAccess.getInstance()) {
            da.addUrgenceToDB(urgence);
        } catch (SQLException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
        return Response
                .status(Response.Status.CREATED)
                .entity(urgence)
                .build();
    }
}

