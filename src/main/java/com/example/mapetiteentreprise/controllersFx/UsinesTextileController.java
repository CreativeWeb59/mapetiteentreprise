package com.example.mapetiteentreprise.controllersFx;

import com.example.mapetiteentreprise.Main;
import com.example.mapetiteentreprise.actions.Outils;
import com.example.mapetiteentreprise.bdd.ConnectionBdd;
import com.example.mapetiteentreprise.jeu.Jeu;
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

public class UsinesTextileController {
    private final String monnaie = " €";
    private final String separationTexte = System.getProperty("line.separator");
    @FXML
    private Label labelHaut,
            labelNbUsineTextile1, labelNbUsineTextile2, labelNbUsineTextile3, labelNbUsineTextile4,
            labelTarifUsineTextile1, labelTarifUsineTextile2, labelTarifUsineTextile3, labelTarifUsineTextile4,
            labelNbMarchandisesTextile1, labelNbMarchandisesTextile2, labelNbMarchandisesTextile3, labelNbMarchandisesTextile4;
    @FXML
    private Button btnAchatUsineTextile1, btnAchatUsineTextile2, btnAchatUsineTextile3, btnAchatUsineTextile4,
            btnAchatUsineTextilePetite, btnAchatUsineTextileMoyenne, btnAchatUsineTextileGrande, btnAchatUsineTextileEnorme,
            btnEncaisserUsineTextile1, btnEncaisserUsineTextile2, btnEncaisserUsineTextile3, btnEncaisserUsineTextile4;
    private final DecimalFormat decimalFormat = Outils.getDecimalFormatWithSpaceSeparator();
    @FXML
    private ProgressBar progressOeufs, progressBC, progressBF, progressSa, progressCo,
            progressScooter, progressCamionette, progressPetitCamion, progressPoidsLourd, progressAvion,
            progressTextile1, progressTextile2, progressTextile3, progressTextile4,
            progressJouets1, progressJouets2, progressJouets3, progressJouets4,
            progressPharmaceutique1, progressPharmaceutique2, progressPharmaceutique3, progressPharmaceutique4,
            progressAgroAlimentaire1, progressAgroAlimentaire2, progressAgroAlimentaire3, progressAgroAlimentaire4;
    private Timeline timelineUsineTextile1, timelineUsineTextile2, timelineUsineTextile3, timelineUsineTextile4;
    private ConnectionBdd connectionBdd = new ConnectionBdd();
    @FXML
    private Pane paneProgress, paneTextile1, paneTextile2, paneTextile3, paneTextile4,
            paneTextile1D, paneTextile2D, paneTextile3D, paneTextile4D;
    @FXML
    private ImageView imgTextile1, imgTextile2, imgTextile3, imgTextile4;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Jeu jeu;

