package com.example.mapetiteentreprise.jeu;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

import java.math.BigDecimal;

public class LivraisonCamionette  extends Livraisons{
    private final BigDecimal prixVehicule = BigDecimal.valueOf(20000); // prix du scooter
    private final BigDecimal prixCourse = BigDecimal.valueOf(200.00); // tarif de chaque course
    // prix des vehicules
    private final int nbMaxiVehicules = 300;
    // vitesse des livraisons
    private final int vitesseLivraion = 60;
    private Timeline timelineCamionette;

    public LivraisonCamionette(String nom, int nbVehicules, long nbCourses, double etatProgressLivraison) {
        super(nom, nbVehicules, nbCourses, etatProgressLivraison);
    }

    public BigDecimal getPrixVehicule() {
        return prixVehicule;
    }

    public BigDecimal getPrixCourse() {
        return prixCourse;
    }

    public int getNbMaxiVehicules() {
        return nbMaxiVehicules;
    }

    public int getVitesseLivraion() {
        return vitesseLivraion;
    }
    /**
     * Methode pour ajouter un distributeur
     */
    public void ajoutServiceDeLivraison(){
        this.setNbVehicules(this.getNbVehicules() + 1);
    }

    public int getNbMaxiServiceDeLivraison() {
        return nbMaxiVehicules;
    }

    /**
     * initialise le nombre de vehicules en cours ainsi que le nombre de vehicules maximum
     */
    public String setNbVehicule() {
        return this.getNbVehicules() + " / " + this.getNbMaxiVehicules();
    }

    /**
     * renvoi true si le nombre maximum de camionette est atteint
     * @return
     */
    public boolean isMaxiNbVehicules(){
        return this.getNbVehicules() >= this.getNbMaxiVehicules();
    }
    /**
     * Barre de progression pour comptabiliser les boissons chaudes
     *
     * @param cycle         : 0 pour cycle infini
     * @param vitesse       : vitesse du distributeur en secondes
     * @param progress    : barre de progress de la livraison en camionette
     */
    public void progressBarStartCamionette(int cycle, double vitesse, double vitesseAjustement, ProgressBar progress) {
        // determine le debut de la barre de progress
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(this.getEtatProgressLivraison());
            etatBarreProgress = this.getEtatProgressLivraison();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineCamionette = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    // ajoute le nombre de courses en camionette necessaires
                    this.majLivraison();
                    System.out.println("Livraison camionette terminé");
                }, new KeyValue(progress.progressProperty(), 1))
        );
        timelineCamionette.setOnFinished(event -> {
            if (cycle == 1) {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartCamionette(cycle - 1, vitesse, vitesse, progress);
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
     * Permet de stopper la timeline
     */
    public void progressBarStop() {
        if (this.timelineCamionette != null) {
            System.out.println("Arret de la barre de progression timelineCamionette");
            this.timelineCamionette.stop();
            this.timelineCamionette = null;
        }
    }
}
