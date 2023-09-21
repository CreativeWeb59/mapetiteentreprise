package com.example.mapetiteentreprise.controllersFx;

import com.example.mapetiteentreprise.Main;
import com.example.mapetiteentreprise.actions.Outils;
import com.example.mapetiteentreprise.bdd.ConnectionBdd;
import com.example.mapetiteentreprise.jeu.Jeu;
import com.example.mapetiteentreprise.jeu.UsineJouets;
import com.example.mapetiteentreprise.jeu.UsineTextile;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.text.DecimalFormat;

public class UsinesJouetController {
    private final String monnaie = " €";
    private final String separationTexte = System.getProperty("line.separator");
    private final DecimalFormat decimalFormat = Outils.getDecimalFormatWithSpaceSeparator();
    @FXML
    private Label labelHaut,
            labelNbUsineJouets1, labelNbUsineJouets2, labelNbUsineJouets3, labelNbUsineJouets4,
            labelTarifUsineJouets1, labelTarifUsineJouets2, labelTarifUsineJouets3, labelTarifUsineJouets4,
            labelNbMarchandisesJouets1, labelNbMarchandisesJouets2, labelNbMarchandisesJouets3, labelNbMarchandisesJouets4;
    @FXML
    private Button btnAchatUsineJouets1, btnAchatUsineJouets2, btnAchatUsineJouets3, btnAchatUsineJouets4,
            btnAchatUsineJouetsPetite, btnAchatUsineJouetsMoyenne, btnAchatUsineJouetsGrande, btnAchatUsineJouetsEnorme,
            btnEncaisserUsineJouets1, btnEncaisserUsineJouets2, btnEncaisserUsineJouets3, btnEncaisserUsineJouets4;
    @FXML
    private ProgressBar progressOeufs, progressBC, progressBF, progressSa, progressCo,
            progressScooter, progressCamionette, progressPetitCamion, progressPoidsLourd, progressAvion,
            progressTextile1, progressTextile2, progressTextile3, progressTextile4,
            progressJouets1, progressJouets2, progressJouets3, progressJouets4;
    private Timeline timelineUsineTextile1, timelineUsineTextile2, timelineUsineTextile3, timelineUsineTextile4;
    private ConnectionBdd connectionBdd = new ConnectionBdd();
    @FXML
    private Pane paneProgress, paneJouets1, paneJouets2, paneJouets3, paneJouets4,
            paneJouets1D, paneJouets2D, paneJouets3D, paneJouets4D;
    @FXML
    private ImageView imgJouets1, imgJouets2, imgJouets3, imgJouets4;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Jeu jeu;
    public void demarrer(Jeu jeu){
        // Recuperation du jeu
        this.jeu = jeu;

        // recuperation des marchandises
        recupMarchandisesToutes();

        // majLabels et boutons
        miseEnPlace();

        // affichage des barres de progression (mode dev)
        jeu.afficheProgression(paneProgress);

        demarrageProgress();
    }
    /**
     * Retour au menu gestion des usines
     * @param event
     */
    public void retourGestionUsines(ActionEvent event) {
        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();
        // sauvegarde bdd
        sauveBdd();

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("gestionUsines.fxml"));
            root = loader.load();
            GestionUsinesController gestionUsinesController = loader.getController();
            // on renvoi les infos a la fenetre suivante (tout est dans l'instance jeu)

