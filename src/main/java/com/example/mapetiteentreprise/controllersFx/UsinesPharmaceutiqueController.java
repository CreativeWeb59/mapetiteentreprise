package com.example.mapetiteentreprise.controllersFx;

import com.example.mapetiteentreprise.Main;
import com.example.mapetiteentreprise.actions.Outils;
import com.example.mapetiteentreprise.bdd.ConnectionBdd;
import com.example.mapetiteentreprise.jeu.Jeu;
import com.example.mapetiteentreprise.jeu.UsinePharmaceutique;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.util.Duration;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class UsinesPharmaceutiqueController {
    private final String monnaie = " €";
    private final String separationTexte = System.getProperty("line.separator");
    private final DecimalFormat decimalFormat = Outils.getDecimalFormatWithSpaceSeparator();
    @FXML
    private Label labelHaut,
            labelNbUsinePharmaceutique1, labelNbUsinePharmaceutique2, labelNbUsinePharmaceutique3, labelNbUsinePharmaceutique4,
            labelTarifUsinePharmaceutique1, labelTarifUsinePharmaceutique2, labelTarifUsinePharmaceutique3, labelTarifUsinePharmaceutique4,
            labelNbMarchandisesPharmaceutique1, labelNbMarchandisesPharmaceutique2, labelNbMarchandisesPharmaceutique3, labelNbMarchandisesPharmaceutique4;
    @FXML
    private Button btnAchatUsinePharmaceutique1, btnAchatUsinePharmaceutique2, btnAchatUsinePharmaceutique3, btnAchatUsinePharmaceutique4,
            btnAchatUsinePharmaceutiquePetite, btnAchatUsinePharmaceutiqueMoyenne, btnAchatUsinePharmaceutiqueGrande, btnAchatUsinePharmaceutiqueEnorme,
            btnEncaisserUsinePharmaceutique1, btnEncaisserUsinePharmaceutique2, btnEncaisserUsinePharmaceutique3, btnEncaisserUsinePharmaceutique4;
    @FXML
    private ProgressBar progressOeufs, progressBC, progressBF, progressSa, progressCo,
            progressScooter, progressCamionette, progressPetitCamion, progressPoidsLourd, progressAvion,
            progressTextile1, progressTextile2, progressTextile3, progressTextile4,
            progressJouets1, progressJouets2, progressJouets3, progressJouets4,
            progressPharmaceutique1, progressPharmaceutique2, progressPharmaceutique3, progressPharmaceutique4,
            progressAgroAlimentaire1, progressAgroAlimentaire2, progressAgroAlimentaire3, progressAgroAlimentaire4;
    private Timeline timelineUsinePharmaceutique1, timelineUsinePharmaceutique2, timelineUsinePharmaceutique3, timelineUsinePharmaceutique4;
    private ConnectionBdd connectionBdd = new ConnectionBdd();
    @FXML
    private Pane paneProgress, panePharmaceutique1, panePharmaceutique2, panePharmaceutique3, panePharmaceutique4,
            panePharmaceutique1D, panePharmaceutique2D, panePharmaceutique3D, panePharmaceutique4D,
            panePrincipal;
    @FXML
    private ImageView imgPharmaceutique1, imgPharmaceutique2, imgPharmaceutique3, imgPharmaceutique4;

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

        // barres de progression
        demarrageProgress();

        // affichage des barres de progression (mode dev)
        jeu.afficheProgression(paneProgress);

        centragePanesPrincipaux();
        centrageBoutons();
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
     * Ajout d'une petite usine de jouets
     */
    public void acheterUsinePharmaceutiquePetite() {
        acheterUsinePharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiquePetite(), progressPharmaceutique1, 1);
    }
    /**
     * Ajout d'une usine de jouets moyenne
     */
    public void acheterUsinePharmaceutiqueMoyenne() {
        acheterUsinePharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiqueMoyenne(), progressPharmaceutique2, 2);
    }
    /**
     * Ajout d'une grande usine de jouets
     */
    public void acheterUsinePharmaceutiqueGrande() {
        acheterUsinePharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiqueGrande(), progressPharmaceutique3, 3);
    }
    /**
     * Ajout d'une enorme usine de jouets
     */
    public void acheterUsinePharmaceutiqueEnorme() {
        acheterUsinePharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiqueEnorme(), progressPharmaceutique4, 4);
    }
    /**
     * Methode d'achat général d'une usine de textile
     *
     * @param usinePharmaceutique   à acheter
     * @param progressPharmaceutique barre de progression de l'usine
     * @param numPharmaceutique        permet de savoir qu'elle usine pour lancer la bonne barre de progression
     */
    public void acheterUsinePharmaceutique(UsinePharmaceutique usinePharmaceutique, ProgressBar progressPharmaceutique, int numPharmaceutique) {
        if (!usinePharmaceutique.isMaxiNbUsines()) {
            BigDecimal montantAchat = usinePharmaceutique.getPrixUsine();
            if (jeu.getJoueur().acheter(montantAchat)) {
                usinePharmaceutique.ajoutUsine();
                System.out.println("Achat d'une " + usinePharmaceutique.getNom() + " : " + usinePharmaceutique.getNbUsines());
                // debloque l'usine de jouets si besoin
                if (usinePharmaceutique.getNbUsines() == 1) {
                    usinePharmaceutique.setUsineActive(1);
                    // demarre la barre de progression de l'usine
                    // recuperation de l'etat de la barre de progression pour la petite usine
                    double vitesseUsineJouets = usinePharmaceutique.getVitesseUsine();
                    switch (numPharmaceutique) {
                        case 1:
                            this.progressBarStartUsinePharmaceutiquePetite(0, vitesseUsineJouets, vitesseUsineJouets, jeu.getJoueur().getUsinePharmaceutiquePetite(), progressPharmaceutique, btnEncaisserUsinePharmaceutique1);
                            break;
                        case 2:
                            this.progressBarStartUsinePharmaceutiqueMoyenne(0, vitesseUsineJouets, vitesseUsineJouets, jeu.getJoueur().getUsinePharmaceutiqueMoyenne(), progressPharmaceutique, btnEncaisserUsinePharmaceutique2);
                            break;
                        case 3:
                            this.progressBarStartUsinePharmaceutiqueGrande(0, vitesseUsineJouets, vitesseUsineJouets, jeu.getJoueur().getUsinePharmaceutiqueGrande(), progressPharmaceutique, btnEncaisserUsinePharmaceutique3);
                            break;
                        case 4:
                            this.progressBarStartUsinePharmaceutiqueGrande(0, vitesseUsineJouets, vitesseUsineJouets, jeu.getJoueur().getUsinePharmaceutiqueEnorme(), progressPharmaceutique, btnEncaisserUsinePharmaceutique4);
                            break;
                        default:
                            System.out.println("erreur d'usine");
                    }
                }
                // mise a jour des valeurs
                miseEnPlace();
            } else {
                System.out.println("Vous n'avez pas assez d'argent pour acheter une " + usinePharmaceutique.getNom() + " : ");
            }
        } else {
            System.out.println("Vous avez trop de " + usinePharmaceutique.getNom() + " : ");
        }
    }
    /**
     * Gere le clic sur le bouton encaisser usine de jouets petite
     */
    public void onBtnEncaisserUsinePharmaceutique1() {
        encaisserUsinePharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiquePetite(), btnEncaisserUsinePharmaceutique1);
    }
    /**
     * Gere le clic sur le bouton encaisser usine de jouets moyenne
     */
    public void onBtnEncaisserUsinePharmaceutique2() {
        encaisserUsinePharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiqueMoyenne(), btnEncaisserUsinePharmaceutique2);
    }
    /**
     * Gere le clic sur le bouton encaisser usine de jouets grande
     */
    public void onBtnEncaisserUsinePharmaceutique3() {
        encaisserUsinePharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiqueGrande(), btnEncaisserUsinePharmaceutique3);
    }
    /**
     * Gere le clic sur le bouton encaisser usine de jouets enorme
     */
    public void onBtnEncaisserUsinePharmaceutique4() {
        encaisserUsinePharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiqueEnorme(), btnEncaisserUsinePharmaceutique4);
    }
    /**
     * Bouton qui permet d'encaisser l'argent des usines de textile
     *
     * @param usinePharmaceutique usine textile spécifiée
     */
    public void encaisserUsinePharmaceutique(UsinePharmaceutique usinePharmaceutique, Button btnEncaisserUsinePharmaceutique) {
        long nbMarchandisesUsineTextile = usinePharmaceutique.getNbMarchandises();

        usinePharmaceutique.setNbMarchandises(0); // raz le nombre de marchandises
        jeu.getJoueur().setArgent(usinePharmaceutique.getGainEnAttenteUsine().add(jeu.getJoueur().getArgent())); // met a jour les nouveaux gains

        String formattedGain = decimalFormat.format(usinePharmaceutique.getGainEnAttenteUsine()) + monnaie;
        // raz des gains en attente
        usinePharmaceutique.setGainEnAttenteUsine(BigDecimal.valueOf(0.00));

        System.out.println("Vous venez de récupérer le prix de " + nbMarchandisesUsineTextile + " de l'usine de jouets " + usinePharmaceutique.getNom() + ", vous avez gagné " + formattedGain + ".");

        btnEncaisserUsinePharmaceutique.setDisable(true);
        this.miseEnPlace();
    }
    public void recupMarchandisesToutes(){
        recupMarchandises(this.jeu.getJoueur().getUsinePharmaceutiquePetite(), btnEncaisserUsinePharmaceutique1, imgPharmaceutique1);
        recupMarchandises(this.jeu.getJoueur().getUsinePharmaceutiqueMoyenne(), btnEncaisserUsinePharmaceutique2, imgPharmaceutique2);
        recupMarchandises(this.jeu.getJoueur().getUsinePharmaceutiqueGrande(), btnEncaisserUsinePharmaceutique3, imgPharmaceutique3);
        recupMarchandises(this.jeu.getJoueur().getUsinePharmaceutiqueEnorme(), btnEncaisserUsinePharmaceutique4, imgPharmaceutique4);
    }
    /**
     * Maj les gains en attente à chaque fin de progression
     * @param usinePharmaceutique
     * @param boutonEncaisser
     */
    public void recupMarchandises(UsinePharmaceutique usinePharmaceutique, Button boutonEncaisser, ImageView imageView) {
        // petite usine de textile
        // maj des gains en attente
        usinePharmaceutique.setGainEnAttenteUsine(usinePharmaceutique.majGainsEnAttente());
        // maj du bouton
        usinePharmaceutique.majBtnEncaisser(boutonEncaisser, imageView);

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
        affichageBtnPharmaceutique1();
        affichageBtnPharmaceutique2();
        affichageBtnPharmaceutique3();
        affichageBtnPharmaceutique4();
        affichageContenuPanes(jeu.getJoueur().getUsinePharmaceutiquePetite(), panePharmaceutique1, panePharmaceutique1D, labelNbMarchandisesPharmaceutique1, labelNbUsinePharmaceutique1, labelTarifUsinePharmaceutique1, btnAchatUsinePharmaceutiquePetite, btnEncaisserUsinePharmaceutique1, imgPharmaceutique1, progressPharmaceutique1);
        System.out.println("btn1");
        affichageContenuPanes(jeu.getJoueur().getUsinePharmaceutiqueMoyenne(), panePharmaceutique2, panePharmaceutique2D, labelNbMarchandisesPharmaceutique2, labelNbUsinePharmaceutique2, labelTarifUsinePharmaceutique2, btnAchatUsinePharmaceutiqueMoyenne, btnEncaisserUsinePharmaceutique2, imgPharmaceutique2, progressPharmaceutique2);
        System.out.println("btn2");
        affichageContenuPanes(jeu.getJoueur().getUsinePharmaceutiqueGrande(), panePharmaceutique3, panePharmaceutique3D, labelNbMarchandisesPharmaceutique3, labelNbUsinePharmaceutique3, labelTarifUsinePharmaceutique3, btnAchatUsinePharmaceutiqueGrande, btnEncaisserUsinePharmaceutique3, imgPharmaceutique3, progressPharmaceutique3);
        System.out.println("btn3");
        affichageContenuPanes(jeu.getJoueur().getUsinePharmaceutiqueEnorme(), panePharmaceutique4, panePharmaceutique4D, labelNbMarchandisesPharmaceutique4, labelNbUsinePharmaceutique4, labelTarifUsinePharmaceutique4, btnAchatUsinePharmaceutiqueEnorme, btnEncaisserUsinePharmaceutique4, imgPharmaceutique4, progressPharmaceutique4);
        System.out.println("btn4");
        testBtnAchats();
    }

    /**
     * Active / desactive le bouton d'achat de vehicule de livraison pour chaque usine
     */
    public void testBtnAchats() {
        // usine de textile petite
        testBtnAchat(jeu.getJoueur().getUsinePharmaceutiquePetite(), btnAchatUsinePharmaceutique1, btnAchatUsinePharmaceutiquePetite);
        // usine de textile moyenne
        testBtnAchat(jeu.getJoueur().getUsinePharmaceutiqueMoyenne(), btnAchatUsinePharmaceutique2, btnAchatUsinePharmaceutiqueMoyenne);
        // usine de textile grande
        testBtnAchat(jeu.getJoueur().getUsinePharmaceutiqueGrande(), btnAchatUsinePharmaceutique3, btnAchatUsinePharmaceutiqueGrande);
        // usine de textile enorme
        testBtnAchat(jeu.getJoueur().getUsinePharmaceutiqueEnorme(), btnAchatUsinePharmaceutique4, btnAchatUsinePharmaceutiqueEnorme);
    }
    /**
     * Active / desactive le bouton d'achat de vehicule de livraison pour l'usine et les boutons donnée en paramètres
     * @param usinePharmaceutique usine à tester
     * @param btnAchatUsine bouton de l'achat d'une usine (qui active l'usine en question)
     * @param btnAjoutUsine bouton de l'ajout d'une usine (qui ajoute une usine quand usine déja activée)
     */
    public void testBtnAchat(UsinePharmaceutique usinePharmaceutique, Button btnAchatUsine, Button btnAjoutUsine){
        if (jeu.getJoueur().isArgent(usinePharmaceutique.getPrixUsine()) && !usinePharmaceutique.isMaxiNbUsines()) {
            btnAchatUsine.setDisable(false);
            btnAjoutUsine.setDisable(false);
        } else {
            btnAchatUsine.setDisable(true);
            btnAjoutUsine.setDisable(true);
        }
    }
    /**
     * Gere l'affichage ou non du contenu des panes pour afficher / masquer l'usine au demarrage
     * @param usinePharmaceutique usine à afficher / masquer
     * @param pane
     * @param labelNb
     * @param labelTarif
     * @param labelTitre
     * @param btnEncaisser
     * @param btnPlus
     * @param barreProgress
     */
    public void affichageContenuPanes(UsinePharmaceutique usinePharmaceutique, Pane pane, Pane paneD, Label labelTitre, Label labelNb, Label labelTarif, Button btnPlus, Button btnEncaisser, ImageView imgPharmaceutique, ProgressBar barreProgress){
        if (usinePharmaceutique.getUsineActive() == 1){
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
    public void affichageBtnPharmaceutique1() {
        // boutons du usine textile
        if (Outils.isActif(jeu.getJoueur().getUsinePharmaceutiquePetite().getUsineActive())) {
            btnEncaisserUsinePharmaceutique1.setVisible(true);
            btnAchatUsinePharmaceutique1.setVisible(false);
            System.out.println("Actif");
            this.jeu.getJoueur().getUsineJouetsPetite().majBtnEncaisser(btnEncaisserUsinePharmaceutique1, imgPharmaceutique1);
        } else {
            btnEncaisserUsinePharmaceutique1.setVisible(false);
            btnAchatUsinePharmaceutique1.setVisible(true);
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getUsinePharmaceutiquePetite().getPrixUsine())) {
                panePharmaceutique1.setOpacity(1);
                panePharmaceutique1.setDisable(false);
                btnAchatUsinePharmaceutique1.setDisable(false);
            } else {
                btnAchatUsinePharmaceutique1.setDisable(true);
                panePharmaceutique1.setOpacity(0.8);
                panePharmaceutique1.setDisable(true);
            }
            System.out.println("non actif");
        }
    }
    /**
     * gere l'affichage des boutons jouets usine moyenne
     */
    public void affichageBtnPharmaceutique2() {
        // boutons du usine textile
        if (Outils.isActif(jeu.getJoueur().getUsinePharmaceutiqueMoyenne().getUsineActive())) {
            btnEncaisserUsinePharmaceutique2.setVisible(true);
            btnAchatUsinePharmaceutique2.setVisible(false);
            System.out.println("Actif");
            this.jeu.getJoueur().getUsineJouetsMoyenne().majBtnEncaisser(btnEncaisserUsinePharmaceutique2, imgPharmaceutique2);
        } else {
            btnEncaisserUsinePharmaceutique2.setVisible(false);
            btnAchatUsinePharmaceutique2.setVisible(true);
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getUsinePharmaceutiqueMoyenne().getPrixUsine())) {
                panePharmaceutique2.setOpacity(1);
                panePharmaceutique2.setDisable(false);
                btnAchatUsinePharmaceutique2.setDisable(false);
            } else {
                btnAchatUsinePharmaceutique2.setDisable(true);
                panePharmaceutique2.setOpacity(0.8);
                panePharmaceutique2.setDisable(true);
            }
            System.out.println("non actif");
        }
    }
    /**
     * gere l'affichage des boutons textile usine grande
     */
    public void affichageBtnPharmaceutique3() {
        if (Outils.isActif(jeu.getJoueur().getUsinePharmaceutiqueGrande().getUsineActive())) {
            btnEncaisserUsinePharmaceutique3.setVisible(true);
            btnAchatUsinePharmaceutique3.setVisible(false);
            System.out.println("Actif");
            this.jeu.getJoueur().getUsineJouetsGrande().majBtnEncaisser(btnEncaisserUsinePharmaceutique3, imgPharmaceutique3);
        } else {
            btnEncaisserUsinePharmaceutique3.setVisible(false);
            btnAchatUsinePharmaceutique3.setVisible(true);
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getUsinePharmaceutiqueGrande().getPrixUsine())) {
                panePharmaceutique3.setOpacity(1);
                panePharmaceutique3.setDisable(false);
                btnAchatUsinePharmaceutique3.setDisable(false);
            } else {
                btnAchatUsinePharmaceutique3.setDisable(true);
                panePharmaceutique3.setOpacity(0.8);
                panePharmaceutique3.setDisable(true);
            }
            System.out.println("non actif");
        }
    }
    /**
     * gere l'affichage des boutons textile usine enorme
     */
    public void affichageBtnPharmaceutique4() {
        if (Outils.isActif(jeu.getJoueur().getUsinePharmaceutiqueEnorme().getUsineActive())) {
            btnEncaisserUsinePharmaceutique4.setVisible(true);
            btnAchatUsinePharmaceutique4.setVisible(false);
            System.out.println("Actif");
            this.jeu.getJoueur().getUsineJouetsEnorme().majBtnEncaisser(btnEncaisserUsinePharmaceutique4, imgPharmaceutique4);
        } else {
            btnEncaisserUsinePharmaceutique4.setVisible(false);
            btnAchatUsinePharmaceutique4.setVisible(true);
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getUsinePharmaceutiqueEnorme().getPrixUsine())) {
                panePharmaceutique4.setOpacity(1);
                panePharmaceutique4.setDisable(false);
                btnAchatUsinePharmaceutique4.setDisable(false);
            } else {
                btnAchatUsinePharmaceutique4.setDisable(true);
                panePharmaceutique4.setOpacity(0.8);
                panePharmaceutique4.setDisable(true);
            }
            System.out.println("non actif");
        }
    }
    /**
     * Maj tous les labels necessaires
     */
    public void majLabels() {
        setLabelHaut();
        labelsUsinePharmaceutique1();
        labelsUsinePharmaceutique2();
        labelsUsinePharmaceutique3();
        labelsUsinePharmaceutique4();
    }

    public void setLabelHaut() {
        String formattedString = "En banque : " + decimalFormat.format(jeu.getJoueur().getArgent()) + monnaie;
        this.labelHaut.setText(formattedString);
    }

    /**
     * Labels de l'usine de pharmacie 1
     */
    public void labelsUsinePharmaceutique1() {
        setNbUsinePharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiquePetite(), labelNbUsinePharmaceutique1);
        setNbMarchandisesPharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiquePetite(), labelNbMarchandisesPharmaceutique1);
        setLabelTarifUsinePharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiquePetite(), labelTarifUsinePharmaceutique1);
        setLabelTarifUsinePharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiquePetite(), btnAchatUsinePharmaceutique1);
    }
    /**
     * Labels de l'usine de pharmacie 2
     */
    public void labelsUsinePharmaceutique2() {
        setNbUsinePharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiqueMoyenne(), labelNbUsinePharmaceutique2);
        setNbMarchandisesPharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiqueMoyenne(), labelNbMarchandisesPharmaceutique2);
        setLabelTarifUsinePharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiqueMoyenne(), labelTarifUsinePharmaceutique2);
        setLabelTarifUsinePharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiqueMoyenne(), btnAchatUsinePharmaceutique2);
    }
    /**
     * Labels de l'usine de pharmacie 3
     */
    public void labelsUsinePharmaceutique3() {
        setNbUsinePharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiqueGrande(), labelNbUsinePharmaceutique3);
        setNbMarchandisesPharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiqueGrande(), labelNbMarchandisesPharmaceutique3);
        setLabelTarifUsinePharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiqueGrande(), labelTarifUsinePharmaceutique3);
        setLabelTarifUsinePharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiqueGrande(), btnAchatUsinePharmaceutique3);
    }
    /**
     * Labels de l'usine de pharmacie 4
     */
    public void labelsUsinePharmaceutique4() {
        setNbUsinePharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiqueEnorme(), labelNbUsinePharmaceutique4);
        setNbMarchandisesPharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiqueEnorme(), labelNbMarchandisesPharmaceutique4);
        setLabelTarifUsinePharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiqueEnorme(), labelTarifUsinePharmaceutique4);
        setLabelTarifUsinePharmaceutique(this.jeu.getJoueur().getUsinePharmaceutiqueEnorme(), btnAchatUsinePharmaceutique4);
    }
    /**
     * initialise le nombre d'usines en cours ainsi que le nombre d'usines maximum
     */
    public void setNbUsinePharmaceutique(UsinePharmaceutique usinePharmaceutique, Label labelUsine) {
        String formattedString = usinePharmaceutique.setNbUsines();
        labelUsine.setText(formattedString);
    }

    /**
     * Centre les panes de chaque usine
     * Platform.runLater permet d'attendre le chargement des fenetres afin de récupérer les valeurs des boutons
     */
    public void centragePanesPrincipaux(){
        Platform.runLater(() -> {
            Outils.centragePane(panePharmaceutique1, panePrincipal.getWidth());
            Outils.centragePane(panePharmaceutique2, panePrincipal.getWidth());
            Outils.centragePane(panePharmaceutique3, panePrincipal.getWidth());
            Outils.centragePane(panePharmaceutique4, panePrincipal.getWidth());
        });
    }

    /**
     * Centre les boutons d'achat des usines
     * Platform.runLater permet d'attendre le chargement des fenetres afin de récupérer les valeurs des boutons
     */
    public void centrageBoutons(){
        Platform.runLater(() -> {
            // centrage horizontal
            Outils.centrageBouton(btnAchatUsinePharmaceutique1, panePrincipal.getWidth(), true);
            Outils.centrageBouton(btnAchatUsinePharmaceutique2, panePrincipal.getWidth(), true);
            Outils.centrageBouton(btnAchatUsinePharmaceutique3, panePrincipal.getWidth(), true);
            Outils.centrageBouton(btnAchatUsinePharmaceutique4, panePrincipal.getWidth(), true);
            // centrage vertical
            Outils.centrageBouton(btnAchatUsinePharmaceutique1, panePharmaceutique1D.getHeight(), false);
            Outils.centrageBouton(btnAchatUsinePharmaceutique2, panePharmaceutique2D.getHeight(), false);
            Outils.centrageBouton(btnAchatUsinePharmaceutique3, panePharmaceutique3D.getHeight(), false);
            Outils.centrageBouton(btnAchatUsinePharmaceutique4, panePharmaceutique4D.getHeight(), false);
        });
    }
    /**
     * initialise le nombre de marchandises produites
     */
    public void setNbMarchandisesPharmaceutique(UsinePharmaceutique usinePharmaceutique, Label labelUsine) {
        labelUsine.setText(usinePharmaceutique.getNbMarchandises() + "");
    }

    /**
     * Inscrit le prix d'achat d'une usine dans le label
     */
    public void setLabelTarifUsinePharmaceutique(UsinePharmaceutique usinePharmaceutique, Label labelUsine) {
        BigDecimal prixUsinePharmaceutique = usinePharmaceutique.getPrixUsine();
        String nomVehicule = usinePharmaceutique.getNom();
        String formattedString = "Acheter " + nomVehicule + " : " + decimalFormat.format(prixUsinePharmaceutique) + monnaie;
        labelUsine.setText(formattedString);
    }

    /**
     * Inscrit le prix d'achat d'une usine sur le bouton
     */
    public void setLabelTarifUsinePharmaceutique(UsinePharmaceutique usinePharmaceutique, Button btnAchatUsine) {
        BigDecimal prixUsinePharmaceutique = usinePharmaceutique.getPrixUsine();
        String nomVehicule = usinePharmaceutique.getNom();
        String formattedString = "Acheter " + nomVehicule + " : " + decimalFormat.format(prixUsinePharmaceutique) + monnaie;
        btnAchatUsine.setText(formattedString);
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
        demarrageUsinesJouets();
        demarrageUsinesAgroAlimentaire();
        demarrageUsinesParapharmaceutique();
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

        // on recupere les barres de progression des usines de jouets
        this.jeu.getJoueur().getUsineJouetsPetite().setEtatProgressUsine(this.progressJouets1.getProgress());
        this.jeu.getJoueur().getUsineJouetsMoyenne().setEtatProgressUsine(this.progressJouets2.getProgress());
        this.jeu.getJoueur().getUsineJouetsGrande().setEtatProgressUsine(this.progressJouets3.getProgress());
        this.jeu.getJoueur().getUsineJouetsEnorme().setEtatProgressUsine(this.progressJouets4.getProgress());

        // on recupere les barres de progression des usines de jouets
        this.jeu.getJoueur().getUsinePharmaceutiquePetite().setEtatProgressUsine(this.progressPharmaceutique1.getProgress());
        this.jeu.getJoueur().getUsinePharmaceutiqueMoyenne().setEtatProgressUsine(this.progressPharmaceutique2.getProgress());
        this.jeu.getJoueur().getUsinePharmaceutiqueGrande().setEtatProgressUsine(this.progressPharmaceutique3.getProgress());
        this.jeu.getJoueur().getUsinePharmaceutiqueEnorme().setEtatProgressUsine(this.progressPharmaceutique4.getProgress());

        // on recupere les barres de progression des usines Agro alimentaire
        this.jeu.getJoueur().getUsineAgroAlimentairePetite().setEtatProgressUsine(this.progressAgroAlimentaire1.getProgress());
        this.jeu.getJoueur().getUsineAgroAlimentaireMoyenne().setEtatProgressUsine(this.progressAgroAlimentaire2.getProgress());
        this.jeu.getJoueur().getUsineAgroAlimentaireGrande().setEtatProgressUsine(this.progressAgroAlimentaire3.getProgress());
        this.jeu.getJoueur().getUsineAgroAlimentaireEnorme().setEtatProgressUsine(this.progressAgroAlimentaire4.getProgress());

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
        jeu.getJoueur().getUsineJouetsPetite().progressBarStop();
        jeu.getJoueur().getUsineJouetsMoyenne().progressBarStop();
        jeu.getJoueur().getUsineJouetsGrande().progressBarStop();
        jeu.getJoueur().getUsineJouetsEnorme().progressBarStop();
        Outils.progressBarStop(timelineUsinePharmaceutique1);
        Outils.progressBarStop(timelineUsinePharmaceutique2);
        Outils.progressBarStop(timelineUsinePharmaceutique3);
        Outils.progressBarStop(timelineUsinePharmaceutique4);
        jeu.getJoueur().getUsineAgroAlimentairePetite().progressBarStop();
        jeu.getJoueur().getUsineAgroAlimentaireMoyenne().progressBarStop();
        jeu.getJoueur().getUsineAgroAlimentaireGrande().progressBarStop();
        jeu.getJoueur().getUsineAgroAlimentaireEnorme().progressBarStop();
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
            double vitesseUsineTextile1 = jeu.getJoueur().getUsineTextilePetite().getVitesseUsine() - (jeu.getJoueur().getUsineTextilePetite().getVitesseUsine() * jeu.getJoueur().getUsineTextilePetite().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineTextilePetite().progressBarStartUsineTextile(1, this.jeu.getJoueur().getUsineTextilePetite().getVitesseUsine(), vitesseUsineTextile1, progressTextile1);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineTextileMoyenne().getUsineActive())) {
            // recupertaion etat barre de progression usine de textil moyenne
            double vitesseUsineTextile2 = jeu.getJoueur().getUsineTextileMoyenne().getVitesseUsine() - (jeu.getJoueur().getUsineTextileMoyenne().getVitesseUsine() * jeu.getJoueur().getUsineTextileMoyenne().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineTextileMoyenne().progressBarStartUsineTextile(1, this.jeu.getJoueur().getUsineTextileMoyenne().getVitesseUsine(), vitesseUsineTextile2, progressTextile2);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineTextileGrande().getUsineActive())) {
            // recupertaion etat barre de progression usine de textil grande
            double vitesseUsineTextile3 = jeu.getJoueur().getUsineTextileGrande().getVitesseUsine() - (jeu.getJoueur().getUsineTextileGrande().getVitesseUsine() * jeu.getJoueur().getUsineTextileGrande().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineTextileGrande().progressBarStartUsineTextile(1, this.jeu.getJoueur().getUsineTextileGrande().getVitesseUsine(), vitesseUsineTextile3, progressTextile3);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineTextileEnorme().getUsineActive())) {
            // recupertaion etat barre de progression usine de textil enorme
            double vitesseUsineTextile4 = jeu.getJoueur().getUsineTextileEnorme().getVitesseUsine() - (jeu.getJoueur().getUsineTextileEnorme().getVitesseUsine() * jeu.getJoueur().getUsineTextileEnorme().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineTextileEnorme().progressBarStartUsineTextile(1, this.jeu.getJoueur().getUsineTextileEnorme().getVitesseUsine(), vitesseUsineTextile4, progressTextile4);
        }
    }
    /**
     * Demarre les usines de parapharmacie lorsqu'elles sont actives
     */
    public void demarrageUsinesParapharmaceutique() {
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsPetite().getUsineActive())) {
            // recupertaion etat barre de progression usine de jouets petite
            double vitesseUsineJouets1 = jeu.getJoueur().getUsineJouetsPetite().getVitesseUsine() - (jeu.getJoueur().getUsineJouetsPetite().getVitesseUsine() * jeu.getJoueur().getUsineJouetsPetite().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineJouetsPetite().progressBarStartUsineJouets(1, this.jeu.getJoueur().getUsineJouetsPetite().getVitesseUsine(), vitesseUsineJouets1, progressJouets1);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsMoyenne().getUsineActive())) {
            // recupertaion etat barre de progression usine de jouets moyenne
            double vitesseUsineJouets2 = jeu.getJoueur().getUsineJouetsMoyenne().getVitesseUsine() - (jeu.getJoueur().getUsineJouetsMoyenne().getVitesseUsine() * jeu.getJoueur().getUsineJouetsMoyenne().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineJouetsMoyenne().progressBarStartUsineJouets(1, this.jeu.getJoueur().getUsineJouetsMoyenne().getVitesseUsine(), vitesseUsineJouets2, progressJouets2);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsGrande().getUsineActive())) {
            // recupertaion etat barre de progression usine de jouets grande
            double vitesseUsineJouets3 = jeu.getJoueur().getUsineJouetsGrande().getVitesseUsine() - (jeu.getJoueur().getUsineJouetsGrande().getVitesseUsine() * jeu.getJoueur().getUsineJouetsGrande().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineJouetsGrande().progressBarStartUsineJouets(1, this.jeu.getJoueur().getUsineJouetsGrande().getVitesseUsine(), vitesseUsineJouets3, progressJouets3);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsEnorme().getUsineActive())) {
            // recupertaion etat barre de progression usine de jouets enorme
            double vitesseUsineJouets4 = jeu.getJoueur().getUsineJouetsEnorme().getVitesseUsine() - (jeu.getJoueur().getUsineJouetsEnorme().getVitesseUsine() * jeu.getJoueur().getUsineJouetsEnorme().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineJouetsEnorme().progressBarStartUsineJouets(1, this.jeu.getJoueur().getUsineJouetsEnorme().getVitesseUsine(), vitesseUsineJouets4, progressJouets4);
        }
    }
    public void demarrageUsinesAgroAlimentaire(){
        if (Outils.isActif(jeu.getJoueur().getUsineAgroAlimentairePetite().getUsineActive())) {
            // recupertaion etat barre de progression usine de jouets petite
            double vitesseUsineAgroAlimentaire1 = jeu.getJoueur().getUsineAgroAlimentairePetite().getVitesseUsine() - (jeu.getJoueur().getUsineAgroAlimentairePetite().getVitesseUsine() * jeu.getJoueur().getUsineAgroAlimentairePetite().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineAgroAlimentairePetite().progressBarStartUsineAgroAlimentaire(1, this.jeu.getJoueur().getUsineAgroAlimentairePetite().getVitesseUsine(), vitesseUsineAgroAlimentaire1, progressAgroAlimentaire1);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineAgroAlimentaireMoyenne().getUsineActive())) {
            // recupertaion etat barre de progression usine de jouets moyenne
            double vitesseUsineAgroAlimentaire2 = jeu.getJoueur().getUsineAgroAlimentaireMoyenne().getVitesseUsine() - (jeu.getJoueur().getUsineAgroAlimentaireMoyenne().getVitesseUsine() * jeu.getJoueur().getUsineAgroAlimentaireMoyenne().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineAgroAlimentaireMoyenne().progressBarStartUsineAgroAlimentaire(1, this.jeu.getJoueur().getUsineAgroAlimentaireMoyenne().getVitesseUsine(), vitesseUsineAgroAlimentaire2, progressAgroAlimentaire2);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineAgroAlimentaireGrande().getUsineActive())) {
            // recupertaion etat barre de progression usine de jouets grande
            double vitesseUsineAgroAlimentaire3 = jeu.getJoueur().getUsineAgroAlimentaireGrande().getVitesseUsine() - (jeu.getJoueur().getUsineAgroAlimentaireGrande().getVitesseUsine() * jeu.getJoueur().getUsineAgroAlimentaireGrande().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineAgroAlimentaireGrande().progressBarStartUsineAgroAlimentaire(1, this.jeu.getJoueur().getUsineAgroAlimentaireGrande().getVitesseUsine(), vitesseUsineAgroAlimentaire3, progressAgroAlimentaire3);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineAgroAlimentaireEnorme().getUsineActive())) {
            // recupertaion etat barre de progression usine de jouets enorme
            double vitesseUsineAgroAlimentaire4 = jeu.getJoueur().getUsineAgroAlimentaireEnorme().getVitesseUsine() - (jeu.getJoueur().getUsineAgroAlimentaireEnorme().getVitesseUsine() * jeu.getJoueur().getUsineAgroAlimentaireEnorme().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineAgroAlimentaireEnorme().progressBarStartUsineAgroAlimentaire(1, this.jeu.getJoueur().getUsineAgroAlimentaireEnorme().getVitesseUsine(), vitesseUsineAgroAlimentaire4, progressAgroAlimentaire4);
        }
    }
    /**
     * Demarre les usines de jouets lorsqu'elles sont actives
     */
    public void demarrageUsinesJouets() {
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsPetite().getUsineActive())) {
            // recupertaion etat barre de progression usine de jouets petite
            double vitesseUsineJouets1 = jeu.getJoueur().getUsineJouetsPetite().getVitesseUsine() - (jeu.getJoueur().getUsineJouetsPetite().getVitesseUsine() * jeu.getJoueur().getUsineJouetsPetite().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineJouetsPetite().progressBarStartUsineJouets(1, this.jeu.getJoueur().getUsineJouetsPetite().getVitesseUsine(), vitesseUsineJouets1, progressJouets1);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsMoyenne().getUsineActive())) {
            // recupertaion etat barre de progression usine de jouets moyenne
            double vitesseUsineJouets2 = jeu.getJoueur().getUsineJouetsMoyenne().getVitesseUsine() - (jeu.getJoueur().getUsineJouetsMoyenne().getVitesseUsine() * jeu.getJoueur().getUsineJouetsMoyenne().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineJouetsMoyenne().progressBarStartUsineJouets(1, this.jeu.getJoueur().getUsineJouetsMoyenne().getVitesseUsine(), vitesseUsineJouets2, progressJouets2);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsGrande().getUsineActive())) {
            // recupertaion etat barre de progression usine de jouets grande
            double vitesseUsineJouets3 = jeu.getJoueur().getUsineJouetsGrande().getVitesseUsine() - (jeu.getJoueur().getUsineJouetsGrande().getVitesseUsine() * jeu.getJoueur().getUsineJouetsGrande().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineJouetsGrande().progressBarStartUsineJouets(1, this.jeu.getJoueur().getUsineJouetsGrande().getVitesseUsine(), vitesseUsineJouets3, progressJouets3);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsEnorme().getUsineActive())) {
            // recupertaion etat barre de progression usine de jouets enorme
            double vitesseUsineJouets4 = jeu.getJoueur().getUsineJouetsEnorme().getVitesseUsine() - (jeu.getJoueur().getUsineJouetsEnorme().getVitesseUsine() * jeu.getJoueur().getUsineJouetsEnorme().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineJouetsEnorme().progressBarStartUsineJouets(1, this.jeu.getJoueur().getUsineJouetsEnorme().getVitesseUsine(), vitesseUsineJouets4, progressJouets4);
        }
    }

    /**
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine jouets petite
     *
     * @param cycle             : 0 pour cycle infini
     * @param vitesse           : vitesse de l'usine en secondes
     * @param vitesseAjustement
     * @param usinePharmaceutique
     * @param progress          : barre de progress de la fabrication d'une marchandise
     * @param btnEncaisser
     */
    public void progressBarStartUsinePharmaceutiquePetite(int cycle, double vitesse, double vitesseAjustement, UsinePharmaceutique usinePharmaceutique, ProgressBar progress, Button btnEncaisser) {
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(usinePharmaceutique.getEtatProgressUsine());
            etatBarreProgress = usinePharmaceutique.getEtatProgressUsine();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineUsinePharmaceutique1 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    System.out.println("cycle");
                    // rend le bouton Encaisser actif
                    btnEncaisser.setDisable(false);
                    // ajoute le nombre de marchandises fabriquées par l'usine
                    usinePharmaceutique.majUsine();
                    // maj le montant des gains en attente
                    recupMarchandises(usinePharmaceutique, btnEncaisserUsinePharmaceutique1, imgPharmaceutique1);
                    System.out.println("Production de marchandises dans " + usinePharmaceutique.getNom() + " terminée");
                    // met à jour les gains en cours ainsi que le bouton encaisser
                    usinePharmaceutique.majBtnEncaisser(btnEncaisser, imgPharmaceutique1);
                    // maj des labels
                    this.miseEnPlace();
                }, new KeyValue(progress.progressProperty(), 1))
        );
        if (cycle == 1) {
            timelineUsinePharmaceutique1.setOnFinished(event -> {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsinePharmaceutiquePetite(cycle - 1, vitesse, vitesse, usinePharmaceutique, progress, btnEncaisser);
            });
        }
        if (cycle == 0) {
            timelineUsinePharmaceutique1.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineUsinePharmaceutique1.setCycleCount(cycle);
        }
        timelineUsinePharmaceutique1.play();
    }
    /**
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine jouets moyenne
     *
     * @param cycle             : 0 pour cycle infini
     * @param vitesse           : vitesse de l'usine en secondes
     * @param vitesseAjustement
     * @param usinePharmaceutique
     * @param progress          : barre de progress de la fabrication d'une marchandise
     * @param btnEncaisser
     */
    public void progressBarStartUsinePharmaceutiqueMoyenne(int cycle, double vitesse, double vitesseAjustement, UsinePharmaceutique usinePharmaceutique, ProgressBar progress, Button btnEncaisser) {
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(usinePharmaceutique.getEtatProgressUsine());
            etatBarreProgress = usinePharmaceutique.getEtatProgressUsine();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineUsinePharmaceutique2 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    System.out.println("cycle");
                    // rend le bouton Encaisser actif
                    btnEncaisser.setDisable(false);
                    // ajoute le nombre de marchandises fabriquées par l'usine
                    usinePharmaceutique.majUsine();
                    // maj le montant des gains en attente
                    recupMarchandises(usinePharmaceutique, btnEncaisserUsinePharmaceutique2, imgPharmaceutique2);
                    System.out.println("Production de marchandises dans " + usinePharmaceutique.getNom() + " terminée");
                    // met à jour les gains en cours ainsi que le bouton encaisser
                    usinePharmaceutique.majBtnEncaisser(btnEncaisser, imgPharmaceutique2);
                    // maj des labels
                    this.miseEnPlace();
                }, new KeyValue(progress.progressProperty(), 1))
        );
        if (cycle == 1) {
            timelineUsinePharmaceutique2.setOnFinished(event -> {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsinePharmaceutiqueMoyenne(cycle - 1, vitesse, vitesse, usinePharmaceutique, progress, btnEncaisser);
            });
        }
        if (cycle == 0) {
            timelineUsinePharmaceutique2.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineUsinePharmaceutique2.setCycleCount(cycle);
        }
        timelineUsinePharmaceutique2.play();
    }
    /**
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine jouets grande
     *
     * @param cycle             : 0 pour cycle infini
     * @param vitesse           : vitesse de l'usine en secondes
     * @param vitesseAjustement
     * @param usinePharmaceutique
     * @param progress          : barre de progress de la fabrication d'une marchandise
     * @param btnEncaisser
     */
    public void progressBarStartUsinePharmaceutiqueGrande(int cycle, double vitesse, double vitesseAjustement, UsinePharmaceutique usinePharmaceutique, ProgressBar progress, Button btnEncaisser) {
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(usinePharmaceutique.getEtatProgressUsine());
            etatBarreProgress = usinePharmaceutique.getEtatProgressUsine();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineUsinePharmaceutique3 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    System.out.println("cycle");
                    // rend le bouton Encaisser actif
                    btnEncaisser.setDisable(false);
                    // ajoute le nombre de marchandises fabriquées par l'usine
                    usinePharmaceutique.majUsine();
                    // maj le montant des gains en attente
                    recupMarchandises(usinePharmaceutique, btnEncaisserUsinePharmaceutique3, imgPharmaceutique3);
                    System.out.println("Production de marchandises dans " + usinePharmaceutique.getNom() + " terminée");
                    // met à jour les gains en cours ainsi que le bouton encaisser
                    usinePharmaceutique.majBtnEncaisser(btnEncaisser, imgPharmaceutique3);
                    // maj des labels
                    this.miseEnPlace();
                }, new KeyValue(progress.progressProperty(), 1))
        );
        if (cycle == 1) {
            timelineUsinePharmaceutique3.setOnFinished(event -> {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsinePharmaceutiqueGrande(cycle - 1, vitesse, vitesse, usinePharmaceutique, progress, btnEncaisser);
            });
        }
        if (cycle == 0) {
            timelineUsinePharmaceutique3.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineUsinePharmaceutique3.setCycleCount(cycle);
        }
        timelineUsinePharmaceutique3.play();
    }
    /**
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine jouets enorme
     *
     * @param cycle             : 0 pour cycle infini
     * @param vitesse           : vitesse de l'usine en secondes
     * @param vitesseAjustement
     * @param usinePharmaceutique
     * @param progress          : barre de progress de la fabrication d'une marchandise
     * @param btnEncaisser
     */
    public void progressBarStartUsineParapharmaceutiqueEnorme(int cycle, double vitesse, double vitesseAjustement, UsinePharmaceutique usinePharmaceutique, ProgressBar progress, Button btnEncaisser) {
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(usinePharmaceutique.getEtatProgressUsine());
            etatBarreProgress = usinePharmaceutique.getEtatProgressUsine();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineUsinePharmaceutique4 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    System.out.println("cycle");
                    // rend le bouton Encaisser actif
                    btnEncaisser.setDisable(false);
                    // ajoute le nombre de marchandises fabriquées par l'usine
                    usinePharmaceutique.majUsine();
                    // maj le montant des gains en attente
                    recupMarchandises(usinePharmaceutique, btnEncaisserUsinePharmaceutique4, imgPharmaceutique4);
                    System.out.println("Production de marchandises dans " + usinePharmaceutique.getNom() + " terminée");
                    // met à jour les gains en cours ainsi que le bouton encaisser
                    usinePharmaceutique.majBtnEncaisser(btnEncaisser, imgPharmaceutique4);
                    // maj des labels
                    this.miseEnPlace();
                }, new KeyValue(progress.progressProperty(), 1))
        );
        if (cycle == 1) {
            timelineUsinePharmaceutique4.setOnFinished(event -> {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsineParapharmaceutiqueEnorme(cycle - 1, vitesse, vitesse, usinePharmaceutique, progress, btnEncaisser);
            });
        }
        if (cycle == 0) {
            timelineUsinePharmaceutique4.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineUsinePharmaceutique4.setCycleCount(cycle);
        }
        timelineUsinePharmaceutique4.play();
    }
}
