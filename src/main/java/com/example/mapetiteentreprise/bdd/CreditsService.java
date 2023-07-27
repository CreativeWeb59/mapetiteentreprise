package com.example.mapetiteentreprise.bdd;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class CreditsService {
    private ConnectionBdd connectionBdd;

    public CreditsService(ConnectionBdd connectionBdd) {
        this.connectionBdd = connectionBdd;
    }

    /**
     * Recupere le credit en cours du joueur selectionne
     *
     * @return
     */
    public Credits creditEnCours(String pseudo) throws SQLException {
        // liste des joueurs
        String query = "SELECT * FROM credits WHERE pseudo = ? AND termine = 0";
        System.out.println("requete : " + query);
        PreparedStatement statement = connectionBdd.prepareStatement(query);
        statement.setString(1, pseudo);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            BigDecimal montantPret = resultSet.getBigDecimal("montantPret");
            BigDecimal coutPret = resultSet.getBigDecimal("coutPret");
            BigDecimal montantRembourse = resultSet.getBigDecimal("montantRembourse");
            BigDecimal mensualite = resultSet.getBigDecimal("mensualite");
            int nbMMensualite = resultSet.getInt("nbMMensualite");
            int cycleMensualite = resultSet.getInt("cycleMensualite");
            int termine = resultSet.getInt("termine");
//            long timestampMillis = resultSet.getLong("dateDebutCredit");
            long dateDebutCredit = resultSet.getLong("dateDebutCredit");
            long dateDerniereMensualite = resultSet.getLong("dateDerniereMensualite");
            long dateProchaineMensualite = resultSet.getLong("dateProchaineMensualite");
            long datePreavis = resultSet.getLong("datePreavis");
            int blocageDatePreavis = resultSet.getInt("blocageDatePreavis");

//            LocalDateTime dateDebutCredit = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMillis), ZoneId.systemDefault());

            Credits credit = new Credits(pseudo, montantPret, coutPret, montantRembourse, mensualite, nbMMensualite, cycleMensualite,
                    termine, dateDebutCredit, dateDerniereMensualite, dateProchaineMensualite, datePreavis, blocageDatePreavis);
            credit.setId(id);
            System.out.println(credit);
            return credit;
        } else {
            System.out.println("Le joueur n'a pas de credit en cours");
            return null; // Le joueur n'a pas de credit en cours
        }
    }

    /**
     * Teste si un pseudo avec termine = 0 dans la table credits
     *
     * @return
     */
    public boolean isCreditEnCours(String pseudo) {
        String query = "SELECT * FROM credits WHERE pseudo LIKE ? AND termine=0";
        try {
            PreparedStatement statement = connectionBdd.prepareStatement(query);
            // Paramètre pour la recherche du mot "pseudo"
            statement.setString(1, pseudo);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cree le prêt du joueur
     *
     * @param credits
     * @return un int indiquant l'id de l'insertion du prêt
     * @throws SQLException
     */
    public void addCredit(Credits credits) {
        // recuperation date en cours au bon format
        LocalDateTime dateDebutCredit = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(dateDebutCredit);
        // Insertion des données
        String sql = "INSERT INTO credits (pseudo, montantPret, coutPret, montantRembourse, mensualite," +
                "nbMMensualite, cycleMensualite, termine, dateDebutCredit, datePreavis, blocageDatePreavis" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = connectionBdd.prepareStatement(sql);
            stmt.setString(1, credits.getPseudo());
            stmt.setDouble(2, credits.getMontantPret().doubleValue());
            stmt.setDouble(3, credits.getCoutPret().doubleValue());
            stmt.setDouble(4, credits.getMontantRembourse().doubleValue());
            stmt.setDouble(5, credits.getMensualite().doubleValue());
            stmt.setInt(6, credits.getNbMMensualite());
            stmt.setInt(7, credits.getCycleMensualite());
            stmt.setInt(8, credits.getTermine());
            stmt.setLong(9, credits.getDateDebutCredit());
            stmt.setLong(10, credits.getDatePreavis());
            stmt.setInt(11, credits.getBlocageDatePreavis());
//            stmt.setTimestamp(9, timestamp);
            stmt.executeUpdate();
            // Récupération de l'ID généré
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                credits.setId(generatedId);
                System.out.println("Le prêt a été crée avec succès dans la base de données avec l'ID : " + generatedId);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Probleme lors de la creation du prêt " + e);
        }
    }

    public void majCredit(Credits credits) {
        // Requête de mise à jour
        String sql = "UPDATE credits SET montantRembourse = ?, "
                + "mensualite = ?, dateDerniereMensualite = ?, termine = ?, dateProchaineMensualite = ?, datePreavis = ?, blocageDatePreavis = ?" +
                " WHERE pseudo LIKE ? AND TERMINE = 0";

        System.out.println("Requete : " + sql);
        // formate le champ date deco en timeStamp
        // Timestamp timestamp = Timestamp.valueOf(sauvegarde.getDateDeco());

        try {
        PreparedStatement stmt = connectionBdd.prepareStatement(sql);
        // Paramétrage des valeurs
        stmt.setBigDecimal(1, credits.getMontantRembourse());
        stmt.setBigDecimal(2, credits.getMensualite());
        stmt.setLong(3, credits.getDateDerniereMensualite());
        stmt.setInt(4, credits.getTermine());
        stmt.setLong(5,credits.getDateProchaineMensualite());
        stmt.setLong(6, credits.getDatePreavis());
        stmt.setInt(7, credits.getBlocageDatePreavis());
        stmt.setString(8, credits.getPseudo());

        // Insertion des données
            stmt.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
