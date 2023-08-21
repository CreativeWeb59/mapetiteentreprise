package com.example.mapetiteentreprise.controllersFx;

import com.example.mapetiteentreprise.Main;
import com.example.mapetiteentreprise.actions.Outils;
import com.example.mapetiteentreprise.bdd.ConnectionBdd;
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

public class GestionLivraisonsController {
    private final String monnaie = " €";
    private final String separationTexte = System.getProperty("line.separator");
    private final DecimalFormat decimalFormat = Outils.getDecimalFormatWithSpaceSeparator();
    @FXML
    private Label labelHaut, labelNbCoursesScooter, labelNbScooter, labelTarifScooter,
            labelTarifCamionette, labelNbCamionette, labelNbCoursesCamionette,
            labelTarifPetitCamion, labelNbPetitCamion, labelNbCoursesPetitCamion,
            labelTarifPoidsLour, labelNbPoidsLourd, labelNbCoursesPoidsLourd;
    @FXML
    private Button retourMenu, btnAchatLivraisonScooter, btnAchatScooter, btnEncaisserCourseScooter,
            onBtnEncaisserCourse2, onAchatLivraisonCamionette, btnAchatCamionette, btnEncaisserCourseCamionette, btnAchatLivraisonCamionette,
            onBtnEncaisserCourse3, onAchatLivraisonPetitCamion, btnAchatPetitCamion, btnEncaisserCoursePetitCamion, btnAchatLivraisonPetitCamion,
            onBtnEncaisserCourse4, onAchatLivraisonPoidsLourd, btnAchatPoidsLourd, btnEncaisserPoidsLourd, btnAchatLivraisonPoidsLourd;
    @FXML
    private Pane paneScooter, paneScooterD, paneCamionette, paneCamionetteD, panePetitCamion, panePetitCamionD, panePoidsLourd, panePoidsLourdD, paneProgress;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Jeu jeu;
    @FXML
    private ProgressBar progressJour, progressOeufs, progressBC, progressBF, progressSa, progressCo, progressScooter, progressCamionette, progressPetitCamion, progressPoidsLourd;
    private Timeline timelineOeufs, timelineJour, timelineBC, timelineBF, timelineSa, timelineCo, timelineScooter, timelineCamionette, timelinePetitCamion, timelinePoidsLourd;
    private ConnectionBdd connectionBdd = new ConnectionBdd();
    private BigDecimal gainEnAttenteScooter = new BigDecimal(0);
    private BigDecimal gainEnAttenteCamionette = new BigDecimal(0);
    private BigDecimal gainEnAttentePetitCamion = new BigDecimal(0);
    private BigDecimal gainEnAttentePoidsLourd = new BigDecimal(0);

    /**
     * Execute la fenetre ferme.xml
     *
     * @param jeu
     */
    public void demarrer(Jeu jeu) {
        // Recuperation du jeu
        this.jeu = jeu;
        System.out.println("Page des livraisons");

        // majLabels et boutons
        miseEnPlace();
        recupCourses();

        // barres de progression
        demarrageLivraisons();
        startProgressBars();

        // affichage des barres de progression (mode dev)
        jeu.afficheProgression(paneProgress);
    }

    /**
     * Action a executer lors de la fermeture de la fenetre avec la croix : sauvegarde
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

    public void retourGestion(ActionEvent event) {
        // on recupere l'etat de la barre de progression des oeufs
//        this.jeu.getJoueur().getFerme().setEtatProgressOeuf(this.progressOeufs.getProgress());
//        this.jeu.getCalendrier().setProgressJour(this.progressJour.getProgress());

        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();

        // on enregistre l'heure de switch de fenetre
        this.jeu.getJoueur().getFerme().setDateDeco(LocalDateTime.now());

        // on stoppe les barres de progression;
//        this.progressBarStop(timelineOeufs);
//        this.progressBarStop(timelineJour);

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
     * Ajout d'un scooter
     */
    public void acheterScooter() {
        BigDecimal montantAchat = this.jeu.getJoueur().getLivraisonScooter().getPrixVehicule();
        if (jeu.getJoueur().acheter(montantAchat)) {
            this.jeu.getJoueur().getLivraisonScooter().ajoutServiceDeLivraison();
            System.out.println("Achat d'un scooter : " + jeu.getJoueur().getLivraisonScooter().getNbVehicules());
            // debloque le scooter si besoin
            if (jeu.getJoueur().getLivraisonScooter().getNbVehicules() == 1) {
                jeu.getJoueur().setLivraison1Active(1);
//                debloquerDistributeur(paneBF, btnDebloquerBF, labelTitreDistributeurBF);
                // demarre la barre de progression du distributeur
                progressBarStartScooter(0, jeu.getJoueur().getLivraisonScooter().getVitesseLivraion());
            }
            // mise a jour des valeurs
            miseEnPlace();
        } else {
            System.out.println("Vous n'avez pas assez d'argent pour acheter un service de livraison en scooter");
        }
    }

