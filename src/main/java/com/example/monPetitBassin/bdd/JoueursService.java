package com.example.monPetitBassin.bdd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JoueursService {
    private Statement statement;

    /**
     * Recuperattion du pseudo des joueurs depuis la bdd
     */
    public List<String> getPseudoJoueurs(ConnectionBdd connexion) {
        // liste des joueurs
        ResultSet resultSet = connexion.query("select pseudo from joueurs order by pseudo");
        List<String> pseudos = new ArrayList<>();

        try {
            while (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                String pseudo = resultSet.getString("pseudo");
//                BigDecimal argent = resultSet.getBigDecimal("argent");
//                Joueur joueur = new Joueur(id, pseudo, argent);

                pseudos.add(resultSet.getString("pseudo"));
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la récupération des joueurs : " + e.getMessage());
        }
        return pseudos;
    }



    public void addJoueur(ConnectionBdd connexion, Sauvegarde sauvegarde) throws SQLException {
//        double argent = joueur.getArgent().doubleValue();
//        String query = "";
//        query += "INSERT INTO joueur VALUES (";
//        query += "'" + joueur.getPseudo() + "', ";
//        query += argent + ")";

        // Insertion des données
        String sql = "INSERT INTO joueurs (pseudo, argent) VALUES (?, ?)";
        PreparedStatement stmt = connexion.prepareStatement(sql);
        stmt.setString(1, sauvegarde.getPseudo());
        stmt.setDouble(2, sauvegarde.getArgent().doubleValue());
        try {
            stmt.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Récupération de l'ID généré
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            int generatedId = generatedKeys.getInt(1);
            sauvegarde.setId(generatedId);
            System.out.println("Le joueur a été inséré avec succès dans la base de données avec l'ID : " + generatedId);
        }

//        System.out.println("requete : " + query);
//        try {
//            statement.executeUpdate(query);
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }


}
