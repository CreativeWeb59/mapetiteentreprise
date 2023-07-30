package com.example.mapetiteentreprise.jeu;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

public class AnimationsBarresProgress {
    private Timeline timeline;
    private ProgressBar progressBar;

    public AnimationsBarresProgress(Timeline timeline, ProgressBar progressBar) {
        this.timeline = timeline;
        this.progressBar = progressBar;
    }
    public void createProgressJournee(int cycle, double vitesse){
//        ProgressBar getProgressJour = getProgressJour();
//        Button btnVendre = getBtnVendre();
        // Réinitialise la barre de progression à 0
        progressBar.setProgress(0);
        System.out.println("Progress barre : " + progressBar.getProgress());
        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(vitesse), e -> {
                    System.out.println("Jour terminé " + vitesse);
                    // incremente un jour et remet l'heure à 1
//                    this.jeu.getCalendrier().setJourSuivant();
                    // mise à jour du calendrier
//                    afficheCalendrier();
                    System.out.println("coucou");
                }, new KeyValue(progressBar.progressProperty(), 1))
        );

        if (cycle == 0) {
            timeline.setCycleCount(Animation.INDEFINITE);
        } else {
            timeline.setCycleCount(cycle);
        }
        timeline.play();
    }

    public void start() {
        timeline.play();
    }

    public void stop() {
        timeline.pause();
    }

    @Override
    public String toString() {
        return "AnimationsBarresProgress{" +
                "timeline=" + timeline +
                ", progressBar=" + progressBar +
                '}';
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;

        // Ajoute un listener pour détecter les changements de valeur de la barre de progression
        progressBar.progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Met à jour l'interface graphique dans le thread JavaFX principal
                Platform.runLater(() -> progressBar.setProgress(newValue.doubleValue()));
            }
        });
    }
    // Dans la classe AnimationsBarresProgress
    public void updateProgressBar(double value) {
        // Mettre à jour la barre de progression avec la nouvelle valeur
        progressBar.setProgress(value);
    }
}
