package com.example.monPetitBassin.jeu;

import java.time.LocalDateTime;

public abstract class Population {
    private String name;
    private TypePoisson typePoisson;
    private double tempMini;
    private double tempMaxi;
    private int faim;
    private int faimMini;
    private int faimMaxi;
    private int niveau;
    private LocalDateTime dateAchat;
    private LocalDateTime dateEvolution;

    public Population(String name, TypePoisson typePoisson, double tempMini, double tempMaxi, int faim, int niveau) {
        this.name = name;
        this.typePoisson = typePoisson;
        this.tempMini = tempMini;
        this.tempMaxi = tempMaxi;
        this.faim = faim;
        this.niveau = niveau;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypePoisson getTypePoisson() {
        return typePoisson;
    }

    public void setTypePoisson(TypePoisson typePoisson) {
        this.typePoisson = typePoisson;
    }

    public double getTempMini() {
        return tempMini;
    }

    public void setTempMini(double tempMini) {
        this.tempMini = tempMini;
    }

    public double getTempMaxi() {
        return tempMaxi;
    }

    public void setTempMaxi(double tempMaxi) {
        this.tempMaxi = tempMaxi;
    }

    public int getFaim() {
        return faim;
    }

    public void setFaim(int faim) {
        this.faim = faim;
    }

    public int getFaimMini() {
        return faimMini;
    }

    public void setFaimMini(int faimMini) {
        this.faimMini = faimMini;
    }

    public int getFaimMaxi() {
        return faimMaxi;
    }

    public void setFaimMaxi(int faimMaxi) {
        this.faimMaxi = faimMaxi;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    @Override
    public String toString() {
        return "Population{" +
                "name='" + name + '\'' +
                ", typePoisson=" + typePoisson +
                ", tempMini=" + tempMini +
                ", tempMaxi=" + tempMaxi +
                ", faim=" + faim +
                ", faimMini=" + faimMini +
                ", faimMaxi=" + faimMaxi +
                ", niveau=" + niveau +
                ", dateAchat=" + dateAchat +
                ", dateEvolution=" + dateEvolution +
                '}';
    }
}
