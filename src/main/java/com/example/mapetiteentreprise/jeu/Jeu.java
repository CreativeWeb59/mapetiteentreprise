package com.example.mapetiteentreprise.jeu;

import com.example.mapetiteentreprise.bdd.Parametres;
import com.example.mapetiteentreprise.bdd.Sauvegarde;

public class Jeu {
    private Joueur joueur;
    private Sauvegarde sauvegarde;
    private Parametres parametres;
    private int numeroJour;

    public Jeu(Joueur joueur, Sauvegarde sauvegarde, Parametres parametres, int numeroJour) {
        this.joueur = joueur;
        this.sauvegarde = sauvegarde;
        this.parametres = parametres;
        this.numeroJour = numeroJour;
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

    public int getNumeroJour() {
        return numeroJour;
    }

    public void setNumeroJour(int numeroJour) {
        this.numeroJour = numeroJour;
    }

    @Override
    public String toString() {
        return "Jeu{" +
                "joueur=" + joueur +
                ", sauvegarde=" + sauvegarde +
                ", parametres=" + parametres +
                ", numeroJour=" + numeroJour +
                '}';
    }
}