    /**
     * Ajout d'une camionnette
     */
    public void acheterCamionette() {
        BigDecimal montantAchat = this.jeu.getJoueur().getLivraisonCamionette().getPrixVehicule();
        if (jeu.getJoueur().acheter(montantAchat)) {
            this.jeu.getJoueur().getLivraisonCamionette().ajoutServiceDeLivraison();
            System.out.println("Achat d'une camionette : " + jeu.getJoueur().getLivraisonCamionette().getNbVehicules());
            // debloque le scooter si besoin
            if (jeu.getJoueur().getLivraisonCamionette().getNbVehicules() == 1) {
                jeu.getJoueur().setLivraison2Active(1);
//                debloquerDistributeur(paneBF, btnDebloquerBF, labelTitreDistributeurBF);
                // demarre la barre de progression du distributeur
                progressBarStartScooter(0, jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion());
            }
            // mise a jour des valeurs
            miseEnPlace();
        } else {
            System.out.println("Vous n'avez pas assez d'argent pour acheter un service de livraison en scooter");
        }
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
        // boutons du scooter
        if (isActif(jeu.getJoueur().getLivraison1Active())) {
            getBtnEncaisserCourseScooter().setVisible(true);
            getBtnAchatLivraisonScooter().setVisible(false);
            System.out.println("Actif");
            setBtnEncaisserCourseScooter();
        } else {
            getBtnEncaisserCourseScooter().setVisible(false);
            getBtnAchatLivraisonScooter().setVisible(true);
            if(jeu.getJoueur().isArgent(jeu.getJoueur().getLivraisonScooter().getPrixVehicule())){
                paneScooterD.setOpacity(1);
                paneScooterD.setDisable(false);
                getBtnAchatLivraisonScooter().setDisable(false);
            } else {
                getBtnAchatLivraisonScooter().setDisable(true);
            }
            System.out.println("non actif");
        }
        // boutons camionette
        if (isActif(jeu.getJoueur().getLivraison2Active())) {
            getBtnEncaisserCourseCamionette().setVisible(true);
            getBtnAchatLivraisonCamionette().setVisible(false);
            System.out.println("Actif");
            setBtnEncaisserCourseScooter();
        } else {
            getBtnEncaisserCourseCamionette().setVisible(false);
            getBtnAchatLivraisonCamionette().setVisible(true);
            if(jeu.getJoueur().isArgent(jeu.getJoueur().getLivraisonCamionette().getPrixVehicule())){
                paneCamionetteD.setOpacity(1);
                paneCamionetteD.setDisable(false);
                getBtnAchatLivraisonCamionette().setDisable(false);
            } else {
                getBtnAchatLivraisonCamionette().setDisable(true);
            }
            System.out.println("non actif");
        }
        testBtnAchats();
    }

    /**
     * bloque le pan
     */
    public void blocageLivraison(){

    }

    /**
     * Active / desactive le bouton d'achat de vehicule de livraison
     */
    public void testBtnAchats(){
        // scooter
        BigDecimal prixScooter = jeu.getJoueur().getLivraisonScooter().getPrixVehicule();
        if(jeu.getJoueur().isArgent(prixScooter)){
            btnAchatScooter.setDisable(false);
        } else{
            btnAchatScooter.setDisable(true);
        }

        // camionette
        BigDecimal camionette = jeu.getJoueur().getLivraisonCamionette().getPrixVehicule();
        if(jeu.getJoueur().isArgent(camionette)){
            btnAchatScooter.setDisable(false);
        } else{
            btnAchatScooter.setDisable(true);
        }
    }

