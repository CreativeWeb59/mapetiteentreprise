package com.example.mapetiteentreprise.controllersFx;

import com.example.mapetiteentreprise.Main;
import com.example.mapetiteentreprise.actions.Outils;
import com.example.mapetiteentreprise.bdd.ConnectionBdd;
import com.example.mapetiteentreprise.bdd.Credits;
import com.example.mapetiteentreprise.bdd.CreditsService;
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
    private Label labelAccueil, labelAPayer, labelMontant, labelInterets, labelTotal, labelRestantDu, labelPrelevement, labelMessageFinDuPret,
            labelCredit;
    @FXML
    Button retourMenu, btnRembourser, btnRembourserTout, btnPret;
    @FXML
    Pane paneCreditEnCours, paneNouveauCredit, paneProgress;
    @FXML
    private ProgressBar progressOeufs, progressJour, progressBC, progressBF, progressSa, progressCo, progressScooter;
    private Timeline timelineOeufs, timelineJour, timelineBC, timelineBF, timelineSa, timelineCo, timelineScooter;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private ConnectionBdd connectionBdd = new ConnectionBdd();
    private Credits credits = new Credits();
    private CreditsService creditsService;

    // attributs pur le prêt
    private final BigDecimal pourcentageInterets = BigDecimal.valueOf(0.08);
    private final int nbMensualites = 30;
    private final int cycleMensualite = 7;

    public void startFenetre(Jeu jeu) {
        // Recuperation du jeu
        this.jeu = jeu;
        setLabelAccueil();
        // affiche le crédit en cours ou la possibilité de prendre un nouveau crédit
        affichagePanneau();

        // barres de progression
        startProgressBars();

        // affichage des barres de progression (mode dev)
        jeu.afficheProgression(paneProgress);

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
     * Fait le choix entre afficher le detail du credit ou effectuer un nouveau crédit
     */
    public void affichagePanneau(){
        if(isCredit()){
            paneCreditEnCours.setVisible(true);
            paneNouveauCredit.setVisible(false);
            affichageCredit();
        } else {
            paneCreditEnCours.setVisible(false);
            paneNouveauCredit.setVisible(true);
            propositionPret();
        }
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
        // calcul des valeurs necessaires
        BigDecimal montantAPreter = montantAPreter();
        BigDecimal coutPret = montantAPreter.add(montantAPreter.multiply(BigDecimal.valueOf(0.08)));
        BigDecimal mensualite = coutPret.divide(BigDecimal.valueOf(nbMensualites));

        long jourEnCours = jeu.getCalendrier().getNumJour();
        long dateProchaineMensualite = jourEnCours + cycleMensualite;
        long datePreavis = jourEnCours + (nbMensualites * cycleMensualite) + 2;

        String formattedStringMontantAPreter = decimalFormat.format(montantAPreter) + monnaie;
        String formattedStringCoutPret = decimalFormat.format(coutPret) + monnaie;
        String formattedStringMensualite = decimalFormat.format(mensualite) + monnaie;


        String texte = "Montant du prêt : " + formattedStringMontantAPreter + separationTexte;
        texte += nbMensualites + " Mensualites de " + formattedStringMensualite + separationTexte;
        texte += "Cout total du crédit : " + formattedStringCoutPret +  separationTexte;
        texte += "Première mensualité le jour " + dateProchaineMensualite;
        setLabelPret(texte);
    }

    /**
     * Renvoi le montant du pret à preter
     * @return
     */
    public BigDecimal montantAPreter(){
        return jeu.valeurEntreprise().divide(BigDecimal.valueOf(2));
    }

    public void setLabelPret(String texte){
        labelCredit.setText(texte);
    }

    /**
     * start les 2 barres de progression : progress oeuf et progress jour
     *  + les barres des distributeurs s'ils sont actifs
     */
    public void startProgressBars(){
        double vitesse = jeu.getParametres().getVitessePonteOeuf() - (jeu.getParametres().getVitessePonteOeuf() * jeu.getJoueur().getFerme().getEtatProgressOeuf());
        this.progressBarStartTimelineEncours(1, vitesse);

        if (jeu.getCalendrier().getHeureActuelle() != 0) {
            // recuperation de l'etat de la barre de progression pour la journee
            double vitesseJour = jeu.getCalendrier().getDureeJour() - (jeu.getCalendrier().getDureeJour() * jeu.getCalendrier().getProgressJour());
            progressBarStartTimelineJourneeEnCours(1, vitesseJour);
        }
        // demarrage des distributeurs
        demarrageDistributeurs();

        // demarrage des livraisons
        demarrageLivraisons();
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

        // on recupere l'etat de la barre de progression des distributeurs
        this.jeu.getJoueur().getBoissonsChaudes().setEtatProgressDistributeur(this.progressBC.getProgress());
        this.jeu.getJoueur().getBoissonsFraiches().setEtatProgressDistributeur(this.progressBF.getProgress());
        this.jeu.getJoueur().getConfiseries().setEtatProgressDistributeur(this.progressCo.getProgress());
        this.jeu.getJoueur().getSandwichs().setEtatProgressDistributeur(this.progressSa.getProgress());

        // on stoppe les barres de progression;
        this.progressBarStop(timelineOeufs);
        this.progressBarStop(timelineJour);
        this.progressBarStop(timelineBC);
        this.progressBarStop(timelineBF);
        this.progressBarStop(timelineCo);
        this.progressBarStop(timelineSa);

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

    /**
     * Click sur le bouton de prêt
     * recupere les infos du type de prêt
     * calcule la somme pretable par rapport à la valeur de l'entreprise
     */
    public void onBtnPret(){
        // calcul de la somme à preter
        BigDecimal montantAPreter = montantAPreter();

        // calcul des valeurs necessaires
        BigDecimal coutPret = montantAPreter.add(montantAPreter.multiply(BigDecimal.valueOf(0.08)));
        BigDecimal mensualite = coutPret.divide(BigDecimal.valueOf(nbMensualites));
        long jourEnCours = jeu.getCalendrier().getNumJour();
        long dateProchaineMensualite = jourEnCours + cycleMensualite;
        long datePreavis = jourEnCours + (nbMensualites * cycleMensualite) + 2;

        CreditEnCours creditEnCours = jeu.getJoueur().getCreditEnCours();
        System.out.println("joueur : " + jeu.getJoueur().getCreditEnCours());
        System.out.println("Credit : " + creditEnCours);
        // verifie si un credit existe déja
        if (creditEnCours == null) {
            // creation du pret
            creditEnCours = new CreditEnCours(montantAPreter, coutPret, BigDecimal.valueOf(0), mensualite, nbMensualites, cycleMensualite, 0,
                    jourEnCours, jourEnCours, dateProchaineMensualite, datePreavis, 0);
            // on l'ajoute au joueur
            jeu.getJoueur().setCreditEnCours(creditEnCours);

            // on ajoute la somme dans la banque du joueur
            jeu.getJoueur().setArgent(jeu.getJoueur().getArgent().add(montantAPreter));

            // on ajoute dans la table credits
            connectionBdd.connect();

            creditsService = new CreditsService(connectionBdd);
            try {
                credits = new Credits(jeu.getJoueur().getPseudo(), montantAPreter, coutPret, BigDecimal.valueOf(0), mensualite, 30, cycleMensualite, 0, jourEnCours, jourEnCours, dateProchaineMensualite, datePreavis, 0);
                creditsService.addCredit(credits);
                System.out.println("Ajout du credit en bdd");
            } catch (Exception e){
                System.out.println("Erreur de creation du crédit");
            }
            connectionBdd.close();

            // maj des labels
            affichagePanneau();

        } else {
            System.out.println("Vous avez déja un crédit en cours, terminez votre crédit avant d'en faire un nouveau");
        }
    }
    /**
     * Permet d'ajuster les distributeurs et les demarrer s'ils sont actifs
     */
    public void demarrageDistributeurs(){
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
    /**
     * Demarrage des barres de progression des livraisons
     */
    public void demarrageLivraisons() {
        if(jeu.getJoueur().getLivraison1Active() == 1){
            // recuperation de l'etat de la barre de progression pour la livraison en scooter
            double vitesseScooter = jeu.getJoueur().getLivraisonScooter().getVitesseLivraion() - (jeu.getJoueur().getLivraisonScooter().getVitesseLivraion() * jeu.getJoueur().getLivraisonScooter().getEtatProgressLivraison());
            progressBarStartScooterEnCours(1, vitesseScooter);
        }
    }
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

}
