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
        String sql = "INSERT INTO sauvegarde (pseudo, argent," +
                "numJourDeco, heureDeco, progressJour," +
                "nbPoules, nbOeufs, fermeActive, distributeursActive, distributeurBCActive, distributeurBFActive, distributeurCoActive, distributeurSaActive, livraisonActive, lavageActive, etatProgressOeuf, dateDeco, " +
                "nbDistributeurBC, nbDistributeurBF, nbDistributeurSa, nbDistributeurCo," +
                "nbMarchandisesBC, nbMarchandisesBF, nbMarchandisesSa, nbMarchandisesCo," +
                "etatProgressBC, etatProgressBF, etatProgressSa, etatProgressCo, dateDebutJeu, poulailler1, poulailler2, poulailler3, poulailler4"+
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = connectionBdd.prepareStatement(sql);
        stmt.setString(1, sauvegarde.getPseudo());
        stmt.setDouble(2, sauvegarde.getArgent().doubleValue());
        stmt.setLong(3, sauvegarde.getNumJourDeco());
        stmt.setInt(4, sauvegarde.getHeureDeco());
        stmt.setDouble(5, sauvegarde.getProgressJour());
        stmt.setInt(6, sauvegarde.getNbPoules());
        stmt.setLong(7, sauvegarde.getNbOeufs());
        stmt.setInt(8, sauvegarde.getFermeActive());
        stmt.setInt(9, sauvegarde.getDistributeursActive());
        stmt.setInt(10, sauvegarde.getDistributeurBCActive());
        stmt.setInt(11, sauvegarde.getDistributeurBFActive());
        stmt.setInt(12, sauvegarde.getDistributeurCoActive());
        stmt.setInt(13, sauvegarde.getDistributeurSaActive());
        stmt.setInt(14, sauvegarde.getLivraisonActive());
        stmt.setInt(15, sauvegarde.getLavageActive());
        stmt.setDouble(16, sauvegarde.getEtatProgressOeuf());
        stmt.setTimestamp(17, timestamp);

        stmt.setInt(18, sauvegarde.getNbDistributeurBC());
        stmt.setInt(19, sauvegarde.getNbDistributeurBF());
        stmt.setInt(20, sauvegarde.getNbDistributeurSa());
        stmt.setInt(21, sauvegarde.getNbDistributeurCo());

        stmt.setLong(22, sauvegarde.getNbMarchandisesBC());
        stmt.setLong(23, sauvegarde.getNbMarchandisesBF());
        stmt.setLong(24, sauvegarde.getNbMarchandisesSa());
        stmt.setLong(25, sauvegarde.getNbMarchandisesCo());

        stmt.setDouble(26, sauvegarde.getEtatProgressBC());
        stmt.setDouble(27, sauvegarde.getEtatProgressBF());
        stmt.setDouble(28, sauvegarde.getEtatProgressSa());
        stmt.setDouble(29, sauvegarde.getEtatProgressCo());

        stmt.setTimestamp(30, timestamp);
        stmt.setInt(31, sauvegarde.getPoulailler1());
        stmt.setInt(32, sauvegarde.getPoulailler2());
        stmt.setInt(33, sauvegarde.getPoulailler3());
        stmt.setInt(34, sauvegarde.getPoulailler4());


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
            long numJourDeco = resultSet.getLong("numJourDeco");
            int heureDeco = resultSet.getInt("heureDeco");
            double progressJour = resultSet.getDouble("progressJour");
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
            int poulailler1 = resultSet.getInt("poulailler1");
            int poulailler2 = resultSet.getInt("poulailler2");
            int poulailler3 = resultSet.getInt("poulailler3");
            int poulailler4 = resultSet.getInt("poulailler4");

            LocalDateTime dateDeco = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMillisDeco), ZoneId.systemDefault());
            LocalDateTime dateDebutJeu = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMillisDebutJeu), ZoneId.systemDefault());

            Sauvegarde sauvegarde = new Sauvegarde(pseudo, argent, numJourDeco, heureDeco, progressJour, nbPoules, nbOeufs, fermeActive, distributeursActive, distributeurBCActive,
                    distributeurBFActive, distributeurCoActive, distributeurSaActive, livraisonActive, livraisonActive
                    , etatProgressOeuf, dateDeco,
                    nbDistributeurBC, nbDistributeurBF, nbDistributeurSa, nbDistributeurCo,
                    nbMarchandisesBC, nbMarchandisesBF, nbMarchandisesSa, nbMarchandisesCo,
                    etatProgressBC, etatProgressBF, etatProgressSa, etatProgressCo, dateDebutJeu, poulailler1, poulailler2, poulailler3, poulailler4);
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
        String sql = "UPDATE sauvegarde SET argent = ?, numJourDeco = ?, heureDeco = ?, progressJour = ?, nbPoules = ?, nbOeufs = ?, "
                + "fermeActive = ?, distributeursActive = ?, distributeurBCActive = ?,distributeurBFActive = ?," +
                "distributeurCoActive = ?,distributeurSaActive = ?,livraisonActive = ?,lavageActive = ?," +
                " etatProgressOeuf = ?, dateDeco = ?," +
                "nbDistributeurBC = ?, nbDistributeurBF = ?, nbDistributeurSa = ?, nbDistributeurCo = ?," +
                "nbMarchandisesBC = ?, nbMarchandisesBF = ?, nbMarchandisesSa = ?, nbMarchandisesCo = ?," +
                "etatProgressBC = ?, etatProgressBF = ?, etatProgressSa = ?, etatProgressCo = ?, " +
                "poulailler1 = ?, poulailler2 = ?, poulailler3 = ?, poulailler4 = ?"+
                " WHERE pseudo LIKE ?";

        System.out.println("Requete : " + sql);
        // formate le champ date deco en timeStamp
        // Timestamp timestamp = Timestamp.valueOf(sauvegarde.getDateDeco());

        LocalDateTime dateDeco = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(dateDeco);

        PreparedStatement stmt = connectionBdd.prepareStatement(sql);
        // Paramétrage des valeurs
        stmt.setBigDecimal(1, sauvegarde.getArgent());
        stmt.setLong(2, sauvegarde.getNumJourDeco());
        stmt.setInt(3, sauvegarde.getHeureDeco());
        stmt.setDouble(4, sauvegarde.getProgressJour());
        stmt.setInt(5, sauvegarde.getNbPoules());
        stmt.setLong(6, sauvegarde.getNbOeufs());
        stmt.setInt(7, sauvegarde.getFermeActive());
        stmt.setInt(8, sauvegarde.getDistributeursActive());
        stmt.setInt(9, sauvegarde.getDistributeurBCActive());
        stmt.setInt(10, sauvegarde.getDistributeurBFActive());
        stmt.setInt(11, sauvegarde.getDistributeurCoActive());
        stmt.setInt(12, sauvegarde.getDistributeurSaActive());
        stmt.setInt(13, sauvegarde.getLivraisonActive());
        stmt.setInt(14, sauvegarde.getLavageActive());
        stmt.setDouble(15, sauvegarde.getEtatProgressOeuf());
        stmt.setTimestamp(16, timestamp);

        stmt.setInt(17, sauvegarde.getNbDistributeurBC());
        stmt.setInt(18, sauvegarde.getNbDistributeurBF());
        stmt.setInt(19, sauvegarde.getNbDistributeurSa());
        stmt.setInt(20, sauvegarde.getNbDistributeurCo());

        stmt.setLong(21, sauvegarde.getNbMarchandisesBC());
        stmt.setLong(22, sauvegarde.getNbMarchandisesBF());
        stmt.setLong(23, sauvegarde.getNbMarchandisesSa());
        stmt.setLong(24, sauvegarde.getNbMarchandisesCo());

        stmt.setDouble(25, sauvegarde.getEtatProgressBC());
        stmt.setDouble(26, sauvegarde.getEtatProgressBF());
        stmt.setDouble(27, sauvegarde.getEtatProgressSa());
        stmt.setDouble(28, sauvegarde.getEtatProgressCo());
        stmt.setInt(29, sauvegarde.getPoulailler1());
        stmt.setInt(30, sauvegarde.getPoulailler2());
        stmt.setInt(31, sauvegarde.getPoulailler3());
        stmt.setInt(32, sauvegarde.getPoulailler4());

        stmt.setString(33, sauvegarde.getPseudo());

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
