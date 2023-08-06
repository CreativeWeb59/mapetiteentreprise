package com.example.mapetiteentreprise.controllersFx;

import com.example.mapetiteentreprise.Main;
import com.example.mapetiteentreprise.actions.Outils;
import com.example.mapetiteentreprise.jeu.CreditEnCours;
import com.example.mapetiteentreprise.jeu.Jeu;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.Event;
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
import java.time.LocalDateTime;

public class Banquecontroller {
    private Jeu jeu;
    private final String monnaie = " €";
    private final String separationTexte = System.getProperty("line.separator");
    // pattern des nombre décimaux
    private final DecimalFormat decimalFormat = Outils.getDecimalFormatWithSpaceSeparator();
    @FXML
    private Label labelAccueil, labelAPayer, labelMontant, labelInterets, labelTotal, labelRestantDu, labelPrelevement, labelMessageFinDuPret;
    @FXML
    Button retourMenu, btnRembourser, btnRembourserTout;
    @FXML
    Pane paneCreditEnCours, paneNouveauCredit;
    @FXML
    private ProgressBar progressOeufs, progressJour;
    private Timeline timelineOeufs, timelineJour;
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void startFenetre(Jeu jeu) {
        // Recuperation du jeu
        this.jeu = jeu;
        setLabelAccueil();
        // affiche le crédit en cours ou la possibilité de prendre un nouveau crédit
        if(isCredit()){
            paneCreditEnCours.setVisible(true);
            paneNouveauCredit.setVisible(false);
            affichageCredit();
        } else {
            paneCreditEnCours.setVisible(false);
            paneNouveauCredit.setVisible(true);
            propositionPret();
        }

        // barres de progression
        startProgressBars();
    }

