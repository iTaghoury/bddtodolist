

package fr.m2i.m2ws.todo.db.ws.CRUD;


import fr.m2i.m2ws.todo.db.ws.Module.Utilisateur;

import java.sql.*;
import java.util.ArrayList;

public class UserCRUD {

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

		public void deleteUtilisateur(int id) {

		try {


			PreparedStatement ps = connection.prepareStatement("DELETE FROM utilisateur WHERE idutilisateur = ? ");
			// pour ajouter un valur en enter de requete (premier ?)
			ps.setInt(1, id);
			// supprission de la base
			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public int insertionUtilisateur(Utilisateur utilisateur){
        int i = 0;
		try {

			PreparedStatement ps = connection.prepareStatement("INSERT INTO utilisateur (nom,prenom) values(?, ?)", Statement.RETURN_GENERATED_KEYS);
			// pour ajouter un valur en enter de requete (premier ?)
			ps.setString(1, utilisateur.getNom());
			// pour ajouter un valur en enter de requete (deuxieme ?)
			ps.setString(2, utilisateur.getPrenom());
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

	public ArrayList<Utilisateur> selectUtilisateur() {

		ArrayList<Utilisateur> utilisateurs= new ArrayList<Utilisateur>();

		try {

			// Porteur de requette sql
			Statement stmt = connection.createStatement();
			// Executer la requette sql , met le retour dans result state
			ResultSet resultats = stmt.executeQuery("SELECT * FROM utilisateur");

			// lire le resultat ligne par ligne
			while(resultats.next()) {
				utilisateurs.add(new Utilisateur(resultats.getInt(1), resultats.getString(2), resultats.getString(3)));
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return utilisateurs;

	}
	public Utilisateur selectUtilisateurParId(int id) {

		Utilisateur utilisateur = new Utilisateur();

		try {


			PreparedStatement ps = connection.prepareStatement("SELECT * FROM utilisateur WHERE idutilisateur  = ? ");
			// pour ajouter un valur en enter de requete (premier ?)
			ps.setInt(1, id);
			ResultSet resultats = ps.executeQuery();
			System.out.println("clients :");

			while(resultats.next())
				utilisateur = new Utilisateur(resultats.getInt(1),resultats.getString(2),resultats.getString(3));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}



		return utilisateur;

	}

	public void updateUtilisateur(int id,Utilisateur utilisateur)  {
					
					try {
						PreparedStatement ps = connection.prepareStatement("UPDATE utilisateur SET nom = ? , prenom= ? WHERE idutilisateur = ? ");
						// pour ajouter un valur en enter de requete (premier ?)
						ps.setString(1, utilisateur.getNom());
						// pour ajouter un valur en enter de requete (deuxieme ?)
						ps.setString(2, utilisateur.getPrenom());
						ps.setInt(3, id);
                        ps.executeUpdate();
					} catch (SQLException e) {
						System.out.println(e.getMessage());
					}

				}
		}
