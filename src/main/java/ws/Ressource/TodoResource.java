package fr.m2i.m2ws.todo.db.ws.Ressource;

import fr.m2i.m2ws.todo.db.ws.CRUD.TodoCRUD;
import fr.m2i.m2ws.todo.db.ws.Module.Todo;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;

@Singleton
@Path("/Todo")
public class TodoResource {


    public static TodoCRUD AccessData = new TodoCRUD();
    @GET
    @Path("/select/tri/urgence")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Todo> selectTriByUrgence() {
        ArrayList<Todo> a = new ArrayList<Todo>();
        AccessData.connection();
        a.addAll(AccessData.selectTodoTriByUrgence());
        AccessData.disconnection();
        return a;
    }

    @GET
    @Path("/select/tri/user")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Todo> selectTriByUser() {
        ArrayList<Todo> a = new ArrayList<Todo>();
        AccessData.connection();
        a.addAll(AccessData.selectTodoTriByUtilisateur());
        AccessData.disconnection();
        return a;
    }
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public String insert(Todo p) {
        AccessData.connection();
        int id = AccessData.insertionTodo(p);
        AccessData.disconnection();
        return  " Todo id  " + id + " : "+ p.getNom() + " " + p.getDescription();
    }
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@PathParam("id") int id) {
        AccessData.connection();
        AccessData.deleteTodo(id);
        AccessData.disconnection();
        return  " Todo with id " + id +" deleted";
    }
    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Todo update(@PathParam("id") int id,Todo p) {
        AccessData.connection();
        AccessData.updateTodo(id,p);
        AccessData.disconnection();
        return p;
    }
    @GET
    @Path("/select/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Todo selectById(@PathParam("id") int id) {
        AccessData.connection();
        Todo p=AccessData.selectTodoParId(id);
        System.out.println(p.toString());
        AccessData.disconnection();
        return p;
    }

}