    public void retourGestion(Event event) {
        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();

        // sauvegardes
        try {
            this.jeu.sauvegardejeu(this.progressOeufs, this.progressJour);
            this.jeu.sauvegardeCredit();
        } catch (Exception e) {
            System.out.println(e);
        }

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
     * Action a executé lors de la fermeture de la fentre avec la croix : sauvegarde
     *
     * @param event
     */
    public void onWindowClose(WindowEvent event) {
        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();

        // sauvegardes
        try {
            this.jeu.sauvegardejeu(this.progressOeufs, this.progressJour);
            this.jeu.sauvegardeCredit();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void setLabelAccueil(){
        String formattedString = decimalFormat.format(jeu.getJoueur().getArgent()) + monnaie;
        labelAccueil.setText("Montant en banque : " + formattedString);
    }
    /**
     * Initialise les labels du crédit
     */
    public void affichageCredit(){
        setLabelMontant();
        setLabelAPayer();
        setLabelTotal();
        setLabelRestantDu();
        setLabelInterets();
        setLabelPrelevement();
        isMensualite();
        isCreditComplet();
        setLabelAccueil();
    }

    /**
     * Mise en place label a payer
     * indique le numero du jour qu'il faudra payer la mensualité
     */
    public void setLabelAPayer(){
        // calcul mensualites en retard
        long nbRetardMensualite = jeu.getJoueur().getCreditEnCours().nbRetardMensualite(jeu.getCalendrier().getNumJour());
        long prochainPrelevement = jeu.getJoueur().getCreditEnCours().getDateProchaineMensualite();

        String texte;
        // gestion si echeances en retard
        if (nbRetardMensualite <= 0) {
            texte = "" + prochainPrelevement;
        } else {
            texte = "Vous avez " + nbRetardMensualite + " écheance(s) en retard" + separationTexte;
            texte += "A payer immédiatement";

            // Arrivee du banquier
            // A faire Blocage des boutons d'achat
        }

        this.labelAPayer.setText(texte);
    }

    /**
     * Mise en place label a payer
     */
    public void setLabelMontant(){
        String formattedString = decimalFormat.format(jeu.getJoueur().getCreditEnCours().getMontantPret()) + monnaie;
        this.labelMontant.setText(formattedString);
    }

    /**
     * Mise en place label a payer
     */
    public void setLabelInterets(){
        BigDecimal mInterets = jeu.getJoueur().getCreditEnCours().getCoutPret().subtract(jeu.getJoueur().getCreditEnCours().getMontantPret());
        String formattedString = decimalFormat.format(mInterets) + monnaie;
        this.labelInterets.setText(formattedString);
    }

    /**
     * Mise en place label a payer
     */
    public void setLabelTotal(){
        String formattedString = decimalFormat.format(jeu.getJoueur().getCreditEnCours().getCoutPret()) + monnaie;
        labelTotal.setText(formattedString);
    }
    /**
     * Mise en place label a payer
     */
    public void setLabelRestantDu(){
        BigDecimal restantDu = jeu.getJoueur().getCreditEnCours().getCoutPret().subtract(jeu.getJoueur().getCreditEnCours().getMontantRembourse());
        String formattedString = decimalFormat.format(restantDu) + monnaie;
        labelRestantDu.setText(formattedString);
    }
    /**
     * Mise en place label a payer
     */
    public void setLabelPrelevement(){
        String formattedString = decimalFormat.format(jeu.getJoueur().getCreditEnCours().getMensualite()) + monnaie;
        labelPrelevement.setText(formattedString);
    }

    /**
     * Renvoi true si credit en cours
     * @return
     */
    public boolean isCredit(){
        CreditEnCours creditEnCours = jeu.getJoueur().getCreditEnCours();
        if (creditEnCours != null) {
            if(jeu.getJoueur().getCreditEnCours().getTermine() == 0){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * gere quand aucun prêt en cours
     * propose un nouveau avec plusieurs solutions de prêts
     */
    public void propositionPret(){
        System.out.println("affichage des propositions de prêts");
    }

    /**
     * start les 2 barres de progression : progress oeuf et progress jour
     */
    public void startProgressBars(){
        double vitesse = jeu.getParametres().getVitessePonteOeuf() - (jeu.getParametres().getVitessePonteOeuf() * jeu.getJoueur().getFerme().getEtatProgressOeuf());
        this.progressBarStartTimelineEncours(1, vitesse);

        if (jeu.getCalendrier().getHeureActuelle() != 0) {
            // recuperation de l'etat de la barre de progression pour la journee
            double vitesseJour = jeu.getCalendrier().getDureeJour() - (jeu.getCalendrier().getDureeJour() * jeu.getCalendrier().getProgressJour());
            progressBarStartTimelineJourneeEnCours(1, vitesseJour);
        }
    }

    /**
     * Met à jour la barre de progression
     *
     * @param cycle
     * @param vitesse
     */
    public void progressBarStartTimelineEncours(int cycle, double vitesse) {
        ProgressBar progressOeufs = getProgressOeufs();
        // Réinitialise la barre de progression à 0
        progressOeufs.setProgress(this.jeu.getJoueur().getFerme().getEtatProgressOeuf());
        timelineOeufs = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressOeufs.progressProperty(), this.jeu.getJoueur().getFerme().getEtatProgressOeuf())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Ajoute une heure");
                    this.jeu.getCalendrier().setIncrementHeure();
                    System.out.println("Heure actuelle : " + jeu.getCalendrier().getHeureActuelle());
                    System.out.println("Oeuf terminé");
                    // ajoute le nombre de poules necesaires
                    majFerme();
                }, new KeyValue(progressOeufs.progressProperty(), 1))
        );
        timelineOeufs.setOnFinished(event -> {
            if (cycle == 1) {
                // Lancer la deuxième exécution de la méthode progressBarStartTimeline
                jeu.getJoueur().getFerme().setEtatProgressOeuf(0);
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartTimeline(cycle - 1, jeu.getParametres().getVitessePonteOeuf());

            }
        });
        if (cycle == 0) {
            timelineOeufs.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineOeufs.setCycleCount(cycle);
        }
        timelineOeufs.play();
    }

    public void progressBarStartTimeline(int cycle, double vitesse) {
        ProgressBar progressOeufs = getProgressOeufs();
        // Réinitialise la barre de progression à 0
        progressOeufs.setProgress(0);
        timelineOeufs = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressOeufs.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    this.jeu.getCalendrier().setIncrementHeure();
                    System.out.println("Heure actuelle : " + jeu.getCalendrier().getHeureActuelle());
                    System.out.println("Oeuf terminé");
                    // ajoute un nombre d'oeuf correspondant au nombre de poules
                    this.majFerme();
                    // lance la progress jour lorsque le jeu est nouveau
                    if (!isProgressBar(timelineJour)) {
                        progressBarStartTimelineJournee(cycle - 1, this.jeu.getCalendrier().getDureeJour());
                    }
                }, new KeyValue(progressOeufs.progressProperty(), 1))
        );

