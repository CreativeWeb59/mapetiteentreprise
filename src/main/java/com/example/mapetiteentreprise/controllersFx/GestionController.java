package com.example.mapetiteentreprise.controllersFx;

import com.example.mapetiteentreprise.Main;
import com.example.mapetiteentreprise.bdd.ConnectionBdd;
import com.example.mapetiteentreprise.bdd.Sauvegarde;
import com.example.mapetiteentreprise.bdd.SauvegardeService;
import com.example.mapetiteentreprise.jeu.Jeu;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.text.DecimalFormat;

public class GestionController {
    @FXML
    private Label pseudoLabel;
    @FXML
    private Button btnNew, btnContinuer, btnFerme, btnAchatFerme, btnDistributeurs;

    private String pseudo;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Jeu jeu;
    private final String monnaie = " €";
    // pattern des nombre décimaux
    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    /**
     *
     * Recupere le nom de la sauvegarde
     * Utile pour relancer une partie
     * @param jeu
     */
    public void debutJeu(Jeu jeu){
        // Recuperation du jeu
        this.jeu = jeu;
        this.pseudoLabel.setText("Bonjour " + jeu.getJoueur().getPseudo() + System.getProperty("line.separator") + "Montant en banque : " + decimalFormat.format(jeu.getJoueur().getArgent()) + monnaie);
        System.out.println(jeu);

        // desactivation des activites non acquises
        // test activation ferme
        testFerme();
        testDistributeur();

    }

    /**
     * Test si l'activite ferme est active
     * 0 pour non
     * et 1 pour oui
     */
    public void testFerme(){
        // on verifie si la ferme est active
        // Gestion des boutons Acheter ferme / Gerer la ferme
        // de l'achat ferme
        if(jeu.getJoueur().getFermeActive() == 0){
            btnAchatFerme.setDisable(false);
            btnAchatFerme.setVisible(true);
            btnFerme.setDisable(true);
            btnFerme.setVisible(false);
        } else {
            btnAchatFerme.setDisable(true);
            btnAchatFerme.setVisible(false);
            btnFerme.setDisable(false);
            btnFerme.setVisible(true);
        }
    }

    public void testDistributeur(){
        // on verifie si le distributeur commun est débloqué
        // si débloqué => bouton visible

        if(jeu.getJoueur().getDistributeursActive() == 0){
            btnDistributeurs.setDisable(true);
        } else {
            btnDistributeurs.setDisable(false);
        }
    }


