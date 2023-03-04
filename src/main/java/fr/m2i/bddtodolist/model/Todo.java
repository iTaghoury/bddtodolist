package fr.m2i.bddtodolist.model;

import java.sql.Date;

public class Todo {
    private String name, description;
    private Date date;
    private int id, urgenceId, userId;

    //region CONSTRUCTORS
    public Todo() {}

    public Todo(String name, String description, Date date, int urgenceId, int userId) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.urgenceId = urgenceId;
        this.userId = userId;
    }

    public Todo(int id, String name, String description, Date date, int urgenceId, int userId) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.id = id;
        this.urgenceId = urgenceId;
        this.userId = userId;
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

    public int getUrgenceId() {
        return urgenceId;
    }

    public void setUrgenceId(int urgenceId) {
        this.urgenceId = urgenceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    //endregion
}
