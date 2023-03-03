package fr.m2i.m2ws.todo.db.ws.Module;

public class Utilisateur {
    private String nom, prenom;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }



    public Utilisateur() {
    }
    public Utilisateur(int id) {
        this.id= id;

    }
    public Utilisateur(int id, String nom, String prenom) {
        this.id= id;
        this.nom = nom;
        this.prenom = prenom;

    }
}