    /**
     * Gere le bouton d'achat du premier service de livraison
     */
    public void onAchatLivraisonScooter() {
        // activation de la livraison
        jeu.getJoueur().setLivraison1Active(1);

        // ajout du scooter
        jeu.getJoueur().getLivraisonScooter().setNbVehicules(1);
        // dépense du montant
        jeu.getJoueur().depenser(jeu.getJoueur().getLivraisonScooter().getPrixVehicule());

        // maj des labels et boutons
        miseEnPlace();

        // opacite du pane
        paneScooter.setOpacity(1);
        paneScooterD.setOpacity(1);

        // demarrage barre de progression
        progressBarStartScooter(0, jeu.getJoueur().getLivraisonScooter().getVitesseLivraion());
    }

    /**
     * Bouton qui permet d'encaisser l'argent du service de livraison
     */
    public void onBtnEncaisserCourse1(){
        long nbCoursesScooter = jeu.getJoueur().getLivraisonScooter().getNbCourses();

        jeu.getJoueur().getLivraisonScooter().setNbCourses(0); // raz le nombre de marchandises
        jeu.getJoueur().setArgent(this.gainEnAttenteScooter.add(jeu.getJoueur().getArgent())); // met a jour les nouveaux gains

        String formattedGain = decimalFormat.format(this.gainEnAttenteScooter) + monnaie;
        // raz des gains en attente
        this.gainEnAttenteScooter = BigDecimal.valueOf(0.00);

        System.out.println("Vous venez de récupérer le prix de " + nbCoursesScooter + " courses en scooter, vous avez gagné " + formattedGain + ".");

        getBtnEncaisserCourseScooter().setDisable(true);
        this.miseEnPlace();
    }
    /**
     * Gere le bouton d'achat du premier service de livraison en camionette
     */
    public void onAchatLivraisonCamionette() {
        // activation de la livraison
        jeu.getJoueur().setLivraison2Active(1);

        // ajout du scooter
        jeu.getJoueur().getLivraisonCamionette().setNbVehicules(1);
        // dépense du montant
        jeu.getJoueur().depenser(jeu.getJoueur().getLivraisonCamionette().getPrixVehicule());

        // maj des labels et boutons
        miseEnPlace();

        // opacite du pane
        paneCamionette.setOpacity(1);
        paneCamionetteD.setOpacity(1);
        System.out.println("opacite pane");

        // demarrage barre de progression
        progressBarStartCamionette(0, jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion());
    }

    /**
     * Bouton qui permet d'encaisser l'argent du service de livraison en camionettes
     */
    public void onBtnEncaisserCourse2(){
        long nbCoursesCamionette = jeu.getJoueur().getLivraisonCamionette().getNbCourses();

        jeu.getJoueur().getLivraisonCamionette().setNbCourses(0); // raz le nombre de marchandises
        jeu.getJoueur().setArgent(this.gainEnAttenteCamionette.add(jeu.getJoueur().getArgent())); // met a jour les nouveaux gains

        String formattedGain = decimalFormat.format(this.gainEnAttenteCamionette) + monnaie;
        // raz des gains en attente
        this.gainEnAttenteCamionette = BigDecimal.valueOf(0.00);

        System.out.println("Vous venez de récupérer le prix de " + nbCoursesCamionette + " courses en camcionette, vous avez gagné " + formattedGain + ".");

        getBtnEncaisserCourseCamionette().setDisable(true);
        this.miseEnPlace();
    }

    /**
     * Demarre les services de livraison s'ils sont actifs
     */
    public void demarrageLivraisons() {
        if (isActif(jeu.getJoueur().getLivraison1Active())) {
            System.out.println("Demarrage livraison en scooter");
            // enable pane de la livraison
            this.debloquerLivraison(paneScooter, paneScooterD);
            // recuperation de l'etat de la barre de progression pour la livraison en scooter
            double vitesseScooter = jeu.getJoueur().getLivraisonScooter().getVitesseLivraion() - (jeu.getJoueur().getLivraisonScooter().getVitesseLivraion() * jeu.getJoueur().getLivraisonScooter().getEtatProgressLivraison());
            progressBarStartScooterEnCours(1, vitesseScooter);
        }
        if(isActif(jeu.getJoueur().getLivraison2Active())){
            System.out.println("Demarrage livraison en camionette");
            // enable pane de la livraison
            this.debloquerLivraison(paneCamionette, paneCamionetteD);
            double vitesseCamionette = jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion() - (jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion() * jeu.getJoueur().getLivraisonCamionette().getEtatProgressLivraison());
            progressBarStartCamionetteEnCours(1, vitesseCamionette);
        }
        if(isActif(jeu.getJoueur().getLivraison3Active())){
            System.out.println("Demarrage livraison en petit camion");
            // enable pane de la livraison
            this.debloquerLivraison(panePetitCamion, panePetitCamionD);
        }
        if(isActif(jeu.getJoueur().getLivraison4Active())){
            System.out.println("Demarrage livraison en poids lourd");
            // enable pane de la livraison
            this.debloquerLivraison(panePoidsLourd, panePoidsLourdD);
        }
    }

