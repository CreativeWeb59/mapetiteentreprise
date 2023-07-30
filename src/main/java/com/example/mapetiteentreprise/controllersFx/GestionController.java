package com.example.mapetiteentreprise.controllersFx;

import com.example.mapetiteentreprise.Main;
import com.example.mapetiteentreprise.actions.Outils;
import com.example.mapetiteentreprise.bdd.*;
import com.example.mapetiteentreprise.jeu.Jeu;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
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

import java.text.DecimalFormat;

public class GestionController {
    @FXML
    private Label pseudoLabel;
    @FXML
    private Button btnNew, btnContinuer, btnFerme, btnAchatFerme, btnDistributeurs;

    private String pseudo;
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
    private Pane paneSemaine1, paneSemaine2, paneParentProgressJour;
    @FXML
    private ProgressBar progressJour;
    private Timeline timelineCalendrier, timelineHeure;
    private SimpleDoubleProperty progressionHeure = new SimpleDoubleProperty(); // utilisé pour stocker la valeur de la progressBar Heures

    /**
     * Recupere le nom de la sauvegarde
     * Utile pour relancer une partie
     *
     * @param jeu
     */
    public void debutJeu(Jeu jeu) {
        // Recuperation du jeu
        this.jeu = jeu;
        this.pseudoLabel.setText("Bonjour " + jeu.getJoueur().getPseudo() + System.getProperty("line.separator") + "Montant en banque : " + decimalFormat.format(jeu.getJoueur().getArgent()) + monnaie+ System.getProperty("line.separator"));
        System.out.println(jeu);

        // Affichage du calendrier
        this.afficheCalendrier();

        this.jeu.getCalendrier().setDureeJour(200);
        jeu.getParametres().setVitessePonteOeuf(20);

        // recuperation de l'etat de la barre de progression pour les heures
        double vitesseHeure = jeu.getParametres().getVitessePonteOeuf() - (jeu.getParametres().getVitessePonteOeuf() * jeu.getJoueur().getFerme().getEtatProgressOeuf());
        progressBarStartTimelineHeureEnCours(1, vitesseHeure);

        // recuperation de l'etat de la barre de progression pour la journee
        double vitesse = jeu.getCalendrier().getDureeJour() - (jeu.getCalendrier().getDureeJour() * jeu.getCalendrier().getProgressJour());

        progressBarStartTimelineJourneeEnCours(1, vitesse);



        // desactivation des activites non acquises
        // test activation ferme
        testFerme();
        testDistributeur();

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

        if (jeu.getJoueur().getDistributeursActive() == 0) {
            btnDistributeurs.setDisable(true);
        } else {
            btnDistributeurs.setDisable(false);
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

        // on enregistre le niveauu de la barre de progression dans le calendrier
        this.jeu.getCalendrier().setProgressJour(this.progressJour.getProgress());

        // on stoppe les barres de progression;
        this.progressBarStop(timelineCalendrier);
        this.progressBarStop(timelineHeure);

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
        System.out.println("Lancement de la ferme et de la production d'oeufs");

        // on enregistre le niveauu de la barre de progression dans le calendrier
        this.jeu.getCalendrier().setProgressJour(this.progressJour.getProgress());
        System.out.println("Valeur barre de progression du jour : " + jeu.getCalendrier().getProgressJour());
        System.out.println("Valeur progression heure : " + progressionHeure.getValue());

        // on stoppe les barres de progression;
        this.progressBarStop(timelineCalendrier);
        this.progressBarStop(timelineHeure);

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

        // on enregistre le niveauu de la barre de progression dans le calendrier
        this.jeu.getCalendrier().setProgressJour(this.progressJour.getProgress());
        // on stoppe les barres de progression;
        this.progressBarStop(timelineCalendrier);
        this.progressBarStop(timelineHeure);

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
     * Sauvegarde les donnees
     * Ferme la partie du joueur => ne renvoie pas l'instance jeu
     * Permet de revenir au menu principal
     *
     * @param event
     */
    public void retourMenu(ActionEvent event) {
        // on stoppe les barres de progression;
        this.progressBarStop(timelineCalendrier);
        this.progressBarStop(timelineHeure);
        // sauvegardes
        try {
            sauvegardejeu();
            sauvegardeCredit();
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
        // Sauvegarde de la base de donnees
        System.out.println("fermeture fenetre : Sauvegarde");
        try {
            sauvegardejeu();
            sauvegardeCredit();
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
        sauvegardejeu();
        sauvegardeCredit();
        // Code pour quitter l'application
        Platform.exit();
    }

    /**
     * Permet de sauvegarder la partie dans la Bdd
     */
    public void sauvegardejeu() {
        // recuperation barre de progress
        this.jeu.getCalendrier().setProgressJour(this.progressJour.getProgress());

        // mise a jour instance sauvegarde
        jeu.getSauvegarde().setArgent(jeu.getJoueur().getArgent());
        jeu.getSauvegarde().setNbPoules(jeu.getJoueur().getFerme().getNbPoules());
        jeu.getSauvegarde().setNbOeufs(jeu.getJoueur().getFerme().getNbOeufs());
        jeu.getSauvegarde().setEtatProgressOeuf(jeu.getJoueur().getFerme().getEtatProgressOeuf());

        jeu.getSauvegarde().setDateDeco(jeu.getJoueur().getFerme().getDateDeco());
        jeu.getSauvegarde().setFermeActive(jeu.getJoueur().getFermeActive());
        jeu.getSauvegarde().setDistributeursActive(jeu.getJoueur().getDistributeursActive());
        jeu.getSauvegarde().setDistributeurBCActive(jeu.getJoueur().getDistributeurBCActive());
        jeu.getSauvegarde().setDistributeurBFActive(jeu.getJoueur().getDistributeurBFActive());
        jeu.getSauvegarde().setDistributeurSaActive(jeu.getJoueur().getDistributeurSaActive());
        jeu.getSauvegarde().setDistributeurCoActive(jeu.getJoueur().getDistributeurCoActive());
        jeu.getSauvegarde().setNbDistributeurBC(jeu.getJoueur().getBoissonsChaudes().getNbDistributeurs());
        jeu.getSauvegarde().setNbDistributeurBF(jeu.getJoueur().getBoissonsFraiches().getNbDistributeurs());
        jeu.getSauvegarde().setNbDistributeurSa(jeu.getJoueur().getSandwichs().getNbDistributeurs());
        jeu.getSauvegarde().setNbDistributeurCo(jeu.getJoueur().getConfiseries().getNbDistributeurs());
        jeu.getSauvegarde().setNbMarchandisesBC(jeu.getJoueur().getBoissonsChaudes().getNbMarchandises());
        jeu.getSauvegarde().setNbMarchandisesBF(jeu.getJoueur().getBoissonsFraiches().getNbMarchandises());
        jeu.getSauvegarde().setNbMarchandisesSa(jeu.getJoueur().getSandwichs().getNbMarchandises());
        jeu.getSauvegarde().setNbMarchandisesCo(jeu.getJoueur().getConfiseries().getNbMarchandises());
        jeu.getSauvegarde().setEtatProgressBC(jeu.getJoueur().getBoissonsChaudes().getEtatProgressDistributeur());
        jeu.getSauvegarde().setEtatProgressBF(jeu.getJoueur().getBoissonsFraiches().getEtatProgressDistributeur());
        jeu.getSauvegarde().setEtatProgressSa(jeu.getJoueur().getSandwichs().getEtatProgressDistributeur());
        jeu.getSauvegarde().setEtatProgressCo(jeu.getJoueur().getConfiseries().getEtatProgressDistributeur());
        jeu.getSauvegarde().setNumJourDeco(jeu.getCalendrier().getNumJour());
        jeu.getSauvegarde().setHeureDeco(jeu.getCalendrier().getHeureActuelle());
        jeu.getSauvegarde().setProgressJour(jeu.getCalendrier().getProgressJour());

        System.out.println("Nouvelles valeurs a sauvegarder" + jeu.getSauvegarde());

        // sauvegarde dans la bdd
        ConnectionBdd connectionBdd = new ConnectionBdd();
        connectionBdd.connect();
        SauvegardeService sauvegardeService = new SauvegardeService(connectionBdd);
        try {
            sauvegardeService.majSauvegarde(jeu.getSauvegarde());
        } catch (Exception e) {
            System.out.println(e);
        }
        connectionBdd.close();
    }

    /**
     * Sauvegarde les modifications du credits
     * desactive si pas de credit en cours
     */
    public void sauvegardeCredit() {
        if (jeu.getJoueur().getCreditEnCours() != null) {
            ConnectionBdd connectionBdd = new ConnectionBdd();
            connectionBdd.connect();
            Credits credits = new Credits();
            CreditsService creditsService = new CreditsService(connectionBdd);
            credits.setPseudo(jeu.getJoueur().getPseudo());
            credits.setMontantRembourse(jeu.getJoueur().getCreditEnCours().getMontantRembourse());
            credits.setDateDerniereMensualite(jeu.getJoueur().getCreditEnCours().getDateDerniereMensualite());
            credits.setTermine(jeu.getJoueur().getCreditEnCours().getTermine());
            credits.setMensualite(jeu.getJoueur().getCreditEnCours().getMensualite());
            credits.setDateProchaineMensualite(jeu.getJoueur().getCreditEnCours().getDateProchaineMensualite());
            credits.setDatePreavis(jeu.getJoueur().getCreditEnCours().getDatePreavis());
            credits.setBlocageDatePreavis(jeu.getJoueur().getCreditEnCours().getBlocageDatePreavis());
            try {
                creditsService.majCredit(credits);
            } catch (Exception e) {
                System.out.println(e);
            }
            connectionBdd.close();
        }
    }

    public void afficheCalendrier() {
        // met en place la semaine en cours
        this.jeu.getCalendrier().setSemainesEnCours();
        this.jourEnCours = jeu.getCalendrier().getNumJour();
        jeu.getCalendrier().createSemaine1Calendrier(paneSemaine1, jeu.getJoueur().getCreditEnCours().getDateProchaineMensualite());
        jeu.getCalendrier().createSemaine2Calendrier(paneSemaine2, jeu.getJoueur().getCreditEnCours().getDateProchaineMensualite());
    }

    public ProgressBar getProgressJour() {
        return progressJour;
    }

    /**
     * Met à jour la barre de progression pour la journee
     *  avec un demarrage de la barre par rapport à la sauvegarde
     * @param cycle : 1 pour on effectue une seule fois
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
                progressBarStartTimelineJournee(cycle - 1, jeu.getCalendrier().getDureeJour());
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
     * @param cycle
     * @param vitesse
     */
    public void progressBarStartTimelineHeureEnCours(int cycle, double vitesse) {
        this.progressionHeure.set(this.jeu.getJoueur().getFerme().getEtatProgressOeuf());
        // recupere l'etat de la progression des oeufs
//        getProgressJour.setProgress(this.jeu.getCalendrier().getProgressJour());
        timelineHeure = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(this.progressionHeure, this.jeu.getJoueur().getFerme().getEtatProgressOeuf())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("heure terminée");
                    // incremente une heure et repasse à 1 quand il est est 10 heures
                    this.jeu.getCalendrier().setIncrementHeure();
                    majFerme();

                }, new KeyValue(this.progressionHeure, 1))
        );

        timelineHeure.setOnFinished(event -> {
            if (cycle == 1) {
                // Lancer la deuxième exécution et remet à 0 la valeur
                jeu.getCalendrier().setProgressJour(0);
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartTimelineHeure(cycle - 1, jeu.getParametres().getVitessePonteOeuf());
            }
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
     * @param cycle
     * @param vitesse
     */
    public void progressBarStartTimelineHeure(int cycle, double vitesse) {
        this.progressionHeure.set(0);
        timelineHeure = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressionHeure, 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("heure terminée");
                    // incremente un jour et remet l'heure à 1
                    majFerme();

                }, new KeyValue(progressionHeure, 1))
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
    public void majFerme(){
        long nbOeufsAAjouter = this.jeu.getJoueur().getFerme().getNbOeufs() + this.jeu.getJoueur().getFerme().getNbPoules();
        this.jeu.getJoueur().getFerme().setNbOeufs(nbOeufsAAjouter);
        System.out.println("ajout de " + nbOeufsAAjouter + " oeuf(s)");
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