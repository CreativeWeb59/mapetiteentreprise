package com.example.mapetiteentreprise.jeu;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class Livraisons {
    private String nom;
    private int nbVehicules = 0; // nb vehicules achetes
    private long nbCourses = 0; // nb de courses effectuees
    private double etatProgressLivraison = 0;

    public Livraisons() {
    }

    public Livraisons(String nom, int nbVehicules, long nbCourses, double etatProgressLivraison) {
        this.nom = nom;
        this.nbVehicules = nbVehicules;
        this.nbCourses = nbCourses;
        this.etatProgressLivraison = etatProgressLivraison;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNbVehicules() {
        return nbVehicules;
    }

    public void setNbVehicules(int nbVehicules) {
        this.nbVehicules = nbVehicules;
    }

    public long getNbCourses() {
        return nbCourses;
    }

    public void setNbCourses(long nbCourses) {
        this.nbCourses = nbCourses;
    }

    public double getEtatProgressLivraison() {
        return etatProgressLivraison;
    }

    public void setEtatProgressLivraison(double etatProgressLivraison) {
        this.etatProgressLivraison = etatProgressLivraison;
    }

    @Override
    public String toString() {
        return "Livraisons{" +
                ", nom='" + nom + '\'' +
                ", nbVehicules=" + nbVehicules +
                ", nbCourses=" + nbCourses +
                ", etatProgressLivraison=" + etatProgressLivraison +
                '}';
    }
    /**
     * Met a jour le chiffre du nombre de livraisons effectuées
     */
    public void majLivraison() {
        long nouvNombre = this.getNbCourses() + this.getNbVehicules();
        this.setNbCourses(nouvNombre);
        System.out.println("maj du nombre de livraisons effectuées : " + nouvNombre);
    }
}
