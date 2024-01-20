package com.example.monPetitBassin.controllersFx;

import javafx.stage.WindowEvent;

import java.sql.SQLException;

public class FrameJeuController {
    public void onLoad() throws SQLException {
        System.out.println("Chargement du jeu");
    }



    /**
     * Action a executer lors de la fermeture de la fentre avec la croix : sauvegarde
     *
     * @param event
     */

    public void onWindowClose(WindowEvent event) {
        // fermeture des barres, enregistrement + stop et sauvegarde date deco

        // sauvegarde bdd
//        sauveBdd();

        // Sauvegarde de la base de donnees
//        System.out.println("fermeture fenetre : Sauvegarde");
//        try {
//            this.jeu.sauvegardejeu();
//            this.jeu.sauvegardeCredit();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
    }
}
