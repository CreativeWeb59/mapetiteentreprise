package com.example.monPetitBassin.bdd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ParametresService {
    private ConnectionBdd connectionBdd;
    private Statement statement;

    public ParametresService(ConnectionBdd connectionBdd) {
        this.connectionBdd = connectionBdd;
    }

    /**
     * Creation de la table parametres
     * avec les donnees par defaut
     */
    public void addParametresDefaut(Parametres parametres) throws SQLException {
        // Insertion des données
        String sql = "INSERT INTO parametres (nbPoules, tarifPoule, tarifOeuf, taxeOeuf, argentDepart, fermeActive, distributeursActive, " +
                "distributeurBCActive, distributeurBFActive, distributeurCoActive, distributeurSaActive, livraisonActive, lavageActive," +
                "vitessePonteOeuf," +
                "prixDistributeurBC, prixDistributeurBF, prixDistributeurSa, prixDistributeurCo," +
                "nbDistributeurBC, nbDistributeurBF, nbDistributeurSa, nbDistributeurCo," +
                "nbMarchandisesBC, nbMarchandisesBF, nbMarchandisesSa, nbMarchandisesCo," +
                "prixMarchandiseBC, prixMarchandiseBF, prixMarchandiseSa, prixMarchandiseCo," +
                "vitesseBC, vitesseBF, vitesseSa, vitesseCo" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connectionBdd.prepareStatement(sql);
        stmt.setInt(1, parametres.getNbPoules());
        stmt.setDouble(2, parametres.getTarifPoule().doubleValue());
        stmt.setDouble(3, parametres.getTarifOeuf().doubleValue());
        stmt.setDouble(4, parametres.getTaxeOeuf().doubleValue());
        stmt.setDouble(5, parametres.getArgentDepart().doubleValue());
        stmt.setInt(6, parametres.getFermeActive());
        stmt.setInt(7, parametres.getDistributeursActive());
        stmt.setInt(8, parametres.getDistributeurBCActive());
        stmt.setInt(9, parametres.getDistributeurBFActive());
        stmt.setInt(10, parametres.getDistributeurCoActive());
        stmt.setInt(11, parametres.getDistributeurSaActive());
        stmt.setInt(12, parametres.getLivraisonActive());
        stmt.setInt(13, parametres.getLavageActive());
        stmt.setInt(14, parametres.getVitessePonteOeuf());

        stmt.setDouble(15, parametres.getPrixDistributeurBC().doubleValue());
        stmt.setDouble(16, parametres.getPrixDistributeurBF().doubleValue());
        stmt.setDouble(17, parametres.getPrixDistributeurSa().doubleValue());
        stmt.setDouble(18, parametres.getPrixDistributeurCo().doubleValue());

        stmt.setInt(19, parametres.getNbDistributeurBC());
        stmt.setInt(20, parametres.getNbDistributeurBF());
        stmt.setInt(21, parametres.getNbDistributeurSa());
        stmt.setInt(22, parametres.getNbDistributeurCo());

        stmt.setInt(23, parametres.getNbMarchandisesBC());
        stmt.setInt(24, parametres.getNbMarchandisesBF());
        stmt.setInt(25, parametres.getNbMarchandisesSa());
        stmt.setInt(26, parametres.getNbMarchandisesCo());

        stmt.setDouble(27, parametres.getPrixMarchandiseBC().doubleValue());
        stmt.setDouble(28, parametres.getPrixMarchandiseBF().doubleValue());
        stmt.setDouble(29, parametres.getPrixMarchandiseSa().doubleValue());
        stmt.setDouble(30, parametres.getPrixMarchandiseCo().doubleValue());

        stmt.setInt(31, parametres.getVitesseBC());
        stmt.setInt(32, parametres.getVitesseBF());
        stmt.setInt(33, parametres.getVitesseSa());
        stmt.setInt(34, parametres.getVitesseCo());

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
            parametres.setId(generatedId);
            System.out.println("Les paramètres ont été insérés avec succès dans la base de données avec l'ID : " + generatedId);
        }
    }

    public List<Parametres> getParametres(){
        List<Parametres> parametresList = new ArrayList<>();
        String query = "SELECT * FROM Parametres";

        try (PreparedStatement statement = connectionBdd.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Parametres parametres = new Parametres();
                parametres.setId(resultSet.getInt("id"));
                parametres.setNbPoules(resultSet.getInt("nbPoules"));
                parametres.setTarifPoule(resultSet.getBigDecimal("tarifPoule"));
                parametres.setTarifOeuf(resultSet.getBigDecimal("tarifOeuf"));
                parametres.setTaxeOeuf(resultSet.getBigDecimal("taxeOeuf"));
                parametres.setArgentDepart(resultSet.getBigDecimal("argentDepart"));
                parametres.setFermeActive(resultSet.getInt("fermeActive"));
                parametres.setDistributeursActive(resultSet.getInt("distributeursActive"));
                parametres.setDistributeurBCActive(resultSet.getInt("distributeurBCActive"));
                parametres.setDistributeurBFActive(resultSet.getInt("distributeurBFActive"));
                parametres.setDistributeurCoActive(resultSet.getInt("distributeurCoActive"));
                parametres.setDistributeurSaActive(resultSet.getInt("distributeurSaActive"));
                parametres.setLivraisonActive(resultSet.getInt("livraisonActive"));
                parametres.setLavageActive(resultSet.getInt("lavageActive"));
                parametres.setVitessePonteOeuf(resultSet.getInt("vitessePonteOeuf"));

                parametres.setPrixDistributeurBC(resultSet.getBigDecimal("prixDistributeurBC"));
                parametres.setPrixDistributeurBF(resultSet.getBigDecimal("prixDistributeurBF"));
                parametres.setPrixDistributeurSa(resultSet.getBigDecimal("prixDistributeurSa"));
                parametres.setPrixDistributeurCo(resultSet.getBigDecimal("prixDistributeurCo"));

                parametres.setNbDistributeurBC(resultSet.getInt("nbDistributeurBC"));
                parametres.setNbDistributeurBF(resultSet.getInt("nbDistributeurBF"));
                parametres.setNbDistributeurSa(resultSet.getInt("nbDistributeurSa"));
                parametres.setNbDistributeurCo(resultSet.getInt("nbDistributeurCo"));

                parametres.setNbMarchandisesBC(resultSet.getInt("nbMarchandisesBC"));
                parametres.setNbMarchandisesBF(resultSet.getInt("nbMarchandisesBF"));
                parametres.setNbMarchandisesSa(resultSet.getInt("nbMarchandisesSa"));
                parametres.setNbMarchandisesCo(resultSet.getInt("nbMarchandisesCo"));

                parametres.setPrixMarchandiseBC(resultSet.getBigDecimal("prixMarchandiseBC"));
                parametres.setPrixMarchandiseBF(resultSet.getBigDecimal("prixMarchandiseBF"));
                parametres.setPrixMarchandiseSa(resultSet.getBigDecimal("prixMarchandiseSa"));
                parametres.setPrixMarchandiseCo(resultSet.getBigDecimal("prixMarchandiseCo"));

                parametres.setVitesseBC(resultSet.getInt("vitesseBC"));
                parametres.setVitesseBF(resultSet.getInt("vitesseBF"));
                parametres.setVitesseSa(resultSet.getInt("vitesseSa"));
                parametres.setVitesseCo(resultSet.getInt("vitesseCo"));

                parametresList.add(parametres);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return parametresList;
    }
}
