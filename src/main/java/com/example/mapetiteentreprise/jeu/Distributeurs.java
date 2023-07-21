package com.example.mapetiteentreprise.jeu;

import java.time.LocalDateTime;

public abstract class Distributeurs {
    private int nbDistributeurs;
    private long nbMarchandises;
    private double etatProgressDistributeur = 0;
    private LocalDateTime dateDeco;

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
}
