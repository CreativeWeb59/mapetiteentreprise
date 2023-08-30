package com.example.mapetiteentreprise.jeu;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

import java.math.BigDecimal;

public class LivraisonPoidsLourd extends Livraisons{
    private final BigDecimal prixVehicule = BigDecimal.valueOf(110000); // prix du scooter
    private final BigDecimal prixCourse = BigDecimal.valueOf(1100.00); // tarif de chaque course
    private final int nbMaxiVehicules = 300;
    // vitesse des livraisons
    private final int vitesseLivraion = 60;
    private Timeline timelinePoidsLourd;

    public LivraisonPoidsLourd(String nom, int nbVehicules, long nbCourses, double etatProgressLivraison) {
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
     * Methode pour ajouter un service de livraison
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
     * renvoi true si le nombre maximum de poids lourd est atteint
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
     * @param progress    : barre de progress du service de livraison en poids lourd
     */
    public void progressBarStartPoidsLourd(int cycle, double vitesse, double vitesseAjustement, ProgressBar progress) {
        // determine le debut de la barre de progress
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(this.getEtatProgressLivraison());
            etatBarreProgress = this.getEtatProgressLivraison();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelinePoidsLourd = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    // ajoute le nombre de courses en poids lourd necessaires
                    this.majLivraison();
                    System.out.println("Livraison en poids lourd terminé");
                }, new KeyValue(progress.progressProperty(), 1))
        );
        timelinePoidsLourd.setOnFinished(event -> {
            if (cycle == 1) {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartPoidsLourd(cycle - 1, vitesse, vitesse, progress);
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
     * Permet de stopper la timeline
     */
    public void progressBarStop() {
        if (this.timelinePoidsLourd != null) {
            System.out.println("Arret de la barre de progression timelinePoidsLourd");
            this.timelinePoidsLourd.stop();
            this.timelinePoidsLourd = null;
        }
    }
}