    /**
     * Arrivee dans le jeu
     * desactivation de la ferme
     */
    public void switchToAchatFerme(ActionEvent event){
        // achat de la ferme
        // Tester le pseudo
        // Test nombre de caractères
        // Test si non vide
        System.out.println("Lancement du jeu, déblocage de la ferme");
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("ferme.fxml"));
            root = loader.load();
            FermeController fermeController = loader.getController();
            fermeController.nouveau(jeu);
        } catch (Exception e) {
            System.out.println(e);
        }
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Permet d'executer la fenetre de gestion de la ferme
     * @param event
     */
    public void switchToFerme(ActionEvent event)  {

        // Tester le pseudo
        // Test nombre de caractères
        // Test si non vide
        System.out.println("Lancement de la ferme et de la production d'oeufs");

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("ferme.fxml"));
            root = loader.load();

            FermeController fermeController = loader.getController();
            fermeController.demarrer(jeu);
        } catch (Exception e) {
            System.out.println(e);
        }

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }
    /**
     * Permet d'executer la fenetre de gestion des distributeurs
     * @param event
     */
    public void switchToDistributeurs(ActionEvent event)  {

        // Tester le pseudo
        // Test nombre de caractères
        // Test si non vide
        System.out.println("Menu de gestion des distributeurs");

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("distributeurs.fxml"));
            root = loader.load();

            GestionDistributeursController gestionDistributeursController = loader.getController();
            gestionDistributeursController.demarrer(jeu);
        } catch (Exception e) {
            System.out.println(e);
        }

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * permet de recuperer et transferer la partie
     * vers le choix des distributeurs
     * @param sauvegarde
     */
    public void lancerSauvegarde(Sauvegarde sauvegarde){
        System.out.println("Anciennement lancer sauvegarde");
    }

    /**
     * Sauvegarde les donnees
     * Ferme la partie du joueur => ne renvoie pas l'instance jeu
     * Permet de revenir au menu principal
     * @param event
     */
    public void retourMenu(ActionEvent event)  {
        try {
            sauvegardejeu();
        } catch (Exception e){
            System.out.println(e);
        }

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("main.fxml"));
            root = loader.load();
            MainController mainController = loader.getController();
            mainController.onLoad();
        } catch (Exception e) {
            System.out.println(e);
        }

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Bouton de sorite du jeu
     * @param event
     */
    public void exitJeu(ActionEvent event)  {
        sauvegardejeu();
        // Code pour quitter l'application
        Platform.exit();
    }

    /**
     * Permet de sauvegarder la partie dans la Bdd
     */
    public void sauvegardejeu()  {
        // mise a jour instance sauvegarde
        jeu.getSauvegarde().setArgent(jeu.getJoueur().getArgent());
        jeu.getSauvegarde().setNbPoules(jeu.getJoueur().getFerme().getNbPoules());
        jeu.getSauvegarde().setNbOeufs(jeu.getJoueur().getFerme().getNbOeufs());
        jeu.getSauvegarde().setEtatProgressOeuf(jeu.getJoueur().getFerme().getEtatProgressOeuf());
        // a modifier quand plusieurs activites
        jeu.getSauvegarde().setDateDeco(jeu.getJoueur().getFerme().getDateDeco());
        jeu.getSauvegarde().setFermeActive(jeu.getJoueur().getFermeActive());
        jeu.getSauvegarde().setDistributeursActive(jeu.getJoueur().getDistributeursActive());
        jeu.getSauvegarde().setDistributeurBCActive(jeu.getJoueur().getDistributeurBCActive());
        jeu.getSauvegarde().setDistributeurBFActive(jeu.getJoueur().getDistributeurBFActive());
        jeu.getSauvegarde().setDistributeurSaActive(jeu.getJoueur().getDistributeurSaActive());
        jeu.getSauvegarde().setDistributeurCoActive(jeu.getJoueur().getDistributeurCoActive());
        jeu.getSauvegarde().setNbDistributeurBC(jeu.getJoueur().getBoissonsChaudes().getNbDistributeurs());
        jeu.getSauvegarde().setNbDistributeurBF(jeu.getJoueur().getBoissonsFraiches().getNbDistributeurs());
        jeu.getSauvegarde().setNbDistributeurSa(jeu.getJoueur().getSandwichs().getNbDistributeurs());
        jeu.getSauvegarde().setNbDistributeurCo(jeu.getJoueur().getConfiseries().getNbDistributeurs());
        jeu.getSauvegarde().setNbMarchandisesBC(jeu.getJoueur().getBoissonsChaudes().getNbMarchandises());
        jeu.getSauvegarde().setNbMarchandisesBF(jeu.getJoueur().getBoissonsFraiches().getNbMarchandises());
        jeu.getSauvegarde().setNbMarchandisesSa(jeu.getJoueur().getSandwichs().getNbMarchandises());
        jeu.getSauvegarde().setNbMarchandisesCo(jeu.getJoueur().getConfiseries().getNbMarchandises());
        jeu.getSauvegarde().setEtatProgressBC(jeu.getJoueur().getBoissonsChaudes().getEtatProgressDistributeur());
        jeu.getSauvegarde().setEtatProgressBF(jeu.getJoueur().getBoissonsFraiches().getEtatProgressDistributeur());
        jeu.getSauvegarde().setEtatProgressSa(jeu.getJoueur().getSandwichs().getEtatProgressDistributeur());
        jeu.getSauvegarde().setEtatProgressCo(jeu.getJoueur().getConfiseries().getEtatProgressDistributeur());

        System.out.println("Nouvelles valeurs a sauvegarder" + jeu.getSauvegarde());

        // sauvegarde dans la bdd
        ConnectionBdd connectionBdd = new ConnectionBdd();
        connectionBdd.connect();
        SauvegardeService sauvegardeService = new SauvegardeService(connectionBdd);
        try {
            sauvegardeService.majSauvegarde(jeu.getSauvegarde());
        } catch (Exception e){
            System.out.println(e);
        }
        connectionBdd.close();
    }
}
