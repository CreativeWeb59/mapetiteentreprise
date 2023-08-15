package com.example.mapetiteentreprise.controllersFx;

import com.example.mapetiteentreprise.Main;
import com.example.mapetiteentreprise.actions.Outils;
import com.example.mapetiteentreprise.bdd.ConnectionBdd;
import com.example.mapetiteentreprise.bdd.SauvegardeService;
import com.example.mapetiteentreprise.jeu.Distributeurs;
import com.example.mapetiteentreprise.jeu.Jeu;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class GestionDistributeursController {
    @FXML
    private Label labelMessageHaut;
    @FXML
    private Label labelTitreDistributeurBC, nbDistributeursBC, nbBC;
    @FXML
    private Label labelTitreDistributeurBF, nbDistributeursBF, nbBF;
    @FXML
    private Label labelTitreDistributeurSa, nbDistributeursSa, nbSa;
    @FXML
    private Label labelTitreDistributeurCo, nbDistributeursCo, nbCo;
    @FXML
    private Button btnDebloquerBC, btnDistributeurPlusBC, btnRecupererBC;
    @FXML
    private Button btnDebloquerBF, btnDistributeurPlusBF, btnRecupererBF;
    @FXML
    private Button btnDebloquerSa, btnDistributeurPlusSa, btnRecupererSa;
    @FXML
    private Button btnDebloquerCo, btnDistributeurPlusCo, btnRecupererCo;
    @FXML
    private Pane paneBC, paneGroupBC, paneBF, paneGroupBF;
    @FXML
    private Pane paneCo, paneGroupCo, paneSa, paneGroupSa;
    @FXML
    private ProgressBar progressBC, progressBF, progressSa, progressCo, progressJour, progressOeufs;
    private BigDecimal gainEnAttenteBC = new BigDecimal(0);
    private BigDecimal gainEnAttenteBF = new BigDecimal(0);
    private BigDecimal gainEnAttenteSa = new BigDecimal(0);
    private BigDecimal gainEnAttenteCo = new BigDecimal(0);
    private Timeline timelineBC, timelineBF, timelineSa, timelineCo, timelineCalendrier, timelineHeure;

    private Jeu jeu;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private final String monnaie = " €";
    private final String separationTexte = System.getProperty("line.separator");
    // pattern des nombre décimaux
    private final DecimalFormat decimalFormat = Outils.getDecimalFormatWithSpaceSeparator();

    /**
     *  Declaration des boutons, labels, progressBar necessaires
     */
    /**
     * /**
     * Gestion de la barre de progression
     * Boissons Chaudes
     *
     * @return
     */
    public Button getBtnRecupereBC() {
        return btnRecupererBC;
    }

    /**
     * Gestion de la barre de progression
     * Boissons Fraiches
     *
     * @return
     */
    public Button getBtnRecupereBF() {
        return btnRecupererBF;
    }

    /**
     * Gestion de la barre de progression
     * Sandwichs
     *
     * @return
     */
    public Button getBtnRecupereSa() {
        return btnRecupererSa;
    }

    /**
     * Gestion de la barre de progression
     * Confiseries
     *
     * @return
     */
    public Button getBtnRecupereCo() {
        return btnRecupererCo;
    }

    /**
     * initialise le montant en banque du joueur
     */
    public void setLabelMessageHaut() {
        String formattedString = decimalFormat.format(jeu.getJoueur().getArgent()) + monnaie;
        this.labelMessageHaut.setText(jeu.getJoueur().getPseudo() + " vous avez " + formattedString + " en banque");
    }

    /**
     * Labels du distributeur de boissons chaudes
     */
    public void setNbDistributeursBC() {
        this.nbDistributeursBC.setText(this.jeu.getJoueur().getBoissonsChaudes().getNbDistributeurs() + "");
    }

    public void setNbBC() {
        this.nbBC.setText(this.jeu.getJoueur().getBoissonsChaudes().getNbMarchandises() + "");
    }

    /**
     * Labels du distributeur de boissons fraiches
     */
    public void setNbDistributeursBF() {
        this.nbDistributeursBF.setText(this.jeu.getJoueur().getBoissonsFraiches().getNbDistributeurs() + "");
    }

    public void setNbBF() {
        this.nbBF.setText(this.jeu.getJoueur().getBoissonsFraiches().getNbMarchandises() + "");
    }

    /**
     * Labels du distributeur de Confiseries
     */
    public void setNbDistributeursCo() {
        this.nbDistributeursCo.setText(this.jeu.getJoueur().getConfiseries().getNbDistributeurs() + "");
    }

    public void setNbCo() {
        this.nbCo.setText(this.jeu.getJoueur().getConfiseries().getNbMarchandises() + "");
    }

    /**
     * Labels du distributeur de Sandwichs
     */
    public void setNbDistributeursSa() {
        this.nbDistributeursSa.setText(this.jeu.getJoueur().getSandwichs().getNbDistributeurs() + "");
    }

    public void setNbSa() {
        this.nbSa.setText(this.jeu.getJoueur().getSandwichs().getNbMarchandises() + "");
    }

    public void demarrer(Jeu jeu) {
        System.out.println("Choisissez un distributeur");

        // recuperation du jeu, paramètres + sauvegarde
        this.jeu = jeu;

        // ajustement de la barre de progression et des distributeurs
        // on compare l'heure et le jour actuel avec la date deco
        // on met a jour les marchandises vendues

        // Reajustement distributeur Boissons chaudes
//        reajustementBC();

        // Reajustement distributeur Boissons fraiches
//        reajustementBF();

        // Reajustement distributeur Confiseries
//        reajustementCo();

        // Reajustement distributeur Sandwichs
//        reajustementSa();

        // mise en place des labels
        this.miseEnPlaceValeurs();


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

        // recuperation de l'etat de la barre de progression pour les heures / oeufs
        double vitesseHeure = jeu.getParametres().getVitessePonteOeuf() - (jeu.getParametres().getVitessePonteOeuf() * jeu.getJoueur().getFerme().getEtatProgressOeuf());
        progressBarStartTimelineHeureEnCours(1, vitesseHeure);

        // recuperation de l'etat de la barre de progression pour la journee
        double vitesse = jeu.getCalendrier().getDureeJour() - (jeu.getCalendrier().getDureeJour() * jeu.getCalendrier().getProgressJour());

        progressBarStartTimelineJourneeEnCours(1, vitesse);
    }

    /**
     * Action a executer lors de la fermeture de la fenetre avec la croix : sauvegarde
     * @param event
     */

    public void onWindowClose(WindowEvent event) {
        // sauvegarde des barres de progression
        this.jeu.getCalendrier().setProgressJour(this.progressJour.getProgress());
        this.jeu.getJoueur().getFerme().setEtatProgressOeuf(this.progressOeufs.getProgress());

        // on stoppe les barres de progression;
        this.progressBarStop(timelineCalendrier);
        this.progressBarStop(timelineHeure);

        // Sauvegarde de la base de donnees
        System.out.println("fermeture fenetre : Sauvegarde");
        try {
            sauvegardejeu();
        } catch (Exception e){
            System.out.println(e);
        }

        // Sauvegarde de la base de donnees
        System.out.println("fermeture fenetre : Sauvegarde complete");
        try {
            this.jeu.sauvegardejeu(this.progressOeufs, this.progressJour);
            this.jeu.sauvegardeCredit();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /**
     * Retour au menu gestion
     *
     * @param event
     */
    public void retourMenuGestion(ActionEvent event) {
        // on recupere l'etat de la barre de progression des distributeurs
        this.jeu.getJoueur().getBoissonsChaudes().setEtatProgressDistributeur(this.progressBC.getProgress());
        this.jeu.getJoueur().getBoissonsFraiches().setEtatProgressDistributeur(this.progressBF.getProgress());
        this.jeu.getJoueur().getConfiseries().setEtatProgressDistributeur(this.progressCo.getProgress());
        this.jeu.getJoueur().getSandwichs().setEtatProgressDistributeur(this.progressSa.getProgress());

        // on enregistre l'heure de switch de fenetre pour tous les distributeurs
        this.jeu.getJoueur().getBoissonsChaudes().setDateDeco(LocalDateTime.now());
        this.jeu.getJoueur().getBoissonsFraiches().setDateDeco(LocalDateTime.now());
        this.jeu.getJoueur().getConfiseries().setDateDeco(LocalDateTime.now());
        this.jeu.getJoueur().getSandwichs().setDateDeco(LocalDateTime.now());

        // sauvegarde des barres de progression
        this.jeu.getCalendrier().setProgressJour(this.progressJour.getProgress());
        this.jeu.getJoueur().getFerme().setEtatProgressOeuf(this.progressOeufs.getProgress());

        // on stoppe les barres de progression;
        this.progressBarStop(timelineCalendrier);
        this.progressBarStop(timelineHeure);
        this.progressBarStopBC();
        this.progressBarStopBF();
        this.progressBarStopCo();
        this.progressBarStopSa();

        // Sauvegarde de la base de donnees
        System.out.println("switch fenetre : Sauvegarde complete");
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
     * Met a jour les labels
     * et active / desactive les distributeurs
     */
    public void miseEnPlaceValeurs() {
        // Mets en place la valeur des labels :
        setLabelMessageHaut();
        majDistributeurBC();
        majDistributeurBF();
        majDistributeurCo();
        majDistributeurSa();

        testBoissonsChaudes(); // blocage ou non du distributeur de boissons chaudes
        testBoissonsFraiches(); // blocage ou non du distributeur de boissons fraiches
        testConfiseries(); // blocage ou non du distributeur de confiseries
        testSandwichs(); // blocage ou non du distributeur de sandwichs

        // Verification du nombre de distributeurs
        verifNbDistributeurs();

        System.out.println("Mise en place des labels et chiffres");
    }


    /**
     * methode generale pour acheter un distributeur
     *
     * @param montantAchat
     * @return true si ok, false si pas assez d'argent
     */
    public Boolean acheter(BigDecimal montantAchat) {
        // on verifie l'argent dispo
        // si c'est le cas on retire l'argent
        if (this.jeu.getJoueur().isArgent(montantAchat)) {
            this.jeu.getJoueur().depenser(montantAchat);
            return true;
        } else {
            return false;
        }
    }

    /**
     * ajustement de la barre de progression et des marchandises vendues
     * on compare l'heure et le jour actuel avec la date de deco
     * methode commune à tous les distributeurs
     */
    public double reajustementProgressDistributeur(LocalDateTime heureDeco, int vitesseDistributeur, double ancBarreProgress) {
        // calcule l'ecart entre heure deco et maintenant
        long ecartEnSecondes = ecartSwitchFenetre(heureDeco);

        // progression a ajouter
        double barreDistributeurAAjouter = calculBarreDistributeurHc(ecartEnSecondes, vitesseDistributeur);

        // ajustement de la progression si > 1

        return ancBarreProgress + barreDistributeurAAjouter;
    }

    /**
     * Calcule le nombre de secondes d'écart entre la date de deco et l'heure actuelle
     *
     * @param heureDeco
     * @return
     */
    public long ecartSwitchFenetre(LocalDateTime heureDeco) {
        LocalDateTime heureActuelle = LocalDateTime.now();
        long ecartEnSecondes = ChronoUnit.SECONDS.between(heureDeco, heureActuelle);
        System.out.println("Debut reajustement, ecart de temps : " + ecartEnSecondes);

        return ecartEnSecondes;
    }

    /**
     * Calcule le nombre de marchandises qu'un distributeur aurait pû vendre
     * pendant un temps donné
     *
     * @param ecartEnSecondes
     * @param vitesseDistributeur
     */

    public double calculBarreDistributeurHc(long ecartEnSecondes, int vitesseDistributeur) {
        double calculReste = ecartEnSecondes % vitesseDistributeur;
        double barreHc = calculReste / vitesseDistributeur;
        return barreHc;
    }

    /**
     * Deblocage du distributeur
     * voir si possible faire une animation genre Opacitiy 0.2 => 1 en plusieurs secondes
     *
     * @param paneDistributeur       => nom du distributeur a debloquer
     * @param btnDebloquer           => nom du bouton à rendre invisible correspondant au distributeur
     * @param labelTitreDistributeur => nom du label affichant le distributeur à rendre invisible
     */
    public void debloquerDistributeur(Pane paneDistributeur, Button btnDebloquer, Label labelTitreDistributeur) {
        paneDistributeur.setDisable(false);
        paneDistributeur.setOpacity(1);
        btnDebloquer.setVisible(false);
        labelTitreDistributeur.setVisible(false);
    }

    /**
     * Methode utile ????????????????????????????
     * Blocage du distributeur de boissons chaudes
     */
    public void blocBoissonsChaudes() {
        this.paneBC.setDisable(true);
        this.paneBC.setOpacity(0);
        this.btnDebloquerBC.setVisible(true);
        this.labelTitreDistributeurBC.setVisible(true);
    }

    /****************************************
     // Debut distributeur Boissons chaudes
     /* *************************************

     /**
     * Affiche le bouton pour debloquer le distributeur de boissons chaudes
     * si le joueur a assez d'argent => disable=false, sinon disable = true
     * Ne doit s'afficher que si le distributeur est bloqué
     */
    public void testBoissonsChaudes() {
        // teste si le distributeur de boisson chaude est bloqué
        if (jeu.getJoueur().getDistributeurBCActive() == 0) {
            this.btnDebloquerBC.setVisible(true);
            // teste si on a assez pour acheter le distributeur
            if (jeu.getJoueur().isArgent(jeu.getParametres().getPrixDistributeurBC())) {
                this.btnDebloquerBC.setDisable(false);
            } else {
                this.btnDebloquerBC.setDisable(true);
            }
        } else {
            debloquerDistributeur(paneBC, btnDebloquerBC, labelTitreDistributeurBC);
        }
    }

    public void acheterBoissonsChaudes() {
        // verifie si le nombre maxi de distributeurs n'est pas atteint
        if(!isMaxiNbDitributeur(this.jeu.getJoueur().getBoissonsChaudes())){
            BigDecimal montantAchat = this.jeu.getParametres().getPrixDistributeurBC();
            if (acheter(montantAchat)) {
                this.jeu.getJoueur().getBoissonsChaudes().ajoutDistributeur();
                // met a jour le label en haut
                setLabelMessageHaut();
                System.out.println("Vous venez d'acheter un distributeur de boissons chaudes");
                // debloque le distributeur si besoin
                if (jeu.getJoueur().getBoissonsChaudes().getNbDistributeurs() == 1) {
                    jeu.getJoueur().setDistributeurBCActive(1);
                    debloquerDistributeur(paneBC, btnDebloquerBC, labelTitreDistributeurBC);
                    // demarre la barre de progression du distributeur
                    progressBarStartBC(0, jeu.getParametres().getVitesseBC());
                    System.out.println("achat du distributueur :" + jeu.getJoueur());
                }
                miseEnPlaceValeurs();
            } else {
                System.out.println("Vous n'avez pas assez d'argent pour acheter un distributeur de boissons chaudes");
            }
        } else {
            System.out.println("Vous avez trop de distributeurs");
        }
    }



    /**
     * Maj des labels et boutons du distributeur de boissons chaudes
     */
    public void majDistributeurBC() {
        setNbDistributeursBC();
        setNbBC();
        // affichage ou non du bouton pour recuperer l'argent du distributeur
        majGainsEnCoursBC();
//        setBtnRecupereBC();
        verifAchatDistributeurs();
    }

    /**
     * verfiie tous les boutons d'achat de distributeur
     * les rends enable si assez d'argent pour en acheter
     */
    public void verifAchatDistributeurs() {
        // verif si assez d'argent pour acheter un distributeur de boissons chaudes
        if (jeu.getJoueur().isArgent(jeu.getParametres().getPrixDistributeurBC())) {
            btnDistributeurPlusBC.setDisable(false);
        } else {
            btnDistributeurPlusBC.setDisable(true);
        }

        // verif si assez d'argent pour acheter un distributeur de boissons fraiches
        // affichage du pane distributeur boissons fraiches => rends accessible le clic sur le bouton d'achat
        // visibilite du bouton +
        if (jeu.getJoueur().isArgent(jeu.getParametres().getPrixDistributeurBF())) {
            btnDistributeurPlusBF.setDisable(false);
            if (jeu.getJoueur().getBoissonsFraiches().getEtatProgressDistributeur() == 0) {
                btnDebloquerBF.setDisable(false);
            }
        } else {
            btnDistributeurPlusBF.setDisable(true);
        }

        // verif si assez d'argent pour acheter un distributeur de confiseries
        // affichage du pane distributeur confiserie => rends accessible le clic sur le bouton d'achat
        // visibilite du bouton +
        if (jeu.getJoueur().isArgent(jeu.getParametres().getPrixDistributeurCo())) {
            btnDistributeurPlusCo.setDisable(false);
            if (jeu.getJoueur().getConfiseries().getEtatProgressDistributeur() == 0) {
                btnDebloquerCo.setDisable(false);
            }
        } else {
            btnDistributeurPlusCo.setDisable(true);
        }

        // verif si assez d'argent pour acheter un distributeur de sandwichs
        // affichage du pane distributeur sandwichs => rends accessible le clic sur le bouton d'achat
        // visibilite du bouton +
        if (jeu.getJoueur().isArgent(jeu.getParametres().getPrixDistributeurSa())) {
            btnDistributeurPlusSa.setDisable(false);
            if (jeu.getJoueur().getSandwichs().getEtatProgressDistributeur() == 0) {
                btnDebloquerSa.setDisable(false);
            }
        } else {
            btnDistributeurPlusSa.setDisable(true);
        }
    }

    /**
     * verfiie tous les boutons d'achat de distributeur
     * les rends enable si assez d'argent pour en acheter
     */
    public void verifNbDistributeurs() {
        // verif nombre distributeurs boissons chaudes
        if(!isMaxiNbDitributeur(jeu.getJoueur().getBoissonsChaudes())){
            btnDistributeurPlusBC.setDisable(false);
        } else {
            btnDistributeurPlusBC.setDisable(true);
        }

        // verif nombre distributeurs boissons fraiches
        if(!isMaxiNbDitributeur(jeu.getJoueur().getBoissonsFraiches())){
            btnDistributeurPlusBF.setDisable(false);
        } else {
            btnDistributeurPlusBF.setDisable(true);
        }

        // verif nombre distributeurs de confiseries
        if(!isMaxiNbDitributeur(jeu.getJoueur().getConfiseries())){
            btnDistributeurPlusCo.setDisable(false);
        } else {
            btnDistributeurPlusCo.setDisable(true);
        }

        // verif nombre distributeurs de sandwichs
        if(!isMaxiNbDitributeur(jeu.getJoueur().getSandwichs())){
            btnDistributeurPlusSa.setDisable(false);
        } else {
            btnDistributeurPlusSa.setDisable(true);
        }
    }

    /**
     * Met à jour la barre de progression pour distributeur de boissons chaudes
     *
     * @param cycle
     * @param vitesse
     */
    public void progressBarStartTimelineEncoursBC(int cycle, double vitesse) {
        ProgressBar progressBarBC = getProgressBC();
        Button btnRecupereBC = getBtnRecupereBC();
        // Réinitialise la barre de progression à 0
        progressBarBC.setProgress(this.jeu.getJoueur().getBoissonsChaudes().getEtatProgressDistributeur());
        timelineBC = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBarBC.progressProperty(), this.jeu.getJoueur().getBoissonsChaudes().getEtatProgressDistributeur())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Boisson chaude terminée");
                    btnRecupereBC.setDisable(false);
                    // ajoute un nombre de marchandises correspondantes au nombre de distributeurs
                    this.majProgressBc();
                    // met à jour les gains en cours
                    this.majGainsEnCoursBC();
                    // maj des boutons
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
        Button btnRecupereBC = getBtnRecupereBC();
        // Réinitialise la barre de progression à 0
        progressBarBC.setProgress(0);
        timelineBC = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBarBC.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Distributeur de boissons chaudes prêt");
                    btnRecupereBC.setDisable(false);
                    // ajoute un nombre de marchandises correspondantes au nombre de distributeurs
                    this.majProgressBc();
                    // met à jour les gains en cours
                    this.majGainsEnCoursBC();
                    // maj des boutons
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
     * Arret de la barre de progression
     */
    private void progressBarStopBC() {
        if (timelineBC != null) {
            timelineBC.stop();
            timelineBC = null;
            System.out.println("Arret de la barre de progression");
        }
    }

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
        this.setNbBC();
        System.out.println("maj du nombre de marchandises vendues dans les distributeurs de Boissons Chaudes : " + nouvNombre);
    }

    /**
     * action a effectuer lors du clic sur le bouton Vendre
     */
    public void onVendreBC() {
        long nbMarchandisesBC = jeu.getJoueur().getBoissonsChaudes().getNbMarchandises();

        jeu.getJoueur().getBoissonsChaudes().setNbMarchandises(0); // raz le nombre de marchandises
        jeu.getJoueur().setArgent(this.gainEnAttenteBC.add(jeu.getJoueur().getArgent())); // met a jour les nouveaux gains

        String formattedGain = decimalFormat.format(this.gainEnAttenteBC) + monnaie;
        // raz des gains en attente
        this.gainEnAttenteBC = BigDecimal.valueOf(0.00);

        System.out.println("Vous venez de vendre " + nbMarchandisesBC + " marchandises au distributeur de boisonns chaudes, vous avez gagné " + formattedGain + ".");

        getBtnRecupereBC().setDisable(true);
        miseEnPlaceValeurs();
    }


    /**
     * Affiche ou non le bouton recuperer marchandises
     * suivant si gainEnAttente > 0
     */
    public void setBtnRecupereBC() {
        System.out.println("***********************");
        System.out.println("Gains en attente BC : " + this.gainEnAttenteBC);
        int comparaison = this.gainEnAttenteBC.compareTo(BigDecimal.valueOf(0));
        if (comparaison > 0) {
            this.getBtnRecupereBC().setDisable(false);
        } else {
            this.getBtnRecupereBC().setDisable(true);
        }
        String formattedString = "Encaisser " + decimalFormat.format(gainEnAttenteBC) + monnaie;
        this.getBtnRecupereBC().setText(formattedString);
    }

    public void majGainsEnCoursBC() {
        System.out.println("maj des gains distributeur");
        // tant que non vendu, on cumule les gains en attente
        long nbMarchandisesBcEnCours = jeu.getJoueur().getBoissonsChaudes().getNbMarchandises();
        BigDecimal tarifMarchandiseBC = jeu.getParametres().getPrixMarchandiseBC();

        // calcul des gains attente
        this.gainEnAttenteBC = tarifMarchandiseBC.multiply(BigDecimal.valueOf(nbMarchandisesBcEnCours));

        // maj le montant sur le bouton Encaisser
        this.setBtnRecupereBC();
    }

    /**
     * Gestion de la barre de progression Hors Connexion
     */
    public void reajustementBC() {
        // variables necessaires
        LocalDateTime heureDeco = jeu.getJoueur().getBoissonsChaudes().getDateDeco();
        int vitesseDistributeur = jeu.getParametres().getVitesseBC();
        double ancBarreProgress = jeu.getJoueur().getBoissonsChaudes().getEtatProgressDistributeur();

        // maj barre de progression
        double nouvProgress = this.reajustementProgressDistributeur(heureDeco, vitesseDistributeur, ancBarreProgress);

        // calcul du nombre de marchandises vendues pendant le switch de fenetres ou déco

        long nbMarchandisesHC = ecartSwitchFenetre(heureDeco) / vitesseDistributeur;

        // gestion si nouvProgress >1
        if (nouvProgress == 1) {
            // ajout d'une marchandise par distributeur et retour de la barre à 0
            nbMarchandisesHC++;
            nouvProgress = 0;
        } else if (nouvProgress > 1) {
            nbMarchandisesHC++;
            nouvProgress = nouvProgress - 1;
        }

        // maj barre de progression
        jeu.getJoueur().getBoissonsChaudes().setEtatProgressDistributeur(nouvProgress);
        // maj marchandises supplémentaires
        nbMarchandisesHC = nbMarchandisesHC * jeu.getJoueur().getBoissonsChaudes().getNbDistributeurs();
        jeu.getJoueur().getBoissonsChaudes().setNbMarchandises(jeu.getJoueur().getBoissonsChaudes().getNbMarchandises() + nbMarchandisesHC);

        majGainsEnCoursBC();
    }

    /****************************************
     // Fin distributeur Boissons chaudes
     /**************************************

     /****************************************
     // Debut distributeur Boissons froides
     /**************************************
     */

    public void majGainsEnCoursBF() {
        System.out.println("maj des gains distributeur");
        // tant que non vendu, on cumule les gains en attente
        long nbMarchandisesBFEnCours = jeu.getJoueur().getBoissonsFraiches().getNbMarchandises();
        BigDecimal tarifMarchandiseBF = jeu.getParametres().getPrixMarchandiseBF();

        // calcul des gains attente
        this.gainEnAttenteBF = tarifMarchandiseBF.multiply(BigDecimal.valueOf(nbMarchandisesBFEnCours));

        // maj le montant sur le bouton Encaisser
        this.setBtnRecupereBF();
    }

    /**
     * Affiche ou non le bouton recuperer marchandises
     * suivant si gainEnAttente > 0
     */
    public void setBtnRecupereBF() {
        int comparaison = this.gainEnAttenteBF.compareTo(BigDecimal.valueOf(0));
        if (comparaison > 0) {
            this.getBtnRecupereBF().setDisable(false);
        } else {
            this.getBtnRecupereBF().setDisable(true);
        }
        String formattedString = "Encaisser " + decimalFormat.format(gainEnAttenteBF) + monnaie;
        this.getBtnRecupereBF().setText(formattedString);
    }

    /**
     * Affiche le bouton pour debloquer le distributeur de boissons chaudes
     * si le joueur a assez d'argent => disable=false, sinon disable = true
     * Ne doit s'afficher que si le distributeur est bloqué
     */
    public void testBoissonsFraiches() {
        // teste si le distributeur de boissons fraiches est bloqué
        if (jeu.getJoueur().getDistributeurBFActive() == 0) {
            this.btnDebloquerBF.setVisible(true);
            // teste si on a assez pour acheter le distributeur
            if (jeu.getJoueur().isArgent(jeu.getParametres().getPrixDistributeurBF())) {
                this.btnDebloquerBF.setDisable(false);
            } else {
                this.btnDebloquerBF.setDisable(true);
            }
        } else {
            debloquerDistributeur(paneBF, btnDebloquerBF, labelTitreDistributeurBF);
        }
    }


    /**
     * Bouton permettant d'acheter un distributeur de boissons fraiches
     */
    public void acheterBoissonsFraiches() {
        BigDecimal montantAchat = this.jeu.getParametres().getPrixDistributeurBF();
        if (acheter(montantAchat)) {
            this.jeu.getJoueur().getBoissonsFraiches().ajoutDistributeur();
            // met a jour le label en haut
            setLabelMessageHaut();
            System.out.println("Vous venez d'acheter un distributeur de boissons fraiches");
            // debloque le distributeur si besoin
            if (jeu.getJoueur().getBoissonsFraiches().getNbDistributeurs() == 1) {
                jeu.getJoueur().setDistributeurBFActive(1);
                debloquerDistributeur(paneBF, btnDebloquerBF, labelTitreDistributeurBF);
                // demarre la barre de progression du distributeur
                progressBarStartBF(0, jeu.getParametres().getVitesseBF());
            }
            miseEnPlaceValeurs();
        } else {
            System.out.println("Vous n'avez pas assez d'argent pour acheter un distributeur de boissons fraiches");
        }
    }

    /**
     * Met à jour la barre de progression pour distributeur de boissons chaudes
     *
     * @param cycle
     * @param vitesse
     */
    public void progressBarStartTimelineEncoursBF(int cycle, double vitesse) {
        ProgressBar progressBarBF = getProgressBF();
        Button btnRecupereBF = getBtnRecupereBF();
        // Réinitialise la barre de progression à 0
        progressBarBF.setProgress(this.jeu.getJoueur().getBoissonsFraiches().getEtatProgressDistributeur());
        timelineBF = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBarBF.progressProperty(), this.jeu.getJoueur().getBoissonsFraiches().getEtatProgressDistributeur())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Boisson fraiche terminée");
                    btnRecupereBF.setDisable(false);
                    // ajoute un nombre de marchandises correspondantes au nombre de distributeurs
                    this.majProgressBF();
                    // met à jour les gains en cours
                    this.majGainsEnCoursBF();
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
        Button btnRecupereBF = getBtnRecupereBF();
        // Réinitialise la barre de progression à 0
        progressBarBF.setProgress(0);
        timelineBF = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBarBF.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Distributeur de boissons fraiches prêt");
                    btnRecupereBF.setDisable(false);
                    // ajoute un nombre de marchandises correspondantes au nombre de distributeurs
                    this.majProgressBF();
                    // met à jour les gains en cours
                    this.majGainsEnCoursBF();
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
     * Arret de la barre de progression
     */
    private void progressBarStopBF() {
        if (timelineBF != null) {
            timelineBF.stop();
            timelineBF = null;
            System.out.println("Arret de la barre de progression");
        }
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
        this.setNbBF();
    }

    /**
     * Maj des labels et boutons du distributeur de boissons chaudes
     */
    public void majDistributeurBF() {
        setNbDistributeursBF();
        setNbBF();
        // affichage ou non du bouton pour recuperer l'argent du distributeur avec recuperation des gains
        majGainsEnCoursBF();
//        setBtnRecupereBF();
        // verif si assez d'argent pour acheter un distributeur
        if (jeu.getJoueur().isArgent(jeu.getParametres().getPrixDistributeurBF())) {
            btnDistributeurPlusBF.setDisable(false);
        } else {
            btnDistributeurPlusBF.setDisable(true);
        }
    }

    /**
     * action a effectuer lors du clic sur le bouton Vendre
     */
    public void onVendreBF() {
        long nbMarchandisesBF = jeu.getJoueur().getBoissonsFraiches().getNbMarchandises();

        jeu.getJoueur().getBoissonsFraiches().setNbMarchandises(0); // raz le nombre de marchandises
        jeu.getJoueur().setArgent(this.gainEnAttenteBF.add(jeu.getJoueur().getArgent())); // met a jour les nouveaux gains

        String formattedGain = decimalFormat.format(this.gainEnAttenteBF) + monnaie;
        // raz des gains en attente
        this.gainEnAttenteBF = BigDecimal.valueOf(0.00);

        System.out.println("Vous venez de vendre " + nbMarchandisesBF + " marchandises au distributeur de boisonns fraiches, vous avez gagné " + formattedGain + ".");

        getBtnRecupereBF().setDisable(true);
        miseEnPlaceValeurs();
    }

    /**
     * Gestion de la barre de progression Hors Connexion
     * pour distributeur de boissons fraiches
     */
    public void reajustementBF() {
        // variables necessaires
        LocalDateTime heureDeco = jeu.getJoueur().getBoissonsFraiches().getDateDeco();
        int vitesseDistributeur = jeu.getParametres().getVitesseBF();
        double ancBarreProgress = jeu.getJoueur().getBoissonsFraiches().getEtatProgressDistributeur();

        // maj barre de progression
        double nouvProgress = this.reajustementProgressDistributeur(heureDeco, vitesseDistributeur, ancBarreProgress);
        System.out.println("nouvProgress progress : " + nouvProgress);

        // calcul du nombre de marchandises vendues pendant le switch de fenetres ou déco

        long nbMarchandisesHC = ecartSwitchFenetre(heureDeco) / vitesseDistributeur;
        System.out.println("nombre de marchandises vendues : " + nbMarchandisesHC);

        // gestion si nouvProgress >1
        if (nouvProgress == 1) {
            // ajout d'une marchandise par distributeur et retour de la barre à 0
            nbMarchandisesHC++;
            nouvProgress = 0;
        } else if (nouvProgress > 1) {
            nbMarchandisesHC++;
            nouvProgress = nouvProgress - 1;
        }

        // maj barre de progression
        jeu.getJoueur().getBoissonsFraiches().setEtatProgressDistributeur(nouvProgress);
        // maj marchandises supplémentaires
        nbMarchandisesHC = nbMarchandisesHC * jeu.getJoueur().getBoissonsFraiches().getNbDistributeurs();
        jeu.getJoueur().getBoissonsFraiches().setNbMarchandises(jeu.getJoueur().getBoissonsFraiches().getNbMarchandises() + nbMarchandisesHC);

        majGainsEnCoursBF();
    }

    /****************************************
     // Debut distributeur Boissons confiseries
     /**************************************
     */

    public void majGainsEnCoursCo() {
        // tant que non vendu, on cumule les gains en attente
        long nbMarchandisesCoEnCours = jeu.getJoueur().getConfiseries().getNbMarchandises();
        BigDecimal tarifMarchandiseCo = jeu.getParametres().getPrixMarchandiseCo();

        // calcul des gains attente
        this.gainEnAttenteCo = tarifMarchandiseCo.multiply(BigDecimal.valueOf(nbMarchandisesCoEnCours));
        // maj le montant sur le bouton Encaisser
        this.setBtnRecupereCo();
    }

    /**
     * Affiche ou non le bouton recuperer marchandises
     * suivant si gainEnAttente > 0
     */
    public void setBtnRecupereCo() {
        int comparaison = this.gainEnAttenteCo.compareTo(BigDecimal.valueOf(0));
        if (comparaison > 0) {
            this.getBtnRecupereCo().setDisable(false);
        } else {
            this.getBtnRecupereCo().setDisable(true);
        }
        String formattedString = "Encaisser " + decimalFormat.format(gainEnAttenteCo) + monnaie;
        this.getBtnRecupereCo().setText(formattedString);
    }

    /**
     * Affiche le bouton pour debloquer le distributeur de confiseries
     * si le joueur a assez d'argent => disable=false, sinon disable = true
     * Ne doit s'afficher que si le distributeur est bloqué
     */
    public void testConfiseries() {
        // teste si le distributeur de confiseries est bloqué
        if (jeu.getJoueur().getDistributeurCoActive() == 0) {
            this.btnDebloquerCo.setVisible(true);
            // teste si on a assez pour acheter le distributeur
            if (jeu.getJoueur().isArgent(jeu.getParametres().getPrixDistributeurCo())) {
                this.btnDebloquerCo.setDisable(false);
            } else {
                this.btnDebloquerCo.setDisable(true);
            }
        } else {
            debloquerDistributeur(paneCo, btnDebloquerCo, labelTitreDistributeurCo);
        }
    }


    /**
     * Bouton permettant d'acheter un distributeur de confiseries
     */
    public void acheterConfiseries() {
        BigDecimal montantAchat = this.jeu.getParametres().getPrixDistributeurCo();
        if (acheter(montantAchat)) {
            this.jeu.getJoueur().getConfiseries().ajoutDistributeur();
            // met a jour le label en haut
            setLabelMessageHaut();
            System.out.println("Vous venez d'acheter un distributeur de confiseries");
            // debloque le distributeur si besoin
            if (jeu.getJoueur().getConfiseries().getNbDistributeurs() == 1) {
                jeu.getJoueur().setDistributeurCoActive(1);
                debloquerDistributeur(paneCo, btnDebloquerCo, labelTitreDistributeurCo);
                // demarre la barre de progression du distributeur
                progressBarStartCo(0, jeu.getParametres().getVitesseCo());
            }
            miseEnPlaceValeurs();
        } else {
            System.out.println("Vous n'avez pas assez d'argent pour acheter un distributeur de confiseries");
        }
        System.out.println("achat du distributueur :" + jeu.getJoueur());
    }

    /**
     * Met à jour la barre de progression pour distributeur de confiseries
     *
     * @param cycle
     * @param vitesse
     */
    public void progressBarStartTimelineEncoursCo(int cycle, double vitesse) {
        ProgressBar progressBarCo = getProgressCo();
        Button btnRecupereCo = getBtnRecupereCo();
        // Réinitialise la barre de progression à 0
        progressBarCo.setProgress(this.jeu.getJoueur().getConfiseries().getEtatProgressDistributeur());
        timelineCo = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBarCo.progressProperty(), this.jeu.getJoueur().getConfiseries().getEtatProgressDistributeur())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Boisson fraiche terminée");
                    btnRecupereCo.setDisable(false);
                    // ajoute un nombre de marchandises correspondantes au nombre de distributeurs
                    this.majProgressCo();
                    // met à jour les gains en cours
                    this.majGainsEnCoursCo();
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
        Button btnRecupereCo = getBtnRecupereCo();
        // Réinitialise la barre de progression à 0
        progressBarCo.setProgress(0);
        timelineCo = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBarCo.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Distributeur de confiseries prêt");
                    btnRecupereCo.setDisable(false);
                    // ajoute un nombre de marchandises correspondantes au nombre de distributeurs
                    this.majProgressCo();
                    // met à jour les gains en cours
                    this.majGainsEnCoursCo();
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
     * Arret de la barre de progression
     */
    private void progressBarStopCo() {
        if (timelineCo != null) {
            timelineCo.stop();
            timelineCo = null;
            System.out.println("Arret de la barre de progression confiseries");
        }
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
        this.setNbCo();
    }

    /**
     * Maj des labels et boutons du distributeur de confiseries
     */
    public void majDistributeurCo() {
        setNbDistributeursCo();
        setNbCo();
        // affichage ou non du bouton pour recuperer l'argent du distributeur avec recuperation des gains
        majGainsEnCoursCo();
//        setBtnRecupereCo();
        // verif si assez d'argent pour acheter un distributeur
        if (jeu.getJoueur().isArgent(jeu.getParametres().getPrixDistributeurCo())) {
            btnDistributeurPlusCo.setDisable(false);
        } else {
            btnDistributeurPlusCo.setDisable(true);
        }
    }

    /**
     * action a effectuer lors du clic sur le bouton Vendre
     */
    public void onVendreCo() {
        long nbMarchandisesCo = jeu.getJoueur().getConfiseries().getNbMarchandises();

        jeu.getJoueur().getConfiseries().setNbMarchandises(0); // raz le nombre de marchandises
        jeu.getJoueur().setArgent(this.gainEnAttenteCo.add(jeu.getJoueur().getArgent())); // met a jour les nouveaux gains

        String formattedGain = decimalFormat.format(this.gainEnAttenteCo) + monnaie;
        // raz des gains en attente
        this.gainEnAttenteCo = BigDecimal.valueOf(0.00);

        System.out.println("Vous venez de vendre " + nbMarchandisesCo + " marchandises au distributeur de confiseries, vous avez gagné " + formattedGain + ".");

        getBtnRecupereCo().setDisable(true);
        miseEnPlaceValeurs();
    }

    /**
     * Gestion de la barre de progression Hors Connexion
     * pour distributeur de confiseries
     */
    public void reajustementCo() {
        // variables necessaires
        LocalDateTime heureDeco = jeu.getJoueur().getConfiseries().getDateDeco();
        int vitesseDistributeur = jeu.getParametres().getVitesseCo();
        double ancBarreProgress = jeu.getJoueur().getConfiseries().getEtatProgressDistributeur();

        // maj barre de progression
        double nouvProgress = this.reajustementProgressDistributeur(heureDeco, vitesseDistributeur, ancBarreProgress);
        System.out.println("nouvProgress progress : " + nouvProgress);

        // calcul du nombre de marchandises vendues pendant le switch de fenetres ou déco

        long nbMarchandisesHC = ecartSwitchFenetre(heureDeco) / vitesseDistributeur;
        System.out.println("nombre de marchandises vendues : " + nbMarchandisesHC);

        // gestion si nouvProgress >1
        if (nouvProgress == 1) {
            // ajout d'une marchandise par distributeur et retour de la barre à 0
            nbMarchandisesHC++;
            nouvProgress = 0;
        } else if (nouvProgress > 1) {
            nbMarchandisesHC++;
            nouvProgress = nouvProgress - 1;
        }

        // maj barre de progression
        jeu.getJoueur().getConfiseries().setEtatProgressDistributeur(nouvProgress);
        // maj marchandises supplémentaires
        nbMarchandisesHC = nbMarchandisesHC * jeu.getJoueur().getConfiseries().getNbDistributeurs();
        jeu.getJoueur().getConfiseries().setNbMarchandises(jeu.getJoueur().getConfiseries().getNbMarchandises() + nbMarchandisesHC);

        majGainsEnCoursCo();
    }

    /****************************************
     // Fin distributeur Confiseries
     /**************************************
     */

    /****************************************
     // Debut distributeur Sandwichs
     /**************************************
     */

    public void majGainsEnCoursSa() {
        // tant que non vendu, on cumule les gains en attente
        long nbMarchandisesSaEnCours = jeu.getJoueur().getSandwichs().getNbMarchandises();
        BigDecimal tarifMarchandiseSa = jeu.getParametres().getPrixMarchandiseSa();

        // calcul des gains attente
        this.gainEnAttenteSa = tarifMarchandiseSa.multiply(BigDecimal.valueOf(nbMarchandisesSaEnCours));

        // maj le montant sur le bouton Encaisser
        this.setBtnRecupereSa();
    }

    /**
     * Affiche ou non le bouton recuperer marchandises
     * suivant si gainEnAttente > 0
     */
    public void setBtnRecupererSa() {
        int comparaison = this.gainEnAttenteSa.compareTo(BigDecimal.valueOf(0));
        if (comparaison > 0) {
            this.getBtnRecupereSa().setDisable(false);
        } else {
            this.getBtnRecupereSa().setDisable(true);
        }
        String formattedString = "Encaisser " + decimalFormat.format(gainEnAttenteSa) + monnaie;
        this.getBtnRecupereSa().setText(formattedString);
    }

    /**
     * Affiche le bouton pour debloquer le distributeur de Sandwichs
     * si le joueur a assez d'argent => disable=false, sinon disable = true
     * Ne doit s'afficher que si le distributeur est bloqué
     */
    public void testSandwichs() {
        // teste si le distributeur de Sandwichs est bloqué
        if (jeu.getJoueur().getDistributeurSaActive() == 0) {
            this.btnDebloquerSa.setVisible(true);
            // teste si on a assez pour acheter le distributeur
            if (jeu.getJoueur().isArgent(jeu.getParametres().getPrixDistributeurSa())) {
                this.btnDebloquerSa.setDisable(false);
            } else {
                this.btnDebloquerSa.setDisable(true);
            }
        } else {
            debloquerDistributeur(paneSa, btnDebloquerSa, labelTitreDistributeurSa);
        }
    }


    /**
     * Bouton permettant d'acheter un distributeur de Sandwichs
     */
    public void acheterSandwichs() {
        BigDecimal montantAchat = this.jeu.getParametres().getPrixDistributeurSa();
        if (acheter(montantAchat)) {
            this.jeu.getJoueur().getSandwichs().ajoutDistributeur();
            System.out.println("Vous venez d'acheter un distributeur de sandwichs");
            // debloque le distributeur si besoin
            if (jeu.getJoueur().getSandwichs().getNbDistributeurs() == 1) {
                jeu.getJoueur().setDistributeurSaActive(1);
                debloquerDistributeur(paneSa, btnDebloquerSa, labelTitreDistributeurSa);
                // demarre la barre de progression du distributeur
                progressBarStartSa(0, jeu.getParametres().getVitesseSa());
            }
            miseEnPlaceValeurs();
        } else {
            System.out.println("Vous n'avez pas assez d'argent pour acheter un distributeur de sandwichs");
        }
        System.out.println("achat du distributueur :" + jeu.getJoueur());
    }

    /**
     * Met à jour la barre de progression pour distributeur de Sandwichs
     *
     * @param cycle
     * @param vitesse
     */
    public void progressBarStartTimelineEncoursSa(int cycle, double vitesse) {
        ProgressBar progressBarSa = getProgressSa();
        Button btnRecupereSa = getBtnRecupereSa();
        // Réinitialise la barre de progression à 0
        progressBarSa.setProgress(this.jeu.getJoueur().getSandwichs().getEtatProgressDistributeur());
        timelineSa = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBarSa.progressProperty(), this.jeu.getJoueur().getSandwichs().getEtatProgressDistributeur())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Sandwichs terminés");
                    btnRecupereSa.setDisable(false);
                    // ajoute un nombre de marchandises correspondantes au nombre de distributeurs
                    this.majProgressSa();
                    // met à jour les gains en cours
                    this.majGainsEnCoursSa();
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
        Button btnRecupereSa = getBtnRecupereSa();
        // Réinitialise la barre de progression à 0
        progressBarSa.setProgress(0);
        timelineSa = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBarSa.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Distributeur de sandwichs prêt");
                    btnRecupererSa.setDisable(false);
                    // ajoute un nombre de marchandises correspondantes au nombre de distributeurs
                    this.majProgressSa();
                    // met à jour les gains en cours
                    this.majGainsEnCoursSa();
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
     * Arret de la barre de progression
     */
    private void progressBarStopSa() {
        if (timelineSa != null) {
            timelineSa.stop();
            timelineSa = null;
            System.out.println("Arret de la barre de progression Sandwichs");
        }
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
        this.setNbSa();
        System.out.println("maj du nombre de marchandises vendues dans les distributeurs de sandwichs : " + nouvNombre);
    }

    /**
     * Maj des labels et boutons du distributeur de sandwichs
     */
    public void majDistributeurSa() {
        setNbDistributeursSa();
        setNbSa();
        // affichage ou non du bouton pour recuperer l'argent du distributeur avec recuperation des gains
        majGainsEnCoursSa();
//        setBtnRecupererSa();
        // verif si assez d'argent pour acheter un distributeur
        if (jeu.getJoueur().isArgent(jeu.getParametres().getPrixDistributeurSa())) {
            btnDistributeurPlusSa.setDisable(false);
        } else {
            btnDistributeurPlusSa.setDisable(true);
        }
    }

    /**
     * action a effectuer lors du clic sur le bouton Vendre
     */
    public void onVendreSa() {
        long nbMarchandisesSa = jeu.getJoueur().getSandwichs().getNbMarchandises();

        jeu.getJoueur().getSandwichs().setNbMarchandises(0); // raz le nombre de marchandises
        jeu.getJoueur().setArgent(this.gainEnAttenteSa.add(jeu.getJoueur().getArgent())); // met a jour les nouveaux gains

        String formattedGain = decimalFormat.format(this.gainEnAttenteSa) + monnaie;
        // raz des gains en attente
        this.gainEnAttenteSa = BigDecimal.valueOf(0.00);

        System.out.println("Vous venez de vendre " + nbMarchandisesSa + " marchandises au distributeur de sandwichs, vous avez gagné " + formattedGain + ".");

        getBtnRecupereSa().setDisable(true);
        miseEnPlaceValeurs();
    }

    /**
     * Gestion de la barre de progression Hors Connexion
     * pour distributeur de confiseries
     */
    public void reajustementSa() {
        // variables necessaires
        LocalDateTime heureDeco = jeu.getJoueur().getSandwichs().getDateDeco();
        int vitesseDistributeur = jeu.getParametres().getVitesseSa();
        double ancBarreProgress = jeu.getJoueur().getSandwichs().getEtatProgressDistributeur();

        // maj barre de progression
        double nouvProgress = this.reajustementProgressDistributeur(heureDeco, vitesseDistributeur, ancBarreProgress);
        System.out.println("nouvProgress progress : " + nouvProgress);

        // calcul du nombre de marchandises vendues pendant le switch de fenetres ou déco

        long nbMarchandisesHC = ecartSwitchFenetre(heureDeco) / vitesseDistributeur;
        System.out.println("nombre de marchandises vendues : " + nbMarchandisesHC);

        // gestion si nouvProgress >1
        if (nouvProgress == 1) {
            // ajout d'une marchandise par distributeur et retour de la barre à 0
            nbMarchandisesHC++;
            nouvProgress = 0;
        } else if (nouvProgress > 1) {
            nbMarchandisesHC++;
            nouvProgress = nouvProgress - 1;
        }

        // maj barre de progression
        jeu.getJoueur().getSandwichs().setEtatProgressDistributeur(nouvProgress);
        // maj marchandises supplémentaires
        nbMarchandisesHC = nbMarchandisesHC * jeu.getJoueur().getSandwichs().getNbDistributeurs();
        jeu.getJoueur().getSandwichs().setNbMarchandises(jeu.getJoueur().getSandwichs().getNbMarchandises() + nbMarchandisesHC);

        majGainsEnCoursSa();
    }

    /**
     * Affiche ou non le bouton recuperer marchandises
     * suivant si gainEnAttente > 0
     */
    public void setBtnRecupereSa() {
        int comparaison = this.gainEnAttenteSa.compareTo(BigDecimal.valueOf(0));
        if (comparaison > 0) {
            this.getBtnRecupereSa().setDisable(false);
        } else {
            this.getBtnRecupereSa().setDisable(true);
        }
        String formattedString = "Encaisser " + decimalFormat.format(gainEnAttenteSa) + monnaie;
        this.getBtnRecupereSa().setText(formattedString);
    }

    /****************************************
     // Fin distributeur Sandwichs
     /**************************************
     */

    /**
     * Permet de sauvegarder la partie dans la Bdd
     */
    public void sauvegardejeu()  {
        // mise a jour instance sauvegarde
        jeu.getSauvegarde().setArgent(jeu.getJoueur().getArgent());
        jeu.getSauvegarde().setNbPoules(jeu.getJoueur().getFerme().getNbPoules());
        jeu.getSauvegarde().setNbOeufs(jeu.getJoueur().getFerme().getNbOeufs());
        jeu.getSauvegarde().setEtatProgressOeuf(jeu.getJoueur().getFerme().getEtatProgressOeuf());
        // a modifier quand plusieurs activites
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

        System.out.println("Nouvelles valeurs a sauvegarder" + jeu.getSauvegarde());

        // sauvegarde dans la bdd
        ConnectionBdd connectionBdd = new ConnectionBdd();
        connectionBdd.connect();
        SauvegardeService sauvegardeService = new SauvegardeService(connectionBdd);
        try {
            sauvegardeService.majSauvegarde(jeu.getSauvegarde());
        } catch (Exception e){
            System.out.println(e);
        }
        connectionBdd.close();
    }

    // partie pour les barres de progress Heure et Jour
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


    // Methodes communes


    /**
     * renvoi true si le nombre maximum de distributeur est atteint
     * @return
     */
    public boolean isMaxiNbDitributeur(Distributeurs distributeurs){
        return distributeurs.getNbDistributeurs() >= distributeurs.getNbMaxiDistributeur();
    }
}