    public void demarrer(Jeu jeu) {
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
     *
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
    public void acheterUsineTextilePetite() {
        acheterUsineTextile(this.jeu.getJoueur().getUsineTextilePetite(), progressTextile1, 1);
    }

    /**
     * Ajout d'une petite usine
     */
    public void acheterUsineTextileMoyenne() {
        acheterUsineTextile(this.jeu.getJoueur().getUsineTextileMoyenne(), progressTextile2, 2);
    }

    /**
     * Ajout d'une petite usine
     */
    public void acheterUsineTextileGrande() {
        acheterUsineTextile(this.jeu.getJoueur().getUsineTextileGrande(), progressTextile3, 3);
    }

    /**
     * Ajout d'une petite usine
     */
    public void acheterUsineTextileEnorme() {
        acheterUsineTextile(this.jeu.getJoueur().getUsineTextileEnorme(), progressTextile4, 4);
    }

    /**
     * Methode d'achat général d'une usine de textile
     *
     * @param usineTextile    à acheter
     * @param progressTextile barre de progression de l'usine
     * @param numUsine        permet de savoir qu'elle usine pour lancer la bonne barre de progression
     */
    public void acheterUsineTextile(UsineTextile usineTextile, ProgressBar progressTextile, int numUsine) {
        if (!usineTextile.isMaxiNbUsines()) {
            BigDecimal montantAchat = usineTextile.getPrixUsine();
            if (jeu.getJoueur().acheter(montantAchat)) {
                usineTextile.ajoutUsine();
                System.out.println("Achat d'une " + usineTextile.getNom() + " : " + usineTextile.getNbUsines());
                // debloque le scooter si besoin
                if (usineTextile.getNbUsines() == 1) {
                    usineTextile.setUsineActive(1);
                    // demarre la barre de progression de l'usine
                    // recuperation de l'etat de la barre de progression pour la petite usine
                    double vitesseUsineTextile = usineTextile.getVitesseUsine();
                    switch (numUsine) {
                        case 1:
                            this.progressBarStartUsineTextilePetite(0, vitesseUsineTextile, vitesseUsineTextile, jeu.getJoueur().getUsineTextilePetite(), progressTextile, btnEncaisserUsineTextile1);
                            break;
                        case 2:
                            this.progressBarStartUsineTextileMoyenne(0, vitesseUsineTextile, vitesseUsineTextile, jeu.getJoueur().getUsineTextileMoyenne(), progressTextile, btnEncaisserUsineTextile2);
                            break;
                        case 3:
                            this.progressBarStartUsineTextileGrande(0, vitesseUsineTextile, vitesseUsineTextile, jeu.getJoueur().getUsineTextileGrande(), progressTextile, btnEncaisserUsineTextile3);
                            break;
                        case 4:
                            this.progressBarStartUsineTextileEnorme(0, vitesseUsineTextile, vitesseUsineTextile, jeu.getJoueur().getUsineTextileEnorme(), progressTextile, btnEncaisserUsineTextile4);
                            break;
                        default:
                            System.out.println("erreur d'usine");
                    }
                }
                // mise a jour des valeurs
                miseEnPlace();
            } else {
                System.out.println("Vous n'avez pas assez d'argent pour acheter une " + usineTextile.getNom() + " : ");
            }
        } else {
            System.out.println("Vous avez trop de " + usineTextile.getNom() + " : ");
        }
    }

    /**
     * Gere le clic sur le bouton encaisser usine de textile petite
     */
    public void onBtnEncaisserUsineTextile1() {
        encaisserUsineTextile(this.jeu.getJoueur().getUsineTextilePetite(), btnEncaisserUsineTextile1);
    }

    /**
     * Gere le clic sur le bouton encaisser usine de textile moyenne
     */
    public void onBtnEncaisserUsineTextile2() {
        encaisserUsineTextile(this.jeu.getJoueur().getUsineTextileMoyenne(), btnEncaisserUsineTextile2);
    }

    /**
     * Gere le clic sur le bouton encaisser usine de textile grande
     */
    public void onBtnEncaisserUsineTextile3() {
        encaisserUsineTextile(this.jeu.getJoueur().getUsineTextileGrande(), btnEncaisserUsineTextile3);
    }

    /**
     * Gere le clic sur le bouton encaisser usine de textile enorme
     */
    public void onBtnEncaisserUsineTextile4() {
        encaisserUsineTextile(this.jeu.getJoueur().getUsineTextileEnorme(), btnEncaisserUsineTextile4);
    }

    /**
     * Bouton qui permet d'encaisser l'argent des usines de textile
     *
     * @param usineTextile usine textile spécifiée
     */
    public void encaisserUsineTextile(UsineTextile usineTextile, Button btnEncaisserUsineTextile) {
        long nbMarchandisesUsineTextile = usineTextile.getNbMarchandises();

        usineTextile.setNbMarchandises(0); // raz le nombre de marchandises
        jeu.getJoueur().setArgent(usineTextile.getGainEnAttenteUsine().add(jeu.getJoueur().getArgent())); // met a jour les nouveaux gains

        String formattedGain = decimalFormat.format(usineTextile.getGainEnAttenteUsine()) + monnaie;
        // raz des gains en attente
        usineTextile.setGainEnAttenteUsine(BigDecimal.valueOf(0.00));

        System.out.println("Vous venez de récupérer le prix de " + nbMarchandisesUsineTextile + " de l'usine de textile " + usineTextile.getNom() + ", vous avez gagné " + formattedGain + ".");

        btnEncaisserUsineTextile.setDisable(true);
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
        affichageBtnTextile1();
        affichageBtnTextile2();
        affichageBtnTextile3();
        affichageBtnTextile4();
        affichageContenuPanes(jeu.getJoueur().getUsineTextilePetite(), paneTextile1, paneTextile1D, labelNbMarchandisesTextile1, labelNbUsineTextile1, labelTarifUsineTextile1, btnAchatUsineTextilePetite, btnEncaisserUsineTextile1, imgTextile1, progressTextile1);
        affichageContenuPanes(jeu.getJoueur().getUsineTextileMoyenne(), paneTextile2, paneTextile2D, labelNbMarchandisesTextile2, labelNbUsineTextile2, labelTarifUsineTextile2, btnAchatUsineTextileMoyenne, btnEncaisserUsineTextile2, imgTextile2, progressTextile2);
        affichageContenuPanes(jeu.getJoueur().getUsineTextileGrande(), paneTextile3, paneTextile3D, labelNbMarchandisesTextile3, labelNbUsineTextile3, labelTarifUsineTextile3, btnAchatUsineTextileGrande, btnEncaisserUsineTextile3, imgTextile3, progressTextile3);
        affichageContenuPanes(jeu.getJoueur().getUsineTextileEnorme(), paneTextile4, paneTextile4D, labelNbMarchandisesTextile4, labelNbUsineTextile4, labelTarifUsineTextile4, btnAchatUsineTextileEnorme, btnEncaisserUsineTextile4, imgTextile4, progressTextile4);
        testBtnAchats();
    }

    /**
     * Active / desactive le bouton d'achat de vehicule de livraison pour chaque usine
     */
    public void testBtnAchats() {
        // usine de textile petite
        testBtnAchat(jeu.getJoueur().getUsineTextilePetite(), btnAchatUsineTextile1, btnAchatUsineTextilePetite);
        // usine de textile moyenne
        testBtnAchat(jeu.getJoueur().getUsineTextileMoyenne(), btnAchatUsineTextile2, btnAchatUsineTextileMoyenne);
        // usine de textile grande
        testBtnAchat(jeu.getJoueur().getUsineTextileGrande(), btnAchatUsineTextile3, btnAchatUsineTextileGrande);
        // usine de textile enorme
        testBtnAchat(jeu.getJoueur().getUsineTextileEnorme(), btnAchatUsineTextile4, btnAchatUsineTextileEnorme);
    }
    /**
     * Gere l'affichage ou non du contenu des panes pour afficher / masquer l'usine au demarrage
     * @param usineTextile usine à afficher / masquer
     * @param pane
     * @param labelNb
     * @param labelTarif
     * @param labelTitre
     * @param btnEncaisser
     * @param btnPlus
     * @param barreProgress
     */
    public void affichageContenuPanes(UsineTextile usineTextile, Pane pane, Pane paneD, Label labelTitre, Label labelNb, Label labelTarif, Button btnPlus, Button btnEncaisser, ImageView imgTextile1, ProgressBar barreProgress){
        if (usineTextile.getUsineActive() == 1){
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
     * @param usineTextile usine à tester
     * @param btnAchatUsine bouton de l'achat d'une usine (qui active l'usine en question)
     * @param btnAjoutUsine bouton de l'ajout d'une usine (qui ajoute une usine quand usine déja activée)
     */
    public void testBtnAchat(UsineTextile usineTextile, Button btnAchatUsine, Button btnAjoutUsine){
        if (jeu.getJoueur().isArgent(usineTextile.getPrixUsine()) && !usineTextile.isMaxiNbUsines()) {
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
    public void affichageBtnTextile1() {
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
    public void affichageBtnTextile2() {
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
    public void affichageBtnTextile3() {
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
    public void affichageBtnTextile4() {
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

    public void recupMarchandisesToutes(){
        recupMarchandises(this.jeu.getJoueur().getUsineTextilePetite(), btnEncaisserUsineTextile1, imgTextile1);
        recupMarchandises(this.jeu.getJoueur().getUsineTextileMoyenne(), btnEncaisserUsineTextile2, imgTextile2);
        recupMarchandises(this.jeu.getJoueur().getUsineTextileGrande(), btnEncaisserUsineTextile3, imgTextile3);
        recupMarchandises(this.jeu.getJoueur().getUsineTextileEnorme(), btnEncaisserUsineTextile4, imgTextile4);
    }
    /**
     * Maj les gains en attente à chaque fin de progression
     * @param usine
     * @param boutonEncaisser
     */
    public void recupMarchandises(UsineTextile usine, Button boutonEncaisser, ImageView imageView) {
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
        setNbUsineTextile(this.jeu.getJoueur().getUsineTextilePetite(), labelNbUsineTextile1);
        setNbMarchandisesTextile(this.jeu.getJoueur().getUsineTextilePetite(), labelNbMarchandisesTextile1);
        setLabelTarifUsineTextile(this.jeu.getJoueur().getUsineTextilePetite(), labelTarifUsineTextile1);
        setLabelTarifUsineTextile(this.jeu.getJoueur().getUsineTextilePetite(), btnAchatUsineTextile1);
    }
    /**
     * Labels de l'usine de textile 2
     */
    public void labelsUsineTextile2() {
        setNbUsineTextile(this.jeu.getJoueur().getUsineTextileMoyenne(), labelNbUsineTextile2);
        setNbMarchandisesTextile(this.jeu.getJoueur().getUsineTextileMoyenne(), labelNbMarchandisesTextile2);
        setLabelTarifUsineTextile(this.jeu.getJoueur().getUsineTextileMoyenne(), labelTarifUsineTextile2);
        setLabelTarifUsineTextile(this.jeu.getJoueur().getUsineTextileMoyenne(), btnAchatUsineTextile2);
    }
    /**
     * Labels de l'usine de textile 3
     */
    public void labelsUsineTextile3() {
        setNbUsineTextile(this.jeu.getJoueur().getUsineTextileGrande(), labelNbUsineTextile3);
        setNbMarchandisesTextile(this.jeu.getJoueur().getUsineTextileGrande(), labelNbMarchandisesTextile3);
        setLabelTarifUsineTextile(this.jeu.getJoueur().getUsineTextileGrande(), labelTarifUsineTextile3);
        setLabelTarifUsineTextile(this.jeu.getJoueur().getUsineTextileGrande(), btnAchatUsineTextile3);
    }
    /**
     * Labels de l'usine de textile 4
     */
    public void labelsUsineTextile4() {
        setNbUsineTextile(this.jeu.getJoueur().getUsineTextileEnorme(), labelNbUsineTextile4);
        setNbMarchandisesTextile(this.jeu.getJoueur().getUsineTextileEnorme(), labelNbMarchandisesTextile4);
        setLabelTarifUsineTextile(this.jeu.getJoueur().getUsineTextileEnorme(), labelTarifUsineTextile4);
        setLabelTarifUsineTextile(this.jeu.getJoueur().getUsineTextileEnorme(), btnAchatUsineTextile4);
    }
    /**
     * initialise le nombre d'usines en cours ainsi que le nombre d'usines maximum
     */
    public void setNbUsineTextile(UsineTextile usineTextile, Label labelUsine) {
        String formattedString = usineTextile.setNbUsines();
        labelUsine.setText(formattedString);
    }

    /**
     * Centre les boutons d'achat des usines
     * Platform.runLater permet d'attendre le chargement des fenetres afin de récupérer les valeurs des boutons
     */
    public void centrageBoutons(){
        Platform.runLater(() -> {
            Outils.centrageBouton(btnAchatUsineTextile1, paneTextile1D.getWidth(), true);
            Outils.centrageBouton(btnAchatUsineTextile2, paneTextile2D.getWidth(), true);
            Outils.centrageBouton(btnAchatUsineTextile3, paneTextile3D.getWidth(),true);
            Outils.centrageBouton(btnAchatUsineTextile4, paneTextile4D.getWidth(),true);
        });
    }
    /**
     * initialise le nombre de marchandises produites
     */
    public void setNbMarchandisesTextile(UsineTextile usineTextile, Label labelUsine) {
        labelUsine.setText(usineTextile.getNbMarchandises() + "");
    }

    /**
     * Inscrit le prix d'achat d'une usine dans le label
     */
    public void setLabelTarifUsineTextile(UsineTextile usineTextile, Label labelUsine) {
        BigDecimal prixUsineTextile = usineTextile.getPrixUsine();
        String nomVehicule = usineTextile.getNom();
        String formattedString = "Acheter " + nomVehicule + " : " + decimalFormat.format(prixUsineTextile) + monnaie;
        labelUsine.setText(formattedString);
    }

    /**
     * Inscrit le prix d'achat d'une usine sur le bouton
     */
    public void setLabelTarifUsineTextile(UsineTextile usineTextile, Button btnAchatUsine) {
        BigDecimal prixUsineTextile = usineTextile.getPrixUsine();
        String nomVehicule = usineTextile.getNom();
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
        demarrageUsinesJouets();
        demarrageUsinesAgroAlimentaire();
        demarrageUsinesPharmaceutique();
    }

    /**
     * Demarre les usines lorsqu'elles sont actives
     */
    public void demarrageUsinesTextile() {
        // usine de textile petite
        if (Outils.isActif(jeu.getJoueur().getUsineTextilePetite().getUsineActive())) {
            System.out.println("Demarrage " + jeu.getJoueur().getUsineTextilePetite().getNom());
            double vitesseUsineTextile1 = jeu.getJoueur().getUsineTextilePetite().getVitesseUsine() - (jeu.getJoueur().getUsineTextilePetite().getVitesseUsine() * jeu.getJoueur().getUsineTextilePetite().getEtatProgressUsine());
            this.progressBarStartUsineTextilePetite(1, jeu.getJoueur().getUsineTextilePetite().getVitesseUsine(), vitesseUsineTextile1, jeu.getJoueur().getUsineTextilePetite(), progressTextile1, btnEncaisserUsineTextile1);
        }
        // usine de textile moyenne
        if (Outils.isActif(jeu.getJoueur().getUsineTextileMoyenne().getUsineActive())) {
            System.out.println("Demarrage " + jeu.getJoueur().getUsineTextileMoyenne().getNom());
            double vitesseUsineTextile2 = jeu.getJoueur().getUsineTextileMoyenne().getVitesseUsine() - (jeu.getJoueur().getUsineTextileMoyenne().getVitesseUsine() * jeu.getJoueur().getUsineTextileMoyenne().getEtatProgressUsine());
            this.progressBarStartUsineTextileMoyenne(1, jeu.getJoueur().getUsineTextileMoyenne().getVitesseUsine(), vitesseUsineTextile2, jeu.getJoueur().getUsineTextileMoyenne(), progressTextile2, btnEncaisserUsineTextile2);
        }
        // usine de textile grande
        if (Outils.isActif(jeu.getJoueur().getUsineTextileGrande().getUsineActive())) {
            System.out.println("Demarrage " + jeu.getJoueur().getUsineTextileGrande().getNom());
            double vitesseUsineTextile3 = jeu.getJoueur().getUsineTextileGrande().getVitesseUsine() - (jeu.getJoueur().getUsineTextileGrande().getVitesseUsine() * jeu.getJoueur().getUsineTextileGrande().getEtatProgressUsine());
            this.progressBarStartUsineTextileGrande(1, jeu.getJoueur().getUsineTextileGrande().getVitesseUsine(), vitesseUsineTextile3, jeu.getJoueur().getUsineTextileGrande(), progressTextile3, btnEncaisserUsineTextile3);
        }
        // usine de textile enorme
        if (Outils.isActif(jeu.getJoueur().getUsineTextileEnorme().getUsineActive())) {
            System.out.println("Demarrage " + jeu.getJoueur().getUsineTextileEnorme().getNom());
            double vitesseUsineTextile4 = jeu.getJoueur().getUsineTextileEnorme().getVitesseUsine() - (jeu.getJoueur().getUsineTextileEnorme().getVitesseUsine() * jeu.getJoueur().getUsineTextileEnorme().getEtatProgressUsine());
            this.progressBarStartUsineTextileEnorme(1, jeu.getJoueur().getUsineTextileEnorme().getVitesseUsine(), vitesseUsineTextile4, jeu.getJoueur().getUsineTextileEnorme(), progressTextile4, btnEncaisserUsineTextile4);
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
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine textile petite
     *
     * @param cycle             : 0 pour cycle infini
     * @param vitesse           : vitesse de l'usine en secondes
     * @param vitesseAjustement
     * @param usineTextile
     * @param progress          : barre de progress de la fabrication d'une marchandise
     * @param btnEncaisser
     */
    public void progressBarStartUsineTextilePetite(int cycle, double vitesse, double vitesseAjustement, UsineTextile usineTextile, ProgressBar progress, Button btnEncaisser) {
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(usineTextile.getEtatProgressUsine());
            etatBarreProgress = usineTextile.getEtatProgressUsine();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineUsineTextile1 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    System.out.println("cycle");
                    // rend le bouton Encaisser actif
                    btnEncaisser.setDisable(false);
                    // ajoute le nombre de marchandises fabriquées par l'usine
                    usineTextile.majUsine();
                    // maj le montant des gains en attente
                    recupMarchandises(this.jeu.getJoueur().getUsineTextilePetite(), btnEncaisserUsineTextile1, imgTextile1);
                    System.out.println("Production de marchandises dans " + usineTextile.getNom() + " terminée");
                    // met à jour les gains en cours ainsi que le bouton encaisser
                    usineTextile.majBtnEncaisser(btnEncaisser, imgTextile1);
                    // maj des labels
                    this.miseEnPlace();
                }, new KeyValue(progress.progressProperty(), 1))
        );
        if (cycle == 1) {
            timelineUsineTextile1.setOnFinished(event -> {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsineTextilePetite(cycle - 1, vitesse, vitesse, usineTextile, progress, btnEncaisser);
            });
        }
        if (cycle == 0) {
            timelineUsineTextile1.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineUsineTextile1.setCycleCount(cycle);
        }
        timelineUsineTextile1.play();
    }

    /**
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine de textile moyenne
     *
     * @param cycle             : 0 pour cycle infini
     * @param vitesse           : vitesse de l'usine en secondes
     * @param vitesseAjustement
     * @param usineTextile
     * @param progress          : barre de progress de la fabrication d'une marchandise
     * @param btnEncaisser
     */
    public void progressBarStartUsineTextileMoyenne(int cycle, double vitesse, double vitesseAjustement, UsineTextile usineTextile, ProgressBar progress, Button btnEncaisser) {
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(usineTextile.getEtatProgressUsine());
            etatBarreProgress = usineTextile.getEtatProgressUsine();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineUsineTextile2 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    System.out.println("cycle");
                    // rend le bouton Encaisser actif
                    btnEncaisser.setDisable(false);
                    // ajoute le nombre de marchandises fabriquées par l'usine
                    usineTextile.majUsine();
                    // maj le montant des gains en attente
                    recupMarchandises(this.jeu.getJoueur().getUsineTextileMoyenne(), btnEncaisserUsineTextile2, imgTextile2);
                    System.out.println("Production de marchandises dans " + usineTextile.getNom() + " terminée");
                    // met à jour les gains en cours ainsi que le bouton encaisser
                    usineTextile.majBtnEncaisser(btnEncaisser, imgTextile2);
                    // maj des labels
                    this.miseEnPlace();
                }, new KeyValue(progress.progressProperty(), 1))
        );
        if (cycle == 1) {
            timelineUsineTextile2.setOnFinished(event -> {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsineTextileMoyenne(cycle - 1, vitesse, vitesse, usineTextile, progress, btnEncaisser);
            });
        }
        if (cycle == 0) {
            timelineUsineTextile2.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineUsineTextile2.setCycleCount(cycle);
        }
        timelineUsineTextile2.play();
    }

    /**
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine textile grande
     *
     * @param cycle             : 0 pour cycle infini
     * @param vitesse           : vitesse de l'usine en secondes
     * @param vitesseAjustement
     * @param usineTextile
     * @param progress          : barre de progress de la fabrication d'une marchandise
     * @param btnEncaisser
     */
    public void progressBarStartUsineTextileGrande(int cycle, double vitesse, double vitesseAjustement, UsineTextile usineTextile, ProgressBar progress, Button btnEncaisser) {
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(usineTextile.getEtatProgressUsine());
            etatBarreProgress = usineTextile.getEtatProgressUsine();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineUsineTextile3 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    System.out.println("cycle");
                    // rend le bouton Encaisser actif
                    btnEncaisser.setDisable(false);
                    // ajoute le nombre de marchandises fabriquées par l'usine
                    usineTextile.majUsine();
                    // maj le montant des gains en attente
                    recupMarchandises(this.jeu.getJoueur().getUsineTextileGrande(), btnEncaisserUsineTextile3, imgTextile3);
                    System.out.println("Production de marchandises dans " + usineTextile.getNom() + " terminée");
                    // met à jour les gains en cours ainsi que le bouton encaisser
                    usineTextile.majBtnEncaisser(btnEncaisser, imgTextile4);
                    // maj des labels
                    this.miseEnPlace();
                }, new KeyValue(progress.progressProperty(), 1))
        );
        if (cycle == 1) {
            timelineUsineTextile3.setOnFinished(event -> {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsineTextileGrande(cycle - 1, vitesse, vitesse, usineTextile, progress, btnEncaisser);
            });
        }
        if (cycle == 0) {
            timelineUsineTextile3.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineUsineTextile3.setCycleCount(cycle);
        }
        timelineUsineTextile3.play();
    }

    /**
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine textile enorme
     *
     * @param cycle             : 0 pour cycle infini
     * @param vitesse           : vitesse de l'usine en secondes
     * @param vitesseAjustement
     * @param usineTextile
     * @param progress          : barre de progress de la fabrication d'une marchandise
     * @param btnEncaisser
     */
    public void progressBarStartUsineTextileEnorme(int cycle, double vitesse, double vitesseAjustement, UsineTextile usineTextile, ProgressBar progress, Button btnEncaisser) {
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(usineTextile.getEtatProgressUsine());
            etatBarreProgress = usineTextile.getEtatProgressUsine();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineUsineTextile4 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    System.out.println("cycle");
                    // rend le bouton Encaisser actif
                    btnEncaisser.setDisable(false);
                    // ajoute le nombre de marchandises fabriquées par l'usine
                    usineTextile.majUsine();
                    // maj le montant des gains en attente
                    recupMarchandises(this.jeu.getJoueur().getUsineTextileEnorme(), btnEncaisserUsineTextile4, imgTextile4);
                    System.out.println("Production de marchandises dans " + usineTextile.getNom() + " terminée");
                    // met à jour les gains en cours ainsi que le bouton encaisser
                    usineTextile.majBtnEncaisser(btnEncaisser, imgTextile4);
                    // maj des labels
                    this.miseEnPlace();
                }, new KeyValue(progress.progressProperty(), 1))
        );
        if (cycle == 1) {
            timelineUsineTextile4.setOnFinished(event -> {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsineTextileEnorme(cycle - 1, vitesse, vitesse, usineTextile, progress, btnEncaisser);
            });
        }
        if (cycle == 0) {
            timelineUsineTextile4.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineUsineTextile4.setCycleCount(cycle);
        }
        timelineUsineTextile4.play();
    }