            gestionUsinesController.demarrer(jeu);
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
     * Demarrage des barres de progression, dans l'ordre
     * la ferme avec les oeufs => incrémente les oeufs
     * les heures => incrément les heures
     * demarrage des distributeurs
     * demarrage des livraisons
     */
    public void demarrageProgress(){
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


    public void recupMarchandisesToutes(){
        recupMarchandises(this.jeu.getJoueur().getUsineJouetsPetite(), btnEncaisserUsineJouets1, imgJouets1);
        recupMarchandises(this.jeu.getJoueur().getUsineJouetsMoyenne(), btnEncaisserUsineJouets2, imgJouets2);
        recupMarchandises(this.jeu.getJoueur().getUsineJouetsGrande(), btnEncaisserUsineJouets3, imgJouets3);
        recupMarchandises(this.jeu.getJoueur().getUsineJouetsEnorme(), btnEncaisserUsineJouets4, imgJouets4);
    }
    /**
     * Maj les gains en attente à chaque fin de progression
     * @param usine
     * @param boutonEncaisser
     */
    public void recupMarchandises(UsineJouets usine, Button boutonEncaisser, ImageView imageView) {
        // petite usine de textile
        // maj des gains en attente
        usine.setGainEnAttenteUsine(usine.majGainsEnAttente());
        // maj du bouton
        usine.majBtnEncaisser(boutonEncaisser, imageView);

    }
    /**
     * Mise en place des labels et boutons
     */
    public void miseEnPlace() {
        affichageBtn();
        majLabels();
    }

    /**
     * Affiche les bons boutons : Achat service de livraison / Encaisser
     */
    public void affichageBtn() {
        affichageBtnJouets1();
        affichageBtnJouets2();
        affichageBtnJouets3();
        affichageBtnJouets4();
        affichageContenuPanes(jeu.getJoueur().getUsineJouetsPetite(), paneJouets1, paneJouets1D, labelNbMarchandisesJouets1, labelNbUsineJouets1, labelTarifUsineJouets1, btnAchatUsineJouetsPetite, btnEncaisserUsineTextile1, imgTextile1, progressTextile1);
        affichageContenuPanes(jeu.getJoueur().getUsineJouetsMoyenne(), paneJouets2, paneJouets2D, labelNbMarchandisesJouets2, labelNbUsineJouets2, labelTarifUsineJouets2, btnAchatUsineJouetsMoyenne, btnEncaisserUsineTextile2, imgTextile2, progressTextile2);
        affichageContenuPanes(jeu.getJoueur().getUsineJouetsGrande(), paneJouets3, paneJouets3D, labelNbMarchandisesJouets3, labelNbUsineJouets3, labelTarifUsineJouets3, btnAchatUsineJouetsGrande, btnEncaisserUsineTextile3, imgTextile3, progressTextile3);
        affichageContenuPanes(jeu.getJoueur().getUsineJouetsEnorme(), paneJouets4, paneJouets4D, labelNbMarchandisesJouets4, labelNbUsineJouets4, labelTarifUsineJouets4, btnAchatUsineJouetsEnorme, btnEncaisserUsineTextile4, imgTextile4, progressTextile4);
        testBtnAchats();
    }

    /**
     * Active / desactive le bouton d'achat de vehicule de livraison pour chaque usine
     */
    public void testBtnAchats() {
        // usine de textile petite
        testBtnAchat(jeu.getJoueur().getUsineJouetsPetite(), btnAchatUsineTextile1, btnAchatUsineTextilePetite);
        // usine de textile moyenne
        testBtnAchat(jeu.getJoueur().getUsineJouetsMoyenne(), btnAchatUsineTextile2, btnAchatUsineTextileMoyenne);
        // usine de textile grande
        testBtnAchat(jeu.getJoueur().getUsineJouetsGrande(), btnAchatUsineTextile3, btnAchatUsineTextileGrande);
        // usine de textile enorme
        testBtnAchat(jeu.getJoueur().getUsineJouetsEnorme(), btnAchatUsineTextile4, btnAchatUsineTextileEnorme);
    }
    /**
     * Active / desactive le bouton d'achat de vehicule de livraison pour l'usine et les boutons donnée en paramètres
     * @param usineJouets usine à tester
     * @param btnAchatUsine bouton de l'achat d'une usine (qui active l'usine en question)
     * @param btnAjoutUsine bouton de l'ajout d'une usine (qui ajoute une usine quand usine déja activée)
     */
    public void testBtnAchat(UsineJouets usineJouets, Button btnAchatUsine, Button btnAjoutUsine){
        if (jeu.getJoueur().isArgent(usineJouets.getPrixUsine()) && !usineJouets.isMaxiNbUsines()) {
            btnAchatUsine.setDisable(false);
            btnAjoutUsine.setDisable(false);
        } else {
            btnAchatUsine.setDisable(true);
            btnAjoutUsine.setDisable(true);
        }
    }
    /**
     * Gere l'affichage ou non du contenu des panes pour afficher / masquer l'usine au demarrage
     * @param usineJouets usine à afficher / masquer
     * @param pane
     * @param labelNb
     * @param labelTarif
     * @param labelTitre
     * @param btnEncaisser
     * @param btnPlus
     * @param barreProgress
     */
    public void affichageContenuPanes(UsineJouets usineJouets, Pane pane, Pane paneD, Label labelTitre, Label labelNb, Label labelTarif, Button btnPlus, Button btnEncaisser, ImageView imgTextile1, ProgressBar barreProgress){
        if (usineJouets.getUsineActive() == 1){
            // opacity du pane
            pane.setOpacity(1);
            // on cache le paneD
            paneD.setVisible(false);
            // on affiche les labels
            labelTitre.setVisible(true);
            labelNb.setVisible(true);
            labelTarif.setVisible(true);
            // on affiche les boutons
            btnPlus.setVisible(true);
            btnEncaisser.setVisible(true);
            // on affiche la barre de progress
            barreProgress.setVisible(true);
        } else {
            // opacity du pane
            pane.setOpacity(0.6);
            // on affiche le paneD
            paneD.setVisible(true);
            // on affiche les labels
            labelTitre.setVisible(false);
            labelNb.setVisible(false);
            labelTarif.setVisible(false);
            // on affiche les boutons
            btnPlus.setVisible(false);
            btnEncaisser.setVisible(false);
            // on affiche la barre de progress
            barreProgress.setVisible(false);
        }
    }
    /**
     * gere l'affichage des boutons jouets petite usine
     */
    public void affichageBtnJouets1() {
        // boutons du usine textile
        if (Outils.isActif(jeu.getJoueur().getUsineTextilePetite().getUsineActive())) {
            btnEncaisserUsineTextile1.setVisible(true);
            btnAchatUsineTextile1.setVisible(false);
            System.out.println("Actif");
            this.jeu.getJoueur().getUsineTextilePetite().majBtnEncaisser(btnEncaisserUsineTextile1, imgTextile1);
        } else {
            btnEncaisserUsineTextile1.setVisible(false);
            btnAchatUsineTextile1.setVisible(true);
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getUsineTextilePetite().getPrixUsine())) {
                paneTextile1.setOpacity(1);
                paneTextile1.setDisable(false);
                btnAchatUsineTextile1.setDisable(false);
            } else {
                btnAchatUsineTextile1.setDisable(true);
                paneTextile1.setOpacity(0.8);
                paneTextile1.setDisable(true);
            }
            System.out.println("non actif");
        }
    }
    /**
     * gere l'affichage des boutons textile usine moyenne
     */
    public void affichageBtnJouets2() {
        // boutons du usine textile
        if (Outils.isActif(jeu.getJoueur().getUsineTextileMoyenne().getUsineActive())) {
            btnEncaisserUsineTextile2.setVisible(true);
            btnAchatUsineTextile2.setVisible(false);
            System.out.println("Actif");
            this.jeu.getJoueur().getUsineTextileMoyenne().majBtnEncaisser(btnEncaisserUsineTextile2, imgTextile2);
        } else {
            btnEncaisserUsineTextile2.setVisible(false);
            btnAchatUsineTextile2.setVisible(true);
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getUsineTextileMoyenne().getPrixUsine())) {
                paneTextile2.setOpacity(1);
                paneTextile2.setDisable(false);
                btnAchatUsineTextile2.setDisable(false);
            } else {
                btnAchatUsineTextile2.setDisable(true);
                paneTextile2.setOpacity(0.8);
                paneTextile2.setDisable(true);
            }
            System.out.println("non actif");
        }
    }
    /**
     * gere l'affichage des boutons textile usine grande
     */
    public void affichageBtnJouets3() {
        if (Outils.isActif(jeu.getJoueur().getUsineTextileGrande().getUsineActive())) {
            btnEncaisserUsineTextile3.setVisible(true);
            btnAchatUsineTextile3.setVisible(false);
            System.out.println("Actif");
            this.jeu.getJoueur().getUsineTextileGrande().majBtnEncaisser(btnEncaisserUsineTextile3, imgTextile3);
        } else {
            btnEncaisserUsineTextile3.setVisible(false);
            btnAchatUsineTextile3.setVisible(true);
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getUsineTextileGrande().getPrixUsine())) {
                paneTextile3.setOpacity(1);
                paneTextile3.setDisable(false);
                btnAchatUsineTextile3.setDisable(false);
            } else {
                btnAchatUsineTextile3.setDisable(true);
                paneTextile3.setOpacity(0.8);
                paneTextile3.setDisable(true);
            }
            System.out.println("non actif");
        }
    }
    /**
     * gere l'affichage des boutons textile usine enorme
     */
    public void affichageBtnJouets4() {
        if (Outils.isActif(jeu.getJoueur().getUsineTextileEnorme().getUsineActive())) {
            btnEncaisserUsineTextile4.setVisible(true);
            btnAchatUsineTextile4.setVisible(false);
            System.out.println("Actif");
            this.jeu.getJoueur().getUsineTextileEnorme().majBtnEncaisser(btnEncaisserUsineTextile4, imgTextile4);
        } else {
            btnEncaisserUsineTextile4.setVisible(false);
            btnAchatUsineTextile4.setVisible(true);
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getUsineTextileEnorme().getPrixUsine())) {
                paneTextile4.setOpacity(1);
                paneTextile4.setDisable(false);
                btnAchatUsineTextile4.setDisable(false);
            } else {
                btnAchatUsineTextile4.setDisable(true);
                paneTextile4.setOpacity(0.8);
                paneTextile4.setDisable(true);
            }
            System.out.println("non actif");
        }
    }









    public void fermetureProgress(){
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
        this.jeu.getJoueur().getUsineTextileMoyenne().setEtatProgressUsine(this.progressTextile2.getProgress());
        this.jeu.getJoueur().getUsineTextileGrande().setEtatProgressUsine(this.progressTextile3.getProgress());
        this.jeu.getJoueur().getUsineTextileEnorme().setEtatProgressUsine(this.progressTextile4.getProgress());

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
        jeu.getJoueur().getUsineTextileMoyenne().progressBarStop();
        jeu.getJoueur().getUsineTextileGrande().progressBarStop();
        jeu.getJoueur().getUsineTextileEnorme().progressBarStop();
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
        if(jeu.getJoueur().getLivraison1Active() == 1){
            // recuperation de l'etat de la barre de progression pour la livraison en scooter
            double vitesseScooter = jeu.getJoueur().getLivraisonScooter().getVitesseLivraion() - (jeu.getJoueur().getLivraisonScooter().getVitesseLivraion() * jeu.getJoueur().getLivraisonScooter().getEtatProgressLivraison());
            System.out.println("Vitesse scooter : " + vitesseScooter);
            this.jeu.getJoueur().getLivraisonScooter().progressBarStartScooter(1, jeu.getJoueur().getLivraisonScooter().getVitesseLivraion(), vitesseScooter, progressScooter);
        }
        if(jeu.getJoueur().getLivraison2Active() == 1){
            // recuperation de l'etat de la barre de progression pour la livraison en camionette
            double vitesseCamionette = jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion() - (jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion() * jeu.getJoueur().getLivraisonCamionette().getEtatProgressLivraison());
            System.out.println("Vitesse camionette : " + vitesseCamionette);
            this.jeu.getJoueur().getLivraisonCamionette().progressBarStartCamionette(1, jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion(), vitesseCamionette, progressCamionette);
        }
        if(jeu.getJoueur().getLivraison3Active() == 1){
            // recuperation de l'etat de la barre de progression pour la livraison en petit camion
            double vitessePetitCamion = jeu.getJoueur().getLivraisonPetitCamion().getVitesseLivraion() - (jeu.getJoueur().getLivraisonPetitCamion().getVitesseLivraion() * jeu.getJoueur().getLivraisonPetitCamion().getEtatProgressLivraison());
            System.out.println("Vitesse petit camion : " + vitessePetitCamion);
            this.jeu.getJoueur().getLivraisonPetitCamion().progressBarStartPetitCamion(1, jeu.getJoueur().getLivraisonPetitCamion().getVitesseLivraion(), vitessePetitCamion, progressPetitCamion);
        }
        if(jeu.getJoueur().getLivraison4Active() == 1){
            // recuperation de l'etat de la barre de progression pour la livraison en poids lours
            double vitessePoidsLourd = jeu.getJoueur().getLivraisonPoidsLourd().getVitesseLivraion() - (jeu.getJoueur().getLivraisonPoidsLourd().getVitesseLivraion() * jeu.getJoueur().getLivraisonPoidsLourd().getEtatProgressLivraison());
            System.out.println("Vitesse poids lourd : " + vitessePoidsLourd);
            this.jeu.getJoueur().getLivraisonPoidsLourd().progressBarStartPoidsLourd(1, jeu.getJoueur().getLivraisonPoidsLourd().getVitesseLivraion(), vitessePoidsLourd, progressPoidsLourd);
        }
        if(jeu.getJoueur().getLivraison5Active() == 1){
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
        if (Outils.isActif(jeu.getJoueur().getUsineTextileMoyenne().getUsineActive())) {
            // recupertaion etat barre de progression usine de textil moyenne
            double vitesseUsineTextile2 = jeu.getJoueur().getUsineTextileMoyenne().getVitesseUsineTextile() - (jeu.getJoueur().getUsineTextileMoyenne().getVitesseUsineTextile() * jeu.getJoueur().getUsineTextileMoyenne().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineTextileMoyenne().progressBarStartUsineTextile(1, this.jeu.getJoueur().getUsineTextileMoyenne().getVitesseUsineTextile(), vitesseUsineTextile2, progressTextile2);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineTextileGrande().getUsineActive())) {
            // recupertaion etat barre de progression usine de textil grande
            double vitesseUsineTextile3 = jeu.getJoueur().getUsineTextileGrande().getVitesseUsineTextile() - (jeu.getJoueur().getUsineTextileGrande().getVitesseUsineTextile() * jeu.getJoueur().getUsineTextileGrande().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineTextileGrande().progressBarStartUsineTextile(1, this.jeu.getJoueur().getUsineTextileGrande().getVitesseUsineTextile(), vitesseUsineTextile3, progressTextile3);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineTextileEnorme().getUsineActive())) {
            // recupertaion etat barre de progression usine de textil enorme
            double vitesseUsineTextile4 = jeu.getJoueur().getUsineTextileEnorme().getVitesseUsineTextile() - (jeu.getJoueur().getUsineTextileEnorme().getVitesseUsineTextile() * jeu.getJoueur().getUsineTextileEnorme().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineTextileEnorme().progressBarStartUsineTextile(1, this.jeu.getJoueur().getUsineTextileEnorme().getVitesseUsineTextile(), vitesseUsineTextile4, progressTextile4);
        }
    }
}
