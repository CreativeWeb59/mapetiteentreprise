package com.example.mapetiteentreprise.jeu;

import com.example.mapetiteentreprise.actions.Outils;
import javafx.scene.control.Button;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public abstract class Usines {
    private String nom;
    private int usineActive;
    private int nbUsines;
    private long nbMarchandises;
    private double etatProgressUsine = 0;
    private int nbMaxiUsine = 400;

    private BigDecimal prixUsine = BigDecimal.valueOf(200000); // prix d'une usine supplémentaire
    private BigDecimal prixFabrication = BigDecimal.valueOf(2000.00); // prix de vente d'un produit textile
    private int vitesseUsineTextile = 60;

    private final String monnaie = " €";
    private final DecimalFormat decimalFormat = Outils.getDecimalFormatWithSpaceSeparator();
    private BigDecimal gainEnAttenteUsine = new BigDecimal(0);


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

    public void setNbMaxiUsine(int nbMaxiUsine) {
        this.nbMaxiUsine = nbMaxiUsine;
    }

    public BigDecimal getPrixUsine() {
        return prixUsine;
    }

    public void setPrixUsine(BigDecimal prixUsine) {
        this.prixUsine = prixUsine;
    }

    public BigDecimal getPrixFabrication() {
        return prixFabrication;
    }

    public void setPrixFabrication(BigDecimal prixFabrication) {
        this.prixFabrication = prixFabrication;
    }

    public int getVitesseUsineTextile() {
        return vitesseUsineTextile;
    }

    public void setVitesseUsineTextile(int vitesseUsineTextile) {
        this.vitesseUsineTextile = vitesseUsineTextile;
    }

    public String getMonnaie() {
        return monnaie;
    }

    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public BigDecimal getGainEnAttenteUsine() {
        return gainEnAttenteUsine;
    }

    public void setGainEnAttenteUsine(BigDecimal gainEnAttenteUsine) {
        this.gainEnAttenteUsine = gainEnAttenteUsine;
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
                ", prixUsine=" + prixUsine +
                ", prixFabrication=" + prixFabrication +
                ", vitesseUsineTextile=" + vitesseUsineTextile +
                ", monnaie='" + monnaie + '\'' +
                ", decimalFormat=" + decimalFormat +
                ", gainEnAttenteUsine=" + gainEnAttenteUsine +
                '}';
    }
/**
     * Methodes communes à toutes les usines
     */


    /**
     * Methode pour ajouter une usine
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

    /**
     * Recup des marchandises produites dans les usines entre les switchs de fenetre
     * et maj du bouton encaisser avec les gains encours
     * @param btnEncaisser Donner le bouton à gérer pour afficher le montant à récupérer
     */
    public void majBtnEncaisser(Button btnEncaisser) {
        if (this.getNbMarchandises() > 0) {
            btnEncaisser.setDisable(false);
            // maj le montant sur le bouton Encaisser
            String formattedString = "Encaisser " + decimalFormat.format(this.gainEnAttenteUsine) + monnaie;
            btnEncaisser.setText(formattedString);
        } else {
            String formattedString = "Gains en attente...";
            btnEncaisser.setText(formattedString);
            btnEncaisser.setDisable(true);
        }
    }
    public BigDecimal majGainsEnAttente(){
        return this.getPrixFabrication().multiply(BigDecimal.valueOf(this.getNbMarchandises()));
    }

    /**
     * initialise le nombre de vehicules en cours ainsi que le nombre de vehicules maximum
     */
    public String setNbUsines() {
        return this.getNbUsines() + " / " + this.getNbMaxiUsine();
    }
    /**
     * renvoi true si le nombre maximum d'usine est atteint
     * @return
     */
    public boolean isMaxiNbUsines(){
        return this.getNbUsines() >= this.getNbMaxiUsine();
    }
}
