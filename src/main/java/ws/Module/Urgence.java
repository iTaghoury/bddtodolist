package fr.m2i.m2ws.todo.db.ws.Module;

public class Urgence {

	 private int Id;
	 private String Nom;

	public Urgence(int id, String nom) {
		Id = id;
		Nom = nom;
	}
	public Urgence(int id) {
		Id = id;
	}

	public Urgence() {

	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getNom() {
		return Nom;
	}

	public void setNom(String nom) {
		Nom = nom;
	}
}
