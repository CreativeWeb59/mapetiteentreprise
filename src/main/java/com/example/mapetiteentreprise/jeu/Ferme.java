package com.example.mapetiteentreprise.jeu;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ferme {
    private int nbPoules;
    private long nbOeufs;
    private double etatProgressOeuf = 0;
    private LocalDateTime dateDeco;
    private Timeline timelineOeufs;


    public Ferme(int nbPoules, long nbOeufs, double etatProgressOeuf, LocalDateTime dateDeco) {
        this.nbPoules = nbPoules;
        this.nbOeufs = nbOeufs;
        this.etatProgressOeuf = etatProgressOeuf;
        this.dateDeco = dateDeco;
    }

    public int getNbPoules() {
        return nbPoules;
    }

    public void setNbPoules(int nbPoules) {
        this.nbPoules = nbPoules;
    }

    public long getNbOeufs() {
        return nbOeufs;
    }

    public void setNbOeufs(long nbOeufs) {
        this.nbOeufs = nbOeufs;
    }

    public double getEtatProgressOeuf() {
        return etatProgressOeuf;
    }

    public void setEtatProgressOeuf(double etatProgressOeuf) {
        this.etatProgressOeuf = etatProgressOeuf;
    }

    public LocalDateTime getDateDeco() {
        return dateDeco;
    }

    public void setDateDeco(LocalDateTime dateDeco) {
        this.dateDeco = dateDeco;
    }


    @Override
    public String toString() {
        return "Ferme{" +
                "nbPoules=" + nbPoules +
                ", nbOeufs=" + nbOeufs +
                ", etatProgressOeuf=" + etatProgressOeuf +
                ", dateDeco=" + dateDeco +
                '}';
    }

    /**
     * Barre de progression pour compter les oeufs
     * Ajoute un nombre d'oeufs équivalent au nombre de poules à chaque fin de cycle
     *
     * @param cycle         : 0 pour cycle infini
     * @param vitesse       : vitesse de ponte en secondes
     * @param progressOeufs : barre de progress des oeufs
     */
    public void progressBarStartOeuf(int cycle, double vitesse, double vitesseAjustement, ProgressBar progressOeufs) {
        // determine le debut de la barre de progress
        double etatBarreProgressOeuf;
        if (cycle == 1) {
            progressOeufs.setProgress(this.getEtatProgressOeuf());
            etatBarreProgressOeuf = this.getEtatProgressOeuf();
        } else {
            progressOeufs.setProgress(0);
            etatBarreProgressOeuf = 0;
        }
        timelineOeufs = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressOeufs.progressProperty(), etatBarreProgressOeuf)),
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    // ajoute le nombre de poules necesaires
                    majOeufsJournalier();
                    System.out.println("Oeuf terminé");
                }, new KeyValue(progressOeufs.progressProperty(), 1))
        );
        timelineOeufs.setOnFinished(event -> {
            if (cycle == 1) {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                progressBarStartOeuf(cycle - 1, vitesse, vitesse, progressOeufs);
            }
        });
        if (cycle == 0) {
            timelineOeufs.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineOeufs.setCycleCount(cycle);
        }
        timelineOeufs.play();
    }

    /**
     * A effectuer lorsqu'une heure est ecoulee
     * ajoute un oeuf suivant le nombre de poules dans le poulailler
     */
    public void majOeufsJournalier() {
        long nbOeufsAAjouter = this.getNbOeufs() + this.getNbPoules();
        this.setNbOeufs(nbOeufsAAjouter);
        System.out.println("ajout de " + nbOeufsAAjouter + " oeuf(s)");
    }
    /**
     * Permet de stopper la timelin
     */
    public void progressBarStop() {
        if (this.timelineOeufs != null) {
            System.out.println("Arret de la barre de progression timelineOeufs");
            this.timelineOeufs.stop();
            this.timelineOeufs = null;
        }
    }
}
