package com.example.monPetitBassin.jeu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Bassin {
    private String name;
    private int temperature;
    private BigDecimal tarif;
    private int niveau;
    private int capacite;
    private boolean actif;
    private final double tempMini = 1;
    private final double tempMaxi = 30;
    private List<Population> populationList = new ArrayList<>(); // liste des poissons

    public Bassin(String name, int temperature, BigDecimal tarif, int niveau, int capacite, boolean actif) {
        this.name = name;
        this.temperature = temperature;
        this.tarif = tarif;
        this.niveau = niveau;
        this.capacite = capacite;
        this.actif = actif;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public BigDecimal getTarif() {
        return tarif;
    }

    public void setTarif(BigDecimal tarif) {
        this.tarif = tarif;
    }

    public double getTempMini() {
        return tempMini;
    }

    public double getTempMaxi() {
        return tempMaxi;
    }

    public List<Population> getPopulationList() {
        return populationList;
    }

    public void setPopulationList(List<Population> populationList) {
        this.populationList = populationList;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public String toString() {
        return "Bassin{" +
                "name='" + name + '\'' +
                ", temperature=" + temperature +
                ", tarif=" + tarif +
                ", niveau=" + niveau +
                ", capacite=" + capacite +
                ", actif=" + actif +
                ", tempMini=" + tempMini +
                ", tempMaxi=" + tempMaxi +
                ", populationList=" + populationList +
                '}';
    }
}
