package com.example.mapetiteentreprise.controllersFx;

import com.example.mapetiteentreprise.jeu.Jeu;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Banquecontroller {

    @FXML
    Button retourMenu;


    public void startFenetre(Jeu jeu) {
        System.out.println("affichage de la banque");

    }

    public void retourGestion(Event event) {
        System.out.println("Retour au menu gestion");
    }


}
