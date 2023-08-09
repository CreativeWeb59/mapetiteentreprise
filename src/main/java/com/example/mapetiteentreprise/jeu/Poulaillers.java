package com.example.mapetiteentreprise.jeu;

import java.math.BigDecimal;

public class Poulaillers {
    private String nom;
    private BigDecimal prixPoulailler;
    private int capacite;

    public Poulaillers(String nom, BigDecimal prixPoulailler, int capacite) {
        this.nom = nom;
        this.prixPoulailler = prixPoulailler;
        this.capacite = capacite;
    }

    public void createTous(){
        createPoulaillerInactif();
        createPoulailler();
        createPoulaillerPro();
        createMegaPoulailler();
        createPoulaillerIndustriel();
    }

    /**
     * Creation du poulailler inactif
     */
    public void createPoulaillerInactif(){
        this.nom = "Inactif";
        this.prixPoulailler = BigDecimal.valueOf(0);
        this.capacite = 0;
    }

    /**
     * Creation du poulailler de base
     */
    public void createPoulailler(){
        this.nom = "poulailler";
        this.prixPoulailler = BigDecimal.valueOf(1000);
        this.capacite = 200;
    }

    /**
     * Creation du poulailler pro
     */
    public void createPoulaillerPro(){
        this.nom = "poulailler pro";
        this.prixPoulailler = BigDecimal.valueOf(5000);
        this.capacite = 1000;
    }

    /**
     * Creation du méga poulailler
     */
    public void createMegaPoulailler(){
        this.nom = "méga poulailler";
        this.prixPoulailler = BigDecimal.valueOf(25000);
        this.capacite = 5000;
    }
    /**
     * Creation du poulailler industriel
     */
    public void createPoulaillerIndustriel(){
        this.nom = "poulailler industriel";
        this.prixPoulailler = BigDecimal.valueOf(50000);
        this.capacite = 10000;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public BigDecimal getPrixPoulailler() {
        return prixPoulailler;
    }

    public void setPrixPoulailler(BigDecimal prixPoulailler) {
        this.prixPoulailler = prixPoulailler;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    @Override
    public String toString() {
        return "Poulaillers{" +
                "nom='" + nom + '\'' +
                ", prixPoulailler=" + prixPoulailler +
                ", capacite=" + capacite +
                '}';
    }
}
