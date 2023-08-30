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
            labelTarifPoidsLourd, labelNbPoidsLourd, labelNbCoursesPoidsLourd,
            labelTarifAvion, labelNbAvion, labelNbCoursesAvion;
    @FXML
    private Button retourMenu, btnAchatLivraisonScooter, btnAchatScooter, btnEncaisserCourseScooter,
            onBtnEncaisserCourse2, onAchatLivraisonCamionette, btnAchatCamionette, btnEncaisserCourseCamionette, btnAchatLivraisonCamionette,
            onBtnEncaisserCourse3, onAchatLivraisonPetitCamion, btnAchatPetitCamion, btnEncaisserCoursePetitCamion, btnAchatLivraisonPetitCamion,
            onBtnEncaisserCourse4, onAchatLivraisonPoidsLourd, btnAchatPoidsLourd, btnEncaisserCoursePoidsLourd, btnAchatLivraisonPoidsLourd,
            onBtnEncaisserCourse5, onAchatLivraisonAvion, btnAchatAvion, btnEncaisserCourseAvion, btnAchatLivraisonAvion;
    @FXML
    private Pane paneScooter, paneScooterD, paneCamionette, paneCamionetteD, panePetitCamion, panePetitCamionD, panePoidsLourd, panePoidsLourdD,
            paneAvion, paneAvionD, paneProgress;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Jeu jeu;
    @FXML
    private ProgressBar progressOeufs, progressBC, progressBF, progressSa, progressCo,
            progressScooter, progressCamionette, progressPetitCamion, progressPoidsLourd, progressAvion;
    private Timeline timelineOeufs, timelineJour, timelineBC, timelineBF, timelineSa, timelineCo,
            timelineScooter, timelineCamionette, timelinePetitCamion, timelinePoidsLourd, timelineAvion;
    private ConnectionBdd connectionBdd = new ConnectionBdd();
    private BigDecimal gainEnAttenteScooter = new BigDecimal(0);
    private BigDecimal gainEnAttenteCamionette = new BigDecimal(0);
    private BigDecimal gainEnAttentePetitCamion = new BigDecimal(0);
    private BigDecimal gainEnAttentePoidsLourd = new BigDecimal(0);
    private BigDecimal gainEnAttenteAvion = new BigDecimal(0);

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

        demarrageProgress();

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
        // sauvegarde bdd
        sauveBdd();
    }

    public void retourGestion(ActionEvent event) {
        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();
        // sauvegarde bdd
        sauveBdd();

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
        if (!this.jeu.getJoueur().getLivraisonScooter().isMaxiNbVehicules()) {
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
        } else {
            System.out.println("Vous avez trop de scooters");
        }

    }


    /**
     * Ajout d'une camionnette
     */
    public void acheterCamionette() {
        if (!this.jeu.getJoueur().getLivraisonCamionette().isMaxiNbVehicules()) {
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
        } else {
            System.out.println("Vous avez trop de camionettes");
        }
    }


    /**
     * Ajout d'un petit camion
     */
    public void acheterPetitCamion() {
        if (!this.jeu.getJoueur().getLivraisonPetitCamion().isMaxiNbVehicules()) {
            BigDecimal montantAchat = this.jeu.getJoueur().getLivraisonPetitCamion().getPrixVehicule();
            if (jeu.getJoueur().acheter(montantAchat)) {
                this.jeu.getJoueur().getLivraisonPetitCamion().ajoutServiceDeLivraison();
                System.out.println("Achat d'un petit camion : " + jeu.getJoueur().getLivraisonPetitCamion().getNbVehicules());
                // debloque le scooter si besoin
                if (jeu.getJoueur().getLivraisonPetitCamion().getNbVehicules() == 1) {
                    jeu.getJoueur().setLivraison3Active(1);
//                debloquerDistributeur(paneBF, btnDebloquerBF, labelTitreDistributeurBF);
                    // demarre la barre de progression du distributeur
                    progressBarStartPetitCamion(0, jeu.getJoueur().getLivraisonPetitCamion().getVitesseLivraion());
                }
                // mise a jour des valeurs
                miseEnPlace();
            } else {
                System.out.println("Vous n'avez pas assez d'argent pour acheter un service de livraison en petit camion");
            }
        } else {
            System.out.println("Vous avez trop de petits camions");
        }
    }

    /**
     * Ajout d'un camion poids lourd
     */
    public void acheterPoidsLourd() {
        if (!this.jeu.getJoueur().getLivraisonPoidsLourd().isMaxiNbVehicules()) {
            BigDecimal montantAchat = this.jeu.getJoueur().getLivraisonPoidsLourd().getPrixVehicule();
            if (jeu.getJoueur().acheter(montantAchat)) {
                this.jeu.getJoueur().getLivraisonPoidsLourd().ajoutServiceDeLivraison();
                System.out.println("Achat d'une camionette : " + jeu.getJoueur().getLivraisonPoidsLourd().getNbVehicules());
                // debloque le poids lourd si besoin
                if (jeu.getJoueur().getLivraisonPoidsLourd().getNbVehicules() == 1) {
                    jeu.getJoueur().setLivraison4Active(1);
//                debloquerDistributeur(paneBF, btnDebloquerBF, labelTitreDistributeurBF);
                    // demarre la barre de progression du distributeur
                    progressBarStartPoidsLourd(0, jeu.getJoueur().getLivraisonPoidsLourd().getVitesseLivraion());
                }
                // mise a jour des valeurs
                miseEnPlace();
            } else {
                System.out.println("Vous n'avez pas assez d'argent pour acheter un service de livraison en poids lourd");
            }
        } else {
            System.out.println("Vous avez trop de poids lourd");
        }
    }

    /**
     * Ajout d'un avion
     */
    public void acheterAvion() {
        if (!this.jeu.getJoueur().getLivraisonAvion().isMaxiNbVehicules()) {
            BigDecimal montantAchat = this.jeu.getJoueur().getLivraisonAvion().getPrixVehicule();
            if (jeu.getJoueur().acheter(montantAchat)) {
                this.jeu.getJoueur().getLivraisonAvion().ajoutServiceDeLivraison();
                System.out.println("Achat d'une avion : " + jeu.getJoueur().getLivraisonAvion().getNbVehicules());
                // debloque la livraions en avion si besoin
                if (jeu.getJoueur().getLivraisonAvion().getNbVehicules() == 1) {
                    jeu.getJoueur().setLivraison5Active(1);
                    // demarre la barre de progression du service en avion
                    progressBarStartAvion(0, jeu.getJoueur().getLivraisonAvion().getVitesseLivraion());
                }
                // mise a jour des valeurs
                miseEnPlace();
            } else {
                System.out.println("Vous n'avez pas assez d'argent pour acheter un service de livraison en avion");
            }
        } else {
            System.out.println("Vous avez trop d'avions");
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
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getLivraisonScooter().getPrixVehicule())) {
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
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getLivraisonCamionette().getPrixVehicule())) {
                paneCamionetteD.setOpacity(1);
                paneCamionetteD.setDisable(false);
                getBtnAchatLivraisonCamionette().setDisable(false);
            } else {
                getBtnAchatLivraisonCamionette().setDisable(true);
            }
            System.out.println("non actif");
        }
        // boutons petit camion
        if (isActif(jeu.getJoueur().getLivraison3Active())) {
            getBtnEncaisserCoursePetitCamion().setVisible(true);
            getBtnAchatLivraisonPetitCamion().setVisible(false);
            System.out.println("Actif");
            setBtnEncaisserCoursePetitCamion();
        } else {
            getBtnEncaisserCoursePetitCamion().setVisible(false);
            getBtnAchatLivraisonPetitCamion().setVisible(true);
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getLivraisonPetitCamion().getPrixVehicule())) {
                panePetitCamionD.setOpacity(1);
                panePetitCamionD.setDisable(false);
                getBtnAchatLivraisonPetitCamion().setDisable(false);
            } else {
                getBtnAchatLivraisonPetitCamion().setDisable(true);
            }
            System.out.println("non actif");
        }
        // boutons poids lourd
        if (isActif(jeu.getJoueur().getLivraison4Active())) {
            getBtnEncaisserCoursePoidsLourd().setVisible(true);
            getBtnAchatLivraisonPoidsLourd().setVisible(false);
            System.out.println("Actif");
            setBtnEncaisserCoursePoidsLourd();
        } else {
            getBtnEncaisserCoursePoidsLourd().setVisible(false);
            getBtnAchatLivraisonPoidsLourd().setVisible(true);
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getLivraisonPoidsLourd().getPrixVehicule())) {
                panePoidsLourdD.setOpacity(1);
                panePoidsLourdD.setDisable(false);
                getBtnAchatLivraisonPoidsLourd().setDisable(false);
            } else {
                getBtnAchatLivraisonPoidsLourd().setDisable(true);
            }
            System.out.println("non actif");
        }
        // boutons avion
        if (isActif(jeu.getJoueur().getLivraison5Active())) {
            getBtnEncaisserCourseAvion().setVisible(true);
            getBtnAchatLivraisonAvion().setVisible(false);
            System.out.println("Actif");
            setBtnEncaisserCourseAvion();
        } else {
            getBtnEncaisserCourseAvion().setVisible(false);
            getBtnAchatLivraisonAvion().setVisible(true);
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getLivraisonAvion().getPrixVehicule())) {
                paneAvionD.setOpacity(1);
                paneAvionD.setDisable(false);
                getBtnAchatLivraisonAvion().setDisable(false);
            } else {
                getBtnAchatLivraisonAvion().setDisable(true);
            }
            System.out.println("non actif");
        }
        testBtnAchats();
    }

    /**
     * bloque le pan
     */
    public void blocageLivraison() {

    }

    /**
     * Active / desactive le bouton d'achat de vehicule de livraison
     */
    public void testBtnAchats() {
        // scooter
        BigDecimal prixScooter = jeu.getJoueur().getLivraisonScooter().getPrixVehicule();
        if (jeu.getJoueur().isArgent(prixScooter) && !this.jeu.getJoueur().getLivraisonScooter().isMaxiNbVehicules()) {
            btnAchatScooter.setDisable(false);
        } else {
            btnAchatScooter.setDisable(true);
        }

        // camionette
        BigDecimal camionette = jeu.getJoueur().getLivraisonCamionette().getPrixVehicule();
        if (jeu.getJoueur().isArgent(camionette) && !this.jeu.getJoueur().getLivraisonCamionette().isMaxiNbVehicules()) {
            btnAchatCamionette.setDisable(false);
        } else {
            btnAchatCamionette.setDisable(true);
        }

        // petit camion
        BigDecimal petitCamion = jeu.getJoueur().getLivraisonPetitCamion().getPrixVehicule();
        if (jeu.getJoueur().isArgent(petitCamion) && !this.jeu.getJoueur().getLivraisonPetitCamion().isMaxiNbVehicules()) {
            btnAchatPetitCamion.setDisable(false);
        } else {
            btnAchatPetitCamion.setDisable(true);
        }

        // poids lourd
        BigDecimal poidsLourd = jeu.getJoueur().getLivraisonPoidsLourd().getPrixVehicule();
        if (jeu.getJoueur().isArgent(poidsLourd) && !this.jeu.getJoueur().getLivraisonPoidsLourd().isMaxiNbVehicules()) {
            btnAchatPoidsLourd.setDisable(false);
        } else {
            btnAchatPoidsLourd.setDisable(true);
        }

        // Avion
        BigDecimal avion = jeu.getJoueur().getLivraisonAvion().getPrixVehicule();
        if (jeu.getJoueur().isArgent(avion) && !this.jeu.getJoueur().getLivraisonAvion().isMaxiNbVehicules()) {
            btnAchatAvion.setDisable(false);
        } else {
            btnAchatAvion.setDisable(true);
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
        paneScooter.setDisable(false);
        paneScooterD.setOpacity(1);

        // demarrage barre de progression
        progressBarStartScooter(0, jeu.getJoueur().getLivraisonScooter().getVitesseLivraion());
    }

    /**
     * Bouton qui permet d'encaisser l'argent du service de livraison
     */
    public void onBtnEncaisserCourse1() {
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
        paneCamionette.setDisable(false);
        paneCamionetteD.setOpacity(1);
        System.out.println("opacite pane");

        // demarrage barre de progression
        progressBarStartCamionette(0, jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion());
    }

    /**
     * Bouton qui permet d'encaisser l'argent du service de livraison en camionettes
     */
    public void onBtnEncaisserCourse2() {
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
     * Gere le bouton d'achat du premier service de livraison petits camions
     */
    public void onAchatLivraisonPetitCamion() {
        // activation de la livraison
        jeu.getJoueur().setLivraison3Active(1);

        // ajout du scooter
        jeu.getJoueur().getLivraisonPetitCamion().setNbVehicules(1);
        // dépense du montant
        jeu.getJoueur().depenser(jeu.getJoueur().getLivraisonPetitCamion().getPrixVehicule());

        // maj des labels et boutons
        miseEnPlace();

        // opacite du pane
        panePetitCamion.setOpacity(1);
        panePetitCamion.setDisable(false);
        paneCamionetteD.setOpacity(1);

        // demarrage barre de progression
        progressBarStartPetitCamion(0, jeu.getJoueur().getLivraisonPetitCamion().getVitesseLivraion());
    }

    /**
     * Bouton qui permet d'encaisser l'argent du service de livraison des petits camions
     */
    public void onBtnEncaisserCourse3() {
        long nbCoursesPetitCamion = jeu.getJoueur().getLivraisonPetitCamion().getNbCourses();

        jeu.getJoueur().getLivraisonPetitCamion().setNbCourses(0); // raz le nombre de marchandises
        jeu.getJoueur().setArgent(this.gainEnAttentePetitCamion.add(jeu.getJoueur().getArgent())); // met a jour les nouveaux gains

        String formattedGain = decimalFormat.format(this.gainEnAttentePetitCamion) + monnaie;
        // raz des gains en attente
        this.gainEnAttentePetitCamion = BigDecimal.valueOf(0.00);

        System.out.println("Vous venez de récupérer le prix de " + nbCoursesPetitCamion + " courses en petit camion, vous avez gagné " + formattedGain + ".");

        getBtnEncaisserCoursePetitCamion().setDisable(true);
        this.miseEnPlace();
    }

    /**
     * Gere le bouton d'achat du premier service de livraison en poids lourd
     */
    public void onAchatLivraisonPoidsLourd() {
        // activation de la livraison
        jeu.getJoueur().setLivraison4Active(1);

        // ajout du scooter
        jeu.getJoueur().getLivraisonPoidsLourd().setNbVehicules(1);
        // dépense du montant
        jeu.getJoueur().depenser(jeu.getJoueur().getLivraisonPoidsLourd().getPrixVehicule());

        // maj des labels et boutons
        miseEnPlace();

        // opacite du pane
        panePoidsLourd.setOpacity(1);
        panePoidsLourd.setDisable(false);
        panePoidsLourdD.setOpacity(1);

        // demarrage barre de progression
        progressBarStartPoidsLourd(0, jeu.getJoueur().getLivraisonPoidsLourd().getVitesseLivraion());
    }

    /**
     * Bouton qui permet d'encaisser l'argent du service de livraison en poids lourd
     */
    public void onBtnEncaisserCourse4() {
        long nbCoursesPoidsLourd = jeu.getJoueur().getLivraisonPoidsLourd().getNbCourses();

        jeu.getJoueur().getLivraisonPoidsLourd().setNbCourses(0); // raz le nombre de marchandises
        jeu.getJoueur().setArgent(this.gainEnAttentePoidsLourd.add(jeu.getJoueur().getArgent())); // met a jour les nouveaux gains

        String formattedGain = decimalFormat.format(this.gainEnAttentePoidsLourd) + monnaie;
        // raz des gains en attente
        this.gainEnAttentePoidsLourd = BigDecimal.valueOf(0.00);

        System.out.println("Vous venez de récupérer le prix de " + nbCoursesPoidsLourd + " courses en poids lourd, vous avez gagné " + formattedGain + ".");

        getBtnEncaisserCoursePoidsLourd().setDisable(true);
        this.miseEnPlace();
    }

    /**
     * Gere le bouton d'achat du service de livraison en avion
     */
    public void onAchatLivraisonAvion() {
        // activation de la livraison
        jeu.getJoueur().setLivraison5Active(1);

        // ajout du scooter
        jeu.getJoueur().getLivraisonAvion().setNbVehicules(1);
        // dépense du montant
        jeu.getJoueur().depenser(jeu.getJoueur().getLivraisonAvion().getPrixVehicule());

        // maj des labels et boutons
        miseEnPlace();

        // opacite du pane
        paneAvion.setOpacity(1);
        paneAvion.setDisable(false);
        paneAvionD.setOpacity(1);

        // demarrage barre de progression
        progressBarStartAvion(0, jeu.getJoueur().getLivraisonAvion().getVitesseLivraion());
    }

    /**
     * Bouton qui permet d'encaisser l'argent du service de livraison
     */
    public void onBtnEncaisserCourse5() {
        long nbCoursesAvion = jeu.getJoueur().getLivraisonAvion().getNbCourses();

        jeu.getJoueur().getLivraisonAvion().setNbCourses(0); // raz le nombre de marchandises
        jeu.getJoueur().setArgent(this.gainEnAttenteAvion.add(jeu.getJoueur().getArgent())); // met a jour les nouveaux gains

        String formattedGain = decimalFormat.format(this.gainEnAttenteAvion) + monnaie;
        // raz des gains en attente
        this.gainEnAttenteAvion = BigDecimal.valueOf(0.00);

        System.out.println("Vous venez de récupérer le prix de " + nbCoursesAvion + " courses en avion, vous avez gagné " + formattedGain + ".");

        getBtnEncaisserCourseAvion().setDisable(true);
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
        if (isActif(jeu.getJoueur().getLivraison2Active())) {
            System.out.println("Demarrage livraison en camionette");
            // enable pane de la livraison
            this.debloquerLivraison(paneCamionette, paneCamionetteD);
            double vitesseCamionette = jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion() - (jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion() * jeu.getJoueur().getLivraisonCamionette().getEtatProgressLivraison());
            progressBarStartCamionetteEnCours(1, vitesseCamionette);
        }
        if (isActif(jeu.getJoueur().getLivraison3Active())) {
            System.out.println("Demarrage livraison en petit camion");
            // enable pane de la livraison
            this.debloquerLivraison(panePetitCamion, panePetitCamionD);
            // recuperation de l'etat de la barre de progression pour la livraison en scooter
            double vitessePetitCamion = jeu.getJoueur().getLivraisonPetitCamion().getVitesseLivraion() - (jeu.getJoueur().getLivraisonPetitCamion().getVitesseLivraion() * jeu.getJoueur().getLivraisonPetitCamion().getEtatProgressLivraison());
            progressBarStartPetitCamionEnCours(1, vitessePetitCamion);
        }
        if (isActif(jeu.getJoueur().getLivraison4Active())) {
            System.out.println("Demarrage livraison en poids lourd");
            // enable pane de la livraison
            this.debloquerLivraison(panePoidsLourd, panePoidsLourdD);
            // recuperation de l'etat de la barre de progression pour la livraison en poids lourd
            double vitessePoidsLourd = jeu.getJoueur().getLivraisonPoidsLourd().getVitesseLivraion() - (jeu.getJoueur().getLivraisonPoidsLourd().getVitesseLivraion() * jeu.getJoueur().getLivraisonPoidsLourd().getEtatProgressLivraison());
            progressBarStartPoidsLourdEnCours(1, vitessePoidsLourd);
        }
        if (isActif(jeu.getJoueur().getLivraison5Active())) {
            System.out.println("Demarrage livraison en avion");
            // enable pane de la livraison
            this.debloquerLivraison(paneAvion, paneAvionD);
            // recuperation de l'etat de la barre de progression pour la livraison en poids lourd
            double vitesseAvion = jeu.getJoueur().getLivraisonAvion().getVitesseLivraion() - (jeu.getJoueur().getLivraisonAvion().getVitesseLivraion() * jeu.getJoueur().getLivraisonAvion().getEtatProgressLivraison());
            progressBarStartAvionEnCours(1, vitesseAvion);
        }
    }

    /**
     * Recup des courses effectuées pendant le switch de fenetre
     * et maj des gains encours
     */
    public void recupCourses() {
        // scooter
        long nbCoursesEnCours = jeu.getJoueur().getLivraisonScooter().getNbCourses();
        BigDecimal tarifCourse = jeu.getJoueur().getLivraisonScooter().getPrixCourse();
        if (nbCoursesEnCours > 0) {
            btnEncaisserCourseScooter.setDisable(false);
            majGainsEnCoursScooter();
        } else {
            btnEncaisserCourseScooter.setDisable(true);
        }
        // camionette
        long nbCoursesEnCours2 = jeu.getJoueur().getLivraisonCamionette().getNbCourses();
        tarifCourse = jeu.getJoueur().getLivraisonCamionette().getPrixCourse();
        if (nbCoursesEnCours2 > 0) {
            btnEncaisserCourseCamionette.setDisable(false);
            majGainsEnCoursCamionette();
        } else {
            btnEncaisserCourseCamionette.setDisable(true);
        }
        // petit camion
        long nbCoursesEnCours3 = jeu.getJoueur().getLivraisonPetitCamion().getNbCourses();
        tarifCourse = jeu.getJoueur().getLivraisonPetitCamion().getPrixCourse();
        if (nbCoursesEnCours3 > 0) {
            btnEncaisserCoursePetitCamion.setDisable(false);
            majGainsEnCoursPetitCamion();
        } else {
            btnEncaisserCoursePetitCamion.setDisable(true);
        }
        // poids lourd
        long nbCoursesEnCours4 = jeu.getJoueur().getLivraisonPoidsLourd().getNbCourses();
        tarifCourse = jeu.getJoueur().getLivraisonPoidsLourd().getPrixCourse();
        if (nbCoursesEnCours4 > 0) {
            btnEncaisserCoursePoidsLourd.setDisable(false);
            majGainsEnCoursPoidsLourd();
        } else {
            btnEncaisserCoursePoidsLourd.setDisable(true);
        }
        // Avion
        long nbCoursesEnCours5 = jeu.getJoueur().getLivraisonAvion().getNbCourses();
        tarifCourse = jeu.getJoueur().getLivraisonAvion().getPrixCourse();
        if (nbCoursesEnCours5 > 0) {
            btnEncaisserCourseAvion.setDisable(false);
            majGainsEnCoursAvion();
        } else {
            btnEncaisserCourseAvion.setDisable(true);
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
     * initialise le nombre de poids lourd en cours ainsi que le nombre de poids lourd  maximum
     */
    public void setNbPoidsLourd() {
        String formattedString = this.jeu.getJoueur().getLivraisonPoidsLourd().setNbVehicule();
        this.labelNbPoidsLourd.setText(formattedString);
    }

    /**
     * initialise le nombre d'avions en cours ainsi que le nombre d'avions maximum
     */
    public void setNbAvion() {
        String formattedString = this.jeu.getJoueur().getLivraisonAvion().setNbVehicule();
        this.labelNbAvion.setText(formattedString);
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
        labelsPoidsLourd();
        labelsAvion();
    }

    /**
     * Labels du scooter
     */
    public void labelsScooter() {
        setNbScooter();
        setNbCoursesScooter();
        setLabelTarifScooter();
    }

    /**
     * Labels du camionnette
     */
    public void labelsCamionette() {
        setNbCamionette();
        setNbCoursesCamionette();
        setLabelTarifCamionette();
    }

    /**
     * Labels petit camion
     */
    public void labelsPetitCamion() {
        setNbPetitCamion();
        setNbCoursesPetitCamion();
        setLabelTarifPetitCamion();
    }

    public void labelsPoidsLourd() {
        setNbPoidsLourd();
        setNbCoursesPoidsLourd();
        setLabelTarifPoidsLourd();
    }

    public void labelsAvion() {
        setNbAvion();
        setNbCoursesAvion();
        setLabelTarifAvion();
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
    public void setLabelTarifScooter() {
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
     * @param paneLivraison  => nom de la livraison a debloquer
     * @param paneLivraisonD => pane de droite de la livraison
     * @param =>             nom du bouton à rendre invisible correspondant au distributeur
     * @param =>             nom du label affichant le distributeur à rendre invisible
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
    public void setLabelTarifCamionette() {
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

    /**
     * Inscrit le prix d'achat d'un petit camion
     */
    public void setLabelTarifPetitCamion() {
        BigDecimal prixPetitCamion = jeu.getJoueur().getLivraisonPetitCamion().getPrixVehicule();
        String nomVehicule = jeu.getJoueur().getLivraisonPetitCamion().getNom();
        String formattedString = "Acheter " + nomVehicule + " : " + decimalFormat.format(prixPetitCamion) + monnaie;
        labelTarifPetitCamion.setText(formattedString);
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
        progressPetitCamion.setProgress(0);
        timelinePetitCamion = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressPetitCamion.progressProperty(), 0)),
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
        System.out.println("nombre livraison petit camion : " + nbLivraisonsPetitCamionEnCours);
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
        BigDecimal tarifCoursePetitCamion = jeu.getJoueur().getLivraisonPetitCamion().getPrixCourse();
        System.out.println("prix de la course : " + tarifCoursePetitCamion);

        // calcul des gains attente
        this.gainEnAttentePetitCamion = tarifCoursePetitCamion.multiply(BigDecimal.valueOf(nbLivraisonsEncours));

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
     * Inscrit le prix d'achat d'un poids lourd
     */
    public void setLabelTarifPoidsLourd() {
        BigDecimal prixPoidsLourd = jeu.getJoueur().getLivraisonPoidsLourd().getPrixVehicule();
        String nomVehicule = jeu.getJoueur().getLivraisonPoidsLourd().getNom();
        String formattedString = "Acheter " + nomVehicule + " : " + decimalFormat.format(prixPoidsLourd) + monnaie;
        labelTarifPoidsLourd.setText(formattedString);
    }

    /**
     * Recuperation du bouton pour encaisser les courses du poids lourd
     *
     * @return
     */
    public Button getBtnEncaisserCoursePoidsLourd() {
        return btnEncaisserCoursePoidsLourd;
    }

    /**
     * Recuperation du bouton pour acheter le service de livraison en poids lourd
     *
     * @return
     */
    public Button getBtnAchatLivraisonPoidsLourd() {
        return btnAchatLivraisonPoidsLourd;
    }

    /**
     * Debut barre progress livraisons
     */

    public ProgressBar getProgressPoidsLourd() {
        return progressPoidsLourd;
    }

    /**
     * Met à jour la barre de progression pour livraison poids lourd
     *
     * @param cycle
     * @param vitesse
     */
    public void progressBarStartPoidsLourdEnCours(int cycle, double vitesse) {
        ProgressBar progressPoidsLourd = getProgressPoidsLourd();
        Button btnEncaisserCoursePoidsLourd = getBtnEncaisserCoursePoidsLourd();
        // Réinitialise la barre de progression à 0
        progressPoidsLourd.setProgress(this.jeu.getJoueur().getLivraisonPoidsLourd().getEtatProgressLivraison());
        timelinePoidsLourd = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressPoidsLourd.progressProperty(), this.jeu.getJoueur().getLivraisonPoidsLourd().getEtatProgressLivraison())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Course en poids lourd terminée");
                    btnEncaisserCoursePoidsLourd.setDisable(false);
                    // ajoute une course au service de livraison
                    this.majProgressPoidsLourd();
                    // met à jour les gains en cours
                    this.majGainsEnCoursPoidsLourd();
                    // maj des boutons
                }, new KeyValue(progressPoidsLourd.progressProperty(), 1))
        );
        timelinePoidsLourd.setOnFinished(event -> {
            if (cycle == 1) {
                // Lancer la deuxième exécution de la méthode progressBarStartTimeline
                jeu.getJoueur().getLivraisonPoidsLourd().setEtatProgressLivraison(0);
                System.out.println("fin premiere barre");
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
     * Barre de progressions Livraison en poids lourd
     */
    public void progressBarStartPoidsLourd(int cycle, double vitesse) {
        ProgressBar progressPoidsLourd = getProgressPoidsLourd();
        Button btnEncaisserCoursePoidsLourd = getBtnEncaisserCoursePoidsLourd();
        // Réinitialise la barre de progression à 0
        progressPoidsLourd.setProgress(0);
        timelinePoidsLourd = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressPoidsLourd.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Livraison en poids lourd terminée");
                    btnEncaisserCoursePoidsLourd.setDisable(false);
                    // ajoute un nombre de courses au service de livraison en poids lourd
                    this.majProgressPoidsLourd();
                    // met à jour les gains en cours
                    this.majGainsEnCoursPoidsLourd();
                    // maj des boutons
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
        this.setNbCoursesPoidsLourd();
        System.out.println("maj du nombre de livraisons effectuées en poids lourd : " + nouvNombre);
    }

    public void setNbCoursesPoidsLourd() {
        this.labelNbCoursesPoidsLourd.setText(jeu.getJoueur().getLivraisonPoidsLourd().getNbCourses() + "");
    }

    public void majGainsEnCoursPoidsLourd() {
        System.out.println("maj des gains courses des livraisons en poids lourd");
        // tant que non vendu, on cumule les gains en attente
        long nbLivraisonsEncours = jeu.getJoueur().getLivraisonPoidsLourd().getNbCourses();
        BigDecimal tarifCoursePoidsLourd = jeu.getJoueur().getLivraisonPoidsLourd().getPrixCourse();
        System.out.println("prix de la course : " + tarifCoursePoidsLourd);

        // calcul des gains attente
        this.gainEnAttentePoidsLourd = tarifCoursePoidsLourd.multiply(BigDecimal.valueOf(nbLivraisonsEncours));

        // maj le montant sur le bouton Encaisser
        this.setBtnEncaisserCoursePoidsLourd();

    }

    /**
     * Affiche ou non le bouton recuperer marchandises
     * suivant si gainEnAttente > 0
     */
    public void setBtnEncaisserCoursePoidsLourd() {
        int comparaison = this.gainEnAttentePoidsLourd.compareTo(BigDecimal.valueOf(0));
        if (comparaison > 0) {
            this.getBtnEncaisserCoursePoidsLourd().setDisable(false);
        } else {
            this.getBtnEncaisserCoursePoidsLourd().setDisable(true);
        }
        String formattedString = "Encaisser " + decimalFormat.format(gainEnAttentePoidsLourd) + monnaie;
        this.getBtnEncaisserCoursePoidsLourd().setText(formattedString);
    }


    // fin livraison poids lourd

    // Debut Livraison en avion

    /**
     * Inscrit le prix d'achat d'un avion
     */
    public void setLabelTarifAvion() {
        BigDecimal prixAvion = jeu.getJoueur().getLivraisonAvion().getPrixVehicule();
        String nomVehicule = jeu.getJoueur().getLivraisonAvion().getNom();
        String formattedString = "Acheter " + nomVehicule + " : " + decimalFormat.format(prixAvion) + monnaie;
        labelTarifAvion.setText(formattedString);
    }

    /**
     * Recuperation du bouton pour encaisser les courses en avion
     *
     * @return
     */
    public Button getBtnEncaisserCourseAvion() {
        return btnEncaisserCourseAvion;
    }

    /**
     * Recuperation du bouton pour acheter le service de livraison en avion
     *
     * @return
     */
    public Button getBtnAchatLivraisonAvion() {
        return btnAchatLivraisonAvion;
    }

    /**
     * Debut barre progress livraisons en avion
     */

    public ProgressBar getProgressAvion() {
        return progressAvion;
    }

    /**
     * Met à jour la barre de progression pour livraison en avion
     *
     * @param cycle
     * @param vitesse
     */
    public void progressBarStartAvionEnCours(int cycle, double vitesse) {
        ProgressBar progressAvion = getProgressAvion();
        Button btnEncaisserCourseAvion = getBtnEncaisserCourseAvion();
        // Réinitialise la barre de progression à 0
        progressAvion.setProgress(this.jeu.getJoueur().getLivraisonAvion().getEtatProgressLivraison());
        timelineAvion = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressAvion.progressProperty(), this.jeu.getJoueur().getLivraisonAvion().getEtatProgressLivraison())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Course en avion terminée");
                    btnEncaisserCourseAvion.setDisable(false);
                    // ajoute une course au service de livraison
                    this.majProgressAvion();
                    // met à jour les gains en cours
                    this.majGainsEnCoursAvion();
                    // maj des boutons
                }, new KeyValue(progressAvion.progressProperty(), 1))
        );
        timelineAvion.setOnFinished(event -> {
            if (cycle == 1) {
                // Lancer la deuxième exécution de la méthode progressBarStartTimeline
                jeu.getJoueur().getLivraisonAvion().setEtatProgressLivraison(0);
                System.out.println("fin premiere barre");
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartAvion(cycle - 1, this.jeu.getJoueur().getLivraisonAvion().getVitesseLivraion());
            }
        });
        if (cycle == 0) {
            timelineAvion.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineAvion.setCycleCount(cycle);
        }
        timelineAvion.play();
    }

    /**
     * Barre de progressions Livraison en poids lourd
     */
    public void progressBarStartAvion(int cycle, double vitesse) {
        ProgressBar progressAvion = getProgressAvion();
        Button btnEncaisserCourseAvion = getBtnEncaisserCourseAvion();
        // Réinitialise la barre de progression à 0
        progressAvion.setProgress(0);
        timelineAvion = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressAvion.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Livraison en avion terminée");
                    btnEncaisserCourseAvion.setDisable(false);
                    // ajoute un nombre de courses au service de livraison en avion
                    this.majProgressAvion();
                    // met à jour les gains en cours
                    this.majGainsEnCoursAvion();
                    // maj des boutons
                }, new KeyValue(progressAvion.progressProperty(), 1))
        );

        if (cycle == 0) {
            timelineAvion.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineAvion.setCycleCount(cycle);
        }
        timelineAvion.play();
    }

    /**
     * Met a jour le chiffre du nombre de livraisons effectuées en avion
     */
    public void majProgressAvion() {
        long nbLivraisonsEncours = jeu.getJoueur().getLivraisonAvion().getNbCourses();
        int nbLivraisonsAvionEnCours = jeu.getJoueur().getLivraisonAvion().getNbVehicules();
        long nouvNombre = nbLivraisonsEncours + nbLivraisonsAvionEnCours;
        jeu.getJoueur().getLivraisonAvion().setNbCourses(nouvNombre);
        this.setNbCoursesAvion();
        System.out.println("maj du nombre de livraisons effectuées en avion : " + nouvNombre);
    }

    public void setNbCoursesAvion() {
        this.labelNbCoursesAvion.setText(jeu.getJoueur().getLivraisonAvion().getNbCourses() + "");
    }

    public void majGainsEnCoursAvion() {
        System.out.println("maj des gains courses des livraisons en avion");
        // tant que non vendu, on cumule les gains en attente
        long nbLivraisonsEncours = jeu.getJoueur().getLivraisonAvion().getNbCourses();
        BigDecimal tarifCourseAvion = jeu.getJoueur().getLivraisonAvion().getPrixCourse();
        System.out.println("prix de la course : " + tarifCourseAvion);

        // calcul des gains attente
        this.gainEnAttenteAvion = tarifCourseAvion.multiply(BigDecimal.valueOf(nbLivraisonsEncours));

        // maj le montant sur le bouton Encaisser
        this.setBtnEncaisserCourseAvion();

    }

    /**
     * Affiche ou non le bouton recuperer courses
     * suivant si gainEnAttente > 0
     */
    public void setBtnEncaisserCourseAvion() {
        int comparaison = this.gainEnAttenteAvion.compareTo(BigDecimal.valueOf(0));
        if (comparaison > 0) {
            this.getBtnEncaisserCourseAvion().setDisable(false);
        } else {
            this.getBtnEncaisserCourseAvion().setDisable(true);
        }
        String formattedString = "Encaisser " + decimalFormat.format(gainEnAttenteAvion) + monnaie;
        this.getBtnEncaisserCourseAvion().setText(formattedString);
    }


    // fin livraison en avion

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
    }

    private void progressBarStop(Timeline timeline) {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
            System.out.println("Arret de la barre de progression");
        }
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
        this.progressBarStop(timelineScooter);
        this.progressBarStop(timelineCamionette);
        this.progressBarStop(timelinePetitCamion);
        this.progressBarStop(timelinePoidsLourd);
        this.progressBarStop(timelineAvion);
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
}
