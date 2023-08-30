package com.example.mapetiteentreprise.jeu;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

import java.time.LocalDateTime;

public abstract class Distributeurs {
    private int nbDistributeurs;
    private long nbMarchandises;
    private double etatProgressDistributeur = 0;
    private LocalDateTime dateDeco;
    private final int nbMaxiDistributeur = 400;

    public Distributeurs(int nbDistributeurs, long nbMarchandises, double etatProgressDistributeur, LocalDateTime dateDeco) {
        this.nbDistributeurs = nbDistributeurs;
        this.nbMarchandises = nbMarchandises;
        this.etatProgressDistributeur = etatProgressDistributeur;
        this.dateDeco = dateDeco;
    }

    public int getNbDistributeurs() {
        return nbDistributeurs;
    }

    public void setNbDistributeurs(int nbDistributeurs) {
        this.nbDistributeurs = nbDistributeurs;
    }

    public long getNbMarchandises() {
        return nbMarchandises;
    }

    public void setNbMarchandises(long nbMarchandises) {
        this.nbMarchandises = nbMarchandises;
    }

    public double getEtatProgressDistributeur() {
        return etatProgressDistributeur;
    }

    public void setEtatProgressDistributeur(double etatProgressDistributeur) {
        this.etatProgressDistributeur = etatProgressDistributeur;
    }

    public LocalDateTime getDateDeco() {
        return dateDeco;
    }

    public void setDateDeco(LocalDateTime dateDeco) {
        this.dateDeco = dateDeco;
    }

    @Override
    public String toString() {
        return "Distributeurs{" +
                "nbDistributeurs=" + nbDistributeurs +
                ", nbMarchandises=" + nbMarchandises +
                ", etatProgressDistributeur=" + etatProgressDistributeur +
                ", dateDeco=" + dateDeco +
                '}';
    }

    /**
     * Methode pour ajouter un distributeur
     */
    public void ajoutDistributeur(){
        this.setNbDistributeurs(this.getNbDistributeurs() + 1);
    }

    public int getNbMaxiDistributeur() {
        return nbMaxiDistributeur;
    }

    /**
     * Met a jour le chiffre du nombre de marchandises vendues par le distributeur
     */
    public void majDistributeur() {
        long nouvNombre = this.getNbMarchandises() + this.getNbDistributeurs();
        this.setNbMarchandises(nouvNombre);
        System.out.println("maj du nombre de marchandises vendues dans les distributeurs : " + nouvNombre);
    }
}