        if (cycle == 0) {
            timelineOeufs.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineOeufs.setCycleCount(cycle);
        }
        timelineOeufs.play();
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
        timelineJour = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(getProgressJour.progressProperty(), this.jeu.getCalendrier().getProgressJour())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("jour terminé");
                    // incremente un jour et remet l'heure à 1
                    // mise à jour du calendrier
                    this.jeu.getCalendrier().setJourSuivant();
//                    setLabelJourEncours();
                }, new KeyValue(getProgressJour.progressProperty(), 1))
        );
        timelineJour.setOnFinished(event -> {
            if (cycle == 1) {
                // Lancer la deuxième exécution de la méthode progressBarStartTimeline
                jeu.getCalendrier().setProgressJour(0);
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartTimelineJournee(cycle - 1, this.jeu.getCalendrier().getDureeJour());
            }
        });

        if (cycle == 0) {
            timelineJour.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineJour.setCycleCount(cycle);
        }
        timelineJour.play();
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
        timelineJour = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(getProgressJour.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Jour terminé ");
                    // mise à jour du calendrier
                    this.jeu.getCalendrier().setJourSuivant();
                }, new KeyValue(getProgressJour.progressProperty(), 1))
        );

        if (cycle == 0) {
            timelineJour.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineJour.setCycleCount(cycle);
        }
        timelineJour.play();
    }

    private void progressBarStop(Timeline timeline) {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
            System.out.println("Arret de la barre de progression");
        }
    }

    private Boolean isProgressBar(Timeline timeline) {
        if (timeline == null) {
            timeline = null;
            System.out.println("Barre de progression terminee");
            return false;
        }
        return true;
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
     * declare la ProgressBar Jours afin de pouvoir l'utiliser
     * pour l'effet de remplissage
     *
     * @return
     */
    public ProgressBar getProgressJour() {
        return progressJour;
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

    /**
     * Fermeture des barres de progression : enregistrement de l'état + stop des barres de progress
     * Sauvegarde date deco
     */
    public void fermetureProgress(){
        // sauvegarde des barres de progression
        this.jeu.getCalendrier().setProgressJour(this.progressJour.getProgress());
        this.jeu.getJoueur().getFerme().setEtatProgressOeuf(this.progressOeufs.getProgress());

        // on stoppe les barres de progression;
        this.progressBarStop(timelineOeufs);
        this.progressBarStop(timelineJour);

        // on enregistre l'heure de switch de fenetre
        this.jeu.getJoueur().getFerme().setDateDeco(LocalDateTime.now());
    }

    /**
     * gere le bouton pour rembourser une mensualite du credit
     */
    public void onRembourserMensualite() {
        // teste si assez argent en banque
        if (jeu.getJoueur().isArgent(jeu.getJoueur().getCreditEnCours().getMensualite())) {
            // enleve le montant de la banque
            jeu.getJoueur().depenser(jeu.getJoueur().getCreditEnCours().getMensualite());
            // met a jour le credit
            // met a jour le montant du credit rembourse
            jeu.getJoueur().getCreditEnCours().payerMensualite(jeu.getCalendrier().getNumJour());
            // met a jour la prochaine date du reglement

            System.out.println("remboursement");
            this.affichageCredit();
            setLabelAccueil();
        } else {
            System.out.println("Vous n'avez pas assez d'argent pour rembourser le credit");
        }
    }

    /**
     * verifie si assez d'argent pour rembourser le prêt
     */
    public void isMensualite() {
        CreditEnCours creditEnCours = jeu.getJoueur().getCreditEnCours();
        if (creditEnCours != null) {
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getCreditEnCours().getMensualite())) {
                btnRembourser.setVisible(true);
                btnRembourser.setDisable(false);
            } else {
                btnRembourser.setVisible(true);
                btnRembourser.setDisable(true);
            }
        }
    }

    /**
     * gere le bouton pour rembourser une mensualite du credit
     */
    public void onRembourserTout() {
        // teste si assez argent en banque
        BigDecimal montantRestant = jeu.getJoueur().getCreditEnCours().getCoutPret().subtract(jeu.getJoueur().getCreditEnCours().getMontantRembourse());
        if (jeu.getJoueur().isArgent(montantRestant)) {
            // enleve le montant de la banque
            jeu.getJoueur().depenser(montantRestant);
            // met a jour le credit
            // met a jour le montant du credit rembourse
            jeu.getJoueur().getCreditEnCours().rembourserCredit(jeu.getCalendrier().getNumJour());
            System.out.println("remboursement complet");

            setLabelAccueil();
            // chanqge de panneau d'affichage
            paneCreditEnCours.setVisible(false);
            paneNouveauCredit.setVisible(true);

            // affichage message temporaire dans le label : labelMessageFinDuPret
            Outils.afficherMessageTemporaire(labelMessageFinDuPret, "Vous venez de rembourser votre prêt", 3000);
            propositionPret();
        } else {
            System.out.println("Vous n'avez pas assez d'argent pour rembourser entierement le credit");
        }
    }

    /**
     * verifie si assez d'argent pour rembourser le prêt
     */
    public void isCreditComplet() {
        CreditEnCours creditEnCours = jeu.getJoueur().getCreditEnCours();
        if (creditEnCours != null) {
            BigDecimal montantRestant = jeu.getJoueur().getCreditEnCours().getCoutPret().subtract(jeu.getJoueur().getCreditEnCours().getMontantRembourse());
            if (jeu.getJoueur().isArgent(montantRestant)) {
                btnRembourserTout.setVisible(true);
                btnRembourserTout.setDisable(false);
            } else {
                btnRembourserTout.setVisible(true);
                btnRembourserTout.setDisable(true);
            }
        }
    }
}
