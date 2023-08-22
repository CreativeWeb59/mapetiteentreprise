package com.example.mapetiteentreprise.controllersFx;

import com.example.mapetiteentreprise.Main;
import com.example.mapetiteentreprise.actions.Outils;
import com.example.mapetiteentreprise.jeu.CreditEnCours;
import com.example.mapetiteentreprise.jeu.Jeu;
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
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class GestionController {
    @FXML
    private Label pseudoLabel, labelBlocageDistributeur, labelBlocageLivraison;
    @FXML
    private Button btnFerme, btnAchatFerme, btnDistributeurs, btnLivraison;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Jeu jeu;
    private final String monnaie = " €";
    // pattern des nombre décimaux
    private final DecimalFormat decimalFormat = Outils.getDecimalFormatWithSpaceSeparator();
    // necessaire au calendrier
    private long jourEnCours;
    @FXML
    private Pane paneSemaine1, paneSemaine2, paneParentProgressJour, paneProgress;
    @FXML
    private ProgressBar progressJour, progressOeufs, progressBC, progressBF, progressSa, progressCo, progressScooter, progressCamionette, progressPoidsLourd, progressPetitCamion;
    private Timeline timelineCalendrier, timelineHeure, timelineBC, timelineBF, timelineSa, timelineCo, timelineScooter, timelineCamionette, timelinePoidsLourd, timelinePetitCamion;

    /**
     * Recupere le nom de la sauvegarde
     * Utile pour relancer une partie
     *
     * @param jeu
     */
    public void debutJeu(Jeu jeu) {
        // Recuperation du jeu
        this.jeu = jeu;
        this.pseudoLabel.setText("Bonjour " + jeu.getJoueur().getPseudo() + System.getProperty("line.separator") + "Montant en banque : " + decimalFormat.format(jeu.getJoueur().getArgent()) + monnaie + System.getProperty("line.separator"));

        // Affichage du calendrier
        this.afficheCalendrier();

//        this.jeu.getCalendrier().setDureeJour(200);
//        jeu.getParametres().setVitessePonteOeuf(20);

        // ajustement oeuf par rapport au jour
        this.jeu.getJoueur().getFerme().ajustementProgressOeuf(jeu.getCalendrier().getProgressJour(), jeu.getCalendrier().getHeureActuelle());
        // recuperation de l'etat de la barre de progression pour les heures / oeufs

        double vitesseHeure = jeu.getParametres().getVitessePonteOeuf() - (jeu.getParametres().getVitessePonteOeuf() * jeu.getJoueur().getFerme().getEtatProgressOeuf());
        progressBarStartTimelineHeureEnCours(1, vitesseHeure);

        System.out.println("vitesse ponte oeuf : " + jeu.getParametres().getVitessePonteOeuf());
        System.out.println("etat progress oeuf : " + jeu.getJoueur().getFerme().getEtatProgressOeuf());
        System.out.println("vitesseHeure: " + vitesseHeure);


        // recuperation de l'etat de la barre de progression pour la journee
        double vitesse = jeu.getCalendrier().getDureeJour() - (jeu.getCalendrier().getDureeJour() * jeu.getCalendrier().getProgressJour());

        progressBarStartTimelineJourneeEnCours(1, vitesse);

        // demarrage des distributeurs
        demarrageDistributeurs();

        // demarrage des livraisons
        demarrageLivraisons();

        // desactivation des activites non acquises
        // test activation ferme
        testFerme();
        testDistributeur();
        testLivraison();

        jeu.valeurEntreprise();

        // affichage des barres de progression (mode dev)
        jeu.afficheProgression(paneProgress);
    }


    /**
     * Test si l'activite ferme est active
     * 0 pour non
     * et 1 pour oui
     */
    public void testFerme() {
        // on verifie si la ferme est active
        // Gestion des boutons Acheter ferme / Gerer la ferme
        // de l'achat ferme
        if (jeu.getJoueur().getFermeActive() == 0) {
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

    public void testDistributeur() {
        // on verifie si le distributeur commun est débloqué
        // si débloqué => bouton visible
        BigDecimal tarifDistributeurBC = jeu.getParametres().getPrixDistributeurBC();

        if (jeu.getJoueur().getDistributeurBCActive() == 1 || jeu.getJoueur().isArgent(tarifDistributeurBC)) {
            btnDistributeurs.setDisable(false);
            labelBlocageDistributeur.setVisible(false);
        } else {
            btnDistributeurs.setDisable(true);
            labelBlocageDistributeur.setVisible(true);
            String formattedString = "Débloqué à partir de " + decimalFormat.format(tarifDistributeurBC) + monnaie;
            labelBlocageDistributeur.setText(formattedString);
        }
    }
    /**
     * On verifie si le joueur a assez d'argent pour acheter le premier service de livraison
     * ou s'il est actif
     */
    public void testLivraison() {
        // recuperation du prix de la livraison en scooter
        BigDecimal tarifScooter = jeu.getJoueur().getLivraisonScooter().getPrixVehicule();
        System.out.println("Prix du scooter : " + tarifScooter);
        // active le bouton si livraison 1 active
        // verifie si l'argent en banque permet d'acheter la livraison en scooter
        if (jeu.getJoueur().getLivraison1Active() == 1 || jeu.getJoueur().isArgent(tarifScooter)) {
            btnLivraison.setDisable(false);
            labelBlocageLivraison.setVisible(false);
        } else {
            btnLivraison.setDisable(true);
            labelBlocageLivraison.setVisible(true);
            String formattedString = "Débloqué à partir de " + decimalFormat.format(tarifScooter) + monnaie;
            labelBlocageLivraison.setText(formattedString);
        }
    }

    /**
     * Arrivee dans le jeu
     * desactivation de la ferme
     */
    public void switchToAchatFerme(ActionEvent event) {
        // achat de la ferme
        // Tester le pseudo
        // Test nombre de caractères
        // Test si non vide
        System.out.println("Lancement du jeu, déblocage de la ferme");
        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("ferme.fxml"));
            root = loader.load();
            FermeController fermeController = loader.getController();
            fermeController.nouveau(jeu);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            // Permet de récupérer le gestionnaire d'événements pour la fermeture de la fenêtre
            stage.setOnCloseRequest(fermeController::onWindowClose);
            stage.show();
            stage.centerOnScreen();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Permet d'executer la fenetre de gestion de la ferme
     *
     * @param event
     */
    public void switchToFerme(ActionEvent event) {

        // Tester le pseudo
        // Test nombre de caractères
        // Test si non vide

        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("ferme.fxml"));
            root = loader.load();

            FermeController fermeController = loader.getController();
            fermeController.demarrer(jeu);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            // Permet de récupérer le gestionnaire d'événements pour la fermeture de la fenêtre
            stage.setOnCloseRequest(fermeController::onWindowClose);
            stage.show();
            stage.centerOnScreen();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Permet d'executer la fenetre de gestion des distributeurs
     *
     * @param event
     */
    public void switchToDistributeurs(ActionEvent event) {

        // Tester le pseudo
        // Test nombre de caractères
        // Test si non vide
        System.out.println("Menu de gestion des distributeurs");

        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("distributeurs.fxml"));
            root = loader.load();

            GestionDistributeursController gestionDistributeursController = loader.getController();
            gestionDistributeursController.demarrer(jeu);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            // Permet de récupérer le gestionnaire d'événements pour la fermeture de la fenêtre
            stage.setOnCloseRequest(gestionDistributeursController::onWindowClose);
            stage.show();
            stage.centerOnScreen();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Arrivee dans le jeu
     * desactivation de la ferme
     */
    public void switchToLivraisons(ActionEvent event) {
        // achat des livraisons

        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("livraisons.fxml"));
            root = loader.load();
            GestionLivraisonsController gestionLivraisonsController = loader.getController();
            gestionLivraisonsController.demarrer(jeu);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            // Permet de récupérer le gestionnaire d'événements pour la fermeture de la fenêtre
            stage.setOnCloseRequest(gestionLivraisonsController::onWindowClose);
            stage.show();
            stage.centerOnScreen();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Gere le bouton pour passer sur la fenetre de la banque
     *
     * @param event
     */
    public void switchToBanque(ActionEvent event) {
        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("banque.fxml"));
            root = loader.load();
            Banquecontroller banquecontroller = loader.getController();
            banquecontroller.startFenetre(jeu);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            // Permet de récupérer le gestionnaire d'événements pour la fermeture de la fenêtre
            stage.setOnCloseRequest(banquecontroller::onWindowClose);
            stage.show();
            stage.centerOnScreen();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Sauvegarde les donnees
     * Ferme la partie du joueur => ne renvoie pas l'instance jeu
     * Permet de revenir au menu principal
     *
     * @param event
     */
    public void retourMenu(ActionEvent event) {
        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();

        // sauvegardes
        try {
            this.jeu.sauvegardejeu(this.progressOeufs, this.progressJour);
            this.jeu.sauvegardeCredit();
        } catch (Exception e) {
            System.out.println(e);
        }

        // ouverture fenetre main
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("main.fxml"));
            root = loader.load();
            MainController mainController = loader.getController();
            mainController.onLoad();
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
        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();

        // Sauvegarde de la base de donnees
        System.out.println("fermeture fenetre : Sauvegarde");
        try {
            this.jeu.sauvegardejeu(this.progressOeufs, this.progressJour);
            this.jeu.sauvegardeCredit();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Bouton de sortie du jeu
     *
     * @param event
     */
    public void exitJeu(ActionEvent event) {
        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();

        this.jeu.sauvegardejeu(this.progressOeufs, this.progressJour);
        this.jeu.sauvegardeCredit();
        // Code pour quitter l'application
        Platform.exit();
    }


    public void afficheCalendrier() {
        // met en place la semaine en cours
        this.jeu.getCalendrier().setSemainesEnCours();
        this.jourEnCours = jeu.getCalendrier().getNumJour();
        CreditEnCours creditEnCours = jeu.getJoueur().getCreditEnCours();
        if (jeu.getJoueur().getCreditEnCours() != null) {
            if (jeu.getJoueur().getCreditEnCours().getTermine() == 0) {
                jeu.getCalendrier().createSemaine1Calendrier(paneSemaine1, jeu.getJoueur().getCreditEnCours().getDateProchaineMensualite());
                jeu.getCalendrier().createSemaine2Calendrier(paneSemaine2, jeu.getJoueur().getCreditEnCours().getDateProchaineMensualite());
            } else {
                jeu.getCalendrier().createSemaine1Calendrier(paneSemaine1, 0);
                jeu.getCalendrier().createSemaine2Calendrier(paneSemaine2, 0);
            }
        } else {
            jeu.getCalendrier().createSemaine1Calendrier(paneSemaine1, 0);
            jeu.getCalendrier().createSemaine2Calendrier(paneSemaine2, 0);
        }
    }

    public ProgressBar getProgressJour() {
        return progressJour;
    }

    /**
     * declare la ProgressBar afin de pouvoir l'utiliser
     * pour l'effet de remplissage
     *
     * @return
     */
    public ProgressBar getProgressOeufs() {
        return progressOeufs;
    }

    /**
     * Met à jour la barre de progression pour la journee
     * avec un demarrage de la barre par rapport à la sauvegarde
     *
     * @param cycle   : 1 pour on effectue une seule fois
     * @param vitesse : calculee suivant le temps restant à effectuer
     */
    public void progressBarStartTimelineJourneeEnCours(int cycle, double vitesse) {
        ProgressBar getProgressJour = getProgressJour();
        // Réinitialise la barre de progression à 0
        getProgressJour.setProgress(this.jeu.getCalendrier().getProgressJour());
        timelineCalendrier = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(getProgressJour.progressProperty(), this.jeu.getCalendrier().getProgressJour())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("jour terminé");
                    // incremente un jour et remet l'heure à 1
                    this.jeu.getCalendrier().setJourSuivant();
                    // mise à jour du calendrier
                    afficheCalendrier();
                }, new KeyValue(getProgressJour.progressProperty(), 1))
        );
        timelineCalendrier.setOnFinished(event -> {
            if (cycle == 1) {
                // Lancer la deuxième exécution de la méthode progressBarStartTimeline
                jeu.getCalendrier().setProgressJour(0);
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartTimelineJournee(cycle - 1, jeu.getCalendrier().getDureeJour() - jeu.getParametres().getVitessePonteOeuf());
            }
        });

        if (cycle == 0) {
            timelineCalendrier.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineCalendrier.setCycleCount(cycle);
        }
        timelineCalendrier.play();
    }

    /**
     * Methode qui affiche la progressbar du calendrier
     * met a jour le calendrier avec le jour en cours, banquier...
     *
     * @param cycle
     * @param vitesse
     */

    public void progressBarStartTimelineJournee(int cycle, double vitesse) {
        ProgressBar getProgressJour = getProgressJour();
        // Réinitialise la barre de progression à 0
        getProgressJour.setProgress(0);
        System.out.println("Progress barre : " + getProgressJour.getProgress());
        timelineCalendrier = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(getProgressJour.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Jour terminé ");
                    this.jeu.getCalendrier().setJourSuivant();
                    // mise à jour du calendrier
                    afficheCalendrier();
                }, new KeyValue(getProgressJour.progressProperty(), 1))
        );

        if (cycle == 0) {
            timelineCalendrier.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineCalendrier.setCycleCount(cycle);
        }
        timelineCalendrier.play();
    }

    /**
     * TimeLine des heures
     * grosse difference avec celle des jours : on affiche pas la barre de progression : se passe en coulisses
     * recupere l'etat de la barre de progressOeuf pour l'avancer jusqu'au bout
     * et commencer une autre timelineHeures classique
     *
     * @param cycle
     * @param vitesse
     */
    public void progressBarStartTimelineHeureEnCours(int cycle, double vitesse) {
        ProgressBar progressOeufs = getProgressOeufs();
        // Réinitialise la barre de progression à 0
        progressOeufs.setProgress(this.jeu.getJoueur().getFerme().getEtatProgressOeuf());
        timelineHeure = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressOeufs.progressProperty(), this.jeu.getJoueur().getFerme().getEtatProgressOeuf())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    this.jeu.getCalendrier().setIncrementHeure();
                    System.out.println("Heure actuelle : " + jeu.getCalendrier().getHeureActuelle());
                    // ajoute le nombre de poules necesaires
                    majFerme();
                    System.out.println("Oeuf terminé");
                }, new KeyValue(progressOeufs.progressProperty(), 1))
        );
        timelineHeure.setOnFinished(event -> {
            // recalcul de la vitesse suivant le niveau de la barre de progression
            progressBarStartTimelineHeure(cycle - 1, jeu.getParametres().getVitessePonteOeuf());
        });

        if (cycle == 0) {
            timelineHeure.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineHeure.setCycleCount(cycle);
        }
        timelineHeure.play();
    }

    /**
     * Barre de progression invisible pour compter les heures
     *
     * @param cycle
     * @param vitesse
     */
    public void progressBarStartTimelineHeure(int cycle, double vitesse) {
        ProgressBar progressOeufs = getProgressOeufs();
        // Réinitialise la barre de progression à 0
        progressOeufs.setProgress(0);
        timelineHeure = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressOeufs.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Ajoute une heure");
                    this.jeu.getCalendrier().setIncrementHeure();
                    System.out.println("Heure actuelle : " + jeu.getCalendrier().getHeureActuelle());
                    // ajoute le nombre de poules necesaires
                    majFerme();
                    System.out.println("Oeuf terminé");
                }, new KeyValue(progressOeufs.progressProperty(), 1))
        );

        if (cycle == 0) {
            timelineHeure.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineHeure.setCycleCount(cycle);
        }
        timelineHeure.play();
    }

    /**
     * Permet de stopper la timeline passée en paramètres
     *
     * @param laTimeline
     */
    private void progressBarStop(Timeline laTimeline) {
        if (laTimeline != null) {
            laTimeline.stop();
            laTimeline = null;
            System.out.println("Arret de la barre de progression");
        }
    }

    /**
     * donne l'état de la timeline
     *
     * @return
     */
    private Boolean isProgressBar(Timeline laTimeline) {
        if (laTimeline == null) {
            laTimeline = null;
            System.out.println("Barre de progression terminee");
            return false;
        }
        return true;
    }

    /**
     * relance une timeline
     */
    // Méthode pour reprendre la Timeline
    public void progressBarPlay(Timeline laTimeline) {
        if (laTimeline != null) {
            laTimeline.play();
            System.out.println("progress play pas null");
        }
        System.out.println("play");
    }

    /**
     * Met en pause la timeline
     */
    // Méthode pour mettre en pause la Timeline
    public void progressBarPause(Timeline laTimeline) {
        if (laTimeline != null) {
            laTimeline.pause();
            System.out.println("progress pause pas null");
        }
        System.out.println("pause");
    }

    /**
     * A effectuer lorsqu'une heure est ecoulee
     * ajoute un oeuf suivant le nombre de poules dans le poulailler
     */
    public void majFerme() {
        long nbOeufsAAjouter = this.jeu.getJoueur().getFerme().getNbOeufs() + this.jeu.getJoueur().getFerme().getNbPoules();
        this.jeu.getJoueur().getFerme().setNbOeufs(nbOeufsAAjouter);
        System.out.println("ajout de " + nbOeufsAAjouter + " oeuf(s)");
    }

    /**
     * Permet d'ajuster les distributeurs et les demarrer s'ils sont actifs
     */
    public void demarrageDistributeurs() {
        // Demmarage des distributueurs
        // Boissons chaudes
        if (jeu.getJoueur().getDistributeursActive() == 1 && jeu.getJoueur().getBoissonsChaudes().getNbDistributeurs() > 0) {
            double vitesse = jeu.getParametres().getVitesseBC() - (jeu.getParametres().getVitesseBC() * jeu.getJoueur().getBoissonsChaudes().getEtatProgressDistributeur());
            this.progressBarStartTimelineEncoursBC(1, vitesse);
        }

        // Boissons fraiches
        if (jeu.getJoueur().getDistributeursActive() == 1 && jeu.getJoueur().getBoissonsFraiches().getNbDistributeurs() > 0) {
            double vitesse = jeu.getParametres().getVitesseBF() - (jeu.getParametres().getVitesseBF() * jeu.getJoueur().getBoissonsFraiches().getEtatProgressDistributeur());
            this.progressBarStartTimelineEncoursBF(1, vitesse);
        }

        // Confiseries
        if (jeu.getJoueur().getDistributeursActive() == 1 && jeu.getJoueur().getConfiseries().getNbDistributeurs() > 0) {
            double vitesse = jeu.getParametres().getVitesseCo() - (jeu.getParametres().getVitesseCo() * jeu.getJoueur().getConfiseries().getEtatProgressDistributeur());
            this.progressBarStartTimelineEncoursCo(1, vitesse);
        }

        // Sandwichs
        if (jeu.getJoueur().getDistributeursActive() == 1 && jeu.getJoueur().getSandwichs().getNbDistributeurs() > 0) {
            double vitesse = jeu.getParametres().getVitesseSa() - (jeu.getParametres().getVitesseSa() * jeu.getJoueur().getSandwichs().getEtatProgressDistributeur());
            this.progressBarStartTimelineEncoursSa(1, vitesse);
        }
        // ajustement oeuf par rapport au jour
        this.jeu.getJoueur().getFerme().ajustementProgressOeuf(jeu.getCalendrier().getProgressJour(), jeu.getCalendrier().getHeureActuelle());
    }

    /**
     * Demarrage des barres de progression des livraisons
     */
    public void demarrageLivraisons() {
        if(jeu.getJoueur().getLivraison1Active() == 1){
            // recuperation de l'etat de la barre de progression pour la livraison en scooter
            double vitesseScooter = jeu.getJoueur().getLivraisonScooter().getVitesseLivraion() - (jeu.getJoueur().getLivraisonScooter().getVitesseLivraion() * jeu.getJoueur().getLivraisonScooter().getEtatProgressLivraison());
            System.out.println("Vitesse scooter : " + vitesseScooter);
            progressBarStartScooterEnCours(1, vitesseScooter);
        }
        if(jeu.getJoueur().getLivraison2Active() == 1){
            // recuperation de l'etat de la barre de progression pour la livraison en camionette
            double vitesseCamionette = jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion() - (jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion() * jeu.getJoueur().getLivraisonCamionette().getEtatProgressLivraison());
            System.out.println("Vitesse camionette : " + vitesseCamionette);
            progressBarStartCamionetteEnCours(1, vitesseCamionette);
        }
        if(jeu.getJoueur().getLivraison3Active() == 1){
            // recuperation de l'etat de la barre de progression pour la livraison en petit camion
            double vitessePetitCamion = jeu.getJoueur().getLivraisonPetitCamion().getVitesseLivraion() - (jeu.getJoueur().getLivraisonPetitCamion().getVitesseLivraion() * jeu.getJoueur().getLivraisonPetitCamion().getEtatProgressLivraison());
            System.out.println("Vitesse petit camion : " + vitessePetitCamion);
            progressBarStartPetitCamionEnCours(1, vitessePetitCamion);
        }
        if(jeu.getJoueur().getLivraison4Active() == 1){
            // recuperation de l'etat de la barre de progression pour la livraison en poids lours
            double vitessePoidsLourd = jeu.getJoueur().getLivraisonPoidsLourd().getVitesseLivraion() - (jeu.getJoueur().getLivraisonPoidsLourd().getVitesseLivraion() * jeu.getJoueur().getLivraisonPoidsLourd().getEtatProgressLivraison());
            System.out.println("Vitesse petit camion : " + vitessePoidsLourd);
            progressBarStartPodisLourdEnCours(1, vitessePoidsLourd);
        }
    }

    /**
     * Barres de progression des distributeurs
     */
    /**
     * Met à jour la barre de progression pour distributeur de boissons chaudes
     *
     * @param cycle
     * @param vitesse
     */
    public void progressBarStartTimelineEncoursBC(int cycle, double vitesse) {
        ProgressBar progressBarBC = getProgressBC();
        // Réinitialise la barre de progression à 0
        progressBarBC.setProgress(this.jeu.getJoueur().getBoissonsChaudes().getEtatProgressDistributeur());
        timelineBC = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBarBC.progressProperty(), this.jeu.getJoueur().getBoissonsChaudes().getEtatProgressDistributeur())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Boisson chaude terminée");
                    // ajoute un nombre de marchandises correspondantes au nombre de distributeurs
                    this.majProgressBc();
                }, new KeyValue(progressBarBC.progressProperty(), 1))
        );
        timelineBC.setOnFinished(event -> {
            if (cycle == 1) {
                // Lancer la deuxième exécution de la méthode progressBarStartTimeline
                jeu.getJoueur().getBoissonsChaudes().setEtatProgressDistributeur(0);
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartBC(cycle - 1, jeu.getParametres().getVitesseBC());
            }
        });

        if (cycle == 0) {
            timelineBC.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineBC.setCycleCount(cycle);
        }
        timelineBC.play();
    }

    /**
     * Barre de progressions Distributeur Boissons Chaudes
     */
    public void progressBarStartBC(int cycle, double vitesse) {
        ProgressBar progressBarBC = getProgressBC();
        // Réinitialise la barre de progression à 0
        progressBarBC.setProgress(0);
        timelineBC = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBarBC.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Distributeur de boissons chaudes prêt");
                    // ajoute un nombre de marchandises correspondantes au nombre de distributeurs
                    this.majProgressBc();
                }, new KeyValue(progressBarBC.progressProperty(), 1))
        );

        if (cycle == 0) {
            timelineBC.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineBC.setCycleCount(cycle);
        }
        timelineBC.play();
    }

    /**
     * Met à jour la barre de progression pour distributeur de boissons chaudes
     *
     * @param cycle
     * @param vitesse
     */
    public void progressBarStartTimelineEncoursBF(int cycle, double vitesse) {
        ProgressBar progressBarBF = getProgressBF();
        // Réinitialise la barre de progression à 0
        progressBarBF.setProgress(this.jeu.getJoueur().getBoissonsFraiches().getEtatProgressDistributeur());
        timelineBF = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBarBF.progressProperty(), this.jeu.getJoueur().getBoissonsFraiches().getEtatProgressDistributeur())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Boisson fraiche terminée");
                    // ajoute un nombre de marchandises correspondantes au nombre de distributeurs
                    this.majProgressBF();
                }, new KeyValue(progressBarBF.progressProperty(), 1))
        );
        timelineBF.setOnFinished(event -> {
            if (cycle == 1) {
                // Lancer la deuxième exécution de la méthode progressBarStartTimeline
                jeu.getJoueur().getBoissonsFraiches().setEtatProgressDistributeur(0);
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartBF(cycle - 1, jeu.getParametres().getVitesseBF());
            }
        });

        if (cycle == 0) {
            timelineBF.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineBF.setCycleCount(cycle);
        }
        timelineBF.play();
    }

    /**
     * Barre de progressions Distributeur Boissons Chaudes
     */
    public void progressBarStartBF(int cycle, double vitesse) {
        ProgressBar progressBarBF = getProgressBF();
        // Réinitialise la barre de progression à 0
        progressBarBF.setProgress(0);
        timelineBF = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBarBF.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Distributeur de boissons fraiches prêt");
                    // ajoute un nombre de marchandises correspondantes au nombre de distributeurs
                    this.majProgressBF();
                }, new KeyValue(progressBarBF.progressProperty(), 1))
        );
        if (cycle == 0) {
            timelineBF.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineBF.setCycleCount(cycle);
        }
        timelineBF.play();
    }

    /**
     * Met à jour la barre de progression pour distributeur de confiseries
     *
     * @param cycle
     * @param vitesse
     */
    public void progressBarStartTimelineEncoursCo(int cycle, double vitesse) {
        ProgressBar progressBarCo = getProgressCo();
        // Réinitialise la barre de progression à 0
        progressBarCo.setProgress(this.jeu.getJoueur().getConfiseries().getEtatProgressDistributeur());
        timelineCo = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBarCo.progressProperty(), this.jeu.getJoueur().getConfiseries().getEtatProgressDistributeur())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Boisson fraiche terminée");
                    // ajoute un nombre de marchandises correspondantes au nombre de distributeurs
                    this.majProgressCo();
                }, new KeyValue(progressBarCo.progressProperty(), 1))
        );
        timelineCo.setOnFinished(event -> {
            if (cycle == 1) {
                // Lancer la deuxième exécution de la méthode progressBarStartTimeline
                jeu.getJoueur().getConfiseries().setEtatProgressDistributeur(0);
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartCo(cycle - 1, jeu.getParametres().getVitesseCo());
            }
        });

        if (cycle == 0) {
            timelineCo.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineCo.setCycleCount(cycle);
        }
        timelineCo.play();
    }

    /**
     * Barre de progressions Distributeur Boissons Chaudes
     */
    public void progressBarStartCo(int cycle, double vitesse) {
        ProgressBar progressBarCo = getProgressCo();
        // Réinitialise la barre de progression à 0
        progressBarCo.setProgress(0);
        timelineCo = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBarCo.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Distributeur de confiseries prêt");
                    // ajoute un nombre de marchandises correspondantes au nombre de distributeurs
                    this.majProgressCo();
                }, new KeyValue(progressBarCo.progressProperty(), 1))
        );

        if (cycle == 0) {
            timelineCo.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineCo.setCycleCount(cycle);
        }
        timelineCo.play();
    }

    /**
     * Met à jour la barre de progression pour distributeur de Sandwichs
     *
     * @param cycle
     * @param vitesse
     */
    public void progressBarStartTimelineEncoursSa(int cycle, double vitesse) {
        ProgressBar progressBarSa = getProgressSa();
        // Réinitialise la barre de progression à 0
        progressBarSa.setProgress(this.jeu.getJoueur().getSandwichs().getEtatProgressDistributeur());
        timelineSa = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBarSa.progressProperty(), this.jeu.getJoueur().getSandwichs().getEtatProgressDistributeur())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Sandwichs terminés");
                    // ajoute un nombre de marchandises correspondantes au nombre de distributeurs
                    this.majProgressSa();
                }, new KeyValue(progressBarSa.progressProperty(), 1))
        );
        timelineSa.setOnFinished(event -> {
            if (cycle == 1) {
                // Lancer la deuxième exécution de la méthode progressBarStartTimeline
                jeu.getJoueur().getSandwichs().setEtatProgressDistributeur(0);
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartSa(cycle - 1, jeu.getParametres().getVitesseSa());
            }
        });

        if (cycle == 0) {
            timelineSa.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineSa.setCycleCount(cycle);
        }
        timelineSa.play();
    }

    /**
     * Barre de progressions Distributeur de Sandwichs
     */
    public void progressBarStartSa(int cycle, double vitesse) {
        ProgressBar progressBarSa = getProgressSa();
        // Réinitialise la barre de progression à 0
        progressBarSa.setProgress(0);
        timelineSa = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBarSa.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Distributeur de sandwichs prêt");
                    // ajoute un nombre de marchandises correspondantes au nombre de distributeurs
                    this.majProgressSa();
                }, new KeyValue(progressBarSa.progressProperty(), 1))
        );

        if (cycle == 0) {
            timelineSa.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineSa.setCycleCount(cycle);
        }
        timelineSa.play();
    }

    /**
     * Barre distributeurs + maj à la fin du progress
     */
    /**
     * Declaration de la barre de progression Distributeur boissons chaudes
     */
    public ProgressBar getProgressBC() {
        return progressBC;
    }

    /**
     * Met a jour le chiffre du nombre de marchandises vendues
     */
    public void majProgressBc() {
        long nbMarchandisesBcEnCours = jeu.getJoueur().getBoissonsChaudes().getNbMarchandises();
        int nbDistributeursBCEnCours = jeu.getJoueur().getBoissonsChaudes().getNbDistributeurs();
        long nouvNombre = nbMarchandisesBcEnCours + nbDistributeursBCEnCours;
        jeu.getJoueur().getBoissonsChaudes().setNbMarchandises(nouvNombre);
        System.out.println("maj du nombre de marchandises vendues dans les distributeurs de Boissons Chaudes : " + nouvNombre);
    }

    /**
     * Declaration de la barre de progression Distributeur boissons chaudes
     */
    public ProgressBar getProgressBF() {
        return progressBF;
    }

    /**
     * Met a jour le chiffre du nombre de marchandises vendues
     */
    public void majProgressBF() {
        long nbMarchandisesBcEnCours = jeu.getJoueur().getBoissonsFraiches().getNbMarchandises();
        int nbDistributeursBCEnCours = jeu.getJoueur().getBoissonsFraiches().getNbDistributeurs();
        long nouvNombre = nbMarchandisesBcEnCours + nbDistributeursBCEnCours;
        jeu.getJoueur().getBoissonsFraiches().setNbMarchandises(nouvNombre);
    }

    /**
     * Declaration de la barre de progression Distributeur boissons chaudes
     */
    public ProgressBar getProgressCo() {
        return progressCo;
    }

    /**
     * Met a jour le chiffre du nombre de marchandises vendues
     */
    public void majProgressCo() {
        long nbMarchandisesCoEnCours = jeu.getJoueur().getConfiseries().getNbMarchandises();
        int nbDistributeursCoEnCours = jeu.getJoueur().getConfiseries().getNbDistributeurs();
        long nouvNombre = nbMarchandisesCoEnCours + nbDistributeursCoEnCours;
        jeu.getJoueur().getConfiseries().setNbMarchandises(nouvNombre);
    }

    /**
     * Declaration de la barre de progression Distributeur de sandwichs
     */
    public ProgressBar getProgressSa() {
        return progressSa;
    }

    /**
     * Met a jour le chiffre du nombre de marchandises vendues
     */
    public void majProgressSa() {
        long nbMarchandisesSaEnCours = jeu.getJoueur().getSandwichs().getNbMarchandises();
        int nbDistributeursSaEnCours = jeu.getJoueur().getSandwichs().getNbDistributeurs();
        long nouvNombre = nbMarchandisesSaEnCours + nbDistributeursSaEnCours;
        jeu.getJoueur().getSandwichs().setNbMarchandises(nouvNombre);
        System.out.println("maj du nombre de marchandises vendues dans les distributeurs de sandwichs : " + nouvNombre);
    }

    // partie progress livraisons
    /**
     * Met à jour la barre de progression pour distributeur de boissons chaudes
     *
     * @param cycle
     * @param vitesse
     */
    public void progressBarStartScooterEnCours(int cycle, double vitesse) {
        ProgressBar progressScooter = getProgressScooter();
        // Réinitialise la barre de progression à 0
        progressScooter.setProgress(this.jeu.getJoueur().getLivraisonScooter().getEtatProgressLivraison());
        timelineScooter = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressScooter.progressProperty(), this.jeu.getJoueur().getLivraisonScooter().getEtatProgressLivraison())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Course en scooter terminée");
                    // ajoute une course au service de livraison
                    this.majProgressScooter();
                    System.out.println("ajout d'un scooter");
                }, new KeyValue(progressScooter.progressProperty(), 1))
        );
        timelineScooter.setOnFinished(event -> {
            if (cycle == 1) {
                // Lancer la deuxième exécution de la méthode progressBarStartTimeline
                jeu.getJoueur().getLivraisonScooter().setEtatProgressLivraison(0);
                System.out.println("fin premiere barre");
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartScooter(cycle - 1, this.jeu.getJoueur().getLivraisonScooter().getVitesseLivraion());
            }
        });
        if (cycle == 0) {
            timelineScooter.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineScooter.setCycleCount(cycle);
        }
        timelineScooter.play();
    }
    /**
     * Barre de progressions Distributeur Boissons Chaudes
     */
    public void progressBarStartScooter(int cycle, double vitesse) {
        ProgressBar progressScooter = getProgressScooter();
        // Réinitialise la barre de progression à 0
        progressScooter.setProgress(0);
        timelineScooter = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressScooter.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Distributeur de boissons chaudes prêt");
                    // ajoute un nombre de marchandises correspondantes au nombre de distributeurs
                    this.majProgressScooter();
                }, new KeyValue(progressScooter.progressProperty(), 1))
        );

        if (cycle == 0) {
            timelineScooter.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineScooter.setCycleCount(cycle);
        }
        timelineScooter.play();
    }

    /**
     * Met a jour le chiffre du nombre de livraisons effectuées
     */
    public void majProgressScooter() {
        long nbLivraisonsEncours = jeu.getJoueur().getLivraisonScooter().getNbCourses();
        int nbLivraisonsScooterEnCours = jeu.getJoueur().getLivraisonScooter().getNbVehicules();
        long nouvNombre = nbLivraisonsEncours + nbLivraisonsScooterEnCours;
        jeu.getJoueur().getLivraisonScooter().setNbCourses(nouvNombre);
        System.out.println("maj du nombre de livraisons effectuées en scooter : " + nouvNombre);
    }

    /**
     * Permet de gerer la barre de progression du scooter
     * @return
     */
    public ProgressBar getProgressScooter() {
        return progressScooter;
    }

    // progress camionettes
    /**
     * Met à jour la barre de progression pour le service de livraison de camionettes
     *
     * @param cycle
     * @param vitesse
     */

    public void progressBarStartCamionetteEnCours(int cycle, double vitesse) {
        ProgressBar progressCamionette = getProgressCamionette();
        // Réinitialise la barre de progression à 0
        progressCamionette.setProgress(this.jeu.getJoueur().getLivraisonCamionette().getEtatProgressLivraison());
        timelineCamionette = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressCamionette.progressProperty(), this.jeu.getJoueur().getLivraisonCamionette().getEtatProgressLivraison())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Course en camionette terminée");
                    // ajoute une course au service de livraison en camionette
                    this.majProgressCamionette();
                }, new KeyValue(progressCamionette.progressProperty(), 1))
        );
        timelineCamionette.setOnFinished(event -> {
            if (cycle == 1) {
                // Lancer la deuxième exécution de la méthode progressBarStartTimeline
                jeu.getJoueur().getLivraisonCamionette().setEtatProgressLivraison(0);
                System.out.println("fin course camionette");
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartCamionette(cycle - 1, this.jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion());
            }
        });
        if (cycle == 0) {
            timelineCamionette.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineCamionette.setCycleCount(cycle);
        }
        timelineCamionette.play();
    }

    /**
     * Barre de progressions Distributeur Boissons Chaudes
     */
    public void progressBarStartCamionette(int cycle, double vitesse) {
        ProgressBar progressCamionette = getProgressCamionette();
        // Réinitialise la barre de progression à 0
        progressCamionette.setProgress(0);
        timelineCamionette = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressCamionette.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("fin course camionette");
                    // ajoute d'une course au service de livraion en camionette
                    this.majProgressCamionette();
                }, new KeyValue(progressCamionette.progressProperty(), 1))
        );

        if (cycle == 0) {
            timelineCamionette.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineCamionette.setCycleCount(cycle);
        }
        timelineCamionette.play();
    }

    /**
     * Met a jour le chiffre du nombre de livraisons effectuées
     */
    public void majProgressCamionette() {
        long nbLivraisonsEncours = jeu.getJoueur().getLivraisonCamionette().getNbCourses();
        int nbLivraisonsCamionetteEnCours = jeu.getJoueur().getLivraisonCamionette().getNbVehicules();
        long nouvNombre = nbLivraisonsEncours + nbLivraisonsCamionetteEnCours;
        jeu.getJoueur().getLivraisonCamionette().setNbCourses(nouvNombre);
        System.out.println("maj du nombre de livraisons effectuées en camionette : " + nouvNombre);
    }

    /**
     * Permet de gerer la barre de progression du scooter
     * @return
     */
    public ProgressBar getProgressCamionette() {
        return progressCamionette;
    }

    // progress petit camion
    /**
     * Met à jour la barre de progression pour le service de livraison en petit camion
     *
     * @param cycle
     * @param vitesse
     */

    public void progressBarStartPetitCamionEnCours(int cycle, double vitesse) {
        ProgressBar progressPetitCamion = getProgressPetitCamion();
        // Réinitialise la barre de progression à 0
        progressPetitCamion.setProgress(this.jeu.getJoueur().getLivraisonPetitCamion().getEtatProgressLivraison());
        timelinePetitCamion = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressPetitCamion.progressProperty(), this.jeu.getJoueur().getLivraisonPetitCamion().getEtatProgressLivraison())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Course en petit camion terminée");
                    // ajoute une course au service de livraison en petit camion
                    this.majProgressPetitCamion();
                }, new KeyValue(progressPetitCamion.progressProperty(), 1))
        );
        timelinePetitCamion.setOnFinished(event -> {
            if (cycle == 1) {
                // Lancer la deuxième exécution de la méthode progressBarStartTimeline
                jeu.getJoueur().getLivraisonPetitCamion().setEtatProgressLivraison(0);
                System.out.println("fin course camionette");
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartPetitCamion(cycle - 1, this.jeu.getJoueur().getLivraisonPetitCamion().getVitesseLivraion());
            }
        });
        if (cycle == 0) {
            timelinePetitCamion.setCycleCount(Animation.INDEFINITE);
        } else {
            timelinePetitCamion.setCycleCount(cycle);
        }
        timelinePetitCamion.play();
    }

    /**
     * Barre de progressions service de livraison en petit camion
     */
    public void progressBarStartPetitCamion(int cycle, double vitesse) {
        ProgressBar progressPetitCamion = getProgressPetitCamion();
        // Réinitialise la barre de progression à 0
        progressPetitCamion.setProgress(0);
        timelinePetitCamion = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressPetitCamion.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("fin course petit camion");
                    // ajoute d'une course au service de livraion en petit camion
                    this.majProgressPetitCamion();
                }, new KeyValue(progressPetitCamion.progressProperty(), 1))
        );

        if (cycle == 0) {
            timelinePetitCamion.setCycleCount(Animation.INDEFINITE);
        } else {
            timelinePetitCamion.setCycleCount(cycle);
        }
        timelinePetitCamion.play();
    }

    /**
     * Met a jour le chiffre du nombre de livraisons effectuées
     */
    public void majProgressPetitCamion() {
        long nbLivraisonsEncours = jeu.getJoueur().getLivraisonPetitCamion().getNbCourses();
        int nbLivraisonsPetitCamionEnCours = jeu.getJoueur().getLivraisonPetitCamion().getNbVehicules();
        long nouvNombre = nbLivraisonsEncours + nbLivraisonsPetitCamionEnCours;
        jeu.getJoueur().getLivraisonPetitCamion().setNbCourses(nouvNombre);
        System.out.println("maj du nombre de livraisons effectuées en petit camion : " + nouvNombre);
    }

    /**
     * Permet de gerer la barre de progression du petit camion
     * @return
     */
    public ProgressBar getProgressPetitCamion() {
        return progressPetitCamion;
    }

    // progress poids lourd
    /**
     * Met à jour la barre de progression pour le service de livraison en poids lourd
     *
     * @param cycle
     * @param vitesse
     */

    public void progressBarStartPodisLourdEnCours(int cycle, double vitesse) {
        ProgressBar progressPoidsLourd = getProgressPoidsLourd();
        // Réinitialise la barre de progression à 0
        progressPoidsLourd.setProgress(this.jeu.getJoueur().getLivraisonPoidsLourd().getEtatProgressLivraison());
        timelinePoidsLourd = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressPoidsLourd.progressProperty(), this.jeu.getJoueur().getLivraisonPoidsLourd().getEtatProgressLivraison())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Course en poids lourd terminée");
                    // ajoute une course au service de livraison en poids lourd
                    this.majProgressPoidsLourd();
                }, new KeyValue(progressPoidsLourd.progressProperty(), 1))
        );
        timelinePoidsLourd.setOnFinished(event -> {
            if (cycle == 1) {
                // Lancer la deuxième exécution de la méthode progressBarStartTimeline
                jeu.getJoueur().getLivraisonPoidsLourd().setEtatProgressLivraison(0);
                System.out.println("fin course poids lourd");
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartPoidsLourd(cycle - 1, this.jeu.getJoueur().getLivraisonPoidsLourd().getVitesseLivraion());
            }
        });
        if (cycle == 0) {
            timelinePoidsLourd.setCycleCount(Animation.INDEFINITE);
        } else {
            timelinePoidsLourd.setCycleCount(cycle);
        }
        timelinePoidsLourd.play();
    }

    /**
     * Barre de progressions service de livraison en petit camion
     */
    public void progressBarStartPoidsLourd(int cycle, double vitesse) {
        ProgressBar progressPoidsLourd = getProgressPoidsLourd();
        // Réinitialise la barre de progression à 0
        progressPoidsLourd.setProgress(0);
        timelinePoidsLourd = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressPoidsLourd.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("fin course petit camion");
                    // ajoute d'une course au service de livraion en poids lourd
                    this.majProgressPoidsLourd();
                }, new KeyValue(progressPoidsLourd.progressProperty(), 1))
        );

        if (cycle == 0) {
            timelinePoidsLourd.setCycleCount(Animation.INDEFINITE);
        } else {
            timelinePoidsLourd.setCycleCount(cycle);
        }
        timelinePoidsLourd.play();
    }

    /**
     * Met a jour le chiffre du nombre de livraisons effectuées
     */
    public void majProgressPoidsLourd() {
        long nbLivraisonsEncours = jeu.getJoueur().getLivraisonPoidsLourd().getNbCourses();
        int nbLivraisonsPoidsLourdEnCours = jeu.getJoueur().getLivraisonPoidsLourd().getNbVehicules();
        long nouvNombre = nbLivraisonsEncours + nbLivraisonsPoidsLourdEnCours;
        jeu.getJoueur().getLivraisonPoidsLourd().setNbCourses(nouvNombre);
        System.out.println("maj du nombre de livraisons effectuées en poids lourd : " + nouvNombre);
    }

    /**
     * Permet de gerer la barre de progression du petit camion
     * @return
     */
    public ProgressBar getProgressPoidsLourd() {
        return progressPoidsLourd;
    }
    // sauvegardes
    /**
     * Fermeture des barres de progression : enregistrement de l'état + stop des barres de progress
     * Sauvegarde date deco
     */
    public void fermetureProgress(){
        // sauvegarde des barres de progression
        this.jeu.getCalendrier().setProgressJour(this.progressJour.getProgress());
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

        // on stoppe les barres de progression;
        this.progressBarStop(timelineHeure);
        this.progressBarStop(timelineCalendrier);
        this.progressBarStop(timelineBC);
        this.progressBarStop(timelineBF);
        this.progressBarStop(timelineCo);
        this.progressBarStop(timelineSa);
        this.progressBarStop(timelineScooter);
        this.progressBarStop(timelineCamionette);
        this.progressBarStop(timelinePetitCamion);
        this.progressBarStop(timelinePoidsLourd);

        // on enregistre l'heure de switch de fenetre
        this.jeu.getJoueur().getFerme().setDateDeco(LocalDateTime.now());
    }
}

