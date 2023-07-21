package com.example.mapetiteentreprise.jeu;

import com.example.mapetiteentreprise.bdd.Parametres;
import com.example.mapetiteentreprise.bdd.Sauvegarde;

public class Jeu {
    private Joueur joueur;
    private Sauvegarde sauvegarde;
    private Parametres parametres;

    public Jeu(Joueur joueur, Sauvegarde sauvegarde, Parametres parametres) {
        this.joueur = joueur;
        this.sauvegarde = sauvegarde;
        this.parametres = parametres;
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

    @Override
    public String toString() {
        return "Jeu{" +
                "joueur=" + joueur +
                ", sauvegarde=" + sauvegarde +
                ", parametres=" + parametres +
                '}';
    }
}
