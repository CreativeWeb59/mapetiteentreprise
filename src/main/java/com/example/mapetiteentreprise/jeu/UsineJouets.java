package com.example.mapetiteentreprise.jeu;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

import java.math.BigDecimal;

public class UsineJouets extends Usines{
    private Timeline timelineUsineJouets;
    public UsineJouets(String nom, int usineActive, int nbUsines, long nbMarchandises, double etatProgressUsine) {
        super(nom, usineActive, nbUsines, nbMarchandises, etatProgressUsine);
    }
    /**
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine
     *
     * @param cycle         : 0 pour cycle infini
     * @param vitesse       : vitesse de l'usine en secondes
     * @param progress    : barre de progress de la fabrication d'une marchandise
     */
    public void progressBarStartUsineJouets(int cycle, double vitesse, double vitesseAjustement, ProgressBar progress) {
        // determine le debut de la barre de progress
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(this.getEtatProgressUsine());
            etatBarreProgress = this.getEtatProgressUsine();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineUsineJouets = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    // ajoute le nombre de marchandises fabriquées par l'usine de jouets
                    this.majUsine();
                    System.out.println("usine jouets terminés");
                }, new KeyValue(progress.progressProperty(), 1))
        );
        timelineUsineJouets.setOnFinished(event -> {
            if (cycle == 1) {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsineJouets(cycle - 1, vitesse, vitesse, progress);
            }
        });
        if (cycle == 0) {
            timelineUsineJouets.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineUsineJouets.setCycleCount(cycle);
        }
        timelineUsineJouets.play();
    }

    /**
     * Permet de stopper la timeline
     */
    public void progressBarStop() {
        if (this.timelineUsineJouets != null) {
            System.out.println("Arret de la barre de progression timelineUsineJouets");
            this.timelineUsineJouets.stop();
            this.timelineUsineJouets = null;
        }
    }

    /**
     * Maj des valeurs de la petite usine de jouets
     * maxUsines, prix d'achat et prix de fabrication
     */
    public void setUsinePetite(){
        this.setNbMaxiUsine(100);
        this.setPrixUsine(BigDecimal.valueOf(2000000));
        this.setPrixFabrication(BigDecimal.valueOf(20000));
    }
    /**
     * Maj des valeurs de la moyenne usine de jouets
     * maxUsines, prix d'achat et prix de fabrication
     */
    public void setUsineMoyenne(){
        this.setNbMaxiUsine(100);
        this.setPrixUsine(BigDecimal.valueOf(2500000));
        this.setPrixFabrication(BigDecimal.valueOf(25000));
    }
    /**
     * Maj des valeurs de la grande usine de jouets
     * maxUsines, prix d'achat et prix de fabrication
     */
    public void setUsineGrande(){
        this.setNbMaxiUsine(100);
        this.setPrixUsine(BigDecimal.valueOf(3000000));
        this.setPrixFabrication(BigDecimal.valueOf(30000));
    }
    /**
     * Maj des valeurs de l'énorme usine de jouets
     * maxUsines, prix d'achat et prix de fabrication
     */
    public void setUsineEnorme(){
        this.setNbMaxiUsine(100);
        this.setPrixUsine(BigDecimal.valueOf(4000000));
        this.setPrixFabrication(BigDecimal.valueOf(40000));
    }
}
