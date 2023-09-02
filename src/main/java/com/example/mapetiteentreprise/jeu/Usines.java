package com.example.mapetiteentreprise.jeu;

import java.time.LocalDateTime;

public abstract class Usines {
    private String nom;
    private int usineActive;
    private int nbUsines;
    private long nbMarchandises;
    private double etatProgressUsine = 0;
    private final int nbMaxiUsine = 400;


    public Usines(String nom, int usineActive, int nbUsines, long nbMarchandises, double etatProgressUsine) {
        this.nom = nom;
        this.usineActive = usineActive;
        this.nbUsines = nbUsines;
        this.nbMarchandises = nbMarchandises;
        this.etatProgressUsine = etatProgressUsine;

    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNbUsines() {
        return nbUsines;
    }

    public void setNbUsines(int nbUsines) {
        this.nbUsines = nbUsines;
    }

    public long getNbMarchandises() {
        return nbMarchandises;
    }

    public void setNbMarchandises(long nbMarchandises) {
        this.nbMarchandises = nbMarchandises;
    }

    public double getEtatProgressUsine() {
        return etatProgressUsine;
    }

    public void setEtatProgressUsine(double etatProgressUsine) {
        this.etatProgressUsine = etatProgressUsine;
    }

    public int getNbMaxiUsine() {
        return nbMaxiUsine;
    }

    public int getUsineActive() {
        return usineActive;
    }

    public void setUsineActive(int usineActive) {
        this.usineActive = usineActive;
    }

    @Override
    public String toString() {
        return "Usines{" +
                "nom='" + nom + '\'' +
                ", usineActive=" + usineActive +
                ", nbUsines=" + nbUsines +
                ", nbMarchandises=" + nbMarchandises +
                ", etatProgressUsine=" + etatProgressUsine +
                ", nbMaxiUsine=" + nbMaxiUsine +
                '}';
    }

    /**
     * Methode pour ajouter un distributeur
     */
    public void ajoutUsine(){
        this.setNbUsines(this.getNbUsines() + 1);
    }


    /**
     * Met a jour le chiffre du nombre de marchandises fabriquées par l'usine
     */
    public void majUsine() {
        long nouvNombre = this.getNbMarchandises() + this.getNbUsines();
        this.setNbMarchandises(nouvNombre);
        System.out.println("maj du nombre de marchandises fabriquées par l'usine : " + nouvNombre);
    }
}
