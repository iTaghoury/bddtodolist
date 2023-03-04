package fr.m2i.bddtodolist.api;

import fr.m2i.bddtodolist.data.UrgenceDataAccess;
import fr.m2i.bddtodolist.data.UserDataAccess;
import fr.m2i.bddtodolist.exception.IdNotFoundException;
import fr.m2i.bddtodolist.exception.TodosRemainingException;
import fr.m2i.bddtodolist.model.Urgence;
import fr.m2i.bddtodolist.model.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;

@Path("/urgence")
public class UrgenceResource {

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

    @PUT
    @Path("/update")
    public Response updateUrgence(@QueryParam("id") int id, @FormParam("urgenceLevel") String urgenceLevel) {
        Urgence urgence = new Urgence(id, urgenceLevel);
        try (UrgenceDataAccess da = UrgenceDataAccess.getInstance()) {
            da.updateUrgence(urgence);
            return Response
                    .status(Response.Status.OK)
                    .entity(urgence)
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
    public Response deleteUrgence(@QueryParam("id") int id) {
        try(UrgenceDataAccess da = UrgenceDataAccess.getInstance()) {
            da.deleteUrgence(id);
            return Response
                    .status(Response.Status.OK)
                    .entity(String.format("Deleted Todo with id %d", id))
                    .build();
        } catch (TodosRemainingException e) {
            return Response
                  .status(Response.Status.FORBIDDEN)
                  .entity(e.getMessage())
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

