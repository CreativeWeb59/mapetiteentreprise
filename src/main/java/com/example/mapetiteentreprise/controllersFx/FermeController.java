package com.example.mapetiteentreprise.controllersFx;
import com.example.mapetiteentreprise.Main;
import com.example.mapetiteentreprise.jeu.CreditEnCours;
import com.example.mapetiteentreprise.jeu.Jeu;
import com.example.mapetiteentreprise.actions.Outils;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class FermeController {
    private final String monnaie = " €";
    private final String separationTexte = System.getProperty("line.separator");
    // pattern des nombre décimaux
//    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private final DecimalFormat decimalFormat = Outils.getDecimalFormatWithSpaceSeparator();
    private BigDecimal gainEnAttente = new BigDecimal(0);
    private BigDecimal taxeEnAttente = new BigDecimal(0);
    @FXML
    private Label montantBanque, labelConsole, nbPoules, nbOeufs, labelPseudo, labelPoule, gainARecuperer, labelCredit, labelJourEncours, labelBanquier,
            labelNivPoulailler1, labelNivPoulailler2, labelNivPoulailler3, labelNivPoulailler4;
    //    labelTaxe, montantTaxe;
    @FXML
    private Pane paneFerme, paneProgress;
    @FXML
    private ProgressBar progressOeufs, progressBC, progressBF, progressSa, progressCo,
            progressScooter, progressCamionette, progressPetitCamion, progressPoidsLourd, progressAvion,
            progressTextile1, progressTextile2, progressTextile3, progressTextile4,
            progressJouets1, progressJouets2, progressJouets3, progressJouets4,
            progressPharmaceutique1, progressPharmaceutique2, progressPharmaceutique3, progressPharmaceutique4,
            progressAgroAlimentaire1, progressAgroAlimentaire2, progressAgroAlimentaire3, progressAgroAlimentaire4;
    private Timeline timelineOeufs;
    @FXML
    private Button btnVendre, btnPPoule, btnPPoulePDix, btnPPouleMax, rembourserCredit, retourMenu,
            btnAmeliorerPoulailler1, btnAmeliorerPoulailler2, btnAmeliorerPoulailler3, btnAmeliorerPoulailler4;
    @FXML
    private ImageView imgAnimation, evenement, imgPoulailler;
    @FXML
    private Group groupBtnAmeliorer1, groupBtnAmeliorer2, groupBtnAmeliorer3, groupBtnAmeliorer4;
    @FXML
    private PieChart pieHorloge;
    private String messageConsole;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Jeu jeu;
    double etatProgress; // permet de gerer l'etat d'avancement de la barre de progression

    // animation de la poule
    String spriteSheetPath = getClass().getResource("/com/example/mapetiteentreprise/images/sprite_poule.png").toExternalForm();
    Image spriteSheet = new Image(spriteSheetPath);
    PixelReader pixelReader = spriteSheet.getPixelReader();
    private Timeline timelinePoule;

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
        this.miseEnPlaceValeurs(); // mise en place des labels et boutons
        blocageComplet(); // bloque les achats si credit non payé
        // barre de progress des oeufs
        double vitesse = jeu.getParametres().getVitessePonteOeuf() - (jeu.getParametres().getVitessePonteOeuf() * jeu.getJoueur().getFerme().getEtatProgressOeuf());
        this.progressBarStartTimelineEncours(1, vitesse);
        demarrageDistributeurs(); // demarrage des distributeurs
        demarrageLivraisons(); // demarrage des livraisons

        // demarrage des usines
        demarrageUsinesTextile();
        demarrageUsinesJouets();
        demarrageUsinesPharmaceutique();

        this.executerAnimation(); // animation de la poule
        affichageEvenementBanquier(); // affiche ou non le banquier
        setPieHorloge();  // affichage de l'horloge
        this.majBoutons();
        jeu.afficheProgression(paneProgress);   // affichage des barres de progression (mode dev)
    }

    public void nouveau(Jeu jeu) {
        // Recuperation du jeu
        this.jeu = jeu;
        System.out.println("Nouveau jeu, désactivation de la ferme");
        this.miseEnPlaceValeurs();  // mise en place des labels et boutons
        this.jeu.getJoueur().setFermeActive(1);   // activation de la ferme
        // desactivation du retour au menu
        // afin de laisser la barre de progression du premier jour s'activer
        setBtnMenuActive();

        progressBarStartTimeline(0, jeu.getParametres().getVitessePonteOeuf());
//        if (jeu.getCalendrier().getHeureActuelle() != 0) {
//            // recuperation de l'etat de la barre de progression pour la journee
//            double vitesseJour = jeu.getCalendrier().getDureeJour() - (jeu.getCalendrier().getDureeJour() * jeu.getCalendrier().getProgressJour());
//            progressBarStartTimelineJourneeEnCours(1, vitesseJour);
//        }
        this.executerAnimation();
        setPieHorloge();
        this.majBoutons();

        // affichage des barres de progression (mode dev)
        jeu.afficheProgression(paneProgress);
    }

    /**
     * Action a executer lors de la fermeture de la fentre avec la croix : sauvegarde
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

    public void miseEnPlaceValeurs() {
        // Mets en place la valeur des labels :
        setLabelPseudo();
        setMontantBanque();
        setNbPoules();
        setNbOeufs();
        setLabelPoule();
        majGainsEnCours();
        setLabelJourEncours();
        setLabelCredit();
        setLabelConsole();
//        setLabelTaxe();
//        setMontantTaxe();
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
     * initialise le nombre de poules ainsi que le nombre de poules maximum
     */
    public void setNbPoules() {
        int poulailler1 = jeu.getJoueur().getPoulailler1();
        int poulailler2 = jeu.getJoueur().getPoulailler2();
        int poulailler3 = jeu.getJoueur().getPoulailler3();
        int poulailler4 = jeu.getJoueur().getPoulailler4();
        int nbCapacitePoules = Outils.capaciteMaxPoulaillers(jeu.getPoulaillersList(), poulailler1, poulailler2, poulailler3, poulailler4);
        String formattedString = jeu.getJoueur().getFerme().getNbPoules() + " / ";
        formattedString += nbCapacitePoules;

        this.nbPoules.setText(formattedString);
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
        int typePoulailler = jeu.getJoueur().getPoulailler1();
        String formattedString = "Ajouter une poule " + decimalFormat.format(jeu.getParametres().getTarifPoule()) + monnaie + separationTexte;
        formattedString = "Capacite des poulaillers : " + separationTexte + separationTexte;
        for (int i = 1; i <= 5; i++) {
            formattedString += i + " - " + jeu.getPoulaillersList().get(i).getNom() + " : ";
            formattedString += jeu.getPoulaillersList().get(i).getCapacite() + " poules" + separationTexte;
        }

//        if (typePoulailler + 1 < jeu.getPoulaillersList().size()) {
//            formattedString += "Prochaine amélioration : " + separationTexte + jeu.getPoulaillersList().get(typePoulailler + 1).getPrixPoulailler() + monnaie + separationTexte;
//            formattedString += "pour maximum " + jeu.getPoulaillersList().get(typePoulailler + 1).getCapacite() + " poules";
//        } else {
//            formattedString += separationTexte + "Amélioration au maximum !";
//        }
        this.labelPoule.setText(formattedString);
    }

    public void setBtnMenuActive() {
        if (jeu.getCalendrier().getHeureActuelle() == 0) {
            retourMenu.setDisable(true);
        } else {
            retourMenu.setDisable(false);
        }
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
//        achatMaxPoules();

        // maj du bouton rembourser credit + autres boutons
        this.majBoutons();

        // bloque les boutons d'achat si credit non rembourse a la date prévue
        this.blocageAchats();
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
//        BigDecimal enBanque = jeu.getJoueur().getArgent();
        int nbAchatPouleMax = achatMaxPoules();

        System.out.println("nombre de poules achetables : " + nbAchatPouleMax);

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
     * Active/desactive le bouton vendre suivant s'il y a un montant a recuperer
     */
    public void majBtnVendre() {
        // affiche le bouton vendre si nbOeufs > 0
        if (jeu.getJoueur().getFerme().getNbOeufs() > 0) {
            this.btnVendre.setDisable(false);
        } else {
            this.btnVendre.setDisable(true);
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

    public void addPouleX() {
        addPoule(achatMaxPoules());
    }

    /**
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

            // on met a jour les boutons
            majBoutons();

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
     * declare la ProgressBar Jours afin de pouvoir l'utiliser
     * pour l'effet de remplissage
     *
     * @return
     */

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
        timelineOeufs = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressOeufs.progressProperty(), this.jeu.getJoueur().getFerme().getEtatProgressOeuf())),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Ajoute une heure");
                    this.jeu.getCalendrier().setIncrementHeure();
                    this.setHeureHorloge();
                    System.out.println("Heure actuelle : " + jeu.getCalendrier().getHeureActuelle());

                    System.out.println("Oeuf terminé");
                    btnVendre.setDisable(false);
                    // ajoute un nombre d'oeuf correspondant au nombre de poules
                    this.majNbOeufs();
                    // met à jour les gains en cours
                    this.majGainsEnCours();
                    // mets à jour le jour en cours
                    this.setLabelJourEncours();
                    // affiche ou non le banquier
                    affichageEvenementBanquier();
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
        Button btnVendre = getBtnVendre();
        // Réinitialise la barre de progression à 0
        progressOeufs.setProgress(0);
        timelineOeufs = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressOeufs.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Ajoute une heure");
                    this.jeu.getCalendrier().setIncrementHeure();
                    System.out.println("Heure actuelle : " + jeu.getCalendrier().getHeureActuelle());

                    this.setHeureHorloge();
                    System.out.println("Oeuf terminé");
                    btnVendre.setDisable(false);
                    // ajoute un nombre d'oeuf correspondant au nombre de poules
                    this.majNbOeufs();
                    // met à jour les gains en cours
                    this.majGainsEnCours();
                    this.setLabelJourEncours();
                    // affiche ou non le banquier
                    affichageEvenementBanquier();
                    setBtnMenuActive();
                    // lance la progress jour lorsque le jeu est nouveau
//                    if (!isProgressBar(timelineJour)) {
//                        progressBarStartTimelineJournee(cycle - 1, this.jeu.getCalendrier().getDureeJour());
//                    }
                }, new KeyValue(progressOeufs.progressProperty(), 1))
        );

        if (cycle == 0) {
            timelineOeufs.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineOeufs.setCycleCount(cycle);
        }
        timelineOeufs.play();
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

    /**
     * Calcule le nombre d'achat max qu'on peut acheter
     * depend de l'argent dispo et du type de poulailler
     *
     * @return
     */
    public int achatMaxPoules() {
        BigDecimal argentEnCours = jeu.getJoueur().getArgent();
        BigDecimal tarifPoule = jeu.getParametres().getTarifPoule();
        int nbPoulesMaxAchetables = nbPoulesMax();
        int maxArgentPoules = argentEnCours.divide(tarifPoule).intValue();

        if (maxArgentPoules > nbPoulesMaxAchetables) {
            return nbPoulesMaxAchetables;
        } else {
            return maxArgentPoules;
        }
    }

    /**
     * renvoi la capacite du poulailler en nombre de poules
     * depend de la capacité du poulailler
     *
     * @return
     */
    public int nbPoulesMax() {
        int poulailler1 = jeu.getJoueur().getPoulailler1();
        int poulailler2 = jeu.getJoueur().getPoulailler2();
        int poulailler3 = jeu.getJoueur().getPoulailler3();
        int poulailler4 = jeu.getJoueur().getPoulailler4();
        int nbPoulesActuel = jeu.getJoueur().getFerme().getNbPoules();
        int nbPoulesCapacite = Outils.capaciteMaxPoulaillers(jeu.getPoulaillersList(), poulailler1, poulailler2, poulailler3, poulailler4);

        // nombre de poules possible
        int nbPoulesMaxAchetables = nbPoulesCapacite - nbPoulesActuel;

        if (nbPoulesMaxAchetables > 0) {
            return nbPoulesMaxAchetables;
        } else {
            return 0;
        }
    }

    public void setLabelJourEncours() {
        String texte = "Jour " + jeu.getCalendrier().getNumJour();
        labelJourEncours.setText(texte);
    }

    /**
     * Affiche le message en haut
     * avec les infos sur le crédit : Montant restant dù, date prochaine échéance et montant prochaine échéance
     */
    public void setLabelCredit() {
        String texte = "";
        CreditEnCours creditEnCours = jeu.getJoueur().getCreditEnCours();
        if (creditEnCours != null) {
            if (jeu.getJoueur().getCreditEnCours().getTermine() == 0) {
                BigDecimal montantPret = jeu.getJoueur().getCreditEnCours().getMontantPret();
                String SMontantPret = decimalFormat.format(montantPret) + monnaie;

                BigDecimal montantRestantDu = jeu.getJoueur().getCreditEnCours().getCoutPret().subtract(jeu.getJoueur().getCreditEnCours().getMontantRembourse());
                String SMontantRestantDu = decimalFormat.format(montantRestantDu) + monnaie;

                BigDecimal coutPret = jeu.getJoueur().getCreditEnCours().getCoutPret();
                String SCoutPret = decimalFormat.format(coutPret) + monnaie;

                BigDecimal interets = jeu.getJoueur().getCreditEnCours().getCoutPret().subtract(montantPret);
                String Sinterets = decimalFormat.format(interets) + monnaie;

                BigDecimal mensualite = jeu.getJoueur().getCreditEnCours().getMensualite();
                String SMensualite = decimalFormat.format(mensualite) + monnaie;

                // calcul mensualites en retard
                long nbRetardMensualite = jeu.getJoueur().getCreditEnCours().nbRetardMensualite(jeu.getCalendrier().getNumJour());

                long prochainPrelevement = jeu.getJoueur().getCreditEnCours().getDateProchaineMensualite();

                texte += "Emprunt : " + SMontantPret + separationTexte;
                texte += "Interêts : " + Sinterets + separationTexte;
                texte += "Total : " + SCoutPret + separationTexte;
                texte += "Restant dû : " + SMontantRestantDu + separationTexte;
                texte += "Prélèvement : " + SMensualite + separationTexte;

                // gestion si echeances en retard
                if (nbRetardMensualite <= 0) {
                    texte += "A payer le jour : " + prochainPrelevement;
                } else {
                    texte += "Vous avez " + nbRetardMensualite + " écheance(s) en retard" + separationTexte;
                    texte += "A payer immédiatement";

                    // Arrivee du banquier
                    // A faire Blocage des boutons d'achat
                }

                // met à jour le bouton rembourser si assez d'argent
                this.majBoutons();
            } else {
                texte += "Pas de crédits en cours";
                // on cache et desactive le bouton pour rembourser le credit
                rembourserCredit.setVisible(false);
                rembourserCredit.setDisable(true);
            }

        } else {
            texte += "Pas de crédits en cours";
            // on cache et desactive le bouton pour rembourser le credit
            rembourserCredit.setVisible(false);
            rembourserCredit.setDisable(true);
        }

        // recuperation des infos
        labelCredit.setText(texte);

        // emprunt en cours 1200
        // cout du credit 1296
        // 16 remboursements = 15x84 + 36

    }

    /**
     * gere le bouton pour rembourser une mensualite du credit
     */
    public void onRembourserCredit() {
        // teste si assez argent en banque
        if (jeu.getJoueur().isArgent(jeu.getJoueur().getCreditEnCours().getMensualite())) {
            // enleve le montant de la banque
            jeu.getJoueur().depenser(jeu.getJoueur().getCreditEnCours().getMensualite());
            // met a jour le credit
            // met a jour le montant du credit rembourse
            jeu.getJoueur().getCreditEnCours().payerMensualite(jeu.getCalendrier().getNumJour());
            // met a jour la prochaine date du reglement

            System.out.println("remboursement");
            this.miseEnPlaceValeurs();
            this.affichageEvenementBanquier();
        } else {
            System.out.println("Vous n'avez pas assez d'argent pour rembourser le credit");
        }

    }

    /**
     * methode executee à la fin de la barre de progression des poules
     * doit verifier si assez d'argent pour rembourser le prêt
     */
    public void majCredit() {
        CreditEnCours creditEnCours = jeu.getJoueur().getCreditEnCours();
        if (creditEnCours != null) {
            if (jeu.getJoueur().getCreditEnCours().getTermine() == 0) {
                if (jeu.getJoueur().isArgent(jeu.getJoueur().getCreditEnCours().getMensualite())) {
                    rembourserCredit.setVisible(true);
                    rembourserCredit.setDisable(false);
                } else {
                    rembourserCredit.setVisible(true);
                    rembourserCredit.setDisable(true);
                }
            } else {
                rembourserCredit.setVisible(true);
                rembourserCredit.setDisable(true);
            }
        }
    }

    /**
     * gerer l'arrivee du banquier
     */
    public void affichageEvenementBanquier() {
        if (afficherBanquier()) {
            evenement.setDisable(false);
            evenement.setVisible(true);
            evenement.setOpacity(1);
            labelBanquier.setVisible(true);
            labelBanquier.setDisable(false);
            labelBanquier.setOpacity(1);
            blocageAchats();
        } else {
            evenement.setDisable(true);
            evenement.setVisible(false);
            evenement.setOpacity(0);
            labelBanquier.setDisable(true);
            labelBanquier.setVisible(false);
            labelBanquier.setOpacity(0);
        }
    }

    public boolean afficherBanquier() {
        CreditEnCours creditEnCours = jeu.getJoueur().getCreditEnCours();
        if (creditEnCours != null) {
            if (jeu.getJoueur().getCreditEnCours().getTermine() == 0) {
                if (jeu.getCalendrier().getNumJour() >= jeu.getJoueur().getCreditEnCours().getDateProchaineMensualite()) {
                    // moddifie la date du preavis qu'une fois
                    if (jeu.getJoueur().getCreditEnCours().getBlocageDatePreavis() == 0) {
                        this.jeu.getJoueur().getCreditEnCours().setDatePreavis(this.jeu.getCalendrier().getNumJour() + 200);
                        this.jeu.getJoueur().getCreditEnCours().setBlocageDatePreavis(1);
                        System.out.println("Date du preavis : " + this.jeu.getJoueur().getCreditEnCours().getDatePreavis());
                    }
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * rends disable les boutons achats quand c'est le moment de payer
     */
    public void blocageAchats() {
        if (afficherBanquier()) {
            btnPPoule.setDisable(true);
            btnPPoulePDix.setDisable(true);
            btnPPouleMax.setDisable(true);
        } else {
            majBtnAchats();
        }
    }

    /**
     * Verifie la date du preavis et bloque tout si elle est dépassée
     */
    public void blocageComplet() {
        CreditEnCours creditEnCours = jeu.getJoueur().getCreditEnCours();
        if (creditEnCours != null) {
            if (jeu.getJoueur().getCreditEnCours().getTermine() == 0) {
                if (jeu.getJoueur().getCreditEnCours().getDatePreavis() <= this.jeu.getCalendrier().getNumJour()) {
                    labelBanquier.setText("Le délai est écoulé, vous avez perdu le jeu et je récupère votre ferme. Elle sera vendue à quelqu'un de plus performant en affaires !!!");
                    paneFerme.setOpacity(0.6);
                    paneFerme.setDisable(true);
                } else {
                    System.out.println("pas de credit");
                }
            }
            if (jeu.getJoueur().getCreditEnCours().getDatePreavis() <= this.jeu.getCalendrier().getNumJour()) {
                labelBanquier.setText("Le délai est écoulé, vous avez perdu le jeu et je récupère votre ferme. Elle sera vendue à quelqu'un de plus performant en affaires !!!");
                paneFerme.setOpacity(0.6);
                paneFerme.setDisable(true);
            }
        } else {
            System.out.println("pas de credit");
        }

    }

    /**
     * execute la methode qui creer l'horloge
     */
    public void setPieHorloge() {
        this.jeu.getCalendrier().createHorloge(pieHorloge);
    }

    /**
     * Maj les couleurs de l'horloge
     */
    public void setHeureHorloge() {
        this.jeu.getCalendrier().modifyPieChartColors(pieHorloge);
    }

    /**
     * Affichage du message dans le label en bas de l'écran
     */
    public void setLabelConsole() {
        BigDecimal tarifOeufNet = jeu.getParametres().getTarifOeuf().subtract(jeu.getParametres().getTaxeOeuf());
        BigDecimal prochainsGains = tarifOeufNet.multiply(BigDecimal.valueOf(jeu.getJoueur().getFerme().getNbPoules()));
        String formattedString = decimalFormat.format(prochainsGains) + monnaie;
        labelConsole.setText("Le prochain cycle vous rapportera : " + formattedString);
    }

    /**
     * Débloque le bouton pour améliorer le poulailler quand on a l'argent
     * cache quand on est au maxi
     * En parametre le bouton utilise
     */
    public void verifBtnAmeliorer(Group groupBtnAmeliorer, Button btnAmeliorer , int typePoulailler) {
//        int typePoulaillerSuivant = jeu.getJoueur().getPoulailler1() + 1;
        int niveauMaxPoulailler = jeu.getPoulaillersList().size() - 2; // niveau max des poulailler
        int typePoulaillerSuivant = typePoulailler + 1;
        if (typePoulailler > niveauMaxPoulailler) {
            groupBtnAmeliorer.setVisible(false);
        } else {
            groupBtnAmeliorer.setVisible(true);
            BigDecimal prixPoulailler = jeu.getPoulaillersList().get(typePoulaillerSuivant).getPrixPoulailler();
            String sMontantAmeliorer = decimalFormat.format(prixPoulailler) + monnaie;
            // on determine si achat ou amelioration = pour un achat typePoulailler = 0
            if(typePoulailler == 0){
                btnAmeliorer.setText("Acheter" + separationTexte + "pour " + sMontantAmeliorer);
            } else {
                btnAmeliorer.setText("Améliorer" + separationTexte + "pour " + sMontantAmeliorer);
            }
            if (jeu.getJoueur().isArgent(prixPoulailler)) {
                groupBtnAmeliorer.setDisable(false);
            } else {
                groupBtnAmeliorer.setDisable(true);
            }
        }
    }

    /**
     * Achete un poulaiiller
     * verifie si l'argent est disponible
     * Incremente la valeur du poulailler
     * Modifie le bon type de poulailler dans le joueur
     *
     * @param numPoulailler nom du poulailler
     */
    public void achatPoulailler(int numPoulailler, int typePoulailler) {
        System.out.println("Achat d'un poulailler, " + numPoulailler + ", " + typePoulailler);
        // superieur à 4 => amelioration terminee
        int niveauMaxPoulailler = jeu.getPoulaillersList().size() - 1; // amelioration max des poulailler
        if (typePoulailler < niveauMaxPoulailler) {
            int typePoulaillerSuivant = typePoulailler + 1;
            BigDecimal prixPoulailler = jeu.getPoulaillersList().get(typePoulaillerSuivant).getPrixPoulailler();
            System.out.println("Prix du poulailler : " + prixPoulailler);
            // on depense uniquement si on a l'argent
            if (jeu.getJoueur().isArgent(prixPoulailler)) {
                jeu.getJoueur().depenser(prixPoulailler);
                // mise en place du nouveau poulailler
                switch (numPoulailler) {
                    case 1:
                        jeu.getJoueur().setPoulailler1(typePoulaillerSuivant);
                        break;
                    case 2:
                        jeu.getJoueur().setPoulailler2(typePoulaillerSuivant);
                        break;
                    case 3:
                        jeu.getJoueur().setPoulailler3(typePoulaillerSuivant);
                        break;
                    case 4:
                        jeu.getJoueur().setPoulailler4(typePoulaillerSuivant);
                        break;
                    default:
                        System.out.println("Erreur de poulailler");
                }
                if (typePoulailler == 0) {
                    System.out.println("Vous venez d'acheter le poulailler " + numPoulailler + " " + jeu.getPoulaillersList().get(typePoulaillerSuivant).getNom());
                } else if (typePoulailler > 0 && typePoulailler <5){
                    System.out.println("Vous venez d'améliorer le poulailler " + numPoulailler + " " + jeu.getPoulaillersList().get(typePoulaillerSuivant).getNom());
                }
                this.majBoutons();
                this.setLabelPoule();
                this.setNbPoules();
            }
        } else {
            System.out.println("Vous ne pouvez plus ameliorer ce poulailler");
        }
    }


    /**
     * gere le clic sur le bouton
     * pour l'achat / amelioration du poulailler 1
     * renvoi à la methode generale d'achat du poulailler spécifié
     */
    public void achatPoulailler1() {
        achatPoulailler(1, jeu.getJoueur().getPoulailler1());
    }

    /**
     * gere le clic sur le bouton
     * pour l'achat / amelioration du poulailler 2
     * renvoi à la methode generale d'achat du poulailler spécifié
     */
    public void achatPoulailler2() {
        achatPoulailler(2, jeu.getJoueur().getPoulailler2());
    }

    /**
     * gere le clic sur le bouton
     * pour l'achat / amelioration du poulailler 3
     * renvoi à la methode generale d'achat du poulailler spécifié
     */
    public void achatPoulailler3() {
        achatPoulailler(3, jeu.getJoueur().getPoulailler3());
    }

    /**
     * gere le clic sur le bouton
     * pour l'achat / amelioration du poulailler 4
     * renvoi à la methode generale d'achat du poulailler spécifié
     */
    public void achatPoulailler4() {
        achatPoulailler(4, jeu.getJoueur().getPoulailler4());
    }

    /**
     * Maj de tous les boutons d'achat
     */
    public void majBoutons() {
        this.majBtnAchats();
        this.majBtnVendre();
        // une execution par poulailler
        this.verifBtnAmeliorer(groupBtnAmeliorer1, btnAmeliorerPoulailler1, jeu.getJoueur().getPoulailler1());
        this.verifBtnAmeliorer(groupBtnAmeliorer2, btnAmeliorerPoulailler2, jeu.getJoueur().getPoulailler2());
        this.verifBtnAmeliorer(groupBtnAmeliorer3, btnAmeliorerPoulailler3, jeu.getJoueur().getPoulailler3());
        this.verifBtnAmeliorer(groupBtnAmeliorer4, btnAmeliorerPoulailler4, jeu.getJoueur().getPoulailler4());
        this.majCredit();
        this.majLabels(); // a voir si utile
        this.setLabelsPoulaillers();
    }

    public void setLabelsPoulaillers(){
        labelNivPoulailler1.setText(jeu.getJoueur().getPoulailler1() + "");
        labelNivPoulailler2.setText(jeu.getJoueur().getPoulailler2() + "");
        labelNivPoulailler3.setText(jeu.getJoueur().getPoulailler3() + "");
        labelNivPoulailler4.setText(jeu.getJoueur().getPoulailler4() + "");
    }

    /**
     * Permet d'ajuster les distributeurs et les demarrer s'ils sont actifs
     */
    public void demarrageDistributeurs(){
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
     * Fermeture des barres de progression : enregistrement de l'état + stop des barres de progress
     * Sauvegarde date deco
     */
    public void fermetureProgress(){
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

        // on recupere les barres de progression des livraisons
        this.jeu.getJoueur().getLivraisonScooter().setEtatProgressLivraison(this.progressScooter.getProgress());
        this.jeu.getJoueur().getLivraisonCamionette().setEtatProgressLivraison(this.progressCamionette.getProgress());
        this.jeu.getJoueur().getLivraisonPetitCamion().setEtatProgressLivraison(this.progressPetitCamion.getProgress());
        this.jeu.getJoueur().getLivraisonPoidsLourd().setEtatProgressLivraison(this.progressPoidsLourd.getProgress());
        this.jeu.getJoueur().getLivraisonAvion().setEtatProgressLivraison(this.progressAvion.getProgress());

        // on recupere les barres de progression des usines de textile
        this.jeu.getJoueur().getUsineTextilePetite().setEtatProgressUsine(this.progressTextile1.getProgress());
        this.jeu.getJoueur().getUsineTextileMoyenne().setEtatProgressUsine(this.progressTextile2.getProgress());
        this.jeu.getJoueur().getUsineTextileGrande().setEtatProgressUsine(this.progressTextile3.getProgress());
        this.jeu.getJoueur().getUsineTextileEnorme().setEtatProgressUsine(this.progressTextile4.getProgress());

        // on recupere les barres de progression des usines de jouets
        this.jeu.getJoueur().getUsineJouetsPetite().setEtatProgressUsine(this.progressJouets1.getProgress());
        this.jeu.getJoueur().getUsineJouetsMoyenne().setEtatProgressUsine(this.progressJouets2.getProgress());
        this.jeu.getJoueur().getUsineJouetsGrande().setEtatProgressUsine(this.progressJouets3.getProgress());
        this.jeu.getJoueur().getUsineJouetsEnorme().setEtatProgressUsine(this.progressJouets4.getProgress());

        // on stoppe les barres de progression
//        jeu.getJoueur().getFerme().progressBarStop();
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
        jeu.getJoueur().getUsineTextilePetite().progressBarStop();
        jeu.getJoueur().getUsineTextileMoyenne().progressBarStop();
        jeu.getJoueur().getUsineTextileGrande().progressBarStop();
        jeu.getJoueur().getUsineTextileEnorme().progressBarStop();
        jeu.getJoueur().getUsineJouetsPetite().progressBarStop();
        jeu.getJoueur().getUsineJouetsMoyenne().progressBarStop();
        jeu.getJoueur().getUsineJouetsGrande().progressBarStop();
        jeu.getJoueur().getUsineJouetsEnorme().progressBarStop();

        // on stoppe les barres de progression;
        this.progressBarStop(timelineOeufs);
    }

    /**
     * Demarrage des barres de progression des livraisons
     */
    public void demarrageLivraisons() {
        if(jeu.getJoueur().getLivraison1Active() == 1){
            // recuperation de l'etat de la barre de progression pour la livraison en scooter
            double vitesseScooter = jeu.getJoueur().getLivraisonScooter().getVitesseLivraion() - (jeu.getJoueur().getLivraisonScooter().getVitesseLivraion() * jeu.getJoueur().getLivraisonScooter().getEtatProgressLivraison());
            System.out.println("Vitesse scooter : " + vitesseScooter);
            this.jeu.getJoueur().getLivraisonScooter().progressBarStartScooter(1, jeu.getJoueur().getLivraisonScooter().getVitesseLivraion(), vitesseScooter, progressScooter);
        }
        if(jeu.getJoueur().getLivraison2Active() == 1){
            // recuperation de l'etat de la barre de progression pour la livraison en camionette
            double vitesseCamionette = jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion() - (jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion() * jeu.getJoueur().getLivraisonCamionette().getEtatProgressLivraison());
            System.out.println("Vitesse camionette : " + vitesseCamionette);
            this.jeu.getJoueur().getLivraisonCamionette().progressBarStartCamionette(1, jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion(), vitesseCamionette, progressCamionette);
        }
        if(jeu.getJoueur().getLivraison3Active() == 1){
            // recuperation de l'etat de la barre de progression pour la livraison en petit camion
            double vitessePetitCamion = jeu.getJoueur().getLivraisonPetitCamion().getVitesseLivraion() - (jeu.getJoueur().getLivraisonPetitCamion().getVitesseLivraion() * jeu.getJoueur().getLivraisonPetitCamion().getEtatProgressLivraison());
            System.out.println("Vitesse petit camion : " + vitessePetitCamion);
            this.jeu.getJoueur().getLivraisonPetitCamion().progressBarStartPetitCamion(1, jeu.getJoueur().getLivraisonPetitCamion().getVitesseLivraion(), vitessePetitCamion, progressPetitCamion);
        }
        if(jeu.getJoueur().getLivraison4Active() == 1){
            // recuperation de l'etat de la barre de progression pour la livraison en poids lours
            double vitessePoidsLourd = jeu.getJoueur().getLivraisonPoidsLourd().getVitesseLivraion() - (jeu.getJoueur().getLivraisonPoidsLourd().getVitesseLivraion() * jeu.getJoueur().getLivraisonPoidsLourd().getEtatProgressLivraison());
            System.out.println("Vitesse poids lourd : " + vitessePoidsLourd);
            this.jeu.getJoueur().getLivraisonPoidsLourd().progressBarStartPoidsLourd(1, jeu.getJoueur().getLivraisonPoidsLourd().getVitesseLivraion(), vitessePoidsLourd, progressPoidsLourd);
        }
        if(jeu.getJoueur().getLivraison5Active() == 1){
            // recuperation de l'etat de la barre de progression pour la livraison en avion
            double vitesseAvion = jeu.getJoueur().getLivraisonAvion().getVitesseLivraion() - (jeu.getJoueur().getLivraisonAvion().getVitesseLivraion() * jeu.getJoueur().getLivraisonAvion().getEtatProgressLivraison());
            System.out.println("Vitesse avion : " + vitesseAvion);
            this.jeu.getJoueur().getLivraisonAvion().progressBarStartAvion(1, jeu.getJoueur().getLivraisonAvion().getVitesseLivraion(), vitesseAvion, progressAvion);
        }
    }
    /**
     * Demarre les usines lorsqu'elles sont actives
     */
    public void demarrageUsinesTextile() {
        if (Outils.isActif(jeu.getJoueur().getUsineTextilePetite().getUsineActive())) {
            // recupertaion etat barre de progression usine de textil petite
            double vitesseUsineTextile1 = jeu.getJoueur().getUsineTextilePetite().getVitesseUsine() - (jeu.getJoueur().getUsineTextilePetite().getVitesseUsine() * jeu.getJoueur().getUsineTextilePetite().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineTextilePetite().progressBarStartUsineTextile(1, this.jeu.getJoueur().getUsineTextilePetite().getVitesseUsine(), vitesseUsineTextile1, progressTextile1);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineTextileMoyenne().getUsineActive())) {
            // recupertaion etat barre de progression usine de textil moyenne
            double vitesseUsineTextile2 = jeu.getJoueur().getUsineTextileMoyenne().getVitesseUsine() - (jeu.getJoueur().getUsineTextileMoyenne().getVitesseUsine() * jeu.getJoueur().getUsineTextileMoyenne().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineTextileMoyenne().progressBarStartUsineTextile(1, this.jeu.getJoueur().getUsineTextileMoyenne().getVitesseUsine(), vitesseUsineTextile2, progressTextile2);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineTextileGrande().getUsineActive())) {
            // recupertaion etat barre de progression usine de textil grande
            double vitesseUsineTextile3 = jeu.getJoueur().getUsineTextileGrande().getVitesseUsine() - (jeu.getJoueur().getUsineTextileGrande().getVitesseUsine() * jeu.getJoueur().getUsineTextileGrande().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineTextileGrande().progressBarStartUsineTextile(1, this.jeu.getJoueur().getUsineTextileGrande().getVitesseUsine(), vitesseUsineTextile3, progressTextile3);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineTextileEnorme().getUsineActive())) {
            // recupertaion etat barre de progression usine de textil enorme
            double vitesseUsineTextile4 = jeu.getJoueur().getUsineTextileEnorme().getVitesseUsine() - (jeu.getJoueur().getUsineTextileEnorme().getVitesseUsine() * jeu.getJoueur().getUsineTextileEnorme().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineTextileEnorme().progressBarStartUsineTextile(1, this.jeu.getJoueur().getUsineTextileEnorme().getVitesseUsine(), vitesseUsineTextile4, progressTextile4);
        }
    }
    /**
     * Demarre les usines de jouets lorsqu'elles sont actives
     */
    public void demarrageUsinesJouets() {
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsPetite().getUsineActive())) {
            // recupertaion etat barre de progression usine de jouets petite
            double vitesseUsineJouets1 = jeu.getJoueur().getUsineJouetsPetite().getVitesseUsine() - (jeu.getJoueur().getUsineJouetsPetite().getVitesseUsine() * jeu.getJoueur().getUsineJouetsPetite().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineJouetsPetite().progressBarStartUsineJouets(1, this.jeu.getJoueur().getUsineJouetsPetite().getVitesseUsine(), vitesseUsineJouets1, progressJouets1);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsMoyenne().getUsineActive())) {
            // recupertaion etat barre de progression usine de jouets moyenne
            double vitesseUsineJouets2 = jeu.getJoueur().getUsineJouetsMoyenne().getVitesseUsine() - (jeu.getJoueur().getUsineJouetsMoyenne().getVitesseUsine() * jeu.getJoueur().getUsineJouetsMoyenne().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineJouetsMoyenne().progressBarStartUsineJouets(1, this.jeu.getJoueur().getUsineJouetsMoyenne().getVitesseUsine(), vitesseUsineJouets2, progressJouets2);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsGrande().getUsineActive())) {
            // recupertaion etat barre de progression usine de jouets grande
            double vitesseUsineJouets3 = jeu.getJoueur().getUsineJouetsGrande().getVitesseUsine() - (jeu.getJoueur().getUsineJouetsGrande().getVitesseUsine() * jeu.getJoueur().getUsineJouetsGrande().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineJouetsGrande().progressBarStartUsineJouets(1, this.jeu.getJoueur().getUsineJouetsGrande().getVitesseUsine(), vitesseUsineJouets3, progressJouets3);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsEnorme().getUsineActive())) {
            // recupertaion etat barre de progression usine de jouets enorme
            double vitesseUsineJouets4 = jeu.getJoueur().getUsineJouetsEnorme().getVitesseUsine() - (jeu.getJoueur().getUsineJouetsEnorme().getVitesseUsine() * jeu.getJoueur().getUsineJouetsEnorme().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineJouetsEnorme().progressBarStartUsineJouets(1, this.jeu.getJoueur().getUsineJouetsEnorme().getVitesseUsine(), vitesseUsineJouets4, progressJouets4);
        }
    }
    /**
     * Demarre les usines de jouets lorsqu'elles sont actives
     */
    public void demarrageUsinesPharmaceutique() {
        if (Outils.isActif(jeu.getJoueur().getUsinePharmaceutiquePetite().getUsineActive())) {
            // recupertaion etat barre de progression usine pharmaceutique petite
            double vitesseUsinePharmaceutique1 = jeu.getJoueur().getUsinePharmaceutiquePetite().getVitesseUsine() - (jeu.getJoueur().getUsinePharmaceutiquePetite().getVitesseUsine() * jeu.getJoueur().getUsinePharmaceutiquePetite().getEtatProgressUsine());
            this.jeu.getJoueur().getUsinePharmaceutiquePetite().progressBarStartUsinePharmaceutique(1, this.jeu.getJoueur().getUsinePharmaceutiquePetite().getVitesseUsine(), vitesseUsinePharmaceutique1, progressJouets1);
        }
        if (Outils.isActif(jeu.getJoueur().getUsinePharmaceutiqueMoyenne().getUsineActive())) {
            // recupertaion etat barre de progression usine pharmaceutique moyenne
            double vitesseUsinePharmaceutique2 = jeu.getJoueur().getUsinePharmaceutiqueMoyenne().getVitesseUsine() - (jeu.getJoueur().getUsinePharmaceutiqueMoyenne().getVitesseUsine() * jeu.getJoueur().getUsinePharmaceutiqueMoyenne().getEtatProgressUsine());
            this.jeu.getJoueur().getUsinePharmaceutiqueMoyenne().progressBarStartUsinePharmaceutique(1, this.jeu.getJoueur().getUsinePharmaceutiqueMoyenne().getVitesseUsine(), vitesseUsinePharmaceutique2, progressJouets2);
        }
        if (Outils.isActif(jeu.getJoueur().getUsinePharmaceutiqueGrande().getUsineActive())) {
            // recupertaion etat barre de progression usine pharmaceutique grande
            double vitesseUsinePharmaceutique3 = jeu.getJoueur().getUsinePharmaceutiqueGrande().getVitesseUsine() - (jeu.getJoueur().getUsinePharmaceutiqueGrande().getVitesseUsine() * jeu.getJoueur().getUsinePharmaceutiqueGrande().getEtatProgressUsine());
            this.jeu.getJoueur().getUsinePharmaceutiqueGrande().progressBarStartUsinePharmaceutique(1, this.jeu.getJoueur().getUsinePharmaceutiqueGrande().getVitesseUsine(), vitesseUsinePharmaceutique3, progressJouets3);
        }
        if (Outils.isActif(jeu.getJoueur().getUsinePharmaceutiqueEnorme().getUsineActive())) {
            // recupertaion etat barre de progression usine pharmaceutique enorme
            double vitesseUsinePharmaceutique4 = jeu.getJoueur().getUsinePharmaceutiqueEnorme().getVitesseUsine() - (jeu.getJoueur().getUsinePharmaceutiqueEnorme().getVitesseUsine() * jeu.getJoueur().getUsinePharmaceutiqueEnorme().getEtatProgressUsine());
            this.jeu.getJoueur().getUsinePharmaceutiqueEnorme().progressBarStartUsinePharmaceutique(1, this.jeu.getJoueur().getUsinePharmaceutiqueEnorme().getVitesseUsine(), vitesseUsinePharmaceutique4, progressJouets4);
        }
    }
    /**
     * Sauvegarde de la base de donnees
     */
    public void sauveBdd(){
        System.out.println("fermeture fenetre : Sauvegarde");
        try {
            this.jeu.sauvegardejeu();
            this.jeu.sauvegardeCredit();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