    /**
     * Recup des courses effectuées pendant le switch de fenetre
     * et maj des gains encours
     */
    public void recupCourses(){
        // scooter
        long nbCoursesEnCours = jeu.getJoueur().getLivraisonScooter().getNbCourses();
        BigDecimal tarifCourse = jeu.getJoueur().getLivraisonScooter().getPrixCourse();
        if(nbCoursesEnCours > 0){
            btnEncaisserCourseScooter.setDisable(false);
            majGainsEnCoursScooter();
        } else {
            btnEncaisserCourseScooter.setDisable(true);
        }
        // camionette
        long nbCoursesEnCours2 = jeu.getJoueur().getLivraisonCamionette().getNbCourses();
        tarifCourse = jeu.getJoueur().getLivraisonCamionette().getPrixCourse();
        if(nbCoursesEnCours2 > 0){
            btnEncaisserCourseCamionette.setDisable(false);
            majGainsEnCoursCamionette();
        } else {
            btnEncaisserCourseCamionette.setDisable(true);
        }
        // petit camion
        long nbCoursesEnCours3 = jeu.getJoueur().getLivraisonPetitCamion().getNbCourses();
        tarifCourse = jeu.getJoueur().getLivraisonPetitCamion().getPrixCourse();
        if(nbCoursesEnCours2 > 0){
            btnEncaisserCoursePetitCamion.setDisable(false);
            majGainsEnCoursCamionette();
        } else {
            btnEncaisserCoursePetitCamion.setDisable(true);
        }
    }


