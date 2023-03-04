package fr.m2i.bddtodolist.api;

import fr.m2i.bddtodolist.data.DataAccess;
import fr.m2i.bddtodolist.data.UrgenceDataAccess;
import fr.m2i.bddtodolist.model.Urgence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;

@Path("/urgence")
public class UrgenceResource {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUrgenceById(@PathParam("id") int id) {
        try(UrgenceDataAccess da = UrgenceDataAccess.getInstance()) {
            return Response
                    .status(Response.Status.OK)
                    .entity(da.getUrgenceById(id))
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
    public Response getUrgence() {
        try(UrgenceDataAccess da = UrgenceDataAccess.getInstance()) {
            return Response
                    .status(Response.Status.OK)
                    .entity(da.getUrgenceFromDB())
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
        try(UrgenceDataAccess da = UrgenceDataAccess.getInstance()) {
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

