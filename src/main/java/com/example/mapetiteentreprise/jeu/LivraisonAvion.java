package com.example.mapetiteentreprise.jeu;

import java.math.BigDecimal;

public class LivraisonAvion extends Livraisons{
    private final BigDecimal prixVehicule = BigDecimal.valueOf(200000); // prix du scooter
    private final BigDecimal prixCourse = BigDecimal.valueOf(2000.00); // tarif de chaque course
    // prix des vehicules
    private final int nbMaxiVehicules = 300;
    // vitesse des livraisons
    private final int vitesseLivraion = 60;

    public LivraisonAvion(String nom, int nbVehicules, long nbCourses, double etatProgressLivraison) {
        super(nom, nbVehicules, nbCourses, etatProgressLivraison);
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
    /**
     * Methode pour ajouter un service de livraison
     */
    public void ajoutServiceDeLivraison(){
        this.setNbVehicules(this.getNbVehicules() + 1);
    }

    /**
     *
     * @return le nombre maximum de vehicules
     */
    public int getNbMaxiServiceDeLivraison() {
        return nbMaxiVehicules;
    }

    /**
     * initialise le nombre de vehicules en cours ainsi que le nombre de vehicules maximum
     */
    public String setNbVehicule() {
        return this.getNbVehicules() + " / " + this.getNbMaxiVehicules();
    }
    /**
     * renvoi true si le nombre maximum d'avions est atteint
     * @return
     */
    public boolean isMaxiNbVehicules(){
        return this.getNbVehicules() >= this.getNbMaxiVehicules();
    }
}
