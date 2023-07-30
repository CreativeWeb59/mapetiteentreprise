package com.example.mapetiteentreprise.jeu;

import com.example.mapetiteentreprise.bdd.Parametres;
import com.example.mapetiteentreprise.bdd.Sauvegarde;
import javafx.animation.Timeline;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class Jeu {
    private Joueur joueur;
    private Sauvegarde sauvegarde;
    private Parametres parametres;
    private Calendrier calendrier;
    // Liste de barres de progression, par defaut
    // la 0 est toujours la progress jour
    // la 2 est celle de la ferme
    // la 3, 4, 5, 6 celle des distributeurs...
    private List<AnimationsBarresProgress> barresDeProgressions = new ArrayList<>();

    public Jeu(Joueur joueur, Sauvegarde sauvegarde, Parametres parametres, Calendrier calendrier) {
        this.joueur = joueur;
        this.sauvegarde = sauvegarde;
        this.parametres = parametres;
        this.calendrier = calendrier;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public Sauvegarde getSauvegarde() {
        return sauvegarde;
    }

    public void setSauvegarde(Sauvegarde sauvegarde) {
        this.sauvegarde = sauvegarde;
    }

    public Parametres getParametres() {
        return parametres;
    }

    public void setParametres(Parametres parametres) {
        this.parametres = parametres;
    }

    public Calendrier getCalendrier() {
        return calendrier;
    }

    public void setCalendrier(Calendrier calendrier) {
        this.calendrier = calendrier;
    }

    public List<AnimationsBarresProgress> getBarresDeProgressions() {
        return barresDeProgressions;
    }

    /**
     * Ajoute une barre de progression
     * @param barresDeProgressions
     */
    public void setUneBarreDeProgression(AnimationsBarresProgress barresDeProgressions) {
        this.barresDeProgressions.add(barresDeProgressions);
    }

    /**
     * fait appel a une methode de la classe AnimationBarreProgress
     * @return
     */
    public void creerTimelineJournee(int cycle, double vitesse) {
        int index = 0;
        if (index >= 0 && index < barresDeProgressions.size()) {
            AnimationsBarresProgress barreDeProgression = barresDeProgressions.get(index);
            barreDeProgression.createProgressJournee(cycle, vitesse);
        }
    }

    public void playTimelineJournee(int cycle, double vitesse) {
        int index = 0;
        if (index >= 0 && index < barresDeProgressions.size()) {
            AnimationsBarresProgress barreDeProgression = barresDeProgressions.get(index);
            barreDeProgression.start();
            System.out.println("start progress");
        }
    }

    public void rePlayTimelineJournee(double value) {
        int index = 0;
        if (index >= 0 && index < barresDeProgressions.size()) {
            AnimationsBarresProgress barreDeProgression = barresDeProgressions.get(index);
            Platform.runLater(() -> barreDeProgression.updateProgressBar(value));
        }
    }

    @Override
    public String toString() {
        return "Jeu{" +
                "joueur=" + joueur +
                ", sauvegarde=" + sauvegarde +
                ", parametres=" + parametres +
                ", calendrier=" + calendrier +
                ", barresDeProgressions=" + barresDeProgressions +
                '}';
    }
}
