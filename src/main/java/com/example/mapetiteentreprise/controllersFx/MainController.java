package com.example.mapetiteentreprise.controllersFx;

import com.example.mapetiteentreprise.Main;
import com.example.mapetiteentreprise.actions.Outils;
import com.example.mapetiteentreprise.actions.Resultat;
import com.example.mapetiteentreprise.bdd.*;
import com.example.mapetiteentreprise.jeu.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class MainController {
    @FXML
    private Label pseudoLabel, labelErreur;
    @FXML
    private TextField pseudoTextField;
    @FXML
    private ChoiceBox<String> choixPartie;
    @FXML
    private Button btnNew, btnContinuer, btnSupprimer;
    private String pseudo;
    private String partieSauvegardee;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private ConnectionBdd connectionBdd = new ConnectionBdd();
    private JoueursService joueursService = new JoueursService();
    private SauvegardeService sauvegardeService;
    private ParametresService parametresService;
    private Sauvegarde sauvegarde;
    private Parametres parametres = new Parametres();
    private Jeu jeu;
    private Ferme ferme;
    private BoissonsChaudes boissonsChaudes;
    private BoissonsFraiches boissonsFraiches;
    private Sandwichs sandwichs;
    private Confiseries confiseries;

    /**
     * Execute au lancement du programme
     * permet de créer les tables necessaires si elles n'existent pas
     * Initialise le contenu de la case à options : liste des sauvegardes
     */
    public void onLoad() throws SQLException {
        // cache le label pour la gestion des erreurs
        this.labelErreur.setVisible(false);

        // creation de la connection à la Bdd
        // cree les tables si elles n'existent pas
        this.createDonnees();

        // construction de la liste des sauvegardes
        this.listeDesSsauvegardes();
    }

    /**
     * Cree une nouvelle partie
     *
     * @param event
     * @throws SQLException
     */
    public void newGame(ActionEvent event) throws SQLException {
        String username = pseudoTextField.getText();

        // Serie de tests sur la validité du pseudo : taille, caractères...
        // renvoi le pseudo modifité (sans espaces...)
        // permet de recuperer l'erreur

        Resultat resultat = Outils.testPseudo(username, 3, 20);
        this.pseudo = resultat.getChaine();
        // si chaine ok on teste l'existence du pseudo en bdd
        if (resultat.getValeurBool()) {
            // teste si le joueur existe en bdd
            connectionBdd.connect();
            if (!sauvegardeService.existPseudo(this.pseudo)) {
                // cree le joueur en bdd
                // et cree l'instance joueur
                this.creerJoueur();
                // ouvre la page de gestion du jeu
                this.switchPageGestion(event);
            } else {
                // Le joueur existe on bloque le lancement du jeu
                this.labelErreur.setText("Le joueur existe déja");
                afficherMessageTemporaire(this.labelErreur, "Le joueur existe déja !", 3000);
            }
            connectionBdd.close();
        } else {
//            this.labelErreur.setText(resultat.getChaine());
            afficherMessageTemporaire(this.labelErreur, resultat.getChaine(), 3000);
        }
    }

    /**
     * permet de lancer une partie sauvegardee
     * cree une instance d'un joueur récupéré à partir de la liste
     * avec toutes ses infos
     *
     * @param event
     */
    public void selectionPartie(ActionEvent event) throws SQLException {
        // chargement du joueur
        // ouverture fenetre suivante
        String userName = choixPartie.getValue();
        // creation de la connection à la Bdd
        connectionBdd.connect();
        // recupere la sauvegarde
        this.sauvegarde = sauvegardeService.getJoueurbyPseudo(userName);
        System.out.println("distributeur actif Co : " + this.sauvegarde.getDistributeurCoActive());
        System.out.println("distributeur actif Sa : " + this.sauvegarde.getDistributeurSaActive());
        // renseigne les parametres (tarifs...) dans les activites
        this.creerActivites(false);
        // creation de la classe joueur
        Joueur joueur = new Joueur(userName, sauvegarde.getArgent(), this.ferme, this.boissonsChaudes, this.boissonsFraiches, this.sandwichs, this.confiseries,
                this.sauvegarde.getFermeActive(), this.sauvegarde.getDistributeursActive(), this.sauvegarde.getDistributeurBCActive(),
                this.sauvegarde.getDistributeurBFActive(), this.sauvegarde.getDistributeurCoActive(), this.sauvegarde.getDistributeurSaActive());


        this.jeu = new Jeu(joueur, sauvegarde, parametres);
        connectionBdd.close();
        System.out.println("Le jeu complet içi : " + this.jeu);
        this.switchPageGestion(event);
    }

    /**
     * Supprime la partie selectionnee en bdd
     *
     * @param event
     */
    public void suprPartie(ActionEvent event) {
        String userName = choixPartie.getValue();
        try {
            connectionBdd.connect();
            this.sauvegardeService.suprSauvegarde(userName);

            // refresh de la liste des sauvegardes
            listeDesSsauvegardes();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void switchPageGestion(Event event) {

        // Tester le pseudo
        // Test nombre de caractères
        // Test si non vide

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("gestion.fxml"));
            root = loader.load();
            GestionController gestionController = loader.getController();
            gestionController.debutJeu(jeu);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            // Permet de récupérer le gestionnaire d'événements pour la fermeture de la fenêtre
            stage.setOnCloseRequest(gestionController::onWindowClose);
            stage.show();
            stage.centerOnScreen();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void exitJeu(ActionEvent event) {
        // Code pour quitter l'application
        Platform.exit();
    }

    public void choixSauvegarde(ActionEvent event) {
        this.partieSauvegardee = choixPartie.getValue();
    }


    /**
     * Crée le joueur s'il n'existe pas dans la base
     *
     * @param
     */
    public void creerJoueur() throws SQLException {
        connectionBdd.connect();
        System.out.println("Jeu lancé, Bienvenue " + this.pseudo);
        System.out.println("Creation du joueur");

        LocalDateTime dateDeco = LocalDateTime.now();

        // creation de la sauvegarde en bdd
        this.sauvegarde = new Sauvegarde(this.pseudo, parametres.getArgentDepart(), parametres.getNbPoules(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                dateDeco,0,0,0,0, 0,0,0,0, 0, 0, 0, 0);
        sauvegardeService.addJoueur(sauvegarde);

        this.creerActivites(true);

        // creation de la classe joueur
        Joueur joueur = new Joueur(this.pseudo, parametres.getArgentDepart(), this.ferme, this.boissonsChaudes, this.boissonsFraiches, this.sandwichs, this.confiseries,
                this.parametres.getFermeActive(), this.parametres.getDistributeursActive(), this.parametres.getDistributeurBCActive(),
                this.parametres.getDistributeurBFActive(), this.parametres.getDistributeurSaActive(), this.parametres.getDistributeurCoActive());

        // creation de la partie
        this.jeu = new Jeu(joueur, sauvegarde, parametres);
    }

    /**
     * Cree les activités :
     * nouveau jeu = true : recupere les donnees par defaut des parametres
     * nouveau jeu = false : recupere les donnees depuis la sauvegarde
     *
     * @param nouveauJeu
     */
    public void creerActivites(Boolean nouveauJeu) {
        // Creation des activites

        if (nouveauJeu) {
            // creation de la ferme
            ferme = new Ferme(parametres.getNbPoules(), 0, LocalDateTime.now());
            // creation des distributeurs
            boissonsChaudes = new BoissonsChaudes(parametres.getNbDistributeurBC(), 0, 0, LocalDateTime.now());
            boissonsFraiches = new BoissonsFraiches(parametres.getNbDistributeurBF(), 0, 0, LocalDateTime.now());
            confiseries = new Confiseries(parametres.getNbDistributeurCo(), 0, 0, LocalDateTime.now());
            sandwichs = new Sandwichs(parametres.getNbDistributeurSa(), 0, 0, LocalDateTime.now());
        } else {
            // recuperation des donnees de la ferme
            ferme = new Ferme(sauvegarde.getNbPoules(), sauvegarde.getNbOeufs(), sauvegarde.getDateDeco());
            // recuperation des donnees des distributeurs
            boissonsChaudes = new BoissonsChaudes(sauvegarde.getNbDistributeurBC(), sauvegarde.getNbMarchandisesBC(), sauvegarde.getEtatProgressBC(), sauvegarde.getDateDeco());
            boissonsFraiches = new BoissonsFraiches(sauvegarde.getNbDistributeurBF(), sauvegarde.getNbMarchandisesBF(), sauvegarde.getEtatProgressBF(), sauvegarde.getDateDeco());
            confiseries = new Confiseries(sauvegarde.getNbDistributeurCo(), sauvegarde.getNbMarchandisesCo(), sauvegarde.getEtatProgressCo(), sauvegarde.getDateDeco());
            sandwichs = new Sandwichs(sauvegarde.getNbDistributeurSa(), sauvegarde.getNbMarchandisesSa(), sauvegarde.getEtatProgressSa(), sauvegarde.getDateDeco());
        }
        System.out.println("Confiserie :" + confiseries);
    }

    /**
     * Affichage du message d'erreur pendant un certain laps de temps donnée
     *
     * @param label
     * @param message
     * @param delai
     */
    private void afficherMessageTemporaire(Label label, String message, int delai) {
        // affiche le label pour la gestion des erreurs
        this.labelErreur.setVisible(true);

        label.setText(message); // Afficher le message dans le label

        // Créer une PauseTransition pour la durée spécifiée
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(delai));
        pauseTransition.setOnFinished(event -> {
            label.setText(""); // Effacer le contenu du label après le délai
            this.labelErreur.setVisible(false); // cache a nouveau le label d'erreur
        });

        pauseTransition.play(); // Démarrer la temporisation
    }

    /**
     * Teste si les tables sauvegarde et parametres existent
     * sinon les cree
     * Insertion des donnees dans les paramètres
     */
    public void createDonnees() throws SQLException {
        connectionBdd.connect();
        System.out.println("Connection");
        if (!connectionBdd.isModel("parametres")) {
            connectionBdd.createModelParametres();
            parametresService = new ParametresService(connectionBdd);
            System.out.println("parametres : " + parametres);
            parametresService.addParametresDefaut(parametres);
        }
        if (!connectionBdd.isModel("sauvegarde")) {
            connectionBdd.createModelSauvegarde();
        }
        connectionBdd.close();
    }

    public void listeDesSsauvegardes() {
        // recuperation de la liste des joueurs
//        ConnectionBdd connexion = new ConnectionBdd();
        connectionBdd.connect();
        sauvegardeService = new SauvegardeService(connectionBdd);
        List<String> tousLesJoueurs = sauvegardeService.listePseudos();
        connectionBdd.close();

        // remplissage du textField
        // uniquement si des pseudos existent dans la base
        if (tousLesJoueurs.size() > 0) {
            ObservableList<String> joueursPseudosList = FXCollections.observableArrayList(tousLesJoueurs);
            choixPartie.setItems(joueursPseudosList);
            choixPartie.setValue(tousLesJoueurs.get(0));
            choixPartie.setOnAction(this::choixSauvegarde);
            this.partieSauvegardee = choixPartie.getValue();
        } else {
            this.btnContinuer.setDisable(true);
            this.btnSupprimer.setDisable(true);
        }
    }
}
