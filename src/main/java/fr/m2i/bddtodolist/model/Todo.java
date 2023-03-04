package fr.m2i.bddtodolist.model;

import java.sql.Date;

public class Todo {
    private String name, description;
    private Date date;
    private int id;
    private Urgence urgence;
    private User user;

    //region CONSTRUCTORS
    public Todo() {}

    public Todo(String name, String description, Date date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public Todo(int id, String name, String description, Date date) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.id = id;
    }
    public Todo(String name, String description, Date date, Urgence urgence, User user) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.urgence = urgence;
        this.user = user;
    }

    public Todo(int id, String name, String description, Date date, Urgence urgence, User user) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.urgence = urgence;
        this.user = user;
        this.id = id;
    }

    //endregion

    //region GETTERS AND SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Urgence getUrgence() {
        return urgence;
    }

    public void setUrgence(Urgence urgence) {
        this.urgence = urgence;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //endregion
}
