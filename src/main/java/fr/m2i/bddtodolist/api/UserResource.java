package fr.m2i.bddtodolist.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/user")
public class UserResource {
    @GET
    public String getUserInfo(@QueryParam("id") int userId) {
        return null;
    }
}
