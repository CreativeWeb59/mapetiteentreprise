package com.example.mapetiteentreprise.bdd;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class SauvegardeService {
    private ConnectionBdd connectionBdd;

    public SauvegardeService(ConnectionBdd connectionBdd) {
        this.connectionBdd = connectionBdd;
    }

    /**
     * Recupere tous les pseudos contenu dans la table sauvegarde
     *
     * @return
     */
    public List<String> listePseudos() {
        List<String> pseudos = new ArrayList<>();
        String query = "SELECT pseudo FROM sauvegarde order by pseudo";
        try {
            PreparedStatement statement = connectionBdd.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                pseudos.add(resultSet.getString("pseudo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pseudos;
    }

    /**
     * Teste si le pseudo donné est dans la Bdd
     *
     * @param pseudo
     * @return
     */
    public Boolean existPseudo(String pseudo) {

        String query = "SELECT pseudo FROM sauvegarde WHERE pseudo LIKE ?";
        try {
            PreparedStatement statement = connectionBdd.prepareStatement(query);
            statement.setString(1, pseudo);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Le pseudo existe dans la base");
                return true;
            } else {
                System.out.println("Le pseudo n'existe pas");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Ajoute une sauvegarde
     *
     * @param sauvegarde
     * @throws SQLException
     */
    public void addJoueur(Sauvegarde sauvegarde) throws SQLException {
        // recuperation date en cours au bon format
        LocalDateTime dateDeco = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(dateDeco);
        // Insertion des données
        String sql = "INSERT INTO sauvegarde (pseudo, argent, nbPoules, nbOeufs, fermeActive, distributeursActive, distributeurBCActive, distributeurBFActive, distributeurCoActive, distributeurSaActive, livraisonActive, lavageActive, etatProgressOeuf, dateDeco, " +
                "nbDistributeurBC, nbDistributeurBF, nbDistributeurSa, nbDistributeurCo," +
                "nbMarchandisesBC, nbMarchandisesBF, nbMarchandisesSa, nbMarchandisesCo," +
                "etatProgressBC, etatProgressBF, etatProgressSa, etatProgressCo, dateDebutJeu"+
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = connectionBdd.prepareStatement(sql);
        stmt.setString(1, sauvegarde.getPseudo());
        stmt.setDouble(2, sauvegarde.getArgent().doubleValue());
        stmt.setInt(3, sauvegarde.getNbPoules());
        stmt.setLong(4, sauvegarde.getNbOeufs());
        stmt.setInt(5, sauvegarde.getFermeActive());
        stmt.setInt(6, sauvegarde.getDistributeursActive());
        stmt.setInt(7, sauvegarde.getDistributeurBCActive());
        stmt.setInt(8, sauvegarde.getDistributeurBFActive());
        stmt.setInt(9, sauvegarde.getDistributeurCoActive());
        stmt.setInt(10, sauvegarde.getDistributeurSaActive());
        stmt.setInt(11, sauvegarde.getLivraisonActive());
        stmt.setInt(12, sauvegarde.getLavageActive());
        stmt.setDouble(13, sauvegarde.getEtatProgressOeuf());
        stmt.setTimestamp(14, timestamp);

        stmt.setInt(15, sauvegarde.getNbDistributeurBC());
        stmt.setInt(16, sauvegarde.getNbDistributeurBF());
        stmt.setInt(17, sauvegarde.getNbDistributeurSa());
        stmt.setInt(18, sauvegarde.getNbDistributeurCo());

        stmt.setLong(19, sauvegarde.getNbMarchandisesBC());
        stmt.setLong(20, sauvegarde.getNbMarchandisesBF());
        stmt.setLong(21, sauvegarde.getNbMarchandisesSa());
        stmt.setLong(22, sauvegarde.getNbMarchandisesCo());

        stmt.setDouble(23, sauvegarde.getEtatProgressBC());
        stmt.setDouble(24, sauvegarde.getEtatProgressBF());
        stmt.setDouble(25, sauvegarde.getEtatProgressSa());
        stmt.setDouble(26, sauvegarde.getEtatProgressCo());

        stmt.setTimestamp(27, timestamp);

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

    /**
     * Recuperattion d'un joueur depuis la bdd
     */
    public Sauvegarde getJoueurbyPseudo(String pseudo) throws SQLException {
        // liste des joueurs
        String query = "SELECT * FROM sauvegarde WHERE pseudo = ?";
        PreparedStatement statement = connectionBdd.prepareStatement(query);
        statement.setString(1, pseudo);
        ResultSet resultSet = statement.executeQuery();
        System.out.println("Pseudo : " + pseudo);
//        Date laDate = new Date();
        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            BigDecimal argent = resultSet.getBigDecimal("argent");
            int nbPoules = resultSet.getInt("nbPoules");
            int nbOeufs = resultSet.getInt("nbOeufs");
            int fermeActive = resultSet.getInt("fermeActive");
            int distributeursActive = resultSet.getInt("distributeursActive");
            int distributeurBCActive = resultSet.getInt("distributeurBCActive");
            int distributeurBFActive = resultSet.getInt("distributeurBFActive");
            int distributeurCoActive = resultSet.getInt("distributeurCoActive");
            int distributeurSaActive = resultSet.getInt("distributeurSaActive");
            int livraisonActive = resultSet.getInt("livraisonActive");
            int lavageActive = resultSet.getInt("lavageActive");
            double etatProgressOeuf = resultSet.getDouble("etatProgressOeuf");
//            Timestamp timestamp = resultSet.getTimestamp("dateDeco");
//            LocalDateTime dateDeco = timestamp.toLocalDateTime();
            long timestampMillisDeco = resultSet.getLong("dateDeco");

            int nbDistributeurBC = resultSet.getInt("nbDistributeurBC");
            int nbDistributeurBF = resultSet.getInt("nbDistributeurBF");
            int nbDistributeurSa = resultSet.getInt("nbDistributeurSa");
            int nbDistributeurCo = resultSet.getInt("nbDistributeurCo");

            int nbMarchandisesBC = resultSet.getInt("nbMarchandisesBC");
            int nbMarchandisesBF = resultSet.getInt("nbMarchandisesBF");
            int nbMarchandisesSa = resultSet.getInt("nbMarchandisesSa");
            int nbMarchandisesCo = resultSet.getInt("nbMarchandisesCo");

            double etatProgressBC = resultSet.getDouble("etatProgressBC");
            double etatProgressBF = resultSet.getDouble("etatProgressBF");
            double etatProgressSa = resultSet.getDouble("etatProgressSa");
            double etatProgressCo = resultSet.getDouble("etatProgressCo");

            long timestampMillisDebutJeu = resultSet.getLong("dateDebutJeu");

            LocalDateTime dateDeco = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMillisDeco), ZoneId.systemDefault());
            LocalDateTime dateDebutJeu = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMillisDebutJeu), ZoneId.systemDefault());

            Sauvegarde sauvegarde = new Sauvegarde(pseudo, argent, nbPoules, nbOeufs, fermeActive, distributeursActive, distributeurBCActive,
                    distributeurBFActive, distributeurCoActive, distributeurSaActive, livraisonActive, livraisonActive
                    , etatProgressOeuf, dateDeco,
                    nbDistributeurBC, nbDistributeurBF, nbDistributeurSa, nbDistributeurCo,
                    nbMarchandisesBC, nbMarchandisesBF, nbMarchandisesSa, nbMarchandisesCo,
                    etatProgressBC, etatProgressBF, etatProgressSa, etatProgressCo, dateDebutJeu);
            sauvegarde.setId(id);
            System.out.println(sauvegarde);
            return sauvegarde;
        } else {
            System.out.println("Le joueur n'a pas ete trouvé");
            return null; // Le joueur n'a pas été trouvé
        }
    }

    public void majSauvegarde(Sauvegarde sauvegarde) throws SQLException {
        // Requête de mise à jour
        String sql = "UPDATE sauvegarde SET argent = ?, nbPoules = ?, nbOeufs = ?, "
                + "fermeActive = ?, distributeursActive = ?, distributeurBCActive = ?,distributeurBFActive = ?," +
                "distributeurCoActive = ?,distributeurSaActive = ?,livraisonActive = ?,lavageActive = ?," +
                " etatProgressOeuf = ?, dateDeco = ?," +
                "nbDistributeurBC = ?, nbDistributeurBF = ?, nbDistributeurSa = ?, nbDistributeurCo = ?," +
                "nbMarchandisesBC = ?, nbMarchandisesBF = ?, nbMarchandisesSa = ?, nbMarchandisesCo = ?," +
                "etatProgressBC = ?, etatProgressBF = ?, etatProgressSa = ?, etatProgressCo = ?"+
                " WHERE pseudo LIKE ?";

        System.out.println("Requete : " + sql);
        // formate le champ date deco en timeStamp
        // Timestamp timestamp = Timestamp.valueOf(sauvegarde.getDateDeco());

        LocalDateTime dateDeco = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(dateDeco);

        PreparedStatement stmt = connectionBdd.prepareStatement(sql);
        // Paramétrage des valeurs
        stmt.setBigDecimal(1, sauvegarde.getArgent());
        stmt.setInt(2, sauvegarde.getNbPoules());
        stmt.setLong(3, sauvegarde.getNbOeufs());
        stmt.setInt(4, sauvegarde.getFermeActive());
        stmt.setInt(5, sauvegarde.getDistributeursActive());
        stmt.setInt(6, sauvegarde.getDistributeurBCActive());
        stmt.setInt(7, sauvegarde.getDistributeurBFActive());
        stmt.setInt(8, sauvegarde.getDistributeurCoActive());
        stmt.setInt(9, sauvegarde.getDistributeurSaActive());
        stmt.setInt(10, sauvegarde.getLivraisonActive());
        stmt.setInt(11, sauvegarde.getLavageActive());
        stmt.setDouble(12, sauvegarde.getEtatProgressOeuf());
        stmt.setTimestamp(13, timestamp);

        stmt.setInt(14, sauvegarde.getNbDistributeurBC());
        stmt.setInt(15, sauvegarde.getNbDistributeurBF());
        stmt.setInt(16, sauvegarde.getNbDistributeurSa());
        stmt.setInt(17, sauvegarde.getNbDistributeurCo());

        stmt.setLong(18, sauvegarde.getNbMarchandisesBC());
        stmt.setLong(19, sauvegarde.getNbMarchandisesBF());
        stmt.setLong(20, sauvegarde.getNbMarchandisesSa());
        stmt.setLong(21, sauvegarde.getNbMarchandisesCo());

        stmt.setDouble(22, sauvegarde.getEtatProgressBC());
        stmt.setDouble(23, sauvegarde.getEtatProgressBF());
        stmt.setDouble(24, sauvegarde.getEtatProgressSa());
        stmt.setDouble(25, sauvegarde.getEtatProgressCo());

        stmt.setString(26, sauvegarde.getPseudo());

        // Insertion des données
        try {
            stmt.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void suprSauvegarde(String pseudo) {

        String sql = "DELETE FROM sauvegarde WHERE pseudo LIKE ?";
        try {
            PreparedStatement stmt = connectionBdd.prepareStatement(sql);
            stmt.setString(1, pseudo);
            stmt.executeUpdate();
            System.out.println("Sauvegarde " + pseudo + " supprimée");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
