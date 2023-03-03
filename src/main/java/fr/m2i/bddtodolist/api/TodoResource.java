package fr.m2i.bddtodolist.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/todo")
public class TodoResource {
    @GET
    public String getTodoById(@QueryParam("id") int id) {
        return null;
    }
}
