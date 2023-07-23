package com.example.mapetiteentreprise.controllersFx;

import com.example.mapetiteentreprise.Main;
import com.example.mapetiteentreprise.bdd.ConnectionBdd;
import com.example.mapetiteentreprise.bdd.SauvegardeService;
import com.example.mapetiteentreprise.jeu.Jeu;
import com.example.mapetiteentreprise.actions.Outils;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class FermeController {
    private final String monnaie = " €";
    // pattern des nombre décimaux
//    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private final DecimalFormat decimalFormat = Outils.getDecimalFormatWithSpaceSeparator();
    private BigDecimal gainEnAttente = new BigDecimal(0);
    private BigDecimal taxeEnAttente = new BigDecimal(0);
    @FXML
    private Label montantBanque, labelConsole, nbPoules, nbOeufs, labelPseudo, labelPoule, gainARecuperer;
//    labelTaxe, montantTaxe;

    @FXML
    private ProgressBar progressOeufs;

    @FXML
    private Button btnVendre, btnPPoule, btnPPoulePDix, btnPPouleMax;
    @FXML
    private ImageView imgAnimation;
    private String messageConsole;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Jeu jeu;
    private Timeline timeline;
    double etatProgress; // permet de gerer l'etat d'avancement de la barre de progression
    private Boolean attenteFinBarre = true;

    // animation de la poule
    String spriteSheetPath = getClass().getResource("/com/example/mapetiteentreprise/images/sprite_poule.png").toExternalForm();
    Image spriteSheet = new Image(spriteSheetPath);
    PixelReader pixelReader = spriteSheet.getPixelReader();
    private Timeline timelinePoule;
    // Réinitialise les valeurs X et Y de depart
    int spriteX = 0;
    int spriteY = 0;
    int ecartSprite = 400;
    int nbSpriteX = 4;
    int nbSrpiteY = 3;

    public ImageView getImgAnimation() {
        return imgAnimation;
    }

    public void setImgAnimation(ImageView imgAnimation) {
        this.imgAnimation = imgAnimation;
    }

    /**
     * Execute la fenetre ferme.xml
     *
     * @param jeu
     */
    public void demarrer(Jeu jeu) {
        // Recuperation du jeu
        this.jeu = jeu;
        System.out.println("Création de la ferme");

        // ajustement de la barre de progression et des oeufs pondus
        // on compare l'heure et le jour actuel avec la date deco
        // on met a jour les oeufs pondus
        this.reajustementSwitchFenetre();

        this.miseEnPlaceValeurs();

        // plus le chiffre est gros plus la vitesse est lente
        // correspond à un nombre de secondes d'un passage de 0 à 100
        double vitesse = jeu.getParametres().getVitessePonteOeuf() - (jeu.getParametres().getVitessePonteOeuf() * jeu.getJoueur().getFerme().getEtatProgressOeuf());
        this.progressBarStartTimelineEncours(1, vitesse);
        this.executerAnimation();
    }

    public void nouveau(Jeu jeu) {
        // Recuperation du jeu
        this.jeu = jeu;
        System.out.println("Nouveau jeu, désactivation de la ferme");

        this.miseEnPlaceValeurs();

        // activation de la ferme
        this.jeu.getJoueur().setFermeActive(1);

        System.out.println("Etat de la sauvegarde " + jeu);
        // plus le chiffre est gros plus la vitesse est lente
        // correspond à un nombre de secondes d'un passage de 0 à 100
        progressBarStartTimeline(0, jeu.getParametres().getVitessePonteOeuf());
        this.executerAnimation();

    }

    /**
     * Action a executé lors de la fermeture de la fentre avec la croix : sauvegarde
     * @param event
     */
    public void onWindowClose(WindowEvent event) {
        // Sauvegarde de la base de donnees
        System.out.println("fermeture fenetre : Sauvegarde");
        try {
            sauvegardejeu();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void retourGestion(ActionEvent event) {
        // on recupere l'etat de la barre de progression des oeufs
        this.jeu.getJoueur().getFerme().setEtatProgressOeuf(this.progressOeufs.getProgress());

        // on enregistre l'heure de switch de fenetre
        this.jeu.getJoueur().getFerme().setDateDeco(LocalDateTime.now());

        // on stoppe la barre de progression;
        this.progressBarStop();

        sauvegardejeu();

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

    public void miseEnPlaceValeurs() {
        // Mets en place la valeur des labels :
        setLabelPseudo();
        setMontantBanque();
        setNbPoules();
        setNbOeufs();
        setLabelPoule();
        majGainsEnCours();
//        setLabelTaxe();
//        setMontantTaxe();
    }

    /**
     * Affiche le message passé en argument dans le label
     *
     * @param messageLabel
     */
    public void afficherDansLabel(String messageLabel) {
        this.labelConsole.setText(messageLabel);
    }

    public void afficherDansLabel(List<String> messageLabel) {
        String messageComplet = "";
        for (String message : messageLabel) {
            messageComplet += message;
        }
        this.labelConsole.setText(messageComplet);
    }

    /**
     * initialise l'affichage du pseudo
     */
    public void setLabelPseudo() {
        this.labelPseudo.setText(jeu.getJoueur().getPseudo());
    }

    /**
     * initialise le montant en banque du joueur
     */
    public void setMontantBanque() {
        String formattedString = "En banque : " + decimalFormat.format(jeu.getJoueur().getArgent()) + monnaie;
        this.montantBanque.setText(formattedString);
    }

    /**
     * initialise le nombre de poules
     */
    public void setNbPoules() {
        this.nbPoules.setText(jeu.getJoueur().getFerme().getNbPoules() + "");
    }

    /**
     * initialise le nombre d'oeufs
     */
    public void setNbOeufs() {
        nbOeufs.setText(jeu.getJoueur().getFerme().getNbOeufs() + "");
    }

//    /**
//     * Affiche le montant de la taxe de vente des oeufs
//     */
//    public void setMontantTaxe(){
//        String formattedString = decimalFormat.format(this.taxeEnAttente) + monnaie;
//        montantTaxe.setText(formattedString);
//    }

    /**
     * initialise les gains à recuperer lors du clic sur vente
     */
    public void setGainARecuperer() {
        String formattedNumber = decimalFormat.format(this.gainEnAttente) + monnaie;
        this.gainARecuperer.setText(formattedNumber);
    }

    /**
     * initialise le message en dessous de poule
     * indiquant la valeur de l'achat d'une poule
     */
    public void setLabelPoule() {
        String formattedString = "Ajouter une poule " + decimalFormat.format(jeu.getParametres().getTarifPoule()) + monnaie;
        this.labelPoule.setText(formattedString);
    }

//    /**
//     * initialise le message en dessous de taxe
//     * indiquant la valeur de la taxte
//     */
//    public void setLabelTaxe() {
////        String formattedString = "Taxe pour la vente d'un oeuf " + decimalFormat.format(jeu.getParametres().getTaxeOeuf()) + monnaie;
//        String formattedString = "Taxe pour la vente d'un oeuf : 0.10 €";
//        this.labelTaxe.setText(formattedString);
//    }

    /**
     * action a effectuer lors du clic sur le bouton Vendre
     */
    public void onVendre() {
        System.out.println("nombre d'oeufs à vendre : " + jeu.getJoueur().getFerme().getNbOeufs());
        long nbOeufsVendre = jeu.getJoueur().getFerme().getNbOeufs();
        BigDecimal tarifOeufs = jeu.getParametres().getTarifOeuf();
        BigDecimal gain = tarifOeufs.multiply(BigDecimal.valueOf(nbOeufsVendre));

        jeu.getJoueur().getFerme().setNbOeufs(0); // raz le nombre d'oeufs

        jeu.getJoueur().setArgent(this.gainEnAttente.add(jeu.getJoueur().getArgent())); // met a jour les nouveaux gains
        String formattedGain = decimalFormat.format(this.gainEnAttente) + monnaie;
        // raz du contenu de la variable gainEnAttente;
        this.gainEnAttente = BigDecimal.valueOf(0.00);
        this.taxeEnAttente = BigDecimal.valueOf(0.00);
//        this.fenetrePrincipale.afficherDansLabel("Vous venez de vendre " + nbOeufsVendre + " oeuf(s), vous avez gagné " + formattedGain + ".");
        System.out.println("Vous venez de vendre " + nbOeufsVendre + " oeuf(s), vous avez gagné " + formattedGain + ".");
        btnVendre.setDisable(true);
        majLabels();
        majBtnAchats();
//        setMontantTaxe();

        // teste si on a assez pour acheter un distributeur
        // uniquement si on ne l'a pas encore passé à 1
        BigDecimal enBanque = this.jeu.getJoueur().getArgent();
        int comparaison = enBanque.compareTo(jeu.getParametres().getPrixDistributeurBC());
        if (comparaison >= 0 && this.jeu.getJoueur().getDistributeursActive() == 0) {
            this.jeu.getJoueur().setDistributeursActive(1);
        }

        System.out.println("Vente des oeufs");
        // calcule le nombre maximum de poules achetables pour mettre à jour le bouton
        achatMaxPoules();

    }

    /**
     * Met à jour les labels de comptage
     */
    public void majLabels() {
        this.setMontantBanque();
        this.setNbOeufs();
        this.setGainARecuperer();
    }

    /**
     * Met à jour les boutons enabale / disable
     * suivant l'argent en banque
     */
    public void majBtnAchats() {
        BigDecimal enBanque = jeu.getJoueur().getArgent();
        int nbAchatPouleMax = achatMaxPoules();

        // comparaisons avec BigDecimal
        // test pour une poule
//        int comparaison = enBanque.compareTo(jeu.getParametres().getTarifPoule());
//        // Test du bouton poule
//        if (comparaison >= 0) {
//            this.getBtnbAchatPoule().setDisable(false);
//        } else {
//            this.getBtnbAchatPoule().setDisable(true);
//        }

        if (nbAchatPouleMax >= 10) {
            this.getBtnPPoulePDix().setDisable(false);
        }
        if (nbAchatPouleMax > 0) {
            this.getBtnbAchatPoule().setDisable(false);
            this.getBtnPPouleMax().setDisable(false);
            this.getBtnPPouleMax().setText("+ " + nbAchatPouleMax);
        } else {
            this.getBtnbAchatPoule().setDisable(true);
            this.getBtnPPoulePDix().setDisable(true);
            this.getBtnPPouleMax().setDisable(true);
        }

    }

    /**
     * achat d'une poule
     */
    public void addPoule1() {
        addPoule(1);
    }

    /**
     * achat de 10 poules
     */
    public void addPoule10() {
        addPoule(10);
    }

    /**
     * achat de x poules
     * où x est le nombre de poules maximum achetables
     */

    public void addPouleX(){
        addPoule(achatMaxPoules());
    }

    /**
     *
     *
     * action a effectuer lors du clic sur le achat d'une poule
     */
    public void addPoule(int nbPoules) {
        // prix d'une poule
        BigDecimal prixPoule = jeu.getParametres().getTarifPoule();

        // argent du joueur
        BigDecimal enBanque = jeu.getJoueur().getArgent();

        // on verifie si l'argent est dispo
        int comparaison = enBanque.compareTo(prixPoule.multiply(BigDecimal.valueOf(nbPoules)));
        if (comparaison >= 0) {
            // on ajoute le nombre de poules
            jeu.getJoueur().getFerme().setNbPoules(jeu.getJoueur().getFerme().getNbPoules() + nbPoules);
            // on retire l'argent
            BigDecimal nouvBanque = jeu.getJoueur().getArgent().subtract(prixPoule.multiply(BigDecimal.valueOf(nbPoules)));
            jeu.getJoueur().setArgent(nouvBanque);

            // on met a jour le bouton addPoule
            majBtnAchats();

            // met à jour l'affichage
            this.miseEnPlaceValeurs();
        } else {
            System.out.println("Vous n'avez pas assez d'argent pour acheter une poule");
        }
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
     * declare le bouton afin de pouvoir l'utiliser
     * par exemple disable true ou false
     *
     * @return
     */
    public Button getBtnVendre() {
        return btnVendre;
    }

    /**
     * declare le bouton afin de pouvoir l'utiliser
     * par exemple disable true ou false
     *
     * @return
     */
    public Button getBtnbAchatPoule() {
        return btnPPoule;
    }

    public Button getBtnPPoulePDix() {
        return btnPPoulePDix;
    }

    public Button getBtnPPouleMax() {
        return btnPPouleMax;
    }

    /**
     * Met à jour la barre de progression
     *
     * @param cycle
     * @param vitesse
     */
    public void progressBarStartTimelineEncours(int cycle, double vitesse) {
        ProgressBar progressOeufs = getProgressOeufs();
        Button btnVendre = getBtnVendre();
        // Réinitialise la barre de progression à 0
        progressOeufs.setProgress(this.jeu.getJoueur().getFerme().getEtatProgressOeuf());
        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressOeufs.progressProperty(), this.jeu.getJoueur().getFerme().getEtatProgressOeuf())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Oeuf terminé");
                    btnVendre.setDisable(false);
                    // ajoute un nombre d'oeuf correspondant au nombre de poules
                    this.majNbOeufs();
                    // met à jour les gains en cours
                    this.majGainsEnCours();
                }, new KeyValue(progressOeufs.progressProperty(), 1))
        );
        timeline.setOnFinished(event -> {
            if (cycle == 1) {
                // Lancer la deuxième exécution de la méthode progressBarStartTimeline
                jeu.getJoueur().getFerme().setEtatProgressOeuf(0);
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartTimeline(cycle - 1, jeu.getParametres().getVitessePonteOeuf());
            }
        });

        if (cycle == 0) {
            timeline.setCycleCount(Animation.INDEFINITE);
        } else {
            timeline.setCycleCount(cycle);
        }
        timeline.play();
    }

    public void progressBarStartTimeline(int cycle, double vitesse) {
        ProgressBar progressOeufs = getProgressOeufs();
        Button btnVendre = getBtnVendre();
        // Réinitialise la barre de progression à 0
        progressOeufs.setProgress(0);
        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressOeufs.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Oeuf terminé");
                    btnVendre.setDisable(false);
                    // ajoute un nombre d'oeuf correspondant au nombre de poules
                    this.majNbOeufs();
                    // met à jour les gains en cours
                    this.majGainsEnCours();
                }, new KeyValue(progressOeufs.progressProperty(), 1))
        );

        if (cycle == 0) {
            timeline.setCycleCount(Animation.INDEFINITE);
        } else {
            timeline.setCycleCount(cycle);
        }
        timeline.play();
    }

    private void progressBarStop() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
            System.out.println("Arret de la barre de progression");
        }
    }

    private Boolean isProgressBar() {
        if (timeline == null) {
            timeline = null;
            System.out.println("Barre de progression terminee");
            return false;
        }
        return true;
    }

    /** autre barre de progression
     *
     */
