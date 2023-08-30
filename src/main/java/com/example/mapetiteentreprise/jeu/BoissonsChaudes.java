package com.example.mapetiteentreprise.jeu;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

import java.time.LocalDateTime;

public class BoissonsChaudes extends Distributeurs{
    private Timeline timelineBC;
    public BoissonsChaudes(int nbDistributeurs, long nbMarchandises, double etatProgressDistributeur, LocalDateTime dateDeco) {
        super(nbDistributeurs, nbMarchandises, etatProgressDistributeur, dateDeco);
    }
    /**
     * Barre de progression pour comptabiliser les boissons chaudes
     * Ajoute des boissons chaudes équivalent au nombre de distributeurs à chaque fin de cycle
     *
     * @param cycle         : 0 pour cycle infini
     * @param vitesse       : vitesse du distributeur en secondes
     * @param progress    : barre de progress du distributeur de boissons chaudes
     */
    public void progressBarStartBC(int cycle, double vitesse, double vitesseAjustement, ProgressBar progress) {
        // determine le debut de la barre de progress
        double etatBarreProgress;
        if (cycle == 1) {
            progress.setProgress(this.getEtatProgressDistributeur());
            etatBarreProgress = this.getEtatProgressDistributeur();
        } else {
            progress.setProgress(0);
            etatBarreProgress = 0;
        }
        timelineBC = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), etatBarreProgress)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    // ajoute le nombre de boissons chaudes necessaires
                    this.majDistributeur();
                    System.out.println("distributeur BC terminé");
                }, new KeyValue(progress.progressProperty(), 1))
        );
        timelineBC.setOnFinished(event -> {
            if (cycle == 1) {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartBC(cycle - 1, vitesse, vitesse, progress);
            }
        });
        if (cycle == 0) {
            timelineBC.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineBC.setCycleCount(cycle);
        }
        timelineBC.play();
    }
    /**
     * Permet de stopper la timeline
     */
    public void progressBarStop() {
        if (this.timelineBC != null) {
            System.out.println("Arret de la barre de progression timelineBC");
            this.timelineBC.stop();
            this.timelineBC = null;
        }
    }
}
