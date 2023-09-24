package com.example.mapetiteentreprise.controllersFx;

import com.example.mapetiteentreprise.Main;
import com.example.mapetiteentreprise.actions.Outils;
import com.example.mapetiteentreprise.bdd.ConnectionBdd;
import com.example.mapetiteentreprise.jeu.Jeu;
import com.example.mapetiteentreprise.jeu.UsineJouets;
import com.example.mapetiteentreprise.jeu.UsineTextile;
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
    private Timeline timelineUsineTextile1, timelineUsineTextile2, timelineUsineTextile3, timelineUsineTextile4,
            timelineUsineJouets1, timelineUsineJouets2, timelineUsineJouets3, timelineUsineJouets4;
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

        // barres de progression
        demarrageProgress();

        // affichage des barres de progression (mode dev)
        jeu.afficheProgression(paneProgress);

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
    public void acheterUsineJouetsPetite() {
        acheterUsineJouets(this.jeu.getJoueur().getUsineJouetsPetite(), progressJouets1, 1);
    }
    /**
     * Ajout d'une usine de jouets moyenne
     */
    public void acheterUsineJouetsMoyenne() {
        acheterUsineJouets(this.jeu.getJoueur().getUsineJouetsMoyenne(), progressJouets2, 1);
    }
    /**
     * Ajout d'une grande usine de jouets
     */
    public void acheterUsineJouetsGrande() {
        acheterUsineJouets(this.jeu.getJoueur().getUsineJouetsGrande(), progressJouets3, 1);
    }
    /**
     * Ajout d'une enorme usine de jouets
     */
    public void acheterUsineJouetsEnorme() {
        acheterUsineJouets(this.jeu.getJoueur().getUsineJouetsEnorme(), progressJouets4, 1);
    }
    /**
     * Methode d'achat général d'une usine de textile
     *
     * @param usineJouets    à acheter
     * @param progressJouets barre de progression de l'usine
     * @param numUsine        permet de savoir qu'elle usine pour lancer la bonne barre de progression
     */
    public void acheterUsineJouets(UsineJouets usineJouets, ProgressBar progressJouets, int numUsine) {
        if (!usineJouets.isMaxiNbUsines()) {
            BigDecimal montantAchat = usineJouets.getPrixUsine();
            if (jeu.getJoueur().acheter(montantAchat)) {
                usineJouets.ajoutUsine();
                System.out.println("Achat d'une " + usineJouets.getNom() + " : " + usineJouets.getNbUsines());
                // debloque l'usine de jouets si besoin
                if (usineJouets.getNbUsines() == 1) {
                    usineJouets.setUsineActive(1);
                    // demarre la barre de progression de l'usine
                    // recuperation de l'etat de la barre de progression pour la petite usine
                    double vitesseUsineJouets = usineJouets.getVitesseUsineTextile();
                    switch (numUsine) {
                        case 1:
                            this.progressBarStartUsineJouetsPetite(0, vitesseUsineJouets, vitesseUsineJouets, jeu.getJoueur().getUsineJouetsPetite(), progressJouets, btnEncaisserUsineJouets1);
                            break;
                        case 2:
                            this.progressBarStartUsineJouetsMoyenne(0, vitesseUsineJouets, vitesseUsineJouets, jeu.getJoueur().getUsineJouetsMoyenne(), progressJouets, btnEncaisserUsineJouets2);
                            break;
                        case 3:
                            this.progressBarStartUsineJouetsGrande(0, vitesseUsineJouets, vitesseUsineJouets, jeu.getJoueur().getUsineJouetsGrande(), progressJouets, btnEncaisserUsineJouets3);
                            break;
                        case 4:
                            this.progressBarStartUsineJouetsGrande(0, vitesseUsineJouets, vitesseUsineJouets, jeu.getJoueur().getUsineJouetsEnorme(), progressJouets, btnEncaisserUsineJouets4);
                            break;
                        default:
                            System.out.println("erreur d'usine");
                    }
                }
                // mise a jour des valeurs
                miseEnPlace();
            } else {
                System.out.println("Vous n'avez pas assez d'argent pour acheter une " + usineJouets.getNom() + " : ");
            }
        } else {
            System.out.println("Vous avez trop de " + usineJouets.getNom() + " : ");
        }
    }
    /**
     * Gere le clic sur le bouton encaisser usine de jouets petite
     */
    public void onBtnEncaisserUsineJouets1() {
        encaisserUsineJouets(this.jeu.getJoueur().getUsineJouetsPetite(), btnEncaisserUsineJouets1);
    }
    /**
     * Gere le clic sur le bouton encaisser usine de jouets moyenne
     */
    public void onBtnEncaisserUsineJouets2() {
        encaisserUsineJouets(this.jeu.getJoueur().getUsineJouetsMoyenne(), btnEncaisserUsineJouets2);
    }
    /**
     * Gere le clic sur le bouton encaisser usine de jouets grande
     */
    public void onBtnEncaisserUsineJouets3() {
        encaisserUsineJouets(this.jeu.getJoueur().getUsineJouetsGrande(), btnEncaisserUsineJouets3);
    }
    /**
     * Gere le clic sur le bouton encaisser usine de jouets enorme
     */
    public void onBtnEncaisserUsineJouets4() {
        encaisserUsineJouets(this.jeu.getJoueur().getUsineJouetsEnorme(), btnEncaisserUsineJouets4);
    }
    /**
     * Bouton qui permet d'encaisser l'argent des usines de textile
     *
     * @param usineJouets usine textile spécifiée
     */
    public void encaisserUsineJouets(UsineJouets usineJouets, Button btnEncaisserUsineJouets) {
        long nbMarchandisesUsineTextile = usineJouets.getNbMarchandises();

        usineJouets.setNbMarchandises(0); // raz le nombre de marchandises
        jeu.getJoueur().setArgent(usineJouets.getGainEnAttenteUsine().add(jeu.getJoueur().getArgent())); // met a jour les nouveaux gains

        String formattedGain = decimalFormat.format(usineJouets.getGainEnAttenteUsine()) + monnaie;
        // raz des gains en attente
        usineJouets.setGainEnAttenteUsine(BigDecimal.valueOf(0.00));

        System.out.println("Vous venez de récupérer le prix de " + nbMarchandisesUsineTextile + " de l'usine de jouets " + usineJouets.getNom() + ", vous avez gagné " + formattedGain + ".");

        btnEncaisserUsineJouets.setDisable(true);
        this.miseEnPlace();
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
        affichageContenuPanes(jeu.getJoueur().getUsineJouetsPetite(), paneJouets1, paneJouets1D, labelNbMarchandisesJouets1, labelNbUsineJouets1, labelTarifUsineJouets1, btnAchatUsineJouetsPetite, btnEncaisserUsineJouets1, imgJouets1, progressJouets1);
        affichageContenuPanes(jeu.getJoueur().getUsineJouetsMoyenne(), paneJouets2, paneJouets2D, labelNbMarchandisesJouets2, labelNbUsineJouets2, labelTarifUsineJouets2, btnAchatUsineJouetsMoyenne, btnEncaisserUsineJouets2, imgJouets2, progressJouets2);
        affichageContenuPanes(jeu.getJoueur().getUsineJouetsGrande(), paneJouets3, paneJouets3D, labelNbMarchandisesJouets3, labelNbUsineJouets3, labelTarifUsineJouets3, btnAchatUsineJouetsGrande, btnEncaisserUsineJouets3, imgJouets3, progressJouets3);
        affichageContenuPanes(jeu.getJoueur().getUsineJouetsEnorme(), paneJouets4, paneJouets4D, labelNbMarchandisesJouets4, labelNbUsineJouets4, labelTarifUsineJouets4, btnAchatUsineJouetsEnorme, btnEncaisserUsineJouets4, imgJouets4, progressJouets4);
        testBtnAchats();
    }

    /**
     * Active / desactive le bouton d'achat de vehicule de livraison pour chaque usine
     */
    public void testBtnAchats() {
        // usine de textile petite
        testBtnAchat(jeu.getJoueur().getUsineJouetsPetite(), btnAchatUsineJouets1, btnAchatUsineJouetsPetite);
        // usine de textile moyenne
        testBtnAchat(jeu.getJoueur().getUsineJouetsMoyenne(), btnAchatUsineJouets2, btnAchatUsineJouetsMoyenne);
        // usine de textile grande
        testBtnAchat(jeu.getJoueur().getUsineJouetsGrande(), btnAchatUsineJouets3, btnAchatUsineJouetsGrande);
        // usine de textile enorme
        testBtnAchat(jeu.getJoueur().getUsineJouetsEnorme(), btnAchatUsineJouets4, btnAchatUsineJouetsEnorme);
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
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsPetite().getUsineActive())) {
            btnEncaisserUsineJouets1.setVisible(true);
            btnAchatUsineJouets1.setVisible(false);
            System.out.println("Actif");
            this.jeu.getJoueur().getUsineJouetsPetite().majBtnEncaisser(btnEncaisserUsineJouets1, imgJouets1);
        } else {
            btnEncaisserUsineJouets1.setVisible(false);
            btnAchatUsineJouets1.setVisible(true);
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getUsineJouetsPetite().getPrixUsine())) {
                paneJouets1.setOpacity(1);
                paneJouets1.setDisable(false);
                btnAchatUsineJouets1.setDisable(false);
            } else {
                btnAchatUsineJouets1.setDisable(true);
                paneJouets1.setOpacity(0.8);
                paneJouets1.setDisable(true);
            }
            System.out.println("non actif");
        }
    }
    /**
     * gere l'affichage des boutons jouets usine moyenne
     */
    public void affichageBtnJouets2() {
        // boutons du usine textile
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsMoyenne().getUsineActive())) {
            btnEncaisserUsineJouets2.setVisible(true);
            btnAchatUsineJouets2.setVisible(false);
            System.out.println("Actif");
            this.jeu.getJoueur().getUsineJouetsMoyenne().majBtnEncaisser(btnEncaisserUsineJouets2, imgJouets2);
        } else {
            btnEncaisserUsineJouets2.setVisible(false);
            btnAchatUsineJouets2.setVisible(true);
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getUsineJouetsMoyenne().getPrixUsine())) {
                paneJouets2.setOpacity(1);
                paneJouets2.setDisable(false);
                btnAchatUsineJouets2.setDisable(false);
            } else {
                btnAchatUsineJouets2.setDisable(true);
                paneJouets2.setOpacity(0.8);
                paneJouets2.setDisable(true);
            }
            System.out.println("non actif");
        }
    }
    /**
     * gere l'affichage des boutons textile usine grande
     */
    public void affichageBtnJouets3() {
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsGrande().getUsineActive())) {
            btnEncaisserUsineJouets3.setVisible(true);
            btnAchatUsineJouets3.setVisible(false);
            System.out.println("Actif");
            this.jeu.getJoueur().getUsineJouetsGrande().majBtnEncaisser(btnEncaisserUsineJouets3, imgJouets3);
        } else {
            btnEncaisserUsineJouets3.setVisible(false);
            btnAchatUsineJouets3.setVisible(true);
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getUsineJouetsGrande().getPrixUsine())) {
                paneJouets3.setOpacity(1);
                paneJouets3.setDisable(false);
                btnAchatUsineJouets3.setDisable(false);
            } else {
                btnAchatUsineJouets3.setDisable(true);
                paneJouets3.setOpacity(0.8);
                paneJouets3.setDisable(true);
            }
            System.out.println("non actif");
        }
    }
    /**
     * gere l'affichage des boutons textile usine enorme
     */
    public void affichageBtnJouets4() {
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsEnorme().getUsineActive())) {
            btnEncaisserUsineJouets4.setVisible(true);
            btnAchatUsineJouets4.setVisible(false);
            System.out.println("Actif");
            this.jeu.getJoueur().getUsineJouetsEnorme().majBtnEncaisser(btnEncaisserUsineJouets4, imgJouets4);
        } else {
            btnEncaisserUsineJouets4.setVisible(false);
            btnAchatUsineJouets4.setVisible(true);
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getUsineJouetsEnorme().getPrixUsine())) {
                paneJouets4.setOpacity(1);
                paneJouets4.setDisable(false);
                btnAchatUsineJouets4.setDisable(false);
            } else {
                btnAchatUsineJouets4.setDisable(true);
                paneJouets4.setOpacity(0.8);
                paneJouets4.setDisable(true);
            }
            System.out.println("non actif");
        }
    }
    /**
     * Maj tous les labels necessaires
     */
    public void majLabels() {
        setLabelHaut();
        labelsUsineJouets1();
        labelsUsineJouets2();
        labelsUsineJouets3();
        labelsUsineJouets4();
    }

    public void setLabelHaut() {
        String formattedString = "En banque : " + decimalFormat.format(jeu.getJoueur().getArgent()) + monnaie;
        this.labelHaut.setText(formattedString);
    }

    /**
     * Labels de l'usine de textile 1
     */
    public void labelsUsineJouets1() {
        setNbUsineJouets(this.jeu.getJoueur().getUsineJouetsPetite(), labelNbUsineJouets1);
        setNbMarchandisesJouets(this.jeu.getJoueur().getUsineJouetsPetite(), labelNbMarchandisesJouets1);
        setLabelTarifUsineJouets(this.jeu.getJoueur().getUsineJouetsPetite(), labelTarifUsineJouets1);
        setLabelTarifUsineJouets(this.jeu.getJoueur().getUsineJouetsPetite(), btnAchatUsineJouets1);
    }
    /**
     * Labels de l'usine de textile 2
     */
    public void labelsUsineJouets2() {
        setNbUsineJouets(this.jeu.getJoueur().getUsineJouetsMoyenne(), labelNbUsineJouets2);
        setNbMarchandisesJouets(this.jeu.getJoueur().getUsineJouetsMoyenne(), labelNbMarchandisesJouets2);
        setLabelTarifUsineJouets(this.jeu.getJoueur().getUsineJouetsMoyenne(), labelTarifUsineJouets2);
        setLabelTarifUsineJouets(this.jeu.getJoueur().getUsineJouetsMoyenne(), btnAchatUsineJouets2);
    }
    /**
     * Labels de l'usine de textile 3
     */
    public void labelsUsineJouets3() {
        setNbUsineJouets(this.jeu.getJoueur().getUsineJouetsGrande(), labelNbUsineJouets3);
        setNbMarchandisesJouets(this.jeu.getJoueur().getUsineJouetsGrande(), labelNbMarchandisesJouets3);
        setLabelTarifUsineJouets(this.jeu.getJoueur().getUsineJouetsGrande(), labelTarifUsineJouets3);
        setLabelTarifUsineJouets(this.jeu.getJoueur().getUsineJouetsGrande(), btnAchatUsineJouets3);
    }
    /**
     * Labels de l'usine de textile 4
     */
    public void labelsUsineJouets4() {
        setNbUsineJouets(this.jeu.getJoueur().getUsineJouetsEnorme(), labelNbUsineJouets4);
        setNbMarchandisesJouets(this.jeu.getJoueur().getUsineJouetsEnorme(), labelNbMarchandisesJouets4);
        setLabelTarifUsineJouets(this.jeu.getJoueur().getUsineJouetsEnorme(), labelTarifUsineJouets4);
        setLabelTarifUsineJouets(this.jeu.getJoueur().getUsineJouetsEnorme(), btnAchatUsineJouets4);
    }
    /**
     * initialise le nombre d'usines en cours ainsi que le nombre d'usines maximum
     */
    public void setNbUsineJouets(UsineJouets usineJouets, Label labelUsine) {
        String formattedString = usineJouets.setNbUsines();
        labelUsine.setText(formattedString);
    }

    /**
     * Centre les boutons d'achat des usines
     * Platform.runLater permet d'attendre le chargement des fenetres afin de récupérer les valeurs des boutons
     */
    public void centrageBoutons(){
        Platform.runLater(() -> {
            Outils.centrageBouton(btnAchatUsineJouets1, paneJouets1D.getWidth());
            Outils.centrageBouton(btnAchatUsineJouets2, paneJouets2D.getWidth());
            Outils.centrageBouton(btnAchatUsineJouets3, paneJouets3D.getWidth());
            Outils.centrageBouton(btnAchatUsineJouets4, paneJouets4D.getWidth());
        });
    }
    /**
     * initialise le nombre de marchandises produites
     */
    public void setNbMarchandisesJouets(UsineJouets usineJouets, Label labelUsine) {
        labelUsine.setText(usineJouets.getNbMarchandises() + "");
    }

    /**
     * Inscrit le prix d'achat d'une usine dans le label
     */
    public void setLabelTarifUsineJouets(UsineJouets usineJouets, Label labelUsine) {
        BigDecimal prixUsineJouets = usineJouets.getPrixUsine();
        String nomVehicule = usineJouets.getNom();
        String formattedString = "Acheter " + nomVehicule + " : " + decimalFormat.format(prixUsineJouets) + monnaie;
        labelUsine.setText(formattedString);
    }

    /**
     * Inscrit le prix d'achat d'une usine sur le bouton
     */
    public void setLabelTarifUsineJouets(UsineJouets usineJouets, Button btnAchatUsine) {
        BigDecimal prixUsineJouets = usineJouets.getPrixUsine();
        String nomVehicule = usineJouets.getNom();
        String formattedString = "Acheter " + nomVehicule + " : " + decimalFormat.format(prixUsineJouets) + monnaie;
        btnAchatUsine.setText(formattedString);
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


    /**
     * Demarre les usines lorsqu'elles sont actives
     */
    public void demarrageUsinesJouets() {
        // usine de jouets petite
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsPetite().getUsineActive())) {
            System.out.println("Demarrage " + jeu.getJoueur().getUsineJouetsPetite().getNom());
            double vitesseUsineJouets1 = jeu.getJoueur().getUsineJouetsPetite().getVitesseUsineTextile() - (jeu.getJoueur().getUsineJouetsPetite().getVitesseUsineTextile() * jeu.getJoueur().getUsineJouetsPetite().getEtatProgressUsine());
            this.progressBarStartUsineJouetsPetite(1, jeu.getJoueur().getUsineJouetsPetite().getVitesseUsineTextile(), vitesseUsineJouets1, jeu.getJoueur().getUsineJouetsPetite(), progressJouets1, btnEncaisserUsineJouets1);
        }
        // usine de jouets moyenne
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsMoyenne().getUsineActive())) {
            System.out.println("Demarrage " + jeu.getJoueur().getUsineJouetsMoyenne().getNom());
            double vitesseUsineJouets2 = jeu.getJoueur().getUsineJouetsMoyenne().getVitesseUsineTextile() - (jeu.getJoueur().getUsineJouetsMoyenne().getVitesseUsineTextile() * jeu.getJoueur().getUsineJouetsMoyenne().getEtatProgressUsine());
            this.progressBarStartUsineJouetsMoyenne(1, jeu.getJoueur().getUsineJouetsMoyenne().getVitesseUsineTextile(), vitesseUsineJouets2, jeu.getJoueur().getUsineJouetsMoyenne(), progressJouets2, btnEncaisserUsineJouets2);
        }
        // usine de jouets grande
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsGrande().getUsineActive())) {
            System.out.println("Demarrage " + jeu.getJoueur().getUsineJouetsGrande().getNom());
            double vitesseUsineJouets3 = jeu.getJoueur().getUsineJouetsGrande().getVitesseUsineTextile() - (jeu.getJoueur().getUsineJouetsGrande().getVitesseUsineTextile() * jeu.getJoueur().getUsineJouetsGrande().getEtatProgressUsine());
            this.progressBarStartUsineJouetsGrande(1, jeu.getJoueur().getUsineJouetsGrande().getVitesseUsineTextile(), vitesseUsineJouets3, jeu.getJoueur().getUsineJouetsGrande(), progressJouets3, btnEncaisserUsineJouets3);
        }
        // usine de jouets enorme
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsEnorme().getUsineActive())) {
            System.out.println("Demarrage " + jeu.getJoueur().getUsineJouetsEnorme().getNom());
            double vitesseUsineJouets4 = jeu.getJoueur().getUsineJouetsEnorme().getVitesseUsineTextile() - (jeu.getJoueur().getUsineJouetsEnorme().getVitesseUsineTextile() * jeu.getJoueur().getUsineJouetsEnorme().getEtatProgressUsine());
            this.progressBarStartUsineJouetsEnorme(1, jeu.getJoueur().getUsineJouetsEnorme().getVitesseUsineTextile(), vitesseUsineJouets4, jeu.getJoueur().getUsineJouetsEnorme(), progressJouets4, btnEncaisserUsineJouets4);
        }
    }

    /**
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine jouets petite
     *
     * @param cycle             : 0 pour cycle infini
     * @param vitesse           : vitesse de l'usine en secondes
     * @param vitesseAjustement
     * @param usineJouets
     * @param progress          : barre de progress de la fabrication d'une marchandise
     * @param btnEncaisser
     */
    public void progressBarStartUsineJouetsPetite(int cycle, double vitesse, double vitesseAjustement, UsineJouets usineJouets, ProgressBar progress, Button btnEncaisser) {
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(usineJouets.getEtatProgressUsine());
            etatBarreProgress = usineJouets.getEtatProgressUsine();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineUsineJouets1 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    System.out.println("cycle");
                    // rend le bouton Encaisser actif
                    btnEncaisser.setDisable(false);
                    // ajoute le nombre de marchandises fabriquées par l'usine
                    usineJouets.majUsine();
                    // maj le montant des gains en attente
                    recupMarchandises(usineJouets, btnEncaisserUsineJouets1, imgJouets1);
                    System.out.println("Production de marchandises dans " + usineJouets.getNom() + " terminée");
                    // met à jour les gains en cours ainsi que le bouton encaisser
                    usineJouets.majBtnEncaisser(btnEncaisser, imgJouets1);
                    // maj des labels
                    this.miseEnPlace();
                }, new KeyValue(progress.progressProperty(), 1))
        );
        if (cycle == 1) {
            timelineUsineJouets1.setOnFinished(event -> {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsineJouetsPetite(cycle - 1, vitesse, vitesse, usineJouets, progress, btnEncaisser);
            });
        }
        if (cycle == 0) {
            timelineUsineJouets1.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineUsineJouets1.setCycleCount(cycle);
        }
        timelineUsineJouets1.play();
    }
    /**
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine jouets moyenne
     *
     * @param cycle             : 0 pour cycle infini
     * @param vitesse           : vitesse de l'usine en secondes
     * @param vitesseAjustement
     * @param usineJouets
     * @param progress          : barre de progress de la fabrication d'une marchandise
     * @param btnEncaisser
     */
    public void progressBarStartUsineJouetsMoyenne(int cycle, double vitesse, double vitesseAjustement, UsineJouets usineJouets, ProgressBar progress, Button btnEncaisser) {
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(usineJouets.getEtatProgressUsine());
            etatBarreProgress = usineJouets.getEtatProgressUsine();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineUsineJouets2 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    System.out.println("cycle");
                    // rend le bouton Encaisser actif
                    btnEncaisser.setDisable(false);
                    // ajoute le nombre de marchandises fabriquées par l'usine
                    usineJouets.majUsine();
                    // maj le montant des gains en attente
                    recupMarchandises(usineJouets, btnEncaisserUsineJouets2, imgJouets2);
                    System.out.println("Production de marchandises dans " + usineJouets.getNom() + " terminée");
                    // met à jour les gains en cours ainsi que le bouton encaisser
                    usineJouets.majBtnEncaisser(btnEncaisser, imgJouets2);
                    // maj des labels
                    this.miseEnPlace();
                }, new KeyValue(progress.progressProperty(), 1))
        );
        if (cycle == 1) {
            timelineUsineJouets2.setOnFinished(event -> {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsineJouetsMoyenne(cycle - 1, vitesse, vitesse, usineJouets, progress, btnEncaisser);
            });
        }
        if (cycle == 0) {
            timelineUsineJouets2.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineUsineJouets2.setCycleCount(cycle);
        }
        timelineUsineJouets2.play();
    }
    /**
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine jouets grande
     *
     * @param cycle             : 0 pour cycle infini
     * @param vitesse           : vitesse de l'usine en secondes
     * @param vitesseAjustement
     * @param usineJouets
     * @param progress          : barre de progress de la fabrication d'une marchandise
     * @param btnEncaisser
     */
    public void progressBarStartUsineJouetsGrande(int cycle, double vitesse, double vitesseAjustement, UsineJouets usineJouets, ProgressBar progress, Button btnEncaisser) {
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(usineJouets.getEtatProgressUsine());
            etatBarreProgress = usineJouets.getEtatProgressUsine();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineUsineJouets3 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    System.out.println("cycle");
                    // rend le bouton Encaisser actif
                    btnEncaisser.setDisable(false);
                    // ajoute le nombre de marchandises fabriquées par l'usine
                    usineJouets.majUsine();
                    // maj le montant des gains en attente
                    recupMarchandises(usineJouets, btnEncaisserUsineJouets3, imgJouets3);
                    System.out.println("Production de marchandises dans " + usineJouets.getNom() + " terminée");
                    // met à jour les gains en cours ainsi que le bouton encaisser
                    usineJouets.majBtnEncaisser(btnEncaisser, imgJouets3);
                    // maj des labels
                    this.miseEnPlace();
                }, new KeyValue(progress.progressProperty(), 1))
        );
        if (cycle == 1) {
            timelineUsineJouets3.setOnFinished(event -> {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsineJouetsGrande(cycle - 1, vitesse, vitesse, usineJouets, progress, btnEncaisser);
            });
        }
        if (cycle == 0) {
            timelineUsineJouets3.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineUsineJouets3.setCycleCount(cycle);
        }
        timelineUsineJouets3.play();
    }
    /**
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine jouets enorme
     *
     * @param cycle             : 0 pour cycle infini
     * @param vitesse           : vitesse de l'usine en secondes
     * @param vitesseAjustement
     * @param usineJouets
     * @param progress          : barre de progress de la fabrication d'une marchandise
     * @param btnEncaisser
     */
    public void progressBarStartUsineJouetsEnorme(int cycle, double vitesse, double vitesseAjustement, UsineJouets usineJouets, ProgressBar progress, Button btnEncaisser) {
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(usineJouets.getEtatProgressUsine());
            etatBarreProgress = usineJouets.getEtatProgressUsine();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineUsineJouets4 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    System.out.println("cycle");
                    // rend le bouton Encaisser actif
                    btnEncaisser.setDisable(false);
                    // ajoute le nombre de marchandises fabriquées par l'usine
                    usineJouets.majUsine();
                    // maj le montant des gains en attente
                    recupMarchandises(usineJouets, btnEncaisserUsineJouets4, imgJouets4);
                    System.out.println("Production de marchandises dans " + usineJouets.getNom() + " terminée");
                    // met à jour les gains en cours ainsi que le bouton encaisser
                    usineJouets.majBtnEncaisser(btnEncaisser, imgJouets4);
                    // maj des labels
                    this.miseEnPlace();
                }, new KeyValue(progress.progressProperty(), 1))
        );
        if (cycle == 1) {
            timelineUsineJouets4.setOnFinished(event -> {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsineJouetsEnorme(cycle - 1, vitesse, vitesse, usineJouets, progress, btnEncaisser);
            });
        }
        if (cycle == 0) {
            timelineUsineJouets4.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineUsineJouets4.setCycleCount(cycle);
        }
        timelineUsineJouets4.play();
    }
}