//    public void progressBarStartTimeline(int cycle, double vitesse) {
//        ProgressBar progressOeufs = getProgressOeufs();
//        Button btnVendre = getBtnVendre();
//        // Réinitialise la barre de progression à la valeur de départ spécifiée
//        progressOeufs.setProgress(jeu.getJoueur().getFerme().getVitessePonteOeuf());
//
//        double dureeTotale = vitesse * (1 - jeu.getJoueur().getFerme().getVitessePonteOeuf());
//        KeyFrame startKeyFrame = new KeyFrame(Duration.ZERO, new KeyValue(progressOeufs.progressProperty(), jeu.getJoueur().getFerme().getVitessePonteOeuf()));
//        KeyFrame endKeyFrame = new KeyFrame(Duration.seconds(dureeTotale), e -> {
//            System.out.println("Oeuf terminé");
//            btnVendre.setDisable(false);
//            // ajoute un nombre d'oeuf correspondant au nombre de poules
//            this.majNbOeufs();
//            // met à jour les gains en cours
//            this.majGainsEnCours();
//            attenteFinBarre = false;
//        });
//        timeline.setOnFinished(event -> {
//            if (cycle == 1) {
//                // Lancer la deuxième exécution de la méthode progressBarStartTimeline
//                jeu.getJoueur().getFerme().setEtatProgressOeuf(0);
//                // recalcul de la vitesse suivant le niveau de la barre de progression
//                progressBarStartTimeline(cycle - 1, jeu.getJoueur().getFerme().getVitessePonteOeuf());
//            }
//        });
//
//        Timeline timeline = new Timeline(startKeyFrame, endKeyFrame);
//        if (cycle == 0) {
//            timeline.setCycleCount(Animation.INDEFINITE);
//        } else {
//            timeline.setCycleCount(cycle);
//        }
//
//        timeline.play();
//    }


    /**
     * Met à jour le nombre d'oeufs prêts à la vente
     */
    public void majNbOeufs() {
        long nbOeufEnCours = jeu.getJoueur().getFerme().getNbOeufs();
        int nbPoulesEnCours = jeu.getJoueur().getFerme().getNbPoules();
        long nouvNombre = nbOeufEnCours + nbPoulesEnCours;
        jeu.getJoueur().getFerme().setNbOeufs(nouvNombre);
        setNbOeufs();
    }

    /**
     * Calcule les gains en cours
     * Gains = nombre d'oeufs * tarif de l'oeuf
     * Modiifie le label gainEnCours
     */
    public void majGainsEnCours() {
        // tant que non vendu, on cumule les gains en attente
        long nbOeufsVendre = jeu.getJoueur().getFerme().getNbOeufs();
        BigDecimal tarifOeufs = jeu.getParametres().getTarifOeuf();

        // calcul des gains attente
        this.gainEnAttente = tarifOeufs.multiply(BigDecimal.valueOf(nbOeufsVendre));

        // calcul de la taxe
        this.taxeEnAttente = this.jeu.getParametres().calulTaxe(nbOeufsVendre);
        // on enleve la taxe

        this.gainEnAttente = this.gainEnAttente.subtract(this.taxeEnAttente);

//        setMontantTaxe();
        this.setGainARecuperer();
    }

    /**
     * ajustement de la barre de progression et des oeufs pondus
     * on compare l'heure et le jour actuel avec la date deco
     */
    public void reajustementSwitchFenetre() {
        LocalDateTime heureDecp = jeu.getJoueur().getFerme().getDateDeco();
        LocalDateTime heureActuelle = LocalDateTime.now();
        long ecartEnSecondes = ChronoUnit.SECONDS.between(heureDecp, heureActuelle);
        System.out.println("ecart de temps : " + ecartEnSecondes);
        // effectue les calculs du nombre d'oeufs en plus pendant le switch de fenetres ou déco
        calculTravailHorsConnection(ecartEnSecondes);
    }

    /**
     * Calcule le nombre d'oeuf qu'une poule aurait pû pondre
     * pendant un temps donné
     *
     * @param ecartEnSecondes
     */
    public void calculTravailHorsConnection(long ecartEnSecondes) {
        int vitessePonte = jeu.getParametres().getVitessePonteOeuf();
        long nbOeufsHc = ecartEnSecondes / vitessePonte;
        double calculReste = ecartEnSecondes % vitessePonte;
        double barreHc = calculReste / vitessePonte;
        // si demarrageBarre > 1
        // creation d'un oeuf
        // calculer la difference
        double demarrageBarre = jeu.getJoueur().getFerme().getEtatProgressOeuf() + barreHc;

        System.out.println("En " + ecartEnSecondes + " secondes, la poule a pondue : " + nbOeufsHc + " oeuf(s) et il reste encore " + calculReste);
        System.out.println("La nouvelle barre de progression est à : " + barreHc);

        barreHc = calculBarreHc(barreHc);
        // ajuste la barre de progression
        jeu.getJoueur().getFerme().setEtatProgressOeuf(barreHc);

        // réajuste suivant le nombre de poules
        nbOeufsHc = nbOeufsHc * jeu.getJoueur().getFerme().getNbPoules();
        jeu.getJoueur().getFerme().setNbOeufs(nbOeufsHc + jeu.getJoueur().getFerme().getNbOeufs());

        // affiche le bouton vendre si nbOeufs > 0
        if (jeu.getJoueur().getFerme().getNbOeufs() > 0) {
            this.btnVendre.setDisable(false);
        }

        // met a jour les autres boutons si disponibles
        this.majBtnAchats();
    }

    public double calculBarreHc(double barreHc) {
        double ancBarre = jeu.getJoueur().getFerme().getEtatProgressOeuf();
        double resultat = ancBarre + barreHc;
        if (resultat == 1) {
            // ajout d'un oeuf par poule et retour de la barre à 0
            jeu.getJoueur().getFerme().setNbOeufs(jeu.getJoueur().getFerme().getNbOeufs() + jeu.getJoueur().getFerme().getNbPoules());
            return 0;
        } else if (resultat < 1) {
            return resultat;
        } else {
            // ajout d'un oeuf
            jeu.getJoueur().getFerme().setNbOeufs(jeu.getJoueur().getFerme().getNbOeufs() + jeu.getJoueur().getFerme().getNbPoules());
            return (resultat - 1);
        }
    }

    /**
     * Affiche l'image de la poule
     * et permet son animation
     */
    public void afficherImage(int spriteX, int spriteY, boolean allerDroite) {
        // test images tirées d'une feuille de sprites

        int spriteWidth = 400;
        int spriteHeight = 400;
        double positionX = imgAnimation.getTranslateX();

        WritableImage spriteImage = new WritableImage(pixelReader, spriteX, spriteY, spriteWidth, spriteHeight);
        imgAnimation.setImage(spriteImage);

        if (allerDroite) {
            imgAnimation.setTranslateX(positionX + 40);
            imgAnimation.setScaleX(1);
        } else {
            imgAnimation.setTranslateX(positionX - 40);
            imgAnimation.setScaleX(-1);
        }
    }

    /**
     * Timeline Animation de la poule
     */
    public void executerAnimation() {
        Timeline timelinePoule = new Timeline();

        // Définir les paramètres pour chaque exécution de la méthode afficherImage
        int spriteX = 0;
        int spriteY = 0;

        // Boucle 1 -> deplacement vers la droite
        KeyFrame keyFrame4 = new KeyFrame(Duration.seconds(0), event -> afficherImage(spriteX + 1200, spriteY, true));
        KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(1), event -> afficherImage(spriteX, spriteY, true));
        KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(2), event -> afficherImage(spriteX + 400, spriteY, true));
        KeyFrame keyFrame3 = new KeyFrame(Duration.seconds(3), event -> afficherImage(spriteX + 800, spriteY, true));

        // Boucle 2 -> deplacement vers la gauche
        KeyFrame keyFrame5 = new KeyFrame(Duration.seconds(5), event -> afficherImage(spriteX + 1200, spriteY, false));
        KeyFrame keyFrame6 = new KeyFrame(Duration.seconds(6), event -> afficherImage(spriteX + 800, spriteY, false));
        KeyFrame keyFrame7 = new KeyFrame(Duration.seconds(7), event -> afficherImage(spriteX + 400, spriteY, false));
        KeyFrame keyFrame8 = new KeyFrame(Duration.seconds(8), event -> afficherImage(spriteX, spriteY, false));

        timelinePoule.getKeyFrames().addAll(keyFrame1, keyFrame2, keyFrame3, keyFrame4, keyFrame5, keyFrame6, keyFrame7, keyFrame8);

        timelinePoule.setCycleCount(Timeline.INDEFINITE);

        // Démarrer la Timeline
        timelinePoule.play();
    }

    public int achatMaxPoules() {
        BigDecimal argentEnCours = jeu.getJoueur().getArgent();
        BigDecimal tarifPoule = jeu.getParametres().getTarifPoule();
        return argentEnCours.divide(tarifPoule).intValue();
    }

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
}
