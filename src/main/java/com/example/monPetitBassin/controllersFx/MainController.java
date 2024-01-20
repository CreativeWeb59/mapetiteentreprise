package com.example.monPetitBassin.controllersFx;


import com.example.monPetitBassin.Main;
import com.example.monPetitBassin.actions.Factory;
import com.example.monPetitBassin.jeu.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class MainController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Jeu jeu;
    private Player player;
    private ArrayList<Bassin> bassins;
    private ArrayList<TypePoisson> listTypePoissons;
    private ArrayList<Poisson> listPoissons;

    /**
     * Execute au lancement du programme
     * permet de créer les tables necessaires si elles n'existent pas
     * Initialise le contenu de la case à options : liste des sauvegardes
     */
    public void onLoad() throws SQLException {
        createPoissons();
        createBassins();
        createJeu();
    }

    /**
     * Initialisation des types de poissons et poissons
     */
    public void createPoissons(){
        listTypePoissons = Factory.listTypePoissons();
        listPoissons = new ArrayList<>();
        listPoissons.addAll(Factory.listePoissonsEauFroide(listTypePoissons.get(0)));
        listPoissons.addAll(Factory.listePoissonsEauChaude(listTypePoissons.get(1)));
        listPoissons.addAll(Factory.listePoissonsEauSalee(listTypePoissons.get(2)));
    }

    public void createBassins(){
        bassins = Factory.createBassinsDefaut();
    }
    /**
     * creation des instances du jeu, joueur et bassins
     */
    public void createJeu(){
        LocalDateTime dateEnCours = LocalDateTime.now();

        player = new Player("Delphine", BigDecimal.valueOf(100), 100, dateEnCours, dateEnCours);
        jeu = new Jeu(player, bassins);
        System.out.println(jeu);
        System.out.println(listTypePoissons);
        System.out.println(listPoissons);
    }
    /**
     * Permet de lancer la partie en ouvrant la fenetre de jeu principale
     */
    public void jouer(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("frameJeu.fxml"));
            root = loader.load();
            FrameJeuController frameJeuController = loader.getController();
            frameJeuController.onLoad();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            // Permet de récupérer le gestionnaire d'événements pour la fermeture de la fenêtre
            stage.setOnCloseRequest(frameJeuController::onWindowClose);
            stage.show();
            stage.centerOnScreen();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
