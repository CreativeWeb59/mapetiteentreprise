package com.example.mapetiteentreprise.bdd;

import java.sql.*;

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
//            System.out.println("Connexion a " + DBPath + " avec succès");
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
                "numJourDeco INTEGER, heureDeco INTEGER, progressJour REAL," +
                "nbPoules INTEGER, nbOeufs INTEGER, fermeActive INTEGER, distributeursActive INTEGER, distributeurBCActive INTEGER, distributeurBFActive INTEGER, distributeurCoActive INTEGER, distributeurSaActive INTEGER," +
                "livraison1Active INTEGER, livraison2Active INTEGER, livraison3Active INTEGER, livraison4Active INTEGER, livraison5Active INTEGER," +
                "etatProgressOeuf REAL, dateDeco TEXT," +
                "nbDistributeurBC INTEGER, nbDistributeurBF INTEGER, nbDistributeurSa INTEGER, nbDistributeurCo INTEGER," +
                "nbMarchandisesBC INTEGER, nbMarchandisesBF INTEGER, nbMarchandisesSa INTEGER, nbMarchandisesCo INTEGER," +
                "etatProgressBC REAL, etatProgressBF REAL, etatProgressSa REAL, etatProgressCo REAL, dateDebutJeu TEXT," +
                "poulailler1 INTEGER, poulailler2 INTEGER, poulailler3 INTEGER, poulailler4 INTEGER," +
                "nbLivraison1 INTEGER, nbLivraison2 INTEGER, nbLivraison3 INTEGER, nbLivraison4 INTEGER, nbLivraison5 INTEGER," +
                "nbCourses1 INTEGER, nbCourses2 INTEGER, nbCourses3 INTEGER, nbCourses4 INTEGER, nbCourses5 INTEGER," +
                "etatProgressLivraison1 REAL, etatProgressLivraison2 REAL, etatProgressLivraison3 REAL, etatProgressLivraison4 REAL, etatProgressLivraison5 REAL," +
                "usineTextileActive1 INTEGER, usineTextileActive2 INTEGER, usineTextileActive3 INTEGER, usineTextileActive4 INTEGER," +
                "nbUsinesTextile1 INTEGER, nbUsinesTextile2 INTEGER, nbUsinesTextile3 INTEGER, nbUsinesTextile4 INTEGER," +
                "nbMarchandisesUsineTextile1 INTEGER, nbMarchandisesUsineTextile2 INTEGER, nbMarchandisesUsineTextile3 INTEGER, nbMarchandisesUsineTextile4 INTEGER," +
                "etatProgressUsineTextile1 REAL, etatProgressUsineTextile2 REAL, etatProgressUsineTextile3 REAL, etatProgressUsineTextile4 REAL," +
                "usineJouetsActive1 INTEGER, usineJouetsActive2 INTEGER, usineJouetsActive3 INTEGER, usineJouetsActive4 INTEGER," +
                "nbUsinesJouets1 INTEGER, nbUsinesJouets2 INTEGER, nbUsinesJouets3 INTEGER, nbUsinesJouets4 INTEGER," +
                "nbMarchandisesUsineJouets1 INTEGER, nbMarchandisesUsineJouets2 INTEGER, nbMarchandisesUsineJouets3 INTEGER, nbMarchandisesUsineJouets4 INTEGER," +
                "etatProgressUsineJouets1 REAL, etatProgressUsineJouets2 REAL, etatProgressUsineJouets3 REAL, etatProgressUsineJouets4 REAL," +
                "usineAgroAlimentaireActive1 INTEGER, usineAgroAlimentaireActive2 INTEGER, usineAgroAlimentaireActive3 INTEGER, usineAgroAlimentaireActive4 INTEGER," +
                "nbUsinesAgroAlimentaire1 REAL, nbUsinesAgroAlimentaire2 REAL, nbUsinesAgroAlimentaire3 REAL, nbUsinesAgroAlimentaire4 REAL," +
                "nbMarchandisesUsineAgroAlimentaire1 INTEGER, nbMarchandisesUsineAgroAlimentaire2 INTEGER, nbMarchandisesUsineAgroAlimentaire3 INTEGER, nbMarchandisesUsineAgroAlimentaire4 INTEGER," +
                "etatProgressUsineAgroAlimentaire1 REAL, etatProgressUsineAgroAlimentaire2 REAL, etatProgressUsineAgroAlimentaire3 REAL, etatProgressUsineAgroAlimentaire4 REAL," +
                "usinePharmaceutiqueActive1 INTEGER, usinePharmaceutiqueActive2 INTEGER, usinePharmaceutiqueActive3 INTEGER, usinePharmaceutiqueActive4 INTEGER," +
                "nbUsinesPharmaceutique1 REAL, nbUsinesPharmaceutique2 REAL, nbUsinesPharmaceutique3 REAL, nbUsinesPharmaceutique4 REAL," +
                "nbMarchandisesUsinePharmaceutique1 INTEGER, nbMarchandisesUsinePharmaceutique2 INTEGER, nbMarchandisesUsinePharmaceutique3 INTEGER, nbMarchandisesUsinePharmaceutique4 INTEGER," +
                "etatProgressUsinePharmaceutique1 REAL, etatProgressUsinePharmaceutique2 REAL, etatProgressUsinePharmaceutique3 REAL, etatProgressUsinePharmaceutique4 REAL" +
                ")";
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
                "mensualite REAL, nbMMensualite INTEGER, cycleMensualite INTEGER, termine INTEGER, dateDebutCredit INTEGER," +
                " dateDerniereMensualite INTEGER, dateProchaineMensualite INTEGER, datePreavis INTEGER, blocageDatePreavis INTEGER)";
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
        System.out.println("La table 'credits' a été créée avec succès.");
    }

}
