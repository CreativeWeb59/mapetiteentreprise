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
                "nbPoules, nbOeufs, fermeActive, distributeursActive, distributeurBCActive, distributeurBFActive, distributeurCoActive, distributeurSaActive," +
                "livraison1Active, livraison2Active, livraison3Active, livraison4Active, livraison5Active," +
                "etatProgressOeuf, dateDeco, " +
                "nbDistributeurBC, nbDistributeurBF, nbDistributeurSa, nbDistributeurCo," +
                "nbMarchandisesBC, nbMarchandisesBF, nbMarchandisesSa, nbMarchandisesCo," +
                "etatProgressBC, etatProgressBF, etatProgressSa, etatProgressCo, dateDebutJeu, poulailler1, poulailler2, poulailler3, poulailler4," +
                "nbLivraison1, nbLivraison2, nbLivraison3, nbLivraison4, nbLivraison5, " +
                "nbCourses1, nbCourses2, nbCourses3, nbCourses4, nbCourses5, " +
                "etatProgressLivraison1, etatProgressLivraison2, etatProgressLivraison3, etatProgressLivraison4, etatProgressLivraison5," +
                "usineTextileActive1, usineTextileActive2, usineTextileActive3, usineTextileActive4," +
                "nbUsinesTextile1, nbUsinesTextile2, nbUsinesTextile3, nbUsinesTextile4," +
                "nbMarchandisesUsineTextile1, nbMarchandisesUsineTextile2, nbMarchandisesUsineTextile3, nbMarchandisesUsineTextile4," +
                "etatProgressUsineTextile1, etatProgressUsineTextile2, etatProgressUsineTextile3, etatProgressUsineTextile4"+
                ") VALUES (" +
                " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                " ?, ?, ?, ?, ?, ?, ?, ?)";

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
        stmt.setInt(14, sauvegarde.getLivraison1Active());
        stmt.setInt(15, sauvegarde.getLivraison2Active());
        stmt.setInt(16, sauvegarde.getLivraison3Active());
        stmt.setInt(17, sauvegarde.getLivraison4Active());
        stmt.setInt(18, sauvegarde.getLivraison5Active());
        stmt.setDouble(19, sauvegarde.getEtatProgressOeuf());
        stmt.setTimestamp(20, timestamp);

        stmt.setInt(21, sauvegarde.getNbDistributeurBC());
        stmt.setInt(22, sauvegarde.getNbDistributeurBF());
        stmt.setInt(23, sauvegarde.getNbDistributeurSa());
        stmt.setInt(24, sauvegarde.getNbDistributeurCo());

        stmt.setLong(25, sauvegarde.getNbMarchandisesBC());
        stmt.setLong(26, sauvegarde.getNbMarchandisesBF());
        stmt.setLong(27, sauvegarde.getNbMarchandisesSa());
        stmt.setLong(28, sauvegarde.getNbMarchandisesCo());

        stmt.setDouble(29, sauvegarde.getEtatProgressBC());
        stmt.setDouble(30, sauvegarde.getEtatProgressBF());
        stmt.setDouble(31, sauvegarde.getEtatProgressSa());
        stmt.setDouble(32, sauvegarde.getEtatProgressCo());

        stmt.setTimestamp(33, timestamp);

        stmt.setInt(34, sauvegarde.getPoulailler1());
        stmt.setInt(35, sauvegarde.getPoulailler2());
        stmt.setInt(36, sauvegarde.getPoulailler3());
        stmt.setInt(37, sauvegarde.getPoulailler4());

        stmt.setInt(38, sauvegarde.getNbLivraison1());
        stmt.setInt(39, sauvegarde.getNbLivraison2());
        stmt.setInt(40, sauvegarde.getNbLivraison3());
        stmt.setInt(41, sauvegarde.getNbLivraison4());
        stmt.setInt(42, sauvegarde.getNbLivraison5());

        stmt.setLong(43, sauvegarde.getNbCourses1());
        stmt.setLong(44, sauvegarde.getNbCourses2());
        stmt.setLong(45, sauvegarde.getNbCourses3());
        stmt.setLong(46, sauvegarde.getNbCourses4());
        stmt.setLong(47, sauvegarde.getNbCourses5());

        stmt.setDouble(48, sauvegarde.getEtatProgressLivraison1());
        stmt.setDouble(49, sauvegarde.getEtatProgressLivraison2());
        stmt.setDouble(50, sauvegarde.getEtatProgressLivraison3());
        stmt.setDouble(51, sauvegarde.getEtatProgressLivraison4());
        stmt.setDouble(52, sauvegarde.getEtatProgressLivraison5());

        stmt.setInt(53, sauvegarde.getUsineTextileActive1());
        stmt.setInt(54, sauvegarde.getUsineTextileActive2());
        stmt.setInt(55, sauvegarde.getUsineTextileActive3());
        stmt.setInt(56, sauvegarde.getUsineTextileActive4());

        stmt.setInt(57, sauvegarde.getNbUsinesTextile1());
        stmt.setInt(58, sauvegarde.getNbUsinesTextile2());
        stmt.setInt(59, sauvegarde.getNbUsinesTextile3());
        stmt.setInt(60, sauvegarde.getNbUsinesTextile4());

        stmt.setLong(61, sauvegarde.getNbMarchandisesUsineTextile1());
        stmt.setLong(62, sauvegarde.getNbMarchandisesUsineTextile2());
        stmt.setLong(63, sauvegarde.getNbMarchandisesUsineTextile3());
        stmt.setLong(64, sauvegarde.getNbMarchandisesUsineTextile4());

        stmt.setDouble(65, sauvegarde.getEtatProgressUsineTextile1());
        stmt.setDouble(66, sauvegarde.getEtatProgressUsineTextile2());
        stmt.setDouble(67, sauvegarde.getEtatProgressUsineTextile3());
        stmt.setDouble(65, sauvegarde.getEtatProgressUsineTextile4());

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
            int livraison1Active = resultSet.getInt("livraison1Active");
            int livraison2Active = resultSet.getInt("livraison2Active");
            int livraison3Active = resultSet.getInt("livraison3Active");
            int livraison4Active = resultSet.getInt("livraison4Active");
            int livraison5Active = resultSet.getInt("livraison5Active");
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

            int nbLivraison1 = resultSet.getInt("nbLivraison1");
            int nbLivraison2 = resultSet.getInt("nbLivraison2");
            int nbLivraison3 = resultSet.getInt("nbLivraison3");
            int nbLivraison4 = resultSet.getInt("nbLivraison4");
            int nbLivraison5 = resultSet.getInt("nbLivraison5");

            long nbCourses1 = resultSet.getLong("nbCourses1");
            long nbCourses2 = resultSet.getLong("nbCourses2");
            long nbCourses3 = resultSet.getLong("nbCourses3");
            long nbCourses4 = resultSet.getLong("nbCourses4");
            long nbCourses5 = resultSet.getLong("nbCourses5");

            double etatProgressLivraison1 = resultSet.getDouble("etatProgressLivraison1");
            double etatProgressLivraison2 = resultSet.getDouble("etatProgressLivraison2");
            double etatProgressLivraison3 = resultSet.getDouble("etatProgressLivraison3");
            double etatProgressLivraison4 = resultSet.getDouble("etatProgressLivraison4");
            double etatProgressLivraison5 = resultSet.getDouble("etatProgressLivraison5");

            int usineTextileActive1 = resultSet.getInt("usineTextileActive1");
            int usineTextileActive2 = resultSet.getInt("usineTextileActive2");
            int usineTextileActive3 = resultSet.getInt("usineTextileActive3");
            int usineTextileActive4 = resultSet.getInt("usineTextileActive4");

            int nbUsinesTextile1 = resultSet.getInt("nbUsinesTextile1");
            int nbUsinesTextile2 = resultSet.getInt("nbUsinesTextile2");
            int nbUsinesTextile3 = resultSet.getInt("nbUsinesTextile3");
            int nbUsinesTextile4 = resultSet.getInt("nbUsinesTextile4");

            long nbMarchandisesUsineTextile1 = resultSet.getInt("nbMarchandisesUsineTextile1");
            long nbMarchandisesUsineTextile2 = resultSet.getInt("nbMarchandisesUsineTextile2");
            long nbMarchandisesUsineTextile3 = resultSet.getInt("nbMarchandisesUsineTextile3");
            long nbMarchandisesUsineTextile4 = resultSet.getInt("nbMarchandisesUsineTextile4");

            double etatProgressUsineTextile1 = resultSet.getInt("etatProgressUsineTextile1");
            double etatProgressUsineTextile2 = resultSet.getInt("etatProgressUsineTextile2");
            double etatProgressUsineTextile3 = resultSet.getInt("etatProgressUsineTextile3");
            double etatProgressUsineTextile4 = resultSet.getInt("etatProgressUsineTextile4");

            LocalDateTime dateDeco = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMillisDeco), ZoneId.systemDefault());
            LocalDateTime dateDebutJeu = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMillisDebutJeu), ZoneId.systemDefault());

            Sauvegarde sauvegarde = new Sauvegarde(pseudo, argent, numJourDeco, heureDeco, progressJour, nbPoules, nbOeufs, fermeActive, distributeursActive, distributeurBCActive,
                    distributeurBFActive, distributeurCoActive, distributeurSaActive,
                    livraison1Active, livraison2Active, livraison3Active, livraison4Active, livraison5Active,
                    etatProgressOeuf, dateDeco,
                    nbDistributeurBC, nbDistributeurBF, nbDistributeurSa, nbDistributeurCo,
                    nbMarchandisesBC, nbMarchandisesBF, nbMarchandisesSa, nbMarchandisesCo,
                    etatProgressBC, etatProgressBF, etatProgressSa, etatProgressCo, dateDebutJeu, poulailler1, poulailler2, poulailler3, poulailler4,
                    nbLivraison1, nbLivraison2, nbLivraison3, nbLivraison4, nbLivraison5,
                    nbCourses1, nbCourses2, nbCourses3, nbCourses4, nbCourses5,
                    etatProgressLivraison1, etatProgressLivraison2, etatProgressLivraison3, etatProgressLivraison4, etatProgressLivraison5,
                    usineTextileActive1, usineTextileActive2, usineTextileActive3, usineTextileActive4,
                    nbUsinesTextile1, nbUsinesTextile2, nbUsinesTextile3, nbUsinesTextile4,
                    nbMarchandisesUsineTextile1, nbMarchandisesUsineTextile2, nbMarchandisesUsineTextile3, nbMarchandisesUsineTextile4,
                    etatProgressUsineTextile1, etatProgressUsineTextile2, etatProgressUsineTextile3, etatProgressUsineTextile4);
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
                "distributeurCoActive = ?,distributeurSaActive = ?," +
                "livraison1Active = ?, livraison2Active = ?, livraison3Active = ?, livraison4Active = ?, livraison5Active = ?," +
                " etatProgressOeuf = ?, dateDeco = ?," +
                "nbDistributeurBC = ?, nbDistributeurBF = ?, nbDistributeurSa = ?, nbDistributeurCo = ?," +
                "nbMarchandisesBC = ?, nbMarchandisesBF = ?, nbMarchandisesSa = ?, nbMarchandisesCo = ?," +
                "etatProgressBC = ?, etatProgressBF = ?, etatProgressSa = ?, etatProgressCo = ?, " +
                "poulailler1 = ?, poulailler2 = ?, poulailler3 = ?, poulailler4 = ?," +
                "nbLivraison1 = ?, nbLivraison2 = ?, nbLivraison3 = ?, nbLivraison4 = ?, nbLivraison5 = ?," +
                "nbCourses1 = ?, nbCourses2 = ?, nbCourses3 = ?, nbCourses4 = ?, nbCourses5 = ?," +
                "etatProgressLivraison1 = ?, etatProgressLivraison2 = ?, etatProgressLivraison3 = ?, etatProgressLivraison4 = ?, etatProgressLivraison5 = ?," +
                "usineTextileActive1 = ?, usineTextileActive2 = ?, usineTextileActive3 = ?, usineTextileActive4 = ?,"+
                "nbUsinesTextile1 = ?, nbUsinesTextile2 = ?, nbUsinesTextile3 = ?, nbUsinesTextile4 = ?,"+
                "nbMarchandisesUsineTextile1 = ?, nbMarchandisesUsineTextile2 = ?, nbMarchandisesUsineTextile3 = ?, nbMarchandisesUsineTextile4 = ?,"+
                "etatProgressUsineTextile1 = ?, etatProgressUsineTextile2 = ?, etatProgressUsineTextile3 = ?, etatProgressUsineTextile4 = ?"+
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
        stmt.setInt(13, sauvegarde.getLivraison1Active());
        stmt.setInt(14, sauvegarde.getLivraison2Active());
        stmt.setInt(15, sauvegarde.getLivraison3Active());
        stmt.setInt(16, sauvegarde.getLivraison4Active());
        stmt.setInt(17, sauvegarde.getLivraison5Active());
        stmt.setDouble(18, sauvegarde.getEtatProgressOeuf());
        stmt.setTimestamp(19, timestamp);

        stmt.setInt(20, sauvegarde.getNbDistributeurBC());
        stmt.setInt(21, sauvegarde.getNbDistributeurBF());
        stmt.setInt(22, sauvegarde.getNbDistributeurSa());
        stmt.setInt(23, sauvegarde.getNbDistributeurCo());

        stmt.setLong(24, sauvegarde.getNbMarchandisesBC());
        stmt.setLong(25, sauvegarde.getNbMarchandisesBF());
        stmt.setLong(26, sauvegarde.getNbMarchandisesSa());
        stmt.setLong(27, sauvegarde.getNbMarchandisesCo());

        stmt.setDouble(28, sauvegarde.getEtatProgressBC());
        stmt.setDouble(29, sauvegarde.getEtatProgressBF());
        stmt.setDouble(30, sauvegarde.getEtatProgressSa());
        stmt.setDouble(31, sauvegarde.getEtatProgressCo());
        stmt.setInt(32, sauvegarde.getPoulailler1());
        stmt.setInt(33, sauvegarde.getPoulailler2());
        stmt.setInt(34, sauvegarde.getPoulailler3());
        stmt.setInt(35, sauvegarde.getPoulailler4());

        stmt.setInt(36, sauvegarde.getNbLivraison1());
        stmt.setInt(37, sauvegarde.getNbLivraison2());
        stmt.setInt(38, sauvegarde.getNbLivraison3());
        stmt.setInt(39, sauvegarde.getNbLivraison4());
        stmt.setInt(40, sauvegarde.getNbLivraison5());

        stmt.setLong(41, sauvegarde.getNbCourses1());
        stmt.setLong(42, sauvegarde.getNbCourses2());
        stmt.setLong(43, sauvegarde.getNbCourses3());
        stmt.setLong(44, sauvegarde.getNbCourses4());
        stmt.setLong(45, sauvegarde.getNbCourses5());

        stmt.setDouble(46, sauvegarde.getEtatProgressLivraison1());
        stmt.setDouble(47, sauvegarde.getEtatProgressLivraison2());
        stmt.setDouble(48, sauvegarde.getEtatProgressLivraison3());
        stmt.setDouble(49, sauvegarde.getEtatProgressLivraison4());
        stmt.setDouble(50, sauvegarde.getEtatProgressLivraison5());

        stmt.setInt(51, sauvegarde.getUsineTextileActive1());
        stmt.setInt(52, sauvegarde.getUsineTextileActive2());
        stmt.setInt(53, sauvegarde.getUsineTextileActive3());
        stmt.setInt(54, sauvegarde.getUsineTextileActive4());

        stmt.setInt(55, sauvegarde.getNbUsinesTextile1());
        stmt.setInt(56, sauvegarde.getNbUsinesTextile2());
        stmt.setInt(57, sauvegarde.getNbUsinesTextile3());
        stmt.setInt(58, sauvegarde.getNbUsinesTextile4());

        stmt.setLong(59, sauvegarde.getNbMarchandisesUsineTextile1());
        stmt.setLong(60, sauvegarde.getNbMarchandisesUsineTextile2());
        stmt.setLong(61, sauvegarde.getNbMarchandisesUsineTextile3());
        stmt.setLong(62, sauvegarde.getNbMarchandisesUsineTextile4());

        stmt.setDouble(63, sauvegarde.getEtatProgressUsineTextile1());
        stmt.setDouble(64, sauvegarde.getEtatProgressUsineTextile2());
        stmt.setDouble(65, sauvegarde.getEtatProgressUsineTextile3());
        stmt.setDouble(66, sauvegarde.getEtatProgressUsineTextile4());

        stmt.setString(67, sauvegarde.getPseudo());

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
