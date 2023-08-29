package com.example.mapetiteentreprise.controllersFx;

import com.example.mapetiteentreprise.Main;
import com.example.mapetiteentreprise.actions.Outils;
import com.example.mapetiteentreprise.jeu.Jeu;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GestionUsinesController {
    @FXML
    private ProgressBar progressOeufs, progressJour, progressBC, progressBF, progressSa, progressCo,
            progressScooter, progressCamionette, progressPetitCamion, progressPoidsLourd, progressAvion;
    private Timeline timelineHeure, timelineJour, timelineBC, timelineBF, timelineSa, timelineCo, timelineOeufs,
            timelineScooter, timelineCamionette, timelinePetitCamion, timelinePoidsLourd, timelineAvion;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Jeu jeu;

    public void demarrer(Jeu jeu){
        // Recuperation du jeu
        this.jeu = jeu;
        demarrageProgress();
    }
    public void retourGestion(ActionEvent event) {
        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();

        // sauvegarde bdd
       sauveBdd();

        try {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("gestion.fxml"));
        root = loader.load();
        GestionController gestionController = loader.getController();
        // on renvoi les infos a la fenetre suivante (tout est dans l'instance jeu)

        gestionController.debutJeu(jeu);
    } catch (Exception e) {
        System.out.println(e);
    }

    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Action a executer lors de la fermeture de la fentre avec la croix : sauvegarde
     *
     * @param event
     */
    public void onWindowClose(WindowEvent event) {
        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();

        // sauvegarde bdd
        sauveBdd();
    }

    /**
     * Bouton qui ouvre les usines de textile
     */
    public void switchTextile(ActionEvent event){
        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();

        // sauvegarde bdd
        sauveBdd();
    }

    /**
     * Demarrage des barres de progression, dans l'ordre
     * la ferme avec les oeufs => incrémente les oeufs
     * les heures => incrément les heures
     */
    public void demarrageProgress(){
        // recuperation de l'etat des barres de progression
        double vitesseOeuf = jeu.getParametres().getVitessePonteOeuf() - (jeu.getParametres().getVitessePonteOeuf() * jeu.getJoueur().getFerme().getEtatProgressOeuf());

        jeu.getJoueur().getFerme().progressBarStartOeuf(1, jeu.getParametres().getVitessePonteOeuf(), vitesseOeuf, progressOeufs);
        jeu.getCalendrier().progressHeure(1, jeu.getParametres().getVitessePonteOeuf(), vitesseOeuf);
    }

    public void fermetureProgress(){
        // sauvegarde des barres de progression
        this.jeu.getJoueur().getFerme().setEtatProgressOeuf(this.progressOeufs.getProgress());

        // on stoppe les barres de progression;
        jeu.getJoueur().getFerme().progressBarStop();
        jeu.getCalendrier().progressBarStop();
    }

    /**
     * Sauvegarde de la base de donnees
     */
    public void sauveBdd(){
        System.out.println("fermeture fenetre : Sauvegarde");
        try {
            this.jeu.sauvegardejeu();
            this.jeu.sauvegardeCredit();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
