

package fr.m2i.m2ws.todo.db.ws.CRUD;


import fr.m2i.m2ws.todo.db.ws.Module.Todo;
import fr.m2i.m2ws.todo.db.ws.Module.Urgence;
import fr.m2i.m2ws.todo.db.ws.Module.Utilisateur;

import java.sql.*;
import java.util.ArrayList;

public class TodoCRUD {

        public static Connection connection;

		public void connection() {

			try{
				Class.forName("com.mysql.cj.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jira?connectTimeout=3000&useSSL=false&allowPublicKeyRetrieval=true", "root", "Ibtissam@12345");
				System.out.println("connection started");

			}catch (SQLException e){
				System.err.println(" hello " + e.getMessage());
			}  catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}

		}

		public void disconnection(){
			try {
				System.out.println("connection stopped");
				connection.close();
			}catch (SQLException e){
				System.out.println(e.getMessage());
			}
		}

		public void deleteTodo(int id) {

		try {


			PreparedStatement ps = connection.prepareStatement("DELETE FROM todo WHERE idtodo = ? ");
			// pour ajouter un valur en enter de requete (premier ?)
			ps.setInt(1, id);
			// supprission de la base
			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public int insertionTodo(Todo Todo){
        int i = 0;
		try {

			PreparedStatement ps = connection.prepareStatement("INSERT INTO todo (nom,description,Date,idUtilisateur,idUrgence) values(?, ?, ?, ?,? )", Statement.RETURN_GENERATED_KEYS);
			// pour ajouter un valur en enter de requete (premier ?)
			ps.setString(1, Todo.getNom());
			// pour ajouter un valur en enter de requete (deuxieme ?)
			ps.setString(2, Todo.getDescription());
			// pour ajouter un valur en enter de requete (deuxieme ?)
			ps.setDate(3, Todo.getDate());
			// pour ajouter un valur en enter de requete (deuxieme ?)
			ps.setInt(4, Todo.getUser().getId());
			// pour ajouter un valur en enter de requete (deuxieme ?)
			ps.setInt(5, Todo.getUrg().getId());
			// insertion a la base
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				i = rs.getInt(1);
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
        return i ;
	}

	// par urgence, par utilisateur
	public ArrayList<Todo> selectTodoTriByUrgence() {

		ArrayList<Todo> Todos= new ArrayList<Todo>();

		try {

			// Porteur de requette sql
			Statement stmt = connection.createStatement();
			// Executer la requette sql , met le retour dans result state
			ResultSet resultats = stmt.executeQuery("SELECT * FROM todo ORDER BY idUrgence Asc");

			// lire le resultat ligne par ligne
			while(resultats.next()) {
				Todos.add(new Todo(resultats.getInt(1), resultats.getString(2), resultats.getString(3),resultats.getDate(4),new Urgence(resultats.getInt(6)),new Utilisateur(resultats.getInt(5))));
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return Todos;

	}

	// par urgence, par utilisateur
	public ArrayList<Todo> selectTodoTriByUtilisateur() {

		ArrayList<Todo> Todos= new ArrayList<Todo>();

		try {

			// Porteur de requette sql
			Statement stmt = connection.createStatement();
			// Executer la requette sql , met le retour dans result state
			ResultSet resultats = stmt.executeQuery("SELECT * FROM todo ORDER BY IdUtilisateur Asc");

			// lire le resultat ligne par ligne
			while(resultats.next()) {
				Todos.add(new Todo(resultats.getInt(1), resultats.getString(2), resultats.getString(3),resultats.getDate(4),new Urgence(resultats.getInt(6)),new Utilisateur(resultats.getInt(5))));
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return Todos;

	}
	public Todo selectTodoParId(int id) {

		Todo Todo = new Todo();

		try {


			PreparedStatement ps = connection.prepareStatement("SELECT * FROM todo WHERE idtodo  = ? ");
			// pour ajouter un valur en enter de requete (premier ?)
			ps.setInt(1, id);
			ResultSet resultats = ps.executeQuery();

			while(resultats.next())
				Todo = new Todo(resultats.getInt(1), resultats.getString(2), resultats.getString(3),resultats.getDate(4),new Urgence(resultats.getInt(6)),new Utilisateur(resultats.getInt(5)));

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}



		return Todo;

	}

	public void updateTodo(int id,Todo Todo)  {
					
					try {
						PreparedStatement ps = connection.prepareStatement("UPDATE todo SET nom = ? , description= ?, date = ? , idUtilisateur = ? , idUrgence = ?  WHERE idtodo = ? ");
						// pour ajouter un valur en enter de requete (premier ?)
						ps.setString(1, Todo.getNom());
						// pour ajouter un valur en enter de requete (deuxieme ?)
						ps.setString(2, Todo.getDescription());
						ps.setDate(3, Todo.getDate());
						ps.setInt(4, Todo.getUser().getId());
						ps.setInt(5, Todo.getUrg().getId());
						ps.setInt(6, id);
                        ps.executeUpdate();
					} catch (SQLException e) {
						System.out.println(e.getMessage());
					}

				}
		}
