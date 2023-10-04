package com.example.mapetiteentreprise.controllersFx;

import com.example.mapetiteentreprise.Main;
import com.example.mapetiteentreprise.actions.Outils;
import com.example.mapetiteentreprise.bdd.ConnectionBdd;
import com.example.mapetiteentreprise.jeu.Jeu;
import com.example.mapetiteentreprise.jeu.UsineAgroAlimentaire;
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

public class UsinesAgroAlimentaireController {
    private final String monnaie = " €";
    private final String separationTexte = System.getProperty("line.separator");
    private final DecimalFormat decimalFormat = Outils.getDecimalFormatWithSpaceSeparator();
    @FXML
    private Label labelHaut,
            labelNbUsineAgroAlimentaire1, labelNbUsineAgroAlimentaire2, labelNbUsineAgroAlimentaire3, labelNbUsineAgroAlimentaire4,
            labelTarifUsineAgroAlimentaire1, labelTarifUsineAgroAlimentaire2, labelTarifUsineAgroAlimentaire3, labelTarifUsineAgroAlimentaire4,
            labelNbMarchandisesAgroAlimentaire1, labelNbMarchandisesAgroAlimentaire2, labelNbMarchandisesAgroAlimentaire3, labelNbMarchandisesAgroAlimentaire4;
    @FXML
    private Button btnAchatUsineAgroAlimentaire1, btnAchatUsineAgroAlimentaire2, btnAchatUsineAgroAlimentaire3, btnAchatUsineAgroAlimentaire4,
            btnAchatUsineAgroAlimentairePetite, btnAchatUsineAgroAlimentaireMoyenne, btnAchatUsineAgroAlimentaireGrande, btnAchatUsineAgroAlimentaireEnorme,
            btnEncaisserUsineAgroAlimentaire1, btnEncaisserUsineAgroAlimentaire2, btnEncaisserUsineAgroAlimentaire3, btnEncaisserUsineAgroAlimentaire4;
    @FXML
    private ProgressBar progressOeufs, progressBC, progressBF, progressSa, progressCo,
            progressScooter, progressCamionette, progressPetitCamion, progressPoidsLourd, progressAvion,
            progressTextile1, progressTextile2, progressTextile3, progressTextile4,
            progressJouets1, progressJouets2, progressJouets3, progressJouets4,
            progressPharmaceutique1, progressPharmaceutique2, progressPharmaceutique3, progressPharmaceutique4,
            progressAgroAlimentaire1, progressAgroAlimentaire2, progressAgroAlimentaire3, progressAgroAlimentaire4;
    private Timeline timelineUsineAgroAlimentaire1, timelineUsineAgroAlimentaire2, timelineUsineAgroAlimentaire3, timelineUsineAgroAlimentaire4;
    private ConnectionBdd connectionBdd = new ConnectionBdd();
    @FXML
    private Pane paneProgress, paneAgroAlimentaire1, paneAgroAlimentaire2, paneAgroAlimentaire3, paneAgroAlimentaire4,
            paneAgroAlimentaire1D, paneAgroAlimentaire2D, paneAgroAlimentaire3D, paneAgroAlimentaire4D,
            panePrincipal;
    @FXML
    private ImageView imgAgroAlimentaire1, imgAgroAlimentaire2, imgAgroAlimentaire3, imgAgroAlimentaire4;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Jeu jeu;
    public void demarrer(Jeu jeu){
        // Recuperation du jeu
        this.jeu = jeu;

        // affichage des barres de progression (mode dev)
        jeu.afficheProgression(paneProgress);

        // recuperation des marchandises
        recupMarchandisesToutes();

        // majLabels et boutons
        miseEnPlace();

        demarrageProgress();

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
     * Ajout d'une petite usine
     */
    public void acheterUsineAgroAlimentairePetite() {
        acheterUsineAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentairePetite(), progressAgroAlimentaire1, 1);
    }

