package com.example.mapetiteentreprise.jeu;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class Livraisons {
    // prix des vehicules
    private final BigDecimal prixVehicule = BigDecimal.valueOf(5000);
    // tarif de chaque course
    private final BigDecimal prixCourse = BigDecimal.valueOf(50.00);
    private final int nbMaxiVehicules = 200;
    // vitesse des livraisons
    private final int vitesseLivraion = 60;

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

    public BigDecimal getPrixVehicule() {
        return prixVehicule;
    }

    public BigDecimal getPrixCourse() {
        return prixCourse;
    }

    public int getNbMaxiVehicules() {
        return nbMaxiVehicules;
    }

    public int getVitesseLivraion() {
        return vitesseLivraion;
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
}
