package fr.m2i.m2ws.todo.db.ws.Ressource;

import fr.m2i.m2ws.todo.db.ws.CRUD.UrgenceCRUD;
import fr.m2i.m2ws.todo.db.ws.Module.Urgence;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;

@Singleton
@Path("/Urgence")
public class UrgenceResource {


    public static UrgenceCRUD AccessData = new UrgenceCRUD();
    @GET
    @Path("/select")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Urgence> select() {
        ArrayList<Urgence> a = new ArrayList<Urgence>();
        AccessData.connection();
        a.addAll(AccessData.selectUrgence());
        AccessData.disconnection();
        return a;
    }
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public String insert(Urgence p) {
        AccessData.connection();
        int id = AccessData.insertionUrgence(p);
        AccessData.disconnection();
        return  " id  " + id + " urgence "+ p.getNom();
    }
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@PathParam("id") int id) {
        AccessData.connection();
        AccessData.deleteUrgence(id);
        AccessData.disconnection();
        return  " urgence with id " + id +" deleted";
    }
    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Urgence update(@PathParam("id") int id,Urgence p) {
        AccessData.connection();
        AccessData.updateUrgence(id,p);
        AccessData.disconnection();
        return p;
    }
    @GET
    @Path("/select/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Urgence selectById(@PathParam("id") int id) {
        AccessData.connection();
        Urgence p=AccessData.selectUrgenceParId(id);
        System.out.println(p.toString());
        AccessData.disconnection();
        return p;
    }

}

