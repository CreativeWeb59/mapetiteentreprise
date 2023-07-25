package com.example.mapetiteentreprise.bdd;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;

public class ConnectionBdd {
    private String DBPath = "Chemin aux base de donnée SQLite";
    private Connection connection = null;
    private Statement statement = null;

    public ConnectionBdd() {
        this.DBPath = "mapetiteentreprise.db";
    }

    /**
     * cree la connection à la Bdd
     */
    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DBPath);
            statement = connection.createStatement();
            System.out.println("Connexion a " + DBPath + " avec succès");
        } catch (ClassNotFoundException notFoundException) {
            notFoundException.printStackTrace();
            System.out.println("Erreur de connection");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.out.println("Erreur de connection");
        }
    }

    /**
     * Ferme la connection à la Bdd
     */
    public void close() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * permet d'effectuer des requettes
     *
     * @param request
     * @return
     */
    public ResultSet query(String request) {
        ResultSet resultat = null;
        try {
            resultat = statement.executeQuery(request);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur dans la requete : " + request);
        }
        return resultat;
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    /**
     * cree la table sauvegarde
     *
     * @throws SQLException
     */
    public void createModelSauvegarde() throws SQLException {
        String sql = "CREATE TABLE sauvegarde (id INTEGER PRIMARY KEY, pseudo TEXT, argent REAL," +
                "nbPoules INTEGER, nbOeufs INTEGER, fermeActive INTEGER, distributeursActive INTEGER, distributeurBCActive INTEGER, distributeurBFActive INTEGER, distributeurCoActive INTEGER, distributeurSaActive INTEGER, livraisonActive INTEGER, lavageActive INTEGER, etatProgressOeuf REAL, dateDeco TEXT," +
                "nbDistributeurBC INTEGER, nbDistributeurBF INTEGER, nbDistributeurSa INTEGER, nbDistributeurCo INTEGER," +
                "nbMarchandisesBC INTEGER, nbMarchandisesBF INTEGER, nbMarchandisesSa INTEGER, nbMarchandisesCo INTEGER," +
                "etatProgressBC REAL, etatProgressBF REAL, etatProgressSa REAL, etatProgressCo REAL, dateDebutJeu TEXT)";
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
        System.out.println("La table 'sauvegarde' a été créée avec succès.");
    }

    /**
     * cree la table parametres
     *
     * @throws SQLException
     */
    public void createModelParametres() throws SQLException {
        String sql = "CREATE TABLE parametres (id INTEGER PRIMARY KEY, nbPoules INTEGER, tarifPoule REAL, tarifOeuf REAL," +
                " taxeOeuf REAL, argentDepart REAL, fermeActive INTEGER, distributeursActive INTEGER, distributeurBCActive INTEGER, distributeurBFActive INTEGER, distributeurCoActive INTEGER, distributeurSaActive INTEGER, livraisonActive INTEGER, lavageActive INTEGER, vitessePonteOeuf INTEGER," +
                "prixDistributeurBC REAL, prixDistributeurBF REAL, prixDistributeurSa REAL, prixDistributeurCo REAL," +
                "nbDistributeurBC INTEGER, nbDistributeurBF INTEGER, nbDistributeurSa INTEGER, nbDistributeurCo INTEGER," +
                "nbMarchandisesBC INTEGER, nbMarchandisesBF INTEGER, nbMarchandisesSa INTEGER, nbMarchandisesCo INTEGER," +
                "prixMarchandiseBC REAL, prixMarchandiseBF REAL, prixMarchandiseSa REAL, prixMarchandiseCo REAL," +
                "vitesseBC INTEGER, vitesseBF INTEGER, vitesseSa INTEGER, vitesseCo INTEGER)";
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
        System.out.println("La table 'parametres' a été créée avec succès.");
    }

    /**
     * Renvoi true si la table existe
     *
     * @return
     */
    public Boolean isModel(String tableName) throws SQLException {
        DatabaseMetaData metadata = connection.getMetaData();
        try (ResultSet resultSet = metadata.getTables(null, null, tableName, null)) {
            return resultSet.next();
        }
    }

    /**
     * cree la table credits
     * conserve une liste des credits par pseudo
     *
     * @throws SQLException
     */
    public void createModelCredits() throws SQLException {
        String sql = "CREATE TABLE credits (id INTEGER PRIMARY KEY, pseudo TEXT, montantPret REAL, coutPret REAL, montantRembourse REAL," +
                "mensualite REAL, nbMMensualite INTEGER, cycleMensualite INTEGER, termine INTEGER, dateDebutCredit TEXT, dateDerniereMensualite TEXT)";
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
        System.out.println("La table 'credits' a été créée avec succès.");
    }

}
