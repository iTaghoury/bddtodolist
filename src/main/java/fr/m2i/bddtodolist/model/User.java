package fr.m2i.bddtodolist.model;

public class User {

    private String name, firstName;
    private int id;

    //region CONSTRUCTORS
    public User() {}


    public User(String name, String firstName) {
        this.name = name;
        this.firstName = firstName;
    }

    public User(int id, String name, String firstName) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    //endregion
}