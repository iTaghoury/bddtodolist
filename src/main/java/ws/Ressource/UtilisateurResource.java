package fr.m2i.m2ws.todo.db.ws.Ressource;

import fr.m2i.m2ws.todo.db.ws.CRUD.UserCRUD;
import fr.m2i.m2ws.todo.db.ws.Module.Utilisateur;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;

@Singleton
@Path("/Utilisateur")
public class UtilisateurResource {


    public static UserCRUD AccessData = new UserCRUD();
    @GET
    @Path("/select")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Utilisateur> select() {
        ArrayList<Utilisateur> a = new ArrayList<Utilisateur>();
        AccessData.connection();
        a.addAll(AccessData.selectUtilisateur());
        AccessData.disconnection();
        return a;
    }
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public String insert(Utilisateur p) {
        AccessData.connection();
        int id = AccessData.insertionUtilisateur(p);
        AccessData.disconnection();
        return  " User id  " + id + " : "+ p.getNom() + " " + p.getPrenom();
    }
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@PathParam("id") int id) {
        AccessData.connection();
        AccessData.deleteUtilisateur(id);
        AccessData.disconnection();
        return  " User with id " + id +" deleted";
    }
    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Utilisateur update(@PathParam("id") int id,Utilisateur p) {
        AccessData.connection();
        AccessData.updateUtilisateur(id,p);
        AccessData.disconnection();
        return p;
    }
    @GET
    @Path("/select/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Utilisateur selectById(@PathParam("id") int id) {
        AccessData.connection();
        Utilisateur p=AccessData.selectUtilisateurParId(id);
        System.out.println(p.toString());
        AccessData.disconnection();
        return p;
    }

}

