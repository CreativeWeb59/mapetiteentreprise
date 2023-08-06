package com.example.mapetiteentreprise.jeu;

import java.math.BigDecimal;

public class Poulaillers {
    private String nom;
    private int nbPoulesMax;
    private BigDecimal prixPoulailler;

    public Poulaillers(String nom, int nbPoulesMax, BigDecimal prixPoulailler) {
        this.nom = nom;
        this.nbPoulesMax = nbPoulesMax;
        this.prixPoulailler = prixPoulailler;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNbPoulesMax() {
        return nbPoulesMax;
    }

    public void setNbPoulesMax(int nbPoulesMax) {
        this.nbPoulesMax = nbPoulesMax;
    }

    public BigDecimal getPrixPoulailler() {
        return prixPoulailler;
    }

    public void setPrixPoulailler(BigDecimal prixPoulailler) {
        this.prixPoulailler = prixPoulailler;
    }
}
