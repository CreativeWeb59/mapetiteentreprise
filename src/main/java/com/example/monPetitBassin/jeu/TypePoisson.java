package com.example.monPetitBassin.jeu;

public class TypePoisson {
    private String name;
    private double tempMini;
    private double tempMaxi;
    private int faimMini;
    private int faimMaxi;
    private long tempsEvolution;

    public TypePoisson(String name, double tempMini, double tempMaxi, int faimMini, int faimMaxi, long tempsEvolution) {
        this.name = name;
        this.tempMini = tempMini;
        this.tempMaxi = tempMaxi;
        this.faimMini = faimMini;
        this.faimMaxi = faimMaxi;
        this.tempsEvolution = tempsEvolution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public long getTempsEvolution() {
        return tempsEvolution;
    }

    public void setTempsEvolution(long tempsEvolution) {
        this.tempsEvolution = tempsEvolution;
    }

    @Override
    public String toString() {
        return "TypePoisson{" +
                "name='" + name + '\'' +
                ", tempMini=" + tempMini +
                ", tempMaxi=" + tempMaxi +
                ", faimMini=" + faimMini +
                ", faimMaxi=" + faimMaxi +
                ", tempsEvolution=" + tempsEvolution +
                '}';
    }
}