    /**
     * Fermeture des barres de progression
     */
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
        this.jeu.getJoueur().getUsineTextileMoyenne().setEtatProgressUsine(this.progressTextile2.getProgress());
        this.jeu.getJoueur().getUsineTextileGrande().setEtatProgressUsine(this.progressTextile3.getProgress());
        this.jeu.getJoueur().getUsineTextileEnorme().setEtatProgressUsine(this.progressTextile4.getProgress());

        // on recupere les barres de progression des usines de jouets
        this.jeu.getJoueur().getUsineJouetsPetite().setEtatProgressUsine(this.progressJouets1.getProgress());
        this.jeu.getJoueur().getUsineJouetsMoyenne().setEtatProgressUsine(this.progressJouets2.getProgress());
        this.jeu.getJoueur().getUsineJouetsGrande().setEtatProgressUsine(this.progressJouets3.getProgress());
        this.jeu.getJoueur().getUsineJouetsEnorme().setEtatProgressUsine(this.progressJouets4.getProgress());

        // on recupere les barres de progression des usines Agro alimentaire
        this.jeu.getJoueur().getUsineAgroAlimentairePetite().setEtatProgressUsine(this.progressAgroAlimentaire1.getProgress());
        this.jeu.getJoueur().getUsineAgroAlimentaireMoyenne().setEtatProgressUsine(this.progressAgroAlimentaire2.getProgress());
        this.jeu.getJoueur().getUsineAgroAlimentaireGrande().setEtatProgressUsine(this.progressAgroAlimentaire3.getProgress());
        this.jeu.getJoueur().getUsineAgroAlimentaireEnorme().setEtatProgressUsine(this.progressAgroAlimentaire4.getProgress());

