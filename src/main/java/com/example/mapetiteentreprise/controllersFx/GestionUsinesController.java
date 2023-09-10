package com.example.mapetiteentreprise.controllersFx;

import com.example.mapetiteentreprise.Main;
import com.example.mapetiteentreprise.actions.Outils;
import com.example.mapetiteentreprise.jeu.Jeu;
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
    private ProgressBar progressOeufs, progressBC, progressBF, progressSa, progressCo,
            progressScooter, progressCamionette, progressPetitCamion, progressPoidsLourd, progressAvion,
            progressTextile1, progressTextile2, progressTextile3, progressTextile4;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Jeu jeu;

    public void demarrer(Jeu jeu) {
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
        System.out.println("fermeture");
    }

    /**
     * Bouton qui ouvre les usines de textile
     */
    public void switchTextile(ActionEvent event) {
        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();
        // sauvegarde bdd
        sauveBdd();
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("usinesTextile.fxml"));
            root = loader.load();
            UsinesTextileController usinesTextileController = loader.getController();
            usinesTextileController.demarrer(jeu);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            // Permet de récupérer le gestionnaire d'événements pour la fermeture de la fenêtre
            stage.setOnCloseRequest(usinesTextileController::onWindowClose);
            stage.show();
            stage.centerOnScreen();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Bouton qui ouvre les usines de jouet
     */
    public void switchJouets(ActionEvent event) {
        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();
        // sauvegarde bdd
        sauveBdd();
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("usinesJouet.fxml"));
            root = loader.load();
            UsinesJouetController usinesJouetController = loader.getController();
            // on renvoi les infos a la fenetre suivante (tout est dans l'instance jeu)

            usinesJouetController.demarrer(jeu);
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
     * Bouton qui ouvre les usines d'agroAlimentaire
     */
    public void switchAgroAlimentaire(ActionEvent event) {
        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();
        // sauvegarde bdd
        sauveBdd();
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("usinesAgroAlimentaire.fxml"));
            root = loader.load();
            UsinesAgroAlimentaireController usinesAgroAlimentaireController = loader.getController();
            // on renvoi les infos a la fenetre suivante (tout est dans l'instance jeu)

            usinesAgroAlimentaireController.demarrer(jeu);
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
     * Bouton qui ouvre les usines pharmaceutiques
     */
    public void switchPharmaceutique(ActionEvent event) {
        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();
        // sauvegarde bdd
        sauveBdd();
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("usinesPharmaceutique.fxml"));
            root = loader.load();
            UsinesPharmaceutiqueController usinesPharmaceutiqueController = loader.getController();
            // on renvoi les infos a la fenetre suivante (tout est dans l'instance jeu)

            usinesPharmaceutiqueController.demarrer(jeu);
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
     * Demarrage des barres de progression, dans l'ordre
     * la ferme avec les oeufs => incrémente les oeufs
     * les heures => incrément les heures
     * demarrage des distributeurs
     * demarrage des livraisons
     */
    public void demarrageProgress() {
        // recuperation de l'etat des barres de progression
        double vitesseOeuf = jeu.getParametres().getVitessePonteOeuf() - (jeu.getParametres().getVitessePonteOeuf() * jeu.getJoueur().getFerme().getEtatProgressOeuf());

        jeu.getJoueur().getFerme().progressBarStartOeuf(1, jeu.getParametres().getVitessePonteOeuf(), vitesseOeuf, progressOeufs);
        jeu.getCalendrier().progressHeure(1, jeu.getParametres().getVitessePonteOeuf(), vitesseOeuf);

        // demarrage des distributeurs
        demarrageDistributeurs();

        // demarrage des livraisons
        demarrageLivraisons();

        // demarrage des usines
        demarrageUsinesTextile();
    }

    public void fermetureProgress() {
        // sauvegarde des barres de progression
        this.jeu.getJoueur().getFerme().setEtatProgressOeuf(this.progressOeufs.getProgress());

        // on recupere l'etat de la barre de progression des distributeurs
        this.jeu.getJoueur().getBoissonsChaudes().setEtatProgressDistributeur(this.progressBC.getProgress());
        this.jeu.getJoueur().getBoissonsFraiches().setEtatProgressDistributeur(this.progressBF.getProgress());
        this.jeu.getJoueur().getConfiseries().setEtatProgressDistributeur(this.progressCo.getProgress());
        this.jeu.getJoueur().getSandwichs().setEtatProgressDistributeur(this.progressSa.getProgress());

        // on recupere les barres de progression des livraisons
        this.jeu.getJoueur().getLivraisonScooter().setEtatProgressLivraison(this.progressScooter.getProgress());
        this.jeu.getJoueur().getLivraisonCamionette().setEtatProgressLivraison(this.progressCamionette.getProgress());
        this.jeu.getJoueur().getLivraisonPetitCamion().setEtatProgressLivraison(this.progressPetitCamion.getProgress());
        this.jeu.getJoueur().getLivraisonPoidsLourd().setEtatProgressLivraison(this.progressPoidsLourd.getProgress());
        this.jeu.getJoueur().getLivraisonAvion().setEtatProgressLivraison(this.progressAvion.getProgress());

        // on recupere les barres de progression des usines de textile
        this.jeu.getJoueur().getUsineTextilePetite().setEtatProgressUsine(this.progressTextile1.getProgress());

        // on stoppe les barres de progression
        jeu.getJoueur().getFerme().progressBarStop();
        jeu.getCalendrier().progressBarStop();
        jeu.getJoueur().getBoissonsChaudes().progressBarStop();
        jeu.getJoueur().getBoissonsFraiches().progressBarStop();
        jeu.getJoueur().getConfiseries().progressBarStop();
        jeu.getJoueur().getSandwichs().progressBarStop();
        jeu.getJoueur().getLivraisonScooter().progressBarStop();
        jeu.getJoueur().getLivraisonCamionette().progressBarStop();
        jeu.getJoueur().getLivraisonPetitCamion().progressBarStop();
        jeu.getJoueur().getLivraisonPoidsLourd().progressBarStop();
        jeu.getJoueur().getLivraisonAvion().progressBarStop();
        jeu.getJoueur().getUsineTextilePetite().progressBarStop();
    }

    /**
     * Sauvegarde de la base de donnees
     */
    public void sauveBdd() {
        System.out.println("fermeture fenetre : Sauvegarde");
        try {
            this.jeu.sauvegardejeu();
            this.jeu.sauvegardeCredit();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Permet d'ajuster les distributeurs et les demarrer s'ils sont actifs
     */
    public void demarrageDistributeurs() {
        // Demmarage des distributueurs
        // Boissons chaudes
        if (jeu.getJoueur().getDistributeursActive() == 1 && jeu.getJoueur().getBoissonsChaudes().getNbDistributeurs() > 0) {
            double vitesse = jeu.getParametres().getVitesseBC() - (jeu.getParametres().getVitesseBC() * jeu.getJoueur().getBoissonsChaudes().getEtatProgressDistributeur());
            this.jeu.getJoueur().getBoissonsChaudes().progressBarStartBC(1, jeu.getParametres().getVitesseBC(), vitesse, progressBC);
        }

        // Boissons fraiches
        if (jeu.getJoueur().getDistributeursActive() == 1 && jeu.getJoueur().getBoissonsFraiches().getNbDistributeurs() > 0) {
            double vitesse = jeu.getParametres().getVitesseBF() - (jeu.getParametres().getVitesseBF() * jeu.getJoueur().getBoissonsFraiches().getEtatProgressDistributeur());
            this.jeu.getJoueur().getBoissonsFraiches().progressBarStartBF(1, jeu.getParametres().getVitesseBF(), vitesse, progressBF);
        }

        // Confiseries
        if (jeu.getJoueur().getDistributeursActive() == 1 && jeu.getJoueur().getConfiseries().getNbDistributeurs() > 0) {
            double vitesse = jeu.getParametres().getVitesseCo() - (jeu.getParametres().getVitesseCo() * jeu.getJoueur().getConfiseries().getEtatProgressDistributeur());
            this.jeu.getJoueur().getConfiseries().progressBarStartCo(1, jeu.getParametres().getVitesseCo(), vitesse, progressCo);
        }

        // Sandwichs
        if (jeu.getJoueur().getDistributeursActive() == 1 && jeu.getJoueur().getSandwichs().getNbDistributeurs() > 0) {
            double vitesse = jeu.getParametres().getVitesseSa() - (jeu.getParametres().getVitesseSa() * jeu.getJoueur().getSandwichs().getEtatProgressDistributeur());
            this.jeu.getJoueur().getSandwichs().progressBarStartSa(1, jeu.getParametres().getVitesseSa(), vitesse, progressSa);
        }
    }

    /**
     * Demarrage des barres de progression des livraisons
     */
    public void demarrageLivraisons() {
        if (jeu.getJoueur().getLivraison1Active() == 1) {
            // recuperation de l'etat de la barre de progression pour la livraison en scooter
            double vitesseScooter = jeu.getJoueur().getLivraisonScooter().getVitesseLivraion() - (jeu.getJoueur().getLivraisonScooter().getVitesseLivraion() * jeu.getJoueur().getLivraisonScooter().getEtatProgressLivraison());
            System.out.println("Vitesse scooter : " + vitesseScooter);
            this.jeu.getJoueur().getLivraisonScooter().progressBarStartScooter(1, jeu.getJoueur().getLivraisonScooter().getVitesseLivraion(), vitesseScooter, progressScooter);
        }
        if (jeu.getJoueur().getLivraison2Active() == 1) {
            // recuperation de l'etat de la barre de progression pour la livraison en camionette
            double vitesseCamionette = jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion() - (jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion() * jeu.getJoueur().getLivraisonCamionette().getEtatProgressLivraison());
            System.out.println("Vitesse camionette : " + vitesseCamionette);
            this.jeu.getJoueur().getLivraisonCamionette().progressBarStartCamionette(1, jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion(), vitesseCamionette, progressCamionette);
        }
        if (jeu.getJoueur().getLivraison3Active() == 1) {
            // recuperation de l'etat de la barre de progression pour la livraison en petit camion
            double vitessePetitCamion = jeu.getJoueur().getLivraisonPetitCamion().getVitesseLivraion() - (jeu.getJoueur().getLivraisonPetitCamion().getVitesseLivraion() * jeu.getJoueur().getLivraisonPetitCamion().getEtatProgressLivraison());
            System.out.println("Vitesse petit camion : " + vitessePetitCamion);
            this.jeu.getJoueur().getLivraisonPetitCamion().progressBarStartPetitCamion(1, jeu.getJoueur().getLivraisonPetitCamion().getVitesseLivraion(), vitessePetitCamion, progressPetitCamion);
        }
        if (jeu.getJoueur().getLivraison4Active() == 1) {
            // recuperation de l'etat de la barre de progression pour la livraison en poids lours
            double vitessePoidsLourd = jeu.getJoueur().getLivraisonPoidsLourd().getVitesseLivraion() - (jeu.getJoueur().getLivraisonPoidsLourd().getVitesseLivraion() * jeu.getJoueur().getLivraisonPoidsLourd().getEtatProgressLivraison());
            System.out.println("Vitesse poids lourd : " + vitessePoidsLourd);
            this.jeu.getJoueur().getLivraisonPoidsLourd().progressBarStartPoidsLourd(1, jeu.getJoueur().getLivraisonPoidsLourd().getVitesseLivraion(), vitessePoidsLourd, progressPoidsLourd);
        }
        if (jeu.getJoueur().getLivraison5Active() == 1) {
            // recuperation de l'etat de la barre de progression pour la livraison en avion
            double vitesseAvion = jeu.getJoueur().getLivraisonAvion().getVitesseLivraion() - (jeu.getJoueur().getLivraisonAvion().getVitesseLivraion() * jeu.getJoueur().getLivraisonAvion().getEtatProgressLivraison());
            System.out.println("Vitesse avion : " + vitesseAvion);
            this.jeu.getJoueur().getLivraisonAvion().progressBarStartAvion(1, jeu.getJoueur().getLivraisonAvion().getVitesseLivraion(), vitesseAvion, progressAvion);
        }
    }

    /**
     * Demarre les usines lorsqu'elles sont actives
     */
    public void demarrageUsinesTextile() {
        if (Outils.isActif(jeu.getJoueur().getUsineTextilePetite().getUsineActive())) {
            // recupertaion etat barre de progression usine de textil petite
            double vitesseUsineTextile1 = jeu.getJoueur().getUsineTextilePetite().getVitesseUsineTextile() - (jeu.getJoueur().getUsineTextilePetite().getVitesseUsineTextile() * jeu.getJoueur().getUsineTextilePetite().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineTextilePetite().progressBarStartUsineTextile(1, this.jeu.getJoueur().getUsineTextilePetite().getVitesseUsineTextile(), vitesseUsineTextile1, progressTextile1);
        }
    }
}