    /**
     * Retourne vrai si le distributeur est actif
     *
     * @param valeurLivraison
     * @return
     */
    public boolean isActif(int valeurLivraison) {
        if (valeurLivraison == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * initialise le nombre de scooters en cours ainsi que le nombre de scooter maximum
     */
    public void setNbScooter() {
        String formattedString = this.jeu.getJoueur().getLivraisonScooter().setNbVehicule();
        this.labelNbScooter.setText(formattedString);
    }
    /**
     * initialise le nombre de scooters en cours ainsi que le nombre de scooter maximum
     */
    public void setNbCamionette() {
        String formattedString = this.jeu.getJoueur().getLivraisonCamionette().setNbVehicule();
        this.labelNbCamionette.setText(formattedString);
    }
    /**
     * initialise le nombre de scooters en cours ainsi que le nombre de scooter maximum
     */
    public void setNbPetitCamion() {
        String formattedString = this.jeu.getJoueur().getLivraisonPetitCamion().setNbVehicule();
        this.labelNbPetitCamion.setText(formattedString);
    }

    /**
     * Recuperation du bouton pour encaisser les courses du scooter
     *
     * @return
     */
    public Button getBtnEncaisserCourseScooter() {
        return btnEncaisserCourseScooter;
    }

    /**
     * Recuperation du bouton pour acheter le service de livraison en scooter
     *
     * @return
     */
    public Button getBtnAchatLivraisonScooter() {
        return btnAchatLivraisonScooter;
    }

    /**
     * Debut barre progress livraisons
     */

    public ProgressBar getProgressScooter() {
        return progressScooter;
    }

    /**
     * Met à jour la barre de progression pour livraison scooter
     *
     * @param cycle
     * @param vitesse
     */
    public void progressBarStartScooterEnCours(int cycle, double vitesse) {
        ProgressBar progressScooter = getProgressScooter();
        Button btnEncaisserCourseScooter = getBtnEncaisserCourseScooter();
        // Réinitialise la barre de progression à 0
        progressScooter.setProgress(this.jeu.getJoueur().getLivraisonScooter().getEtatProgressLivraison());
        timelineScooter = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressScooter.progressProperty(), this.jeu.getJoueur().getLivraisonScooter().getEtatProgressLivraison())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Course en scooter terminée");
                    btnEncaisserCourseScooter.setDisable(false);
                    // ajoute une course au service de livraison
                    this.majProgressScooter();
                    // met à jour les gains en cours
                    this.majGainsEnCoursScooter();
                    // maj des boutons
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
        Button btnEncaisserCourseScooter = getBtnEncaisserCourseScooter();
        // Réinitialise la barre de progression à 0
        progressScooter.setProgress(0);
        timelineScooter = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressScooter.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Distributeur de boissons chaudes prêt");
                    btnEncaisserCourseScooter.setDisable(false);
                    // ajoute un nombre de marchandises correspondantes au nombre de distributeurs
                    this.majProgressScooter();
                    // met à jour les gains en cours
                    this.majGainsEnCoursScooter();
                    // maj des boutons
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
        this.setNbCoursesScooter();
        System.out.println("maj du nombre de livraisons effectuées en scooter : " + nouvNombre);
    }

    public void setNbCoursesScooter() {
        this.labelNbCoursesScooter.setText(jeu.getJoueur().getLivraisonScooter().getNbCourses() + "");
    }

    public void majLabels() {
        setLabelHaut();
        labelsScooter();
        labelsCamionette();
        labelsPetitCamion();
    }
    /**
     * Labels du scooter
     */
    public void labelsScooter(){
        setNbScooter();
        setNbCoursesScooter();
        setLabelTarifScooter();
    }
    /**
     * Labels du camionnette
     */
    public void labelsCamionette(){
        setNbCamionette();
        setNbCoursesCamionette();
        setLabelTarifCamionette();
    }

    /**
     * Labels petit camion
     */
    public void labelsPetitCamion(){
        setNbPetitCamion();
        setNbCoursesPetitCamion();
        setLabelTarifPetitCamion();
    }

    public void majGainsEnCoursScooter() {
        System.out.println("maj des gains courses des livraisons en scooter");
        // tant que non vendu, on cumule les gains en attente
        long nbLivraisonsEncours = jeu.getJoueur().getLivraisonScooter().getNbCourses();
        BigDecimal tarifCourseScooter = jeu.getJoueur().getLivraisonScooter().getPrixCourse();

        // calcul des gains attente
        this.gainEnAttenteScooter = tarifCourseScooter.multiply(BigDecimal.valueOf(nbLivraisonsEncours));

        // maj le montant sur le bouton Encaisser
        this.setBtnEncaisserCourseScooter();
    }

    /**
     * Inscrit le prix d'achat d'un scooter
     */
    public void setLabelTarifScooter(){
        BigDecimal prixScooter = jeu.getJoueur().getLivraisonScooter().getPrixVehicule();
        String nomVehicule = jeu.getJoueur().getLivraisonScooter().getNom();
        String formattedString = "Acheter " + nomVehicule + " : " + decimalFormat.format(prixScooter) + monnaie;
        labelTarifScooter.setText(formattedString);
    }

    /**
     * Affiche ou non le bouton recuperer marchandises
     * suivant si gainEnAttente > 0
     */
    public void setBtnEncaisserCourseScooter() {
        int comparaison = this.gainEnAttenteScooter.compareTo(BigDecimal.valueOf(0));
        if (comparaison > 0) {
            this.getBtnEncaisserCourseScooter().setDisable(false);
        } else {
            this.getBtnEncaisserCourseScooter().setDisable(true);
        }
        String formattedString = "Encaisser " + decimalFormat.format(gainEnAttenteScooter) + monnaie;
        this.getBtnEncaisserCourseScooter().setText(formattedString);
    }

    public void setLabelHaut() {
        String formattedString = "En banque : " + decimalFormat.format(jeu.getJoueur().getArgent()) + monnaie;
        this.labelHaut.setText(formattedString);
    }

    /**
     * Gestion de la visibilite des livraisons acquises
     */
    /**

     * Activation de la livraison en parametres
     *
     * @param paneLivraison       => nom de la livraison a debloquer
     * @param paneLivraisonD    => pane de droite de la livraison
     * @param            => nom du bouton à rendre invisible correspondant au distributeur
     * @param  => nom du label affichant le distributeur à rendre invisible
     */
    public void debloquerLivraison(Pane paneLivraison, Pane paneLivraisonD) {
        paneLivraison.setDisable(false);
        paneLivraison.setOpacity(1);
        paneLivraisonD.setDisable(false);
        paneLivraisonD.setOpacity(1);

//        btnDebloquer.setVisible(false);
//        labelTitreLivraison.setVisible(false);
    }

    // Livraisons en camionette

    /**
     * Inscrit le prix d'achat d'un scooter
     */
    public void setLabelTarifCamionette(){
        BigDecimal prixCamionette = jeu.getJoueur().getLivraisonCamionette().getPrixVehicule();
        String nomVehicule = jeu.getJoueur().getLivraisonCamionette().getNom();
        String formattedString = "Acheter " + nomVehicule + " : " + decimalFormat.format(prixCamionette) + monnaie;
        labelTarifCamionette.setText(formattedString);
    }

    /**
     * Recuperation du bouton pour encaisser les courses du scooter
     *
     * @return
     */
    public Button getBtnEncaisserCourseCamionette() {
        return btnEncaisserCourseCamionette;
    }

    /**
     * Recuperation du bouton pour acheter le service de livraison en scooter
     *
     * @return
     */
    public Button getBtnAchatLivraisonCamionette() {
        return btnAchatLivraisonCamionette;
    }

    /**
     * Debut barre progress livraisons
     */

    public ProgressBar getProgressCamionette() {
        return progressCamionette;
    }

    /**
     * Met à jour la barre de progression pour livraison camionette
     *
     * @param cycle
     * @param vitesse
     */
    public void progressBarStartCamionetteEnCours(int cycle, double vitesse) {
        ProgressBar progressCamionette = getProgressCamionette();
        Button btnEncaisserCourseCamionette = getBtnEncaisserCourseCamionette();
        // Réinitialise la barre de progression à 0
        progressCamionette.setProgress(this.jeu.getJoueur().getLivraisonCamionette().getEtatProgressLivraison());
        timelineCamionette = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressCamionette.progressProperty(), this.jeu.getJoueur().getLivraisonCamionette().getEtatProgressLivraison())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Course en camionette terminée");
                    btnEncaisserCourseCamionette.setDisable(false);
                    // ajoute une course au service de livraison
                    this.majProgressCamionette();
                    // met à jour les gains en cours
                    this.majGainsEnCoursCamionette();
                    // maj des boutons
                }, new KeyValue(progressCamionette.progressProperty(), 1))
        );
        timelineCamionette.setOnFinished(event -> {
            if (cycle == 1) {
                // Lancer la deuxième exécution de la méthode progressBarStartTimeline
                jeu.getJoueur().getLivraisonCamionette().setEtatProgressLivraison(0);
                System.out.println("fin course de la camionette");
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
     * Barre de progressions du service de livraison camionette
     */
    public void progressBarStartCamionette(int cycle, double vitesse) {
        ProgressBar progressCamionette = getProgressCamionette();
        Button btnEncaisserCourseCamionette = getBtnEncaisserCourseCamionette();
        // Réinitialise la barre de progression à 0
        progressCamionette.setProgress(0);
        timelineCamionette = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressCamionette.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Fin livraison camionette");
                    btnEncaisserCourseCamionette.setDisable(false);
                    // ajoute un courses a la livraison avec camionettes
                    this.majProgressCamionette();
                    // met à jour les gains en cours
                    this.majGainsEnCoursCamionette();
                    // maj des boutons
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
        System.out.println("nombre livraison en cours : " + nbLivraisonsEncours);
        System.out.println("nombre livraison camionette : " + nbLivraisonsCamionetteEnCours);
        long nouvNombre = nbLivraisonsEncours + nbLivraisonsCamionetteEnCours;
        jeu.getJoueur().getLivraisonCamionette().setNbCourses(nouvNombre);
        this.setNbCoursesCamionette();
        System.out.println("maj du nombre de livraisons effectuées en camionette : " + nouvNombre);
    }

    public void setNbCoursesCamionette() {
        this.labelNbCoursesCamionette.setText(jeu.getJoueur().getLivraisonCamionette().getNbCourses() + "");
    }
    public void majGainsEnCoursCamionette() {
        System.out.println("maj des gains courses des livraisons en camionette");
        // tant que non vendu, on cumule les gains en attente
        long nbLivraisonsEncours = jeu.getJoueur().getLivraisonCamionette().getNbCourses();
        BigDecimal tarifCourseCamionette = jeu.getJoueur().getLivraisonCamionette().getPrixCourse();
        System.out.println("****************** prix de la course : " + tarifCourseCamionette);

        // calcul des gains attente
        this.gainEnAttenteCamionette = tarifCourseCamionette.multiply(BigDecimal.valueOf(nbLivraisonsEncours));

        // maj le montant sur le bouton Encaisser
        this.setBtnEncaisserCourseCamionette();

    }

    /**
     * Affiche ou non le bouton recuperer marchandises
     * suivant si gainEnAttente > 0
     */
    public void setBtnEncaisserCourseCamionette() {
        int comparaison = this.gainEnAttenteCamionette.compareTo(BigDecimal.valueOf(0));
        if (comparaison > 0) {
            this.getBtnEncaisserCourseCamionette().setDisable(false);
        } else {
            this.getBtnEncaisserCourseCamionette().setDisable(true);
        }
        String formattedString = "Encaisser " + decimalFormat.format(gainEnAttenteCamionette) + monnaie;
        this.getBtnEncaisserCourseCamionette().setText(formattedString);
    }



    // livraison en petit camion
// Livraisons en camionette

    /**
     * Inscrit le prix d'achat d'un petit camion
     */
    public void setLabelTarifPetitCamion(){
        BigDecimal prixPetitCamion = jeu.getJoueur().getLivraisonPetitCamion().getPrixVehicule();
        String nomVehicule = jeu.getJoueur().getLivraisonPetitCamion().getNom();
        String formattedString = "Acheter " + nomVehicule + " : " + decimalFormat.format(prixPetitCamion) + monnaie;
        labelTarifCamionette.setText(formattedString);
    }

    /**
     * Recuperation du bouton pour encaisser les courses du petit camion
     *
     * @return
     */
    public Button getBtnEncaisserCoursePetitCamion() {
        return btnEncaisserCoursePetitCamion;
    }

    /**
     * Recuperation du bouton pour acheter le service de livraison en petit camion
     *
     * @return
     */
    public Button getBtnAchatLivraisonPetitCamion() {
        return btnAchatLivraisonPetitCamion;
    }

    /**
     * Debut barre progress livraisons
     */

    public ProgressBar getProgressPetitCamion() {
        return progressPetitCamion;
    }

    /**
     * Met à jour la barre de progression pour livraison petit camion
     *
     * @param cycle
     * @param vitesse
     */
    public void progressBarStartPetitCamionEnCours(int cycle, double vitesse) {
        ProgressBar progressPetitCamion = getProgressPetitCamion();
        Button btnEncaisserCoursePetitCamion = getBtnEncaisserCoursePetitCamion();
        // Réinitialise la barre de progression à 0
        progressPetitCamion.setProgress(this.jeu.getJoueur().getLivraisonPetitCamion().getEtatProgressLivraison());
        timelinePetitCamion = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressPetitCamion.progressProperty(), this.jeu.getJoueur().getLivraisonPetitCamion().getEtatProgressLivraison())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Course en petit camion terminée");
                    btnEncaisserCoursePetitCamion.setDisable(false);
                    // ajoute une course au service de livraison
                    this.majProgressPetitCamion();
                    // met à jour les gains en cours
                    this.majGainsEnCoursPetitCamion();
                    // maj des boutons
                }, new KeyValue(progressPetitCamion.progressProperty(), 1))
        );
        timelinePetitCamion.setOnFinished(event -> {
            if (cycle == 1) {
                // Lancer la deuxième exécution de la méthode progressBarStartTimeline
                jeu.getJoueur().getLivraisonPetitCamion().setEtatProgressLivraison(0);
                System.out.println("fin course du petit camion");
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
     * Barre de progressions du service de livraison camionette
     */
    public void progressBarStartPetitCamion(int cycle, double vitesse) {
        ProgressBar progressPetitCamion = getProgressPetitCamion();
        Button btnEncaisserCoursePetitCamion = getBtnEncaisserCoursePetitCamion();
        // Réinitialise la barre de progression à 0
        progressCamionette.setProgress(0);
        timelinePetitCamion = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressCamionette.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Course en petit camion terminée");
                    btnEncaisserCoursePetitCamion.setDisable(false);
                    // ajoute une course au service de livraison
                    this.majProgressPetitCamion();
                    // met à jour les gains en cours
                    this.majGainsEnCoursPetitCamion();
                    // maj des boutons
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
        System.out.println("nombre livraison en cours : " + nbLivraisonsEncours);
        System.out.println("nombre livraison camionette : " + nbLivraisonsPetitCamionEnCours);
        long nouvNombre = nbLivraisonsEncours + nbLivraisonsPetitCamionEnCours;
        jeu.getJoueur().getLivraisonPetitCamion().setNbCourses(nouvNombre);
        this.setNbCoursesPetitCamion();
        System.out.println("maj du nombre de livraisons effectuées en petit Camion : " + nouvNombre);
    }

    public void setNbCoursesPetitCamion() {
        this.labelNbCoursesPetitCamion.setText(jeu.getJoueur().getLivraisonPetitCamion().getNbCourses() + "");
    }
    public void majGainsEnCoursPetitCamion() {
        System.out.println("maj des gains courses des livraisons en petit camion");
        // tant que non vendu, on cumule les gains en attente
        long nbLivraisonsEncours = jeu.getJoueur().getLivraisonPetitCamion().getNbCourses();
        BigDecimal tarifCoursepetitCamion = jeu.getJoueur().getLivraisonPetitCamion().getPrixCourse();
        System.out.println("prix de la course : " + tarifCoursepetitCamion);

        // calcul des gains attente
        this.gainEnAttentePetitCamion = tarifCoursepetitCamion.multiply(BigDecimal.valueOf(nbLivraisonsEncours));

        // maj le montant sur le bouton Encaisser
        this.setBtnEncaisserCoursePetitCamion();

    }

    /**
     * Affiche ou non le bouton recuperer marchandises
     * suivant si gainEnAttente > 0
     */
    public void setBtnEncaisserCoursePetitCamion() {
        int comparaison = this.gainEnAttentePetitCamion.compareTo(BigDecimal.valueOf(0));
        if (comparaison > 0) {
            this.getBtnEncaisserCoursePetitCamion().setDisable(false);
        } else {
            this.getBtnEncaisserCoursePetitCamion().setDisable(true);
        }
        String formattedString = "Encaisser " + decimalFormat.format(gainEnAttentePetitCamion) + monnaie;
        this.getBtnEncaisserCoursePetitCamion().setText(formattedString);
    }

    // Livraison en poids lourd






    /**
     * start les 2 barres de progression : progress oeuf et progress jour
     * + les barres des distributeurs s'ils sont actifs
     */
    public void startProgressBars() {
        double vitesse = jeu.getParametres().getVitessePonteOeuf() - (jeu.getParametres().getVitessePonteOeuf() * jeu.getJoueur().getFerme().getEtatProgressOeuf());
        this.progressBarStartTimelineEncours(1, vitesse);

        if (jeu.getCalendrier().getHeureActuelle() != 0) {
            // recuperation de l'etat de la barre de progression pour la journee
            double vitesseJour = jeu.getCalendrier().getDureeJour() - (jeu.getCalendrier().getDureeJour() * jeu.getCalendrier().getProgressJour());
            progressBarStartTimelineJourneeEnCours(1, vitesseJour);
        }
        // demarrage des distributeurs
        demarrageDistributeurs();
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
    public void majFerme() {
        long nbOeufsAAjouter = this.jeu.getJoueur().getFerme().getNbOeufs() + this.jeu.getJoueur().getFerme().getNbPoules();
        this.jeu.getJoueur().getFerme().setNbOeufs(nbOeufsAAjouter);
        System.out.println("ajout de " + nbOeufsAAjouter + " oeuf(s)");
    }

    /**
     * Fermeture des barres de progression : enregistrement de l'état + stop des barres de progress
     * Sauvegarde date deco
     */
    public void fermetureProgress() {
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

        // on stoppe les barres de progression;
        this.progressBarStop(timelineOeufs);
        this.progressBarStop(timelineJour);
        this.progressBarStop(timelineBC);
        this.progressBarStop(timelineBF);
        this.progressBarStop(timelineCo);
        this.progressBarStop(timelineSa);
        this.progressBarStop(timelineScooter);
        this.progressBarStop(timelineCamionette);
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
}