    /**
     * Ajout d'une petite usine
     */
    public void acheterUsineAgroAlimentaireMoyenne() {
        acheterUsineAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireMoyenne(), progressAgroAlimentaire2, 2);
    }

    /**
     * Ajout d'une petite usine
     */
    public void acheterUsineAgroAlimentaireGrande() {
        acheterUsineAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireGrande(), progressAgroAlimentaire3, 3);
    }

    /**
     * Ajout d'une petite usine
     */
    public void acheterUsineAgroAlimentaireEnorme() {
        acheterUsineAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireEnorme(), progressAgroAlimentaire4, 4);
    }

    /**
     * Methode d'achat général d'une usine de textile
     *
     * @param usineAgroAlimentaire    à acheter
     * @param progressAgroAlimentaire barre de progression de l'usine
     * @param numUsine        permet de savoir qu'elle usine pour lancer la bonne barre de progression
     */
    public void acheterUsineAgroAlimentaire(UsineAgroAlimentaire usineAgroAlimentaire, ProgressBar progressAgroAlimentaire, int numUsine) {
        if (!usineAgroAlimentaire.isMaxiNbUsines()) {
            BigDecimal montantAchat = usineAgroAlimentaire.getPrixUsine();
            if (jeu.getJoueur().acheter(montantAchat)) {
                usineAgroAlimentaire.ajoutUsine();
                System.out.println("Achat d'une " + usineAgroAlimentaire.getNom() + " : " + usineAgroAlimentaire.getNbUsines());
                // debloque le scooter si besoin
                if (usineAgroAlimentaire.getNbUsines() == 1) {
                    usineAgroAlimentaire.setUsineActive(1);
                    // demarre la barre de progression de l'usine
                    // recuperation de l'etat de la barre de progression pour la petite usine
                    double vitesseUsineTextile = usineAgroAlimentaire.getVitesseUsine();
                    switch (numUsine) {
                        case 1:
                            this.progressBarStartUsineAgroAlimentairePetite(0, vitesseUsineTextile, vitesseUsineTextile, jeu.getJoueur().getUsineAgroAlimentairePetite(), progressAgroAlimentaire, btnEncaisserUsineAgroAlimentaire1);
                            break;
                        case 2:
                            this.progressBarStartUsineAgroAlimentaireMoyenne(0, vitesseUsineTextile, vitesseUsineTextile, jeu.getJoueur().getUsineAgroAlimentaireMoyenne(), progressAgroAlimentaire, btnEncaisserUsineAgroAlimentaire2);
                            break;
                        case 3:
                            this.progressBarStartUsineAgroAlimentaireGrande(0, vitesseUsineTextile, vitesseUsineTextile, jeu.getJoueur().getUsineAgroAlimentaireGrande(), progressAgroAlimentaire, btnEncaisserUsineAgroAlimentaire3);
                            break;
                        case 4:
                            this.progressBarStartUsineAgroAlimentaireEnorme(0, vitesseUsineTextile, vitesseUsineTextile, jeu.getJoueur().getUsineAgroAlimentaireEnorme(), progressAgroAlimentaire, btnEncaisserUsineAgroAlimentaire4);
                            break;
                        default:
                            System.out.println("erreur d'usine");
                    }
                }
                // mise a jour des valeurs
                miseEnPlace();
            } else {
                System.out.println("Vous n'avez pas assez d'argent pour acheter une " + usineAgroAlimentaire.getNom() + " : ");
            }
        } else {
            System.out.println("Vous avez trop de " + usineAgroAlimentaire.getNom() + " : ");
        }
    }

    /**
     * Gere le clic sur le bouton encaisser usine de textile petite
     */
    public void onBtnEncaisserUsineAgroAlimentaire1() {
        encaisserUsineAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentairePetite(), btnEncaisserUsineAgroAlimentaire1);
    }

    /**
     * Gere le clic sur le bouton encaisser usine de textile moyenne
     */
    public void onBtnEncaisserUsineAgroAlimentaire2() {
        encaisserUsineAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireMoyenne(), btnEncaisserUsineAgroAlimentaire2);
    }

    /**
     * Gere le clic sur le bouton encaisser usine de textile grande
     */
    public void onBtnEncaisserUsineAgroAlimentaire3() {
        encaisserUsineAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireGrande(), btnEncaisserUsineAgroAlimentaire3);
    }

    /**
     * Gere le clic sur le bouton encaisser usine de textile enorme
     */
    public void onBtnEncaisserUsineAgroAlimentaire4() {
        encaisserUsineAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireEnorme(), btnEncaisserUsineAgroAlimentaire4);
    }

    /**
     * Bouton qui permet d'encaisser l'argent des usines agro alimentaire
     *
     * @param usineAgroAlimentaire usine textile spécifiée
     */
    public void encaisserUsineAgroAlimentaire(UsineAgroAlimentaire usineAgroAlimentaire, Button btnEncaisserUsineAgroAlimentaire) {
        long nbMarchandisesUsineTextile = usineAgroAlimentaire.getNbMarchandises();

        usineAgroAlimentaire.setNbMarchandises(0); // raz le nombre de marchandises
        jeu.getJoueur().setArgent(usineAgroAlimentaire.getGainEnAttenteUsine().add(jeu.getJoueur().getArgent())); // met a jour les nouveaux gains

        String formattedGain = decimalFormat.format(usineAgroAlimentaire.getGainEnAttenteUsine()) + monnaie;
        // raz des gains en attente
        usineAgroAlimentaire.setGainEnAttenteUsine(BigDecimal.valueOf(0.00));

        System.out.println("Vous venez de récupérer le prix de " + nbMarchandisesUsineTextile + " de l'usine de textile " + usineAgroAlimentaire.getNom() + ", vous avez gagné " + formattedGain + ".");

        btnEncaisserUsineAgroAlimentaire.setDisable(true);
        this.miseEnPlace();
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
        affichageBtnAgroAlimentaire1();
        affichageBtnAgroAlimentaire2();
        affichageBtnAgroAlimentaire3();
        affichageBtnAgroAlimentaire4();
        affichageContenuPanes(jeu.getJoueur().getUsineAgroAlimentairePetite(), paneAgroAlimentaire1, paneAgroAlimentaire1D, labelNbMarchandisesAgroAlimentaire1, labelNbUsineAgroAlimentaire1, labelTarifUsineAgroAlimentaire1, btnAchatUsineAgroAlimentairePetite, btnEncaisserUsineAgroAlimentaire1, imgAgroAlimentaire1, progressTextile1);
        affichageContenuPanes(jeu.getJoueur().getUsineAgroAlimentaireMoyenne(), paneAgroAlimentaire2, paneAgroAlimentaire2D, labelNbMarchandisesAgroAlimentaire2, labelNbUsineAgroAlimentaire2, labelTarifUsineAgroAlimentaire2, btnAchatUsineAgroAlimentaireMoyenne, btnEncaisserUsineAgroAlimentaire2, imgAgroAlimentaire2, progressTextile2);
        affichageContenuPanes(jeu.getJoueur().getUsineAgroAlimentaireGrande(), paneAgroAlimentaire3, paneAgroAlimentaire3D, labelNbMarchandisesAgroAlimentaire3, labelNbUsineAgroAlimentaire3, labelTarifUsineAgroAlimentaire3, btnAchatUsineAgroAlimentaireGrande, btnEncaisserUsineAgroAlimentaire3, imgAgroAlimentaire3, progressTextile3);
        affichageContenuPanes(jeu.getJoueur().getUsineAgroAlimentaireEnorme(), paneAgroAlimentaire4, paneAgroAlimentaire4D, labelNbMarchandisesAgroAlimentaire4, labelNbUsineAgroAlimentaire4, labelTarifUsineAgroAlimentaire4, btnAchatUsineAgroAlimentaireEnorme, btnEncaisserUsineAgroAlimentaire4, imgAgroAlimentaire4, progressTextile4);
        testBtnAchats();
    }

    /**
     * Active / desactive le bouton d'achat de vehicule de livraison pour chaque usine
     */
    public void testBtnAchats() {
        // usine de textile petite
        testBtnAchat(jeu.getJoueur().getUsineAgroAlimentairePetite(), btnAchatUsineAgroAlimentaire1, btnAchatUsineAgroAlimentairePetite);
        // usine de textile moyenne
        testBtnAchat(jeu.getJoueur().getUsineAgroAlimentaireMoyenne(), btnAchatUsineAgroAlimentaire2, btnAchatUsineAgroAlimentaireMoyenne);
        // usine de textile grande
        testBtnAchat(jeu.getJoueur().getUsineAgroAlimentaireGrande(), btnAchatUsineAgroAlimentaire3, btnAchatUsineAgroAlimentaireGrande);
        // usine de textile enorme
        testBtnAchat(jeu.getJoueur().getUsineAgroAlimentaireEnorme(), btnAchatUsineAgroAlimentaire4, btnAchatUsineAgroAlimentaireEnorme);
    }
    /**
     * Gere l'affichage ou non du contenu des panes pour afficher / masquer l'usine au demarrage
     * @param usineAgroAlimentaire usine à afficher / masquer
     * @param pane
     * @param labelNb
     * @param labelTarif
     * @param labelTitre
     * @param btnEncaisser
     * @param btnPlus
     * @param barreProgress
     */
    public void affichageContenuPanes(UsineAgroAlimentaire usineAgroAlimentaire, Pane pane, Pane paneD, Label labelTitre, Label labelNb, Label labelTarif, Button btnPlus, Button btnEncaisser, ImageView imgAgroAlimentaire, ProgressBar barreProgress){
        if (usineAgroAlimentaire.getUsineActive() == 1){
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
     * Active / desactive le bouton d'achat de vehicule de livraison pour l'usine et les boutons donnée en paramètres
     * @param usineAgroAlimentaire usine à tester
     * @param btnAchatUsine bouton de l'achat d'une usine (qui active l'usine en question)
     * @param btnAjoutUsine bouton de l'ajout d'une usine (qui ajoute une usine quand usine déja activée)
     */
    public void testBtnAchat(UsineAgroAlimentaire usineAgroAlimentaire, Button btnAchatUsine, Button btnAjoutUsine){
        if (jeu.getJoueur().isArgent(usineAgroAlimentaire.getPrixUsine()) && !usineAgroAlimentaire.isMaxiNbUsines()) {
            btnAchatUsine.setDisable(false);
            btnAjoutUsine.setDisable(false);
        } else {
            btnAchatUsine.setDisable(true);
            btnAjoutUsine.setDisable(true);
        }
    }
    /**
     * gere l'affichage des boutons jouets petite usine
     */
    public void affichageBtnAgroAlimentaire1() {
        // boutons du usine agro alimentaire
        if (Outils.isActif(jeu.getJoueur().getUsineAgroAlimentairePetite().getUsineActive())) {
            btnEncaisserUsineAgroAlimentaire1.setVisible(true);
            btnAchatUsineAgroAlimentaire1.setVisible(false);
            System.out.println("Actif");
            this.jeu.getJoueur().getUsineAgroAlimentairePetite().majBtnEncaisser(btnEncaisserUsineAgroAlimentaire1, imgAgroAlimentaire1);
        } else {
            btnEncaisserUsineAgroAlimentaire1.setVisible(false);
            btnAchatUsineAgroAlimentaire1.setVisible(true);
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getUsineAgroAlimentairePetite().getPrixUsine())) {
                paneAgroAlimentaire1.setOpacity(1);
                paneAgroAlimentaire1.setDisable(false);
                btnAchatUsineAgroAlimentaire1.setDisable(false);
            } else {
                btnAchatUsineAgroAlimentaire1.setDisable(true);
                paneAgroAlimentaire1.setOpacity(0.8);
                paneAgroAlimentaire1.setDisable(true);
            }
            System.out.println("non actif");
        }
    }
    /**
     * gere l'affichage des boutons textile usine moyenne
     */
    public void affichageBtnAgroAlimentaire2() {
        // boutons du usine agro alimentaire
        if (Outils.isActif(jeu.getJoueur().getUsineAgroAlimentaireMoyenne().getUsineActive())) {
            btnEncaisserUsineAgroAlimentaire2.setVisible(true);
            btnAchatUsineAgroAlimentaire2.setVisible(false);
            System.out.println("Actif");
            this.jeu.getJoueur().getUsineAgroAlimentaireMoyenne().majBtnEncaisser(btnEncaisserUsineAgroAlimentaire2, imgAgroAlimentaire2);
        } else {
            btnEncaisserUsineAgroAlimentaire2.setVisible(false);
            btnAchatUsineAgroAlimentaire2.setVisible(true);
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getUsineAgroAlimentaireMoyenne().getPrixUsine())) {
                paneAgroAlimentaire2.setOpacity(1);
                paneAgroAlimentaire2.setDisable(false);
                btnAchatUsineAgroAlimentaire2.setDisable(false);
            } else {
                btnAchatUsineAgroAlimentaire2.setDisable(true);
                paneAgroAlimentaire2.setOpacity(0.8);
                paneAgroAlimentaire2.setDisable(true);
            }
            System.out.println("non actif");
        }
    }
    /**
     * gere l'affichage des boutons agro alimentaire usine grande
     */
    public void affichageBtnAgroAlimentaire3() {
        if (Outils.isActif(jeu.getJoueur().getUsineAgroAlimentaireGrande().getUsineActive())) {
            btnEncaisserUsineAgroAlimentaire3.setVisible(true);
            btnAchatUsineAgroAlimentaire3.setVisible(false);
            System.out.println("Actif");
            this.jeu.getJoueur().getUsineAgroAlimentaireGrande().majBtnEncaisser(btnEncaisserUsineAgroAlimentaire3, imgAgroAlimentaire3);
        } else {
            btnEncaisserUsineAgroAlimentaire3.setVisible(false);
            btnAchatUsineAgroAlimentaire3.setVisible(true);
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getUsineAgroAlimentaireGrande().getPrixUsine())) {
                paneAgroAlimentaire3.setOpacity(1);
                paneAgroAlimentaire3.setDisable(false);
                btnAchatUsineAgroAlimentaire3.setDisable(false);
            } else {
                btnAchatUsineAgroAlimentaire3.setDisable(true);
                paneAgroAlimentaire3.setOpacity(0.8);
                paneAgroAlimentaire3.setDisable(true);
            }
            System.out.println("non actif");
        }
    }
    /**
     * gere l'affichage des boutons agro alimentaire usine enorme
     */
    public void affichageBtnAgroAlimentaire4() {
        if (Outils.isActif(jeu.getJoueur().getUsineAgroAlimentaireEnorme().getUsineActive())) {
            btnEncaisserUsineAgroAlimentaire4.setVisible(true);
            btnAchatUsineAgroAlimentaire4.setVisible(false);
            System.out.println("Actif");
            this.jeu.getJoueur().getUsineAgroAlimentaireEnorme().majBtnEncaisser(btnEncaisserUsineAgroAlimentaire4, imgAgroAlimentaire4);
        } else {
            btnEncaisserUsineAgroAlimentaire4.setVisible(false);
            btnAchatUsineAgroAlimentaire4.setVisible(true);
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getUsineAgroAlimentaireEnorme().getPrixUsine())) {
                paneAgroAlimentaire4.setOpacity(1);
                paneAgroAlimentaire4.setDisable(false);
                btnAchatUsineAgroAlimentaire4.setDisable(false);
            } else {
                btnAchatUsineAgroAlimentaire4.setDisable(true);
                paneAgroAlimentaire4.setOpacity(0.8);
                paneAgroAlimentaire4.setDisable(true);
            }
            System.out.println("non actif");
        }
    }

    public void recupMarchandisesToutes(){
        recupMarchandises(this.jeu.getJoueur().getUsineAgroAlimentairePetite(), btnEncaisserUsineAgroAlimentaire1, imgAgroAlimentaire1);
        recupMarchandises(this.jeu.getJoueur().getUsineAgroAlimentaireMoyenne(), btnEncaisserUsineAgroAlimentaire2, imgAgroAlimentaire2);
        recupMarchandises(this.jeu.getJoueur().getUsineAgroAlimentaireGrande(), btnEncaisserUsineAgroAlimentaire3, imgAgroAlimentaire3);
        recupMarchandises(this.jeu.getJoueur().getUsineAgroAlimentaireEnorme(), btnEncaisserUsineAgroAlimentaire4, imgAgroAlimentaire4);
    }
    /**
     * Maj les gains en attente à chaque fin de progression
     * @param usine
     * @param boutonEncaisser
     */
    public void recupMarchandises(UsineAgroAlimentaire usine, Button boutonEncaisser, ImageView imageView) {
        // petite usine de textile
        // maj des gains en attente
        usine.setGainEnAttenteUsine(usine.majGainsEnAttente());
        // maj du bouton
        usine.majBtnEncaisser(boutonEncaisser, imageView);

    }

    /**
     * Maj tous les labels necessaires
     */
    public void majLabels() {
        setLabelHaut();
        labelsUsineTextile1();
        labelsUsineTextile2();
        labelsUsineTextile3();
        labelsUsineTextile4();
    }

    public void setLabelHaut() {
        String formattedString = "En banque : " + decimalFormat.format(jeu.getJoueur().getArgent()) + monnaie;
        this.labelHaut.setText(formattedString);
    }

    /**
     * Labels de l'usine de textile 1
     */
    public void labelsUsineTextile1() {
        setNbUsineAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentairePetite(), labelNbUsineAgroAlimentaire1);
        setNbMarchandisesAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireMoyenne(), labelNbMarchandisesAgroAlimentaire1);
        setLabelTarifUsineAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireGrande(), labelTarifUsineAgroAlimentaire1);
        setLabelTarifUsineAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireEnorme(), btnAchatUsineAgroAlimentaire1);
    }
    /**
     * Labels de l'usine de textile 2
     */
    public void labelsUsineTextile2() {
        setNbUsineAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireMoyenne(), labelNbUsineAgroAlimentaire2);
        setNbMarchandisesAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireMoyenne(), labelNbMarchandisesAgroAlimentaire2);
        setLabelTarifUsineAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireMoyenne(), labelTarifUsineAgroAlimentaire2);
        setLabelTarifUsineAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireMoyenne(), btnAchatUsineAgroAlimentaire2);
    }
    /**
     * Labels de l'usine de textile 3
     */
    public void labelsUsineTextile3() {
        setNbUsineAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireGrande(), labelNbUsineAgroAlimentaire3);
        setNbMarchandisesAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireGrande(), labelNbMarchandisesAgroAlimentaire3);
        setLabelTarifUsineAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireGrande(), labelTarifUsineAgroAlimentaire3);
        setLabelTarifUsineAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireGrande(), btnAchatUsineAgroAlimentaire3);
    }
    /**
     * Labels de l'usine de textile 4
     */
    public void labelsUsineTextile4() {
        setNbUsineAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireEnorme(), labelNbUsineAgroAlimentaire4);
        setNbMarchandisesAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireEnorme(), labelNbMarchandisesAgroAlimentaire4);
        setLabelTarifUsineAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireEnorme(), labelTarifUsineAgroAlimentaire4);
        setLabelTarifUsineAgroAlimentaire(this.jeu.getJoueur().getUsineAgroAlimentaireEnorme(), btnAchatUsineAgroAlimentaire4);
    }
    /**
     * initialise le nombre d'usines en cours ainsi que le nombre d'usines maximum
     */
    public void setNbUsineAgroAlimentaire(UsineAgroAlimentaire usineAgroAlimentaire, Label labelUsine) {
        String formattedString = usineAgroAlimentaire.setNbUsines();
        labelUsine.setText(formattedString);
    }

    /**
     * Centre les panes de chaque usine
     * Platform.runLater permet d'attendre le chargement des fenetres afin de récupérer les valeurs des boutons
     */
    public void centragePanesPrincipaux(){
        Platform.runLater(() -> {
            Outils.centragePane(paneAgroAlimentaire1, panePrincipal.getWidth());
            Outils.centragePane(paneAgroAlimentaire2, panePrincipal.getWidth());
            Outils.centragePane(paneAgroAlimentaire3, panePrincipal.getWidth());
            Outils.centragePane(paneAgroAlimentaire4, panePrincipal.getWidth());
        });
    }

    /**
     * Centre les boutons d'achat des usines
     * Platform.runLater permet d'attendre le chargement des fenetres afin de récupérer les valeurs des boutons
     */
    public void centrageBoutons(){
        Platform.runLater(() -> {
            Outils.centrageBouton(btnAchatUsineAgroAlimentaire1, paneAgroAlimentaire1D.getWidth(), true);
            Outils.centrageBouton(btnAchatUsineAgroAlimentaire2, paneAgroAlimentaire2D.getWidth(), true);
            Outils.centrageBouton(btnAchatUsineAgroAlimentaire3, paneAgroAlimentaire3D.getWidth(),true);
            Outils.centrageBouton(btnAchatUsineAgroAlimentaire4, paneAgroAlimentaire4D.getWidth(),true);
        });
    }
    /**
     * initialise le nombre de marchandises produites
     */
    public void setNbMarchandisesAgroAlimentaire(UsineAgroAlimentaire usineAgroAlimentaire, Label labelUsine) {
        labelUsine.setText(usineAgroAlimentaire.getNbMarchandises() + "");
    }

    /**
     * Inscrit le prix d'achat d'une usine dans le label
     */
    public void setLabelTarifUsineAgroAlimentaire(UsineAgroAlimentaire usineAgroAlimentaire, Label labelUsine) {
        BigDecimal prixUsineTextile = usineAgroAlimentaire.getPrixUsine();
        String nomVehicule = usineAgroAlimentaire.getNom();
        String formattedString = "Acheter " + nomVehicule + " : " + decimalFormat.format(prixUsineTextile) + monnaie;
        labelUsine.setText(formattedString);
    }

    /**
     * Inscrit le prix d'achat d'une usine sur le bouton
     */
    public void setLabelTarifUsineAgroAlimentaire(UsineAgroAlimentaire usineAgroAlimentaire, Button btnAchatUsine) {
        BigDecimal prixUsineTextile = usineAgroAlimentaire.getPrixUsine();
        String nomVehicule = usineAgroAlimentaire.getNom();
        String formattedString = "Acheter " + nomVehicule + " : " + decimalFormat.format(prixUsineTextile) + monnaie;
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
        demarrageUsinesPharmaceutique();
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

        // on recupere les barres de progression des usines agro alimentaire
        this.jeu.getJoueur().getUsineAgroAlimentairePetite().setEtatProgressUsine(this.progressAgroAlimentaire1.getProgress());
        this.jeu.getJoueur().getUsineAgroAlimentaireMoyenne().setEtatProgressUsine(this.progressAgroAlimentaire2.getProgress());
        this.jeu.getJoueur().getUsineAgroAlimentaireGrande().setEtatProgressUsine(this.progressAgroAlimentaire3.getProgress());
        this.jeu.getJoueur().getUsineAgroAlimentaireEnorme().setEtatProgressUsine(this.progressAgroAlimentaire4.getProgress());

        // on recupere les barres de progression des usines pharmaceutique
        this.jeu.getJoueur().getUsinePharmaceutiquePetite().setEtatProgressUsine(this.progressPharmaceutique1.getProgress());
        this.jeu.getJoueur().getUsinePharmaceutiqueMoyenne().setEtatProgressUsine(this.progressPharmaceutique2.getProgress());
        this.jeu.getJoueur().getUsinePharmaceutiqueGrande().setEtatProgressUsine(this.progressPharmaceutique3.getProgress());
        this.jeu.getJoueur().getUsinePharmaceutiqueEnorme().setEtatProgressUsine(this.progressPharmaceutique4.getProgress());

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
        Outils.progressBarStop(timelineUsineAgroAlimentaire1);
        Outils.progressBarStop(timelineUsineAgroAlimentaire2);
        Outils.progressBarStop(timelineUsineAgroAlimentaire3);
        Outils.progressBarStop(timelineUsineAgroAlimentaire4);
        jeu.getJoueur().getUsinePharmaceutiquePetite().progressBarStop();
        jeu.getJoueur().getUsinePharmaceutiqueMoyenne().progressBarStop();
        jeu.getJoueur().getUsinePharmaceutiqueGrande().progressBarStop();
        jeu.getJoueur().getUsinePharmaceutiqueEnorme().progressBarStop();
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
     * Demarre les usines de jouets lorsqu'elles sont actives
     */
    public void demarrageUsinesPharmaceutique() {
        if (Outils.isActif(jeu.getJoueur().getUsinePharmaceutiquePetite().getUsineActive())) {
            // recupertaion etat barre de progression usine pharmaceutique petite
            double vitesseUsinePharmaceutique1 = jeu.getJoueur().getUsinePharmaceutiquePetite().getVitesseUsine() - (jeu.getJoueur().getUsinePharmaceutiquePetite().getVitesseUsine() * jeu.getJoueur().getUsinePharmaceutiquePetite().getEtatProgressUsine());
            this.jeu.getJoueur().getUsinePharmaceutiquePetite().progressBarStartUsinePharmaceutique(1, this.jeu.getJoueur().getUsinePharmaceutiquePetite().getVitesseUsine(), vitesseUsinePharmaceutique1, progressJouets1);
        }
        if (Outils.isActif(jeu.getJoueur().getUsinePharmaceutiqueMoyenne().getUsineActive())) {
            // recupertaion etat barre de progression usine pharmaceutique moyenne
            double vitesseUsinePharmaceutique2 = jeu.getJoueur().getUsinePharmaceutiqueMoyenne().getVitesseUsine() - (jeu.getJoueur().getUsinePharmaceutiqueMoyenne().getVitesseUsine() * jeu.getJoueur().getUsinePharmaceutiqueMoyenne().getEtatProgressUsine());
            this.jeu.getJoueur().getUsinePharmaceutiqueMoyenne().progressBarStartUsinePharmaceutique(1, this.jeu.getJoueur().getUsinePharmaceutiqueMoyenne().getVitesseUsine(), vitesseUsinePharmaceutique2, progressJouets2);
        }
        if (Outils.isActif(jeu.getJoueur().getUsinePharmaceutiqueGrande().getUsineActive())) {
            // recupertaion etat barre de progression usine pharmaceutique grande
            double vitesseUsinePharmaceutique3 = jeu.getJoueur().getUsinePharmaceutiqueGrande().getVitesseUsine() - (jeu.getJoueur().getUsinePharmaceutiqueGrande().getVitesseUsine() * jeu.getJoueur().getUsinePharmaceutiqueGrande().getEtatProgressUsine());
            this.jeu.getJoueur().getUsinePharmaceutiqueGrande().progressBarStartUsinePharmaceutique(1, this.jeu.getJoueur().getUsinePharmaceutiqueGrande().getVitesseUsine(), vitesseUsinePharmaceutique3, progressJouets3);
        }
        if (Outils.isActif(jeu.getJoueur().getUsinePharmaceutiqueEnorme().getUsineActive())) {
            // recupertaion etat barre de progression usine pharmaceutique enorme
            double vitesseUsinePharmaceutique4 = jeu.getJoueur().getUsinePharmaceutiqueEnorme().getVitesseUsine() - (jeu.getJoueur().getUsinePharmaceutiqueEnorme().getVitesseUsine() * jeu.getJoueur().getUsinePharmaceutiqueEnorme().getEtatProgressUsine());
            this.jeu.getJoueur().getUsinePharmaceutiqueEnorme().progressBarStartUsinePharmaceutique(1, this.jeu.getJoueur().getUsinePharmaceutiqueEnorme().getVitesseUsine(), vitesseUsinePharmaceutique4, progressJouets4);
        }
    }
    /**
     * Demarre les usines lorsqu'elles sont actives
     */
    public void demarrageUsinesAgroAlimentaire() {
        // usine agro alimentaire petite
        if (Outils.isActif(jeu.getJoueur().getUsineAgroAlimentairePetite().getUsineActive())) {
            System.out.println("Demarrage " + jeu.getJoueur().getUsineAgroAlimentairePetite().getNom());
            double vitesseUsineAgroAlimentaire1 = jeu.getJoueur().getUsineAgroAlimentairePetite().getVitesseUsine() - (jeu.getJoueur().getUsineAgroAlimentairePetite().getVitesseUsine() * jeu.getJoueur().getUsineAgroAlimentairePetite().getEtatProgressUsine());
            this.progressBarStartUsineAgroAlimentairePetite(1, jeu.getJoueur().getUsineAgroAlimentairePetite().getVitesseUsine(), vitesseUsineAgroAlimentaire1, jeu.getJoueur().getUsineAgroAlimentairePetite(), progressTextile1, btnEncaisserUsineAgroAlimentaire1);
        }
        // usine agro alimentaire moyenne
        if (Outils.isActif(jeu.getJoueur().getUsineAgroAlimentaireMoyenne().getUsineActive())) {
            System.out.println("Demarrage " + jeu.getJoueur().getUsineAgroAlimentaireMoyenne().getNom());
            double vitesseUsineAgroAlimentaire2 = jeu.getJoueur().getUsineAgroAlimentaireMoyenne().getVitesseUsine() - (jeu.getJoueur().getUsineAgroAlimentaireMoyenne().getVitesseUsine() * jeu.getJoueur().getUsineAgroAlimentaireMoyenne().getEtatProgressUsine());
            this.progressBarStartUsineAgroAlimentaireMoyenne(1, jeu.getJoueur().getUsineAgroAlimentaireMoyenne().getVitesseUsine(), vitesseUsineAgroAlimentaire2, jeu.getJoueur().getUsineAgroAlimentaireMoyenne(), progressTextile2, btnEncaisserUsineAgroAlimentaire2);
        }
        // usine agro alimentaire grande
        if (Outils.isActif(jeu.getJoueur().getUsineAgroAlimentaireGrande().getUsineActive())) {
            System.out.println("Demarrage " + jeu.getJoueur().getUsineAgroAlimentaireGrande().getNom());
            double vitesseUsineAgroAlimentaire3 = jeu.getJoueur().getUsineAgroAlimentaireGrande().getVitesseUsine() - (jeu.getJoueur().getUsineAgroAlimentaireGrande().getVitesseUsine() * jeu.getJoueur().getUsineAgroAlimentaireGrande().getEtatProgressUsine());
            this.progressBarStartUsineAgroAlimentaireGrande(1, jeu.getJoueur().getUsineAgroAlimentaireGrande().getVitesseUsine(), vitesseUsineAgroAlimentaire3, jeu.getJoueur().getUsineAgroAlimentaireGrande(), progressTextile3, btnEncaisserUsineAgroAlimentaire3);
        }
        // usine agro alimentaire enorme
        if (Outils.isActif(jeu.getJoueur().getUsineAgroAlimentaireEnorme().getUsineActive())) {
            System.out.println("Demarrage " + jeu.getJoueur().getUsineAgroAlimentaireEnorme().getNom());
            double vitesseUsineAgroAlimentaire4 = jeu.getJoueur().getUsineAgroAlimentaireEnorme().getVitesseUsine() - (jeu.getJoueur().getUsineAgroAlimentaireEnorme().getVitesseUsine() * jeu.getJoueur().getUsineAgroAlimentaireEnorme().getEtatProgressUsine());
            this.progressBarStartUsineAgroAlimentaireEnorme(1, jeu.getJoueur().getUsineAgroAlimentaireEnorme().getVitesseUsine(), vitesseUsineAgroAlimentaire4, jeu.getJoueur().getUsineAgroAlimentaireEnorme(), progressTextile4, btnEncaisserUsineAgroAlimentaire4);
        }
    }

    /**
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine textile petite
     *
     * @param cycle             : 0 pour cycle infini
     * @param vitesse           : vitesse de l'usine en secondes
     * @param vitesseAjustement
     * @param usineAgroAlimentaire
     * @param progress          : barre de progress de la fabrication d'une marchandise
     * @param btnEncaisser
     */
    public void progressBarStartUsineAgroAlimentairePetite(int cycle, double vitesse, double vitesseAjustement, UsineAgroAlimentaire usineAgroAlimentaire, ProgressBar progress, Button btnEncaisser) {
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(usineAgroAlimentaire.getEtatProgressUsine());
            etatBarreProgress = usineAgroAlimentaire.getEtatProgressUsine();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineUsineAgroAlimentaire1 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    System.out.println("cycle");
                    // rend le bouton Encaisser actif
                    btnEncaisser.setDisable(false);
                    // ajoute le nombre de marchandises fabriquées par l'usine
                    usineAgroAlimentaire.majUsine();
                    // maj le montant des gains en attente
                    recupMarchandises(this.jeu.getJoueur().getUsineAgroAlimentairePetite(), btnEncaisserUsineAgroAlimentaire1, imgAgroAlimentaire1);
                    System.out.println("Production de marchandises dans " + usineAgroAlimentaire.getNom() + " terminée");
                    // met à jour les gains en cours ainsi que le bouton encaisser
                    usineAgroAlimentaire.majBtnEncaisser(btnEncaisser, imgAgroAlimentaire1);
                    // maj des labels
                    this.miseEnPlace();
                }, new KeyValue(progress.progressProperty(), 1))
        );
        if (cycle == 1) {
            timelineUsineAgroAlimentaire1.setOnFinished(event -> {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsineAgroAlimentairePetite(cycle - 1, vitesse, vitesse, usineAgroAlimentaire, progress, btnEncaisser);
            });
        }
        if (cycle == 0) {
            timelineUsineAgroAlimentaire1.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineUsineAgroAlimentaire1.setCycleCount(cycle);
        }
        timelineUsineAgroAlimentaire1.play();
    }

    /**
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine de textile moyenne
     *
     * @param cycle             : 0 pour cycle infini
     * @param vitesse           : vitesse de l'usine en secondes
     * @param vitesseAjustement
     * @param usineAgroAlimentaire
     * @param progress          : barre de progress de la fabrication d'une marchandise
     * @param btnEncaisser
     */
    public void progressBarStartUsineAgroAlimentaireMoyenne(int cycle, double vitesse, double vitesseAjustement, UsineAgroAlimentaire usineAgroAlimentaire , ProgressBar progress, Button btnEncaisser) {
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(usineAgroAlimentaire.getEtatProgressUsine());
            etatBarreProgress = usineAgroAlimentaire.getEtatProgressUsine();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineUsineAgroAlimentaire2 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    System.out.println("cycle");
                    // rend le bouton Encaisser actif
                    btnEncaisser.setDisable(false);
                    // ajoute le nombre de marchandises fabriquées par l'usine
                    usineAgroAlimentaire.majUsine();
                    // maj le montant des gains en attente
                    recupMarchandises(this.jeu.getJoueur().getUsineAgroAlimentaireMoyenne(), btnEncaisserUsineAgroAlimentaire2, imgAgroAlimentaire2);
                    System.out.println("Production de marchandises dans " + usineAgroAlimentaire.getNom() + " terminée");
                    // met à jour les gains en cours ainsi que le bouton encaisser
                    usineAgroAlimentaire.majBtnEncaisser(btnEncaisser, imgAgroAlimentaire2);
                    // maj des labels
                    this.miseEnPlace();
                }, new KeyValue(progress.progressProperty(), 1))
        );
        if (cycle == 1) {
            timelineUsineAgroAlimentaire2.setOnFinished(event -> {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsineAgroAlimentaireMoyenne(cycle - 1, vitesse, vitesse, usineAgroAlimentaire, progress, btnEncaisser);
            });
        }
        if (cycle == 0) {
            timelineUsineAgroAlimentaire2.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineUsineAgroAlimentaire2.setCycleCount(cycle);
        }
        timelineUsineAgroAlimentaire2.play();
    }

    /**
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine textile grande
     *
     * @param cycle             : 0 pour cycle infini
     * @param vitesse           : vitesse de l'usine en secondes
     * @param vitesseAjustement
     * @param usineAgroAlimentaire
     * @param progress          : barre de progress de la fabrication d'une marchandise
     * @param btnEncaisser
     */
    public void progressBarStartUsineAgroAlimentaireGrande(int cycle, double vitesse, double vitesseAjustement, UsineAgroAlimentaire usineAgroAlimentaire , ProgressBar progress, Button btnEncaisser) {
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(usineAgroAlimentaire.getEtatProgressUsine());
            etatBarreProgress = usineAgroAlimentaire.getEtatProgressUsine();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineUsineAgroAlimentaire3 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    System.out.println("cycle");
                    // rend le bouton Encaisser actif
                    btnEncaisser.setDisable(false);
                    // ajoute le nombre de marchandises fabriquées par l'usine
                    usineAgroAlimentaire.majUsine();
                    // maj le montant des gains en attente
                    recupMarchandises(this.jeu.getJoueur().getUsineAgroAlimentaireGrande(), btnEncaisserUsineAgroAlimentaire3, imgAgroAlimentaire3);
                    System.out.println("Production de marchandises dans " + usineAgroAlimentaire.getNom() + " terminée");
                    // met à jour les gains en cours ainsi que le bouton encaisser
                    usineAgroAlimentaire.majBtnEncaisser(btnEncaisser, imgAgroAlimentaire3);
                    // maj des labels
                    this.miseEnPlace();
                }, new KeyValue(progress.progressProperty(), 1))
        );
        if (cycle == 1) {
            timelineUsineAgroAlimentaire3.setOnFinished(event -> {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsineAgroAlimentaireGrande(cycle - 1, vitesse, vitesse, usineAgroAlimentaire, progress, btnEncaisser);
            });
        }
        if (cycle == 0) {
            timelineUsineAgroAlimentaire3.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineUsineAgroAlimentaire3.setCycleCount(cycle);
        }
        timelineUsineAgroAlimentaire3.play();
    }

    /**
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine textile enorme
     *
     * @param cycle             : 0 pour cycle infini
     * @param vitesse           : vitesse de l'usine en secondes
     * @param vitesseAjustement
     * @param usineAgroAlimentaire
     * @param progress          : barre de progress de la fabrication d'une marchandise
     * @param btnEncaisser
     */
    public void progressBarStartUsineAgroAlimentaireEnorme(int cycle, double vitesse, double vitesseAjustement, UsineAgroAlimentaire usineAgroAlimentaire , ProgressBar progress, Button btnEncaisser) {
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(usineAgroAlimentaire.getEtatProgressUsine());
            etatBarreProgress = usineAgroAlimentaire.getEtatProgressUsine();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineUsineAgroAlimentaire4 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    System.out.println("cycle");
                    // rend le bouton Encaisser actif
                    btnEncaisser.setDisable(false);
                    // ajoute le nombre de marchandises fabriquées par l'usine
                    usineAgroAlimentaire.majUsine();
                    // maj le montant des gains en attente
                    recupMarchandises(this.jeu.getJoueur().getUsineAgroAlimentaireEnorme(), btnEncaisserUsineAgroAlimentaire4, imgAgroAlimentaire4);
                    System.out.println("Production de marchandises dans " + usineAgroAlimentaire.getNom() + " terminée");
                    // met à jour les gains en cours ainsi que le bouton encaisser
                    usineAgroAlimentaire.majBtnEncaisser(btnEncaisser, imgAgroAlimentaire4);
                    // maj des labels
                    this.miseEnPlace();
                }, new KeyValue(progress.progressProperty(), 1))
        );
        if (cycle == 1) {
            timelineUsineAgroAlimentaire4.setOnFinished(event -> {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsineAgroAlimentaireEnorme(cycle - 1, vitesse, vitesse, usineAgroAlimentaire, progress, btnEncaisser);
            });
        }
        if (cycle == 0) {
            timelineUsineAgroAlimentaire4.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineUsineAgroAlimentaire4.setCycleCount(cycle);
        }
        timelineUsineAgroAlimentaire4.play();
    }


}
