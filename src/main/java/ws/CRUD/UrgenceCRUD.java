

package fr.m2i.m2ws.todo.db.ws.CRUD;


import fr.m2i.m2ws.todo.db.ws.Module.Urgence;

import java.sql.*;
import java.util.ArrayList;

public class UrgenceCRUD {

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

		public void deleteUrgence(int id) {

		try {


			PreparedStatement ps = connection.prepareStatement("DELETE FROM urgence WHERE idUrgence = ? ");
			// pour ajouter un valur en enter de requete (premier ?)
			ps.setInt(1, id);
			// supprission de la base
			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public int insertionUrgence(Urgence urgence){
        int i = 0;
		try {

			PreparedStatement ps = connection.prepareStatement("INSERT INTO urgence (nom) values( ?)", Statement.RETURN_GENERATED_KEYS);
			// pour ajouter un valur en enter de requete (premier ?)
			ps.setString(1, urgence.getNom());

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

	public ArrayList<Urgence> selectUrgence() {

		ArrayList<Urgence> urgences= new ArrayList<Urgence>();

		try {

			// Porteur de requette sql
			Statement stmt = connection.createStatement();
			// Executer la requette sql , met le retour dans result state
			ResultSet resultats = stmt.executeQuery("SELECT * FROM urgence");

			// lire le resultat ligne par ligne
			while(resultats.next()) {
				urgences.add(new Urgence(resultats.getInt(1), resultats.getString(2)));
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return urgences;

	}
	public Urgence selectUrgenceParId(int id) {

		Urgence urgence = new Urgence();

		try {


			PreparedStatement ps = connection.prepareStatement("SELECT * FROM urgence WHERE idUrgence  = ? ");
			// pour ajouter un valur en enter de requete (premier ?)
			ps.setInt(1, id);
			ResultSet resultats = ps.executeQuery();
			System.out.println("clients :");

			while(resultats.next())
				urgence = new Urgence(resultats.getInt(1),resultats.getString(2));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}



		return urgence;

	}

	public void updateUrgence(int id,Urgence urgence)  {
					
					try {
						PreparedStatement ps = connection.prepareStatement("UPDATE urgence SET nom = ? WHERE idUrgence = ? ");
						// pour ajouter un valur en enter de requete (premier ?)
						ps.setString(1, urgence.getNom());
						ps.setInt(2, id);
                        ps.executeUpdate();
					} catch (SQLException e) {
						System.out.println(e.getMessage());
					}

				}
		}
