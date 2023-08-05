package com.example.mapetiteentreprise.controllersFx;

import com.example.mapetiteentreprise.Main;
import com.example.mapetiteentreprise.jeu.Jeu;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.time.LocalDateTime;

public class Banquecontroller {
    private Jeu jeu;
    @FXML
    Button retourMenu;
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void startFenetre(Jeu jeu) {
        // Recuperation du jeu
        this.jeu = jeu;
        System.out.println("affichage de la banque");
    }

    public void retourGestion(Event event) {
        System.out.println("Retour au menu gestion");
        // on recupere l'etat de la barre de progression des oeufs
//        this.jeu.getJoueur().getFerme().setEtatProgressOeuf(this.progressOeufs.getProgress());
//        this.jeu.getCalendrier().setProgressJour(this.progressJour.getProgress());

        // on enregistre l'heure de switch de fenetre
//        this.jeu.getJoueur().getFerme().setDateDeco(LocalDateTime.now());
//
//        // on stoppe les barres de progression;
//        this.progressBarStop(timelineOeufs);
//        this.progressBarStop(timelineJour);
//
//        sauvegardeCredit();
//        sauvegardejeu();

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
     * Action a executé lors de la fermeture de la fentre avec la croix : sauvegarde
     *
     * @param event
     */
    public void onWindowClose(WindowEvent event) {
        // sauvegarde des barres de progression
//        this.jeu.getCalendrier().setProgressJour(this.progressJour.getProgress());
//        this.jeu.getJoueur().getFerme().setEtatProgressOeuf(this.progressOeufs.getProgress());

        // Sauvegarde de la base de donnees
        System.out.println("fermeture fenetre : Sauvegarde");
//        try {
//            sauvegardejeu();
//            sauvegardeCredit();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
    }
}
