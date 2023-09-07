package com.example.mapetiteentreprise.jeu;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

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


}