//    /**
//     * test de methode de barre de progression avec action possible suivant le temps ecoulé
//     * n'est pas utilisée
//     * @param cycle
//     * @param vitesse
//     */
//    private void progressBarStartTimelineJourneeAnc(int cycle, double vitesse) {
//        vitesse = 120;
//        ProgressBar getProgressJour = getProgressJour();
//        getProgressJour.setProgress(0);
//
//        // Ajoute un ChangeListener pour la propriété de progression de la barre de progression
//        getProgressJour.progressProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                // newValue contient la nouvelle valeur de progression (entre 0 et 1)
//                if (newValue.doubleValue() < 0.2) { // 0.2 représente 20% de progression
//                    // Exécutez votre méthode ici lorsque la progression atteint 20%
//                    getProgressJour.setStyle("-fx-accent: black;");
//                    System.out.println(newValue.doubleValue());
//                }
//                if (newValue.doubleValue() >= 0.2 && newValue.doubleValue() < 0.4) { // 0.2 représente 20% de progression
//                    // Exécutez votre méthode ici lorsque la progression atteint 20%
//                    getProgressJour.setStyle("-fx-accent: red;");
//                    System.out.println(newValue.doubleValue());
//                }
//                if (newValue.doubleValue() >= 0.4 && newValue.doubleValue() < 0.6) { // 0.2 représente 40% de progression
//                    // Exécutez votre méthode ici lorsque la progression atteint 20%
//                    getProgressJour.setStyle("-fx-accent: green;");
//                    System.out.println(newValue.doubleValue());
//                }
//                if (newValue.doubleValue() >= 0.6 && newValue.doubleValue() < 0.8) { // 0.2 représente 60% de progression
//                    // Exécutez votre méthode ici lorsque la progression atteint 20%
//                    getProgressJour.setStyle("-fx-accent: yellow;");
//                    System.out.println(newValue.doubleValue());
//                }
//                if (newValue.doubleValue() >= 0.8 && newValue.doubleValue() < 1) { // 0.2 représente 80% de progression
//                    // Exécutez votre méthode ici lorsque la progression atteint 20%
//                    getProgressJour.setStyle("-fx-accent: blue;");
//                    System.out.println(newValue.doubleValue());
//                }
//            }
//        });
//
//        timelineCalendrier = new Timeline(
//                new KeyFrame(Duration.ZERO, new KeyValue(getProgressJour.progressProperty(), 0)),
//                new KeyFrame(Duration.seconds(vitesse / 5), e -> {
//                    System.out.println("coucou");
//                }, new KeyValue(getProgressJour.progressProperty(), 1))
//        );
//
//        if (cycle == 0) {
//            timelineCalendrier.setCycleCount(Animation.INDEFINITE);
//        } else {
//            timelineCalendrier.setCycleCount(cycle);
//        }
//        timelineCalendrier.play();
//    }