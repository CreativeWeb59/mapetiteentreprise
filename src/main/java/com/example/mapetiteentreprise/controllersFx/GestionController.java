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
    private ProgressBar progressJour, progressOeufs;
    private Timeline timelineCalendrier, timelineHeure;

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

        // sauvegarde des barres de progression
        this.jeu.getCalendrier().setProgressJour(this.progressJour.getProgress());
        this.jeu.getJoueur().getFerme().setEtatProgressOeuf(this.progressOeufs.getProgress());

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

        // sauvegarde des barres de progression
        this.jeu.getCalendrier().setProgressJour(this.progressJour.getProgress());
        this.jeu.getJoueur().getFerme().setEtatProgressOeuf(this.progressOeufs.getProgress());


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

        // sauvegarde des barres de progression
        this.jeu.getCalendrier().setProgressJour(this.progressJour.getProgress());
        this.jeu.getJoueur().getFerme().setEtatProgressOeuf(this.progressOeufs.getProgress());

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
     * Gere le bouton pour passer sur la fenetre de la banque
     * @param event
     */
    public void switchToBanque(ActionEvent event) {
        // sauvegarde des barres de progression
        this.jeu.getCalendrier().setProgressJour(this.progressJour.getProgress());
        this.jeu.getJoueur().getFerme().setEtatProgressOeuf(this.progressOeufs.getProgress());

        // on stoppe les barres de progression;
        this.progressBarStop(timelineCalendrier);
        this.progressBarStop(timelineHeure);

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
        // sauvegarde des barres de progression
        this.jeu.getCalendrier().setProgressJour(this.progressJour.getProgress());
        this.jeu.getJoueur().getFerme().setEtatProgressOeuf(this.progressOeufs.getProgress());

        // on stoppe les barres de progression;
        this.progressBarStop(timelineCalendrier);
        this.progressBarStop(timelineHeure);

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
        // sauvegarde des barres de progression
        this.jeu.getCalendrier().setProgressJour(this.progressJour.getProgress());
        this.jeu.getJoueur().getFerme().setEtatProgressOeuf(this.progressOeufs.getProgress());
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
        // sauvegarde des barres de progression
        this.jeu.getCalendrier().setProgressJour(this.progressJour.getProgress());
        this.jeu.getJoueur().getFerme().setEtatProgressOeuf(this.progressOeufs.getProgress());

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
            if(jeu.getJoueur().getCreditEnCours().getTermine()==0){
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