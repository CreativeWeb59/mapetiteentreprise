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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
            btnAchatUsineTextilePetite,
            btnEncaisserUsineTextile1, btnEncaisserUsineTextile2, btnEncaisserUsineTextile3, btnEncaisserUsineTextile14;
    private final DecimalFormat decimalFormat = Outils.getDecimalFormatWithSpaceSeparator();
    @FXML
    private ProgressBar progressOeufs, progressBC, progressBF, progressSa, progressCo,
            progressScooter, progressCamionette, progressPetitCamion, progressPoidsLourd, progressAvion,
            progressTextile1, progressTextile2, progressTextile3, progressTextile4;
    private Timeline timelineUsineTextile1, timelineUsineTextile2, timelineUsineTextile3, timelineUsineTextile4;
    private ConnectionBdd connectionBdd = new ConnectionBdd();
    @FXML
    private Pane paneProgress, paneTextile1, paneTextile2, paneTextile3, paneTextile4;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Jeu jeu;

    public void demarrer(Jeu jeu) {
        // Recuperation du jeu
        this.jeu = jeu;

        // recuperation des marchandises
        recupMarchandises();

        // majLabels et boutons
        miseEnPlace();

        // barres de progression
        demarrageProgress();

        // affichage des barres de progression (mode dev)
        jeu.afficheProgression(paneProgress);
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
        System.out.println("Test 1");
        fermetureProgress();
        // sauvegarde bdd
        sauveBdd();
    }

    /**
     * Ajout d'une petite usine
     */
    public void acheterUsineTextilePetite() {
        acheterUsineTextile(this.jeu.getJoueur().getUsineTextilePetite(), progressTextile1);
    }

    /**
     * Ajout d'une petite usine
     */
    public void acheterUsineTextileMoyenne() {
        acheterUsineTextile(this.jeu.getJoueur().getUsineTextileMoyenne(), progressTextile2);
    }

    /**
     * Ajout d'une petite usine
     */
    public void acheterUsineTextileGrande() {
        acheterUsineTextile(this.jeu.getJoueur().getUsineTextileGrande(), progressTextile3);
    }

    /**
     * Ajout d'une petite usine
     */
    public void acheterUsineTextileEnorme() {
        acheterUsineTextile(this.jeu.getJoueur().getUsineTextileEnorme(), progressTextile4);
    }

    /**
     * Methode d'achat général d'une usine de textile
     *
     * @param usineTextile    à acheter
     * @param progressTextile barre de progression de l'usine
     */
    public void acheterUsineTextile(UsineTextile usineTextile, ProgressBar progressTextile) {
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
                    double vitesseUsineTextile = usineTextile.getVitesseUsineTextile();
                    this.progressBarStartUsineTextile(0, vitesseUsineTextile, vitesseUsineTextile, jeu.getJoueur().getUsineTextilePetite(), progressTextile, btnEncaisserUsineTextile1);
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

    public void onBtnEncaisserUsineTextile1() {
        encaisserUsineTextile(this.jeu.getJoueur().getUsineTextilePetite(), btnEncaisserUsineTextile1);
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
        // boutons du usine textile
        if (Outils.isActif(jeu.getJoueur().getUsineTextilePetite().getUsineActive())) {
            btnEncaisserUsineTextile1.setVisible(true);
            btnAchatUsineTextile1.setVisible(false);
            System.out.println("Actif");
            this.jeu.getJoueur().getUsineTextilePetite().majBtnEncaisser(btnEncaisserUsineTextile1);
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
        testBtnAchats();
    }

    /**
     * Active / desactive le bouton d'achat de vehicule de livraison
     */
    public void testBtnAchats() {
        // scooter
        BigDecimal prixUsineTextile1 = jeu.getJoueur().getUsineTextilePetite().getPrixUsine();
        if (jeu.getJoueur().isArgent(prixUsineTextile1) && !this.jeu.getJoueur().getUsineTextilePetite().isMaxiNbUsines()) {
            btnAchatUsineTextile1.setDisable(false);
            btnAchatUsineTextilePetite.setDisable(false);
        } else {
            btnAchatUsineTextile1.setDisable(true);
            btnAchatUsineTextilePetite.setDisable(true);
        }
    }

    public void recupMarchandises() {
        // petite usine de textile
        // maj des gains en attente
        this.jeu.getJoueur().getUsineTextilePetite().setGainEnAttenteUsine(this.jeu.getJoueur().getUsineTextilePetite().majGainsEnAttente());
        // maj du bouton
        this.jeu.getJoueur().getUsineTextilePetite().majBtnEncaisser(btnEncaisserUsineTextile1);
    }

    /**
     * Maj tous les labels necessaires
     */
    public void majLabels() {
        setLabelHaut();
        labelsUsineTextile1();
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
     * initialise le nombre d'usines en cours ainsi que le nombre d'usines maximum
     */
    public void setNbUsineTextile(UsineTextile usineTextile, Label labelUsine) {
        String formattedString = usineTextile.setNbUsines();
        labelUsine.setText(formattedString);
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
    }

    /**
     * Demarre les usines lorsqu'elles sont actives
     */
    public void demarrageUsinesTextile() {
        if (Outils.isActif(jeu.getJoueur().getUsineTextilePetite().getUsineActive())) {
            System.out.println("Demarrage " + jeu.getJoueur().getUsineTextilePetite().getNom());
            // enable pane de la livraison
//            this.debloquerLivraison(paneScooter, paneScooterD);
            double vitesseUsineTextile1 = jeu.getJoueur().getUsineTextilePetite().getVitesseUsineTextile() - (jeu.getJoueur().getUsineTextilePetite().getVitesseUsineTextile() * jeu.getJoueur().getUsineTextilePetite().getEtatProgressUsine());

            System.out.println("Vitesse ajustement : " + vitesseUsineTextile1);
            System.out.println("etatBarreProgress : " + jeu.getJoueur().getUsineTextilePetite().getEtatProgressUsine());

            this.progressBarStartUsineTextile(1, jeu.getJoueur().getUsineTextilePetite().getVitesseUsineTextile(), vitesseUsineTextile1, jeu.getJoueur().getUsineTextilePetite(), progressTextile1, btnEncaisserUsineTextile1);
        }
    }

    /**
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine spécifiée en parametres
     *
     * @param cycle             : 0 pour cycle infini
     * @param vitesse           : vitesse de l'usine en secondes
     * @param vitesseAjustement
     * @param usineTextile
     * @param progress          : barre de progress de la fabrication d'une marchandise
     * @param btnEncaisser
     */
    public void progressBarStartUsineTextile(int cycle, double vitesse, double vitesseAjustement, UsineTextile usineTextile, ProgressBar progress, Button btnEncaisser) {
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
                    recupMarchandises();
                    System.out.println("Production de marchandises dans " + usineTextile.getNom() + " terminée");
                    // met à jour les gains en cours ainsi que le bouton encaisser
                    usineTextile.majBtnEncaisser(btnEncaisser);
                    // maj des labels
                    this.miseEnPlace();
                }, new KeyValue(progress.progressProperty(), 1))
        );
        if (cycle == 1) {
            timelineUsineTextile1.setOnFinished(event -> {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsineTextile(cycle - 1, vitesse, vitesse, usineTextile, progress, btnEncaisser);
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
        System.out.println("Recup barre usine textile1 : " + this.jeu.getJoueur().getUsineTextilePetite().getEtatProgressUsine());
        System.out.println("Recup progress usinte textile1 : " + this.progressTextile1.getProgress());

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
