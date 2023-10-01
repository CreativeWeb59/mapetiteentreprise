package com.example.mapetiteentreprise.jeu;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

import java.math.BigDecimal;

public class UsinePharmaceutique extends Usines{
    Timeline timelinePharmaceutique;

    public UsinePharmaceutique(String nom, int usineActive, int nbUsines, long nbMarchandises, double etatProgressUsine) {
        super(nom, usineActive, nbUsines, nbMarchandises, etatProgressUsine);
    }
    /**
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine
     *
     * @param cycle         : 0 pour cycle infini
     * @param vitesse       : vitesse de l'usine en secondes
     * @param progress    : barre de progress de la fabrication d'une marchandise
     */
    public void progressBarStartUsinePharmaceutique(int cycle, double vitesse, double vitesseAjustement, ProgressBar progress) {
        // determine le debut de la barre de progress
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(this.getEtatProgressUsine());
            etatBarreProgress = this.getEtatProgressUsine();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelinePharmaceutique = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    // ajoute le nombre de marchandises fabriquées par l'usine pharmaceutique
                    this.majUsine();
                    System.out.println("usine phamarceutique terminée");
                }, new KeyValue(progress.progressProperty(), 1))
        );
        timelinePharmaceutique.setOnFinished(event -> {
            if (cycle == 1) {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsinePharmaceutique(cycle - 1, vitesse, vitesse, progress);
            }
        });
        if (cycle == 0) {
            timelinePharmaceutique.setCycleCount(Animation.INDEFINITE);
        } else {
            timelinePharmaceutique.setCycleCount(cycle);
        }
        timelinePharmaceutique.play();
    }

    /**
     * Permet de stopper la timeline
     */
    public void progressBarStop() {
        if (this.timelinePharmaceutique != null) {
            System.out.println("Arret de la barre de progression timelinePharmaceutique");
            this.timelinePharmaceutique.stop();
            this.timelinePharmaceutique = null;
        }
    }

    /**
     * Maj des valeurs de la petite usine pharmaceutique
     * maxUsines, prix d'achat et prix de fabrication
     */
    public void setUsinePetite(){
        this.setNbMaxiUsine(100);
        this.setPrixUsine(BigDecimal.valueOf(8000000));
        this.setPrixFabrication(BigDecimal.valueOf(80000));
    }
    /**
     * Maj des valeurs de la moyenne usine de jouets
     * maxUsines, prix d'achat et prix de fabrication
     */
    public void setUsineMoyenne(){
        this.setNbMaxiUsine(100);
        this.setPrixUsine(BigDecimal.valueOf(9000000));
        this.setPrixFabrication(BigDecimal.valueOf(90000));
    }
    /**
     * Maj des valeurs de la grande usine de jouets
     * maxUsines, prix d'achat et prix de fabrication
     */
    public void setUsineGrande(){
        this.setNbMaxiUsine(100);
        this.setPrixUsine(BigDecimal.valueOf(10000000));
        this.setPrixFabrication(BigDecimal.valueOf(100000));
    }
    /**
     * Maj des valeurs de l'énorme usine de jouets
     * maxUsines, prix d'achat et prix de fabrication
     */
    public void setUsineEnorme(){
        this.setNbMaxiUsine(100);
        this.setPrixUsine(BigDecimal.valueOf(20000000));
        this.setPrixFabrication(BigDecimal.valueOf(200000));
    }
}
