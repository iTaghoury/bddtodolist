package fr.m2i.bddtodolist.model;

import java.util.Date;

public class Todo {
    private String name, description;
    private Date date;
    private Urgence urgence;
    private int userId;

    public Todo() {}

    public Todo(String name, String description, Date date, Urgence urgence, int userId) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.urgence = urgence;
        this.userId = userId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
