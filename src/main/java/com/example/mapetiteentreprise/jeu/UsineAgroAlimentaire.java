package com.example.mapetiteentreprise.jeu;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

import java.math.BigDecimal;

public class UsineAgroAlimentaire extends Usines{
    private Timeline timelineUsineAgroAlimentaire;

    public UsineAgroAlimentaire(String nom, int usineActive, int nbUsines, long nbMarchandises, double etatProgressUsine) {
        super(nom, usineActive, nbUsines, nbMarchandises, etatProgressUsine);
    }
    /**
     * Barre de progression pour comptabiliser le stock de marchandises dans l'usine
     *
     * @param cycle         : 0 pour cycle infini
     * @param vitesse       : vitesse de l'usine en secondes
     * @param progress    : barre de progress de la fabrication d'une marchandise
     */
    public void progressBarStartUsineAgroAlimentaire(int cycle, double vitesse, double vitesseAjustement, ProgressBar progress) {
        // determine le debut de la barre de progress
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(this.getEtatProgressUsine());
            etatBarreProgress = this.getEtatProgressUsine();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineUsineAgroAlimentaire = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    // ajoute le nombre de marchandises fabriquées par l'usine agroAlimentaire
                    this.majUsine();
                    System.out.println("usine agroAlimentaire terminée");
                }, new KeyValue(progress.progressProperty(), 1))
        );
        timelineUsineAgroAlimentaire.setOnFinished(event -> {
            if (cycle == 1) {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartUsineAgroAlimentaire(cycle - 1, vitesse, vitesse, progress);
            }
        });
        if (cycle == 0) {
            timelineUsineAgroAlimentaire.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineUsineAgroAlimentaire.setCycleCount(cycle);
        }
        timelineUsineAgroAlimentaire.play();
    }

    /**
     * Permet de stopper la timeline
     */
    public void progressBarStop() {
        if (this.timelineUsineAgroAlimentaire != null) {
            System.out.println("Arret de la barre de progression timelineUsineAgroAlimentaire");
            this.timelineUsineAgroAlimentaire.stop();
            this.timelineUsineAgroAlimentaire = null;
        }
    }

    /**
     * Maj des valeurs de la petite usine agroAlimentaire
     * maxUsines, prix d'achat et prix de fabrication
     */
    public void setUsinePetite(){
        this.setNbMaxiUsine(100);
        this.setPrixUsine(BigDecimal.valueOf(5000000));
        this.setPrixFabrication(BigDecimal.valueOf(50000));
    }
    /**
     * Maj des valeurs de la moyenne usine agroAlimentaire
     * maxUsines, prix d'achat et prix de fabrication
     */
    public void setUsineMoyenne(){
        this.setNbMaxiUsine(100);
        this.setPrixUsine(BigDecimal.valueOf(5500000));
        this.setPrixFabrication(BigDecimal.valueOf(55000));
    }
    /**
     * Maj des valeurs de la grande usine agroAlimentaire
     * maxUsines, prix d'achat et prix de fabrication
     */
    public void setUsineGrande(){
        this.setNbMaxiUsine(100);
        this.setPrixUsine(BigDecimal.valueOf(6000000));
        this.setPrixFabrication(BigDecimal.valueOf(60000));
    }
    /**
     * Maj des valeurs de l'énorme usine agroAlimentaire
     * maxUsines, prix d'achat et prix de fabrication
     */
    public void setUsineEnorme(){
        this.setNbMaxiUsine(100);
        this.setPrixUsine(BigDecimal.valueOf(7000000));
        this.setPrixFabrication(BigDecimal.valueOf(70000));
    }
}