        // on recupere les barres de progression des usines Pharmaceutique
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
        Outils.progressBarStop(timelineUsineTextile1);
        Outils.progressBarStop(timelineUsineTextile2);
        Outils.progressBarStop(timelineUsineTextile3);
        Outils.progressBarStop(timelineUsineTextile4);
        jeu.getJoueur().getUsineJouetsPetite().progressBarStop();
        jeu.getJoueur().getUsineJouetsMoyenne().progressBarStop();
        jeu.getJoueur().getUsineJouetsGrande().progressBarStop();
        jeu.getJoueur().getUsineJouetsEnorme().progressBarStop();
        jeu.getJoueur().getUsineAgroAlimentairePetite().progressBarStop();
        jeu.getJoueur().getUsineAgroAlimentaireMoyenne().progressBarStop();
        jeu.getJoueur().getUsineAgroAlimentaireGrande().progressBarStop();
        jeu.getJoueur().getUsineAgroAlimentaireEnorme().progressBarStop();
        jeu.getJoueur().getUsinePharmaceutiquePetite().progressBarStop();
        jeu.getJoueur().getUsinePharmaceutiquePetite().progressBarStop();
        jeu.getJoueur().getUsinePharmaceutiquePetite().progressBarStop();
        jeu.getJoueur().getUsinePharmaceutiquePetite().progressBarStop();
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
}
