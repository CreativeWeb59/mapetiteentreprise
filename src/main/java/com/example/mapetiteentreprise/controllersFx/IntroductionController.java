package com.example.mapetiteentreprise.controllersFx;

import com.example.mapetiteentreprise.Main;
import com.example.mapetiteentreprise.bdd.ConnectionBdd;
import com.example.mapetiteentreprise.bdd.Credits;
import com.example.mapetiteentreprise.bdd.CreditsService;
import com.example.mapetiteentreprise.jeu.CreditEnCours;
import com.example.mapetiteentreprise.jeu.Jeu;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class IntroductionController {
    @FXML
    private Pane pane1, pane2, pane3, pane4, pane5, pane6;
    @FXML
    private Button btnAcheter;

    private Timeline timelinePanes;
    final private double paneOpacity = 0.2;
    // Valeurs du prêt par defaut
    private BigDecimal montantPret = BigDecimal.valueOf(1200);
    private BigDecimal coutPret = BigDecimal.valueOf(1296);
    private BigDecimal montantRembourse = BigDecimal.valueOf(0);
    private BigDecimal mensualite = BigDecimal.valueOf(18);
    private int nbMMensualite = 72;
    private int cycleMensualite = 18000; // 1 jour = 600 secondes, // 30 jours = 18000 secondes
    private int termine = 0; // 0 credit en cours, 1 credit termine
    private Jeu jeu;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private ConnectionBdd connectionBdd = new ConnectionBdd();
    private Credits credits = new Credits();
    private CreditsService creditsService;
    public void debutIntro(Jeu jeu){
        System.out.println("Lance la page d'indroduction");
        animationPanes();
        this.jeu = jeu;
        System.out.println("Le jeu : " + jeu);
    }

    /**
     * Timeline Animation de la page
     */
    public void animationPanes() {
        Timeline timelinePanes = new Timeline();

        // Boucle 1 -> affiche sprite 2 avec effet opacite augmenté
        KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(0), event -> affichePane(pane2));
        KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(1), event -> opacity(pane2));
        KeyFrame keyFrame3 = new KeyFrame(Duration.seconds(2), event -> opacity(pane2));
        KeyFrame keyFrame4 = new KeyFrame(Duration.seconds(3), event -> opacity(pane2));
        KeyFrame keyFrame5 = new KeyFrame(Duration.seconds(4), event -> opacity(pane2));
        KeyFrame keyFrame6 = new KeyFrame(Duration.seconds(5), event -> opacity(pane2));

        // Boucle 2 -> affiche sprite 3 avec effet opacite augmenté
        KeyFrame keyFrame7 = new KeyFrame(Duration.seconds(6), event -> affichePane(pane3));
        KeyFrame keyFrame8 = new KeyFrame(Duration.seconds(7), event -> opacity(pane3));
        KeyFrame keyFrame9 = new KeyFrame(Duration.seconds(8), event -> opacity(pane3));
        KeyFrame keyFrame10 = new KeyFrame(Duration.seconds(9), event -> opacity(pane3));
        KeyFrame keyFrame11 = new KeyFrame(Duration.seconds(10), event -> opacity(pane3));
        KeyFrame keyFrame12 = new KeyFrame(Duration.seconds(11), event -> opacity(pane3));

        // Boucle 3 -> affiche sprite 4 avec effet opacite augmenté
        KeyFrame keyFrame13 = new KeyFrame(Duration.seconds(12), event -> affichePane(pane4));
        KeyFrame keyFrame14 = new KeyFrame(Duration.seconds(13), event -> opacity(pane4));
        KeyFrame keyFrame15 = new KeyFrame(Duration.seconds(14), event -> opacity(pane4));
        KeyFrame keyFrame16 = new KeyFrame(Duration.seconds(15), event -> opacity(pane4));
        KeyFrame keyFrame17 = new KeyFrame(Duration.seconds(16), event -> opacity(pane4));
        KeyFrame keyFrame18 = new KeyFrame(Duration.seconds(17), event -> opacity(pane4));

        // Boucle 4 -> affiche sprite 5 avec effet opacite augmenté
        KeyFrame keyFrame19 = new KeyFrame(Duration.seconds(18), event -> affichePane(pane5));
        KeyFrame keyFrame20 = new KeyFrame(Duration.seconds(19), event -> opacity(pane5));
        KeyFrame keyFrame21 = new KeyFrame(Duration.seconds(20), event -> opacity(pane5));
        KeyFrame keyFrame22 = new KeyFrame(Duration.seconds(21), event -> opacity(pane5));
        KeyFrame keyFrame23 = new KeyFrame(Duration.seconds(22), event -> opacity(pane5));
        KeyFrame keyFrame24 = new KeyFrame(Duration.seconds(23), event -> opacity(pane5));

        // Boucle 5 -> affiche sprite 6 avec effet opacite augmenté
        KeyFrame keyFrame25 = new KeyFrame(Duration.seconds(24), event -> affichePane(pane6));
        KeyFrame keyFrame26 = new KeyFrame(Duration.seconds(25), event -> opacity(pane6));
        KeyFrame keyFrame27 = new KeyFrame(Duration.seconds(26), event -> opacity(pane6));
        KeyFrame keyFrame28 = new KeyFrame(Duration.seconds(27), event -> opacity(pane6));
        KeyFrame keyFrame29 = new KeyFrame(Duration.seconds(28), event -> opacity(pane6));
        KeyFrame keyFrame30 = new KeyFrame(Duration.seconds(29), event -> opacity(pane6));

        timelinePanes.getKeyFrames().addAll(keyFrame1, keyFrame2, keyFrame3, keyFrame4, keyFrame5, keyFrame6, keyFrame7, keyFrame8, keyFrame9, keyFrame10,
                keyFrame11, keyFrame12, keyFrame13, keyFrame14, keyFrame15, keyFrame16, keyFrame17, keyFrame18, keyFrame19, keyFrame20,
                keyFrame21, keyFrame22, keyFrame23, keyFrame24, keyFrame25, keyFrame26, keyFrame27, keyFrame28, keyFrame29);

        timelinePanes.setCycleCount(1);

        // Démarrer la Timeline
        timelinePanes.play();
    }

    /**
     * affiche le pane
     * @param pane
     */
    public void affichePane(Pane pane){
        pane.setVisible(true);
    }

    /**
     * Increment l'opacite tant qu'elle n'est pas superieure à 1
     * @param pane
     */
    public void opacity(Pane pane){
        if((pane.getOpacity() + this.paneOpacity) <= 1){
            pane.setOpacity(pane.getOpacity() + this.paneOpacity);
        }
    }

    /**
     * Bouton pour faire le prêt et acheter la ferme
     * Renvoi vers la gestion de la ferme
     * @param event
     */
    public void acheterFerme(Event event){
        // creation du prêt
        LocalDateTime dateEnCours = LocalDateTime.now();
        CreditEnCours creditEnCours = new CreditEnCours(montantPret, coutPret, montantRembourse, mensualite, nbMMensualite, cycleMensualite, termine, dateEnCours, dateEnCours);

        // ajout du prêt en bdd
        creationBddPret();

        // ajout du credit dans la classe joueur
        jeu.getJoueur().setCreditEnCours(creditEnCours);
        System.out.println("Le jeu avec prêt : " + jeu);

        // modification du nombre de poules
        this.jeu.getJoueur().getFerme().setNbPoules(10);

        // switch vers la ferme
        System.out.println("Lancement du jeu, déblocage de la ferme");
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("ferme.fxml"));
            root = loader.load();
            FermeController fermeController = loader.getController();
            fermeController.nouveau(jeu);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
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
    public void creationBddPret(){
        LocalDateTime dateEnCours = LocalDateTime.now();
        credits = new Credits(jeu.getJoueur().getPseudo(), montantPret, coutPret, montantRembourse, mensualite, nbMMensualite, cycleMensualite, termine, dateEnCours, dateEnCours);

        connectionBdd.connect();
        creditsService = new CreditsService(connectionBdd);
        try {
            creditsService.addCredit(credits);
        } catch (Exception e){
            System.out.println("Erreur de creation du crédit");
        }
        connectionBdd.close();
    }
}
