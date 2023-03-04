package fr.m2i.bddtodolist.model;

public class  Urgence {
    private String urgenceLevel;
    private int urgenceId;
    public Urgence(String urgenceLevel) {
        this.urgenceLevel = urgenceLevel;
    }

    public Urgence(int urgenceId, String urgenceLevel) {
        this.urgenceLevel = urgenceLevel;
        this.urgenceId = urgenceId;
    }

    public int getUrgenceId() {
        return urgenceId;
    }

    public void setUrgenceId(int urgenceId) {
        this.urgenceId = urgenceId;
    }



    public String getUrgenceLevel() {
        return urgenceLevel;
    }

    public void setUrgenceLevel(String urgenceLevel) {
        this.urgenceLevel = urgenceLevel;
    }
}