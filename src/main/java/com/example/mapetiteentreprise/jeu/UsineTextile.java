package com.example.mapetiteentreprise.jeu;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

import java.math.BigDecimal;

public class UsineTextile extends Usines{
    private Timeline timelineUsineTextile;

    public UsineTextile(String nom, int usineActive, int nbUsines, long nbMarchandises, double etatProgressUsine) {
        super(nom, usineActive, nbUsines, nbMarchandises, etatProgressUsine);
    }

    public Timeline getTimelineUsineTextile() {
        return timelineUsineTextile;
    }

    public void setTimelineUsineTextile(Timeline timelineUsineTextile) {
        this.timelineUsineTextile = timelineUsineTextile;
    }


    /**
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine
     *
     * @param cycle         : 0 pour cycle infini
     * @param vitesse       : vitesse de l'usine en secondes
     * @param progress    : barre de progress de la fabrication d'une marchandise
     */
    public void progressBarStartUsineTextile(int cycle, double vitesse, double vitesseAjustement, ProgressBar progress) {
        // determine le debut de la barre de progress
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(this.getEtatProgressUsine());
            etatBarreProgress = this.getEtatProgressUsine();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineUsineTextile = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    // ajoute le nombre de marchandises fabriquées par l'usine
                    this.majUsine();
                    System.out.println("usine textile terminés");
                }, new KeyValue(progress.progressProperty(), 1))
        );
        timelineUsineTextile.setOnFinished(event -> {
            if (cycle == 1) {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsineTextile(cycle - 1, vitesse, vitesse, progress);
            }
        });
        if (cycle == 0) {
            timelineUsineTextile.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineUsineTextile.setCycleCount(cycle);
        }
        timelineUsineTextile.play();
    }

    /**
     * Permet de stopper la timeline
     */
    public void progressBarStop() {
        if (this.timelineUsineTextile != null) {
            System.out.println("Arret de la barre de progression timelineUsineTextile");
            this.timelineUsineTextile.stop();
            this.timelineUsineTextile = null;
        }
    }

    /**
     * Maj des valeurs de la petite usine de textile
     * maxUsines, prix d'achat et prix de fabrication
     */
    public void setUsinePetite(){
        this.setNbMaxiUsine(50);
        this.setPrixUsine(BigDecimal.valueOf(500000));
        this.setPrixFabrication(BigDecimal.valueOf(5000));
    }
    /**
     * Maj des valeurs de la moyenne usine de textile
     * maxUsines, prix d'achat et prix de fabrication
     */
    public void setUsineMoyenne(){
        this.setNbMaxiUsine(50);
        this.setPrixUsine(BigDecimal.valueOf(750000));
        this.setPrixFabrication(BigDecimal.valueOf(7500));
    }
    /**
     * Maj des valeurs de la grande usine de textile
     * maxUsines, prix d'achat et prix de fabrication
     */
    public void setUsineGrande(){
        this.setNbMaxiUsine(50);
        this.setPrixUsine(BigDecimal.valueOf(1000000));
        this.setPrixFabrication(BigDecimal.valueOf(10000));
    }
    /**
     * Maj des valeurs de l'énorme usine de textile
     * maxUsines, prix d'achat et prix de fabrication
     */
    public void setUsineEnorme(){
        this.setNbMaxiUsine(50);
        this.setPrixUsine(BigDecimal.valueOf(1500000));
        this.setPrixFabrication(BigDecimal.valueOf(15000));
    }
}
