package fr.m2i.bddtodolist.api;

import fr.m2i.bddtodolist.data.DataAccess;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.sql.SQLException;

@Path("/hello-world")
public class HelloResource {
    @GET
    @Path("/test")
    public String getUrgenceTest() {
        try(DataAccess da = DataAccess.getInstance())
        {
            return String.format("%s", da.isConnectionSuccess());
        } catch (SQLException e) {
            return e.getMessage();
        }
    }
}