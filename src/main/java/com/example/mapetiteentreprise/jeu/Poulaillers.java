package com.example.mapetiteentreprise.jeu;

import java.math.BigDecimal;

public class Poulaillers {
    private String nom;
    private int nbPoulesMax;
    private BigDecimal prixPoulailler;
    private double imgWidth;
    private double imgHeight;
    private double layoutX;
    private double layoutY;
    private double posLayoutBtnX;
    private double posLayoutBtnY;

    public Poulaillers(String nom, int nbPoulesMax, BigDecimal prixPoulailler, double imgWidth, double imgHeight, double layoutX, double layoutY, double posLayoutBtnX, double posLayoutBtnY) {
        this.nom = nom;
        this.nbPoulesMax = nbPoulesMax;
        this.prixPoulailler = prixPoulailler;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        this.layoutX = layoutX;
        this.layoutY = layoutY;
        this.posLayoutBtnX = posLayoutBtnX;
        this.posLayoutBtnY = posLayoutBtnY;
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

    public double getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(double imgWidth) {
        this.imgWidth = imgWidth;
    }

    public double getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(double imgHeight) {
        this.imgHeight = imgHeight;
    }

    public double getLayoutX() {
        return layoutX;
    }

    public void setLayoutX(double layoutX) {
        this.layoutX = layoutX;
    }

    public double getLayoutY() {
        return layoutY;
    }

    public void setLayoutY(double layoutY) {
        this.layoutY = layoutY;
    }

    public double getPosLayoutBtnX() {
        return posLayoutBtnX;
    }

    public void setPosLayoutBtnX(double posLayoutBtnX) {
        this.posLayoutBtnX = posLayoutBtnX;
    }

    public double getPosLayoutBtnY() {
        return posLayoutBtnY;
    }

    public void setPosLayoutBtnY(double posLayoutBtnY) {
        this.posLayoutBtnY = posLayoutBtnY;
    }

    @Override
    public String toString() {
        return "Poulaillers{" +
                "nom='" + nom + '\'' +
                ", nbPoulesMax=" + nbPoulesMax +
                ", prixPoulailler=" + prixPoulailler +
                ", posX=" + imgWidth +
                ", posY=" + imgHeight +
                ", layoutX=" + layoutX +
                ", layoutY=" + layoutY +
                ", posLayoutBtnX=" + posLayoutBtnX +
                ", posLayoutBtnY=" + posLayoutBtnY +
                '}';
    }
}
