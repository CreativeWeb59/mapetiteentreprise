package com.example.mapetiteentreprise.jeu;

import java.time.LocalDateTime;

public class Ferme {
    private int nbPoules;
    private long nbOeufs;
    private double etatProgressOeuf = 0;
    private LocalDateTime dateDeco;

    public Ferme(int nbPoules, long nbOeufs, double etatProgressOeuf , LocalDateTime dateDeco) {
        this.nbPoules = nbPoules;
        this.nbOeufs = nbOeufs;
        this.etatProgressOeuf = etatProgressOeuf;
        this.dateDeco = dateDeco;
    }

    public int getNbPoules() {
        return nbPoules;
    }

    public void setNbPoules(int nbPoules) {
        this.nbPoules = nbPoules;
    }

    public long getNbOeufs() {
        return nbOeufs;
    }

    public void setNbOeufs(long nbOeufs) {
        this.nbOeufs = nbOeufs;
    }

    public double getEtatProgressOeuf() {
        return etatProgressOeuf;
    }

    public void setEtatProgressOeuf(double etatProgressOeuf) {
        this.etatProgressOeuf = etatProgressOeuf;
    }

    public LocalDateTime getDateDeco() {
        return dateDeco;
    }

    public void setDateDeco(LocalDateTime dateDeco) {
        this.dateDeco = dateDeco;
    }


    @Override
    public String toString() {
        return "Ferme{" +
                "nbPoules=" + nbPoules +
                ", nbOeufs=" + nbOeufs +
                ", etatProgressOeuf=" + etatProgressOeuf +
                ", dateDeco=" + dateDeco +
                '}';
    }

    /**
     * ajuste la barre de progress oeuf en fonction de la barre de progress jour et de l'heure actuelle
     * @param progressJour
     * @param heureActuelle
     */
    public void ajustementProgressOeuf(double progressJour, int heureActuelle){
        double resultat = (progressJour * 10) - heureActuelle ;
        System.out.println("valeur nouvelle barre : " + resultat);
    }
}
