package com.example.mapetiteentreprise.controllersFx;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Banquecontroller {

    @FXML
    Button retourMenu;



    public void retourGestion(Event event){
        System.out.println("Retour au menu gestion");
    }
}
