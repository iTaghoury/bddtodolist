package fr.m2i.m2ws.todo.db.ws.Module;



import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Date;

@XmlRootElement(name="todo")
public class Todo {

	private int Id;

	private String Nom;
	private String Description;
	private Date date;
	private Urgence urg;
	private Utilisateur  user;

	@XmlElement()
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	@XmlElement()
	public Urgence getUrg() {
		return urg;
	}
	public void setUrg(Urgence urg) {
		this.urg = urg;
	}


	public String getNom() {
		return Nom;
	}

	public void setNom(String nom) {
		Nom = nom;
	}

	public Utilisateur getUser() {
		return user;
	}

	public void setUser(Utilisateur user) {
		this.user = user;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Todo(int id, String nom, String description, Date date, Urgence urg, Utilisateur user) {
		this.Id = id;
		this.Nom = nom;
		this.Description = description;
		this.date=date;
		this.urg = urg;
		this.user = user;
	}

	public Todo(){

	}



}
