package com.example.monPetitBassin.jeu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Jeu {
    private final boolean dev = false;
    private Player player;
    private List<Bassin> bassinList = new ArrayList<>(); // liste des bassins
    private final BigDecimal tarifNourriture = BigDecimal.valueOf(10);
    private final int qNourriture = 100;
    public Jeu(Player player, List<Bassin> bassinList) {
        this.player = player;
        this.bassinList = bassinList;
    }

    public boolean isDev() {
        return dev;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Bassin> getBassinList() {
        return bassinList;
    }

    public void setBassinList(List<Bassin> bassinList) {
        this.bassinList = bassinList;
    }

    public BigDecimal getTarifNourriture() {
        return tarifNourriture;
    }

    public int getqNourriture() {
        return qNourriture;
    }

    @Override
    public String toString() {
        return "Jeu{" +
                "dev=" + dev +
                ", player=" + player +
                ", bassinList=" + bassinList +
                ", tarifNourriture=" + tarifNourriture +
                ", qNourriture=" + qNourriture +
                '}';
    }
}
