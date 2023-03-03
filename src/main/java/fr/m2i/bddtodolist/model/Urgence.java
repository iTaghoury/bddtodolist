package fr.m2i.bddtodolist.model;

public class  Urgence {
    private String urgenceLevel;

    public Urgence(String urgenceLevel) {
        this.urgenceLevel = urgenceLevel;
    }

    public String getUrgenceLevel() {
        return urgenceLevel;
    }

    public void setUrgenceLevel(String urgenceLevel) {
        this.urgenceLevel = urgenceLevel;
    }
}