package com.example.monPetitBassin.actions;

public class Resultat {
    private String chaine;
    private boolean valeurBool;

    public Resultat(String chaine, boolean valeurBool) {
        this.chaine = chaine;
        this.valeurBool = valeurBool;
    }

    public String getChaine() {
        return chaine;
    }

    public boolean getValeurBool() {
        return valeurBool;
    }
}
