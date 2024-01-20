package com.example.monPetitBassin.jeu;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Player {
    private String name;
    private BigDecimal argent;
    private int qNourriture;
    private LocalDateTime dateDebutJeu;
    private LocalDateTime dateDeco;

    public Player(String name, BigDecimal argent, int qNourriture, LocalDateTime dateDebutJeu, LocalDateTime dateDeco) {
        this.name = name;
        this.argent = argent;
        this.qNourriture = qNourriture;
        this.dateDebutJeu = dateDebutJeu;
        this.dateDeco = dateDeco;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getArgent() {
        return argent;
    }

    public void setArgent(BigDecimal argent) {
        this.argent = argent;
    }

    public int getqNourriture() {
        return qNourriture;
    }

    public void setqNourriture(int qNourriture) {
        this.qNourriture = qNourriture;
    }

    public LocalDateTime getDateDebutJeu() {
        return dateDebutJeu;
    }

    public void setDateDebutJeu(LocalDateTime dateDebutJeu) {
        this.dateDebutJeu = dateDebutJeu;
    }

    public LocalDateTime getDateDeco() {
        return dateDeco;
    }

    public void setDateDeco(LocalDateTime dateDeco) {
        this.dateDeco = dateDeco;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", argent=" + argent +
                ", qNourriture=" + qNourriture +
                ", dateDebutJeu=" + dateDebutJeu +
                ", dateDeco=" + dateDeco +
                '}';
    }
}
