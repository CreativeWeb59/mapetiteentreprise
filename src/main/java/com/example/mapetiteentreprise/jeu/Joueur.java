package com.example.mapetiteentreprise.jeu;

import java.math.BigDecimal;

public class Joueur {
    private String pseudo;
    private BigDecimal argent;
    private Ferme ferme;
    private BoissonsChaudes boissonsChaudes;
    private BoissonsFraiches boissonsFraiches;
    private Sandwichs sandwichs;
    private Confiseries confiseries;
    private CreditEnCours creditEnCours;
    private int fermeActive = 0;
    private int distributeursActive;
    private int distributeurBCActive;
    private int distributeurBFActive;
    private int distributeurCoActive;
    private int distributeurSaActive;
    private int poulailler1; // correspond a l'index du poullailler attribué dans la liste des poulaillers
    private int poulailler2;
    private int poulailler3;
    private int poulailler4;

    public Joueur(String pseudo, BigDecimal argent, Ferme ferme, BoissonsChaudes boissonsChaudes, BoissonsFraiches boissonsFraiches, Sandwichs sandwichs, Confiseries confiseries, CreditEnCours creditEnCours, int fermeActive, int distributeursActive, int distributeurBCActive, int distributeurBFActive, int distributeurCoActive, int distributeurSaActive, int poulailler1, int poulailler2, int poulailler3, int poulailler4) {
        this.pseudo = pseudo;
        this.argent = argent;
        this.ferme = ferme;
        this.boissonsChaudes = boissonsChaudes;
        this.boissonsFraiches = boissonsFraiches;
        this.sandwichs = sandwichs;
        this.confiseries = confiseries;
        this.creditEnCours = creditEnCours;
        this.fermeActive = fermeActive;
        this.distributeursActive = distributeursActive;
        this.distributeurBCActive = distributeurBCActive;
        this.distributeurBFActive = distributeurBFActive;
        this.distributeurCoActive = distributeurCoActive;
        this.distributeurSaActive = distributeurSaActive;
        this.poulailler1 = poulailler1;
        this.poulailler2 = poulailler2;
        this.poulailler3 = poulailler3;
        this.poulailler4 = poulailler4;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public BigDecimal getArgent() {
        return argent;
    }

    public void setArgent(BigDecimal argent) {
        this.argent = argent;
    }

    public Ferme getFerme() {
        return ferme;
    }

    public void setFerme(Ferme ferme) {
        this.ferme = ferme;
    }

    public BoissonsChaudes getBoissonsChaudes() {
        return boissonsChaudes;
    }

    public void setBoissonsChaudes(BoissonsChaudes boissonsChaudes) {
        this.boissonsChaudes = boissonsChaudes;
    }

    public BoissonsFraiches getBoissonsFraiches() {
        return boissonsFraiches;
    }

    public void setBoissonsFraiches(BoissonsFraiches boissonsFraiches) {
        this.boissonsFraiches = boissonsFraiches;
    }

    public Sandwichs getSandwichs() {
        return sandwichs;
    }

    public void setSandwichs(Sandwichs sandwichs) {
        this.sandwichs = sandwichs;
    }

    public Confiseries getConfiseries() {
        return confiseries;
    }

    public void setConfiseries(Confiseries confiseries) {
        this.confiseries = confiseries;
    }

    public int getFermeActive() {
        return fermeActive;
    }

    public void setFermeActive(int fermeActive) {
        this.fermeActive = fermeActive;
    }

    public int getDistributeursActive() {
        return distributeursActive;
    }

    public void setDistributeursActive(int distributeursActive) {
        this.distributeursActive = distributeursActive;
    }

    public int getDistributeurBCActive() {
        return distributeurBCActive;
    }

    public void setDistributeurBCActive(int distributeurBCActive) {
        this.distributeurBCActive = distributeurBCActive;
    }

    public int getDistributeurBFActive() {
        return distributeurBFActive;
    }

    public void setDistributeurBFActive(int distributeurBFActive) {
        this.distributeurBFActive = distributeurBFActive;
    }

    public int getDistributeurCoActive() {
        return distributeurCoActive;
    }

    public void setDistributeurCoActive(int distributeurCoActive) {
        this.distributeurCoActive = distributeurCoActive;
    }

    public int getDistributeurSaActive() {
        return distributeurSaActive;
    }

    public void setDistributeurSaActive(int distributeurSaActive) {
        this.distributeurSaActive = distributeurSaActive;
    }

    public CreditEnCours getCreditEnCours() {
        return creditEnCours;
    }

    public void setCreditEnCours(CreditEnCours creditEnCours) {
        this.creditEnCours = creditEnCours;
    }

    public int getPoulailler1() {
        return poulailler1;
    }

    public void setPoulailler1(int poulailler1) {
        this.poulailler1 = poulailler1;
    }

    public int getPoulailler2() {
        return poulailler2;
    }

    public void setPoulailler2(int poulailler2) {
        this.poulailler2 = poulailler2;
    }

    public int getPoulailler3() {
        return poulailler3;
    }

    public void setPoulailler3(int poulailler3) {
        this.poulailler3 = poulailler3;
    }

    public int getPoulailler4() {
        return poulailler4;
    }

    public void setPoulailler4(int poulailler4) {
        this.poulailler4 = poulailler4;
    }

    @Override
    public String toString() {
        return "Joueur{" +
                "pseudo='" + pseudo + '\'' +
                ", argent=" + argent +
                ", ferme=" + ferme +
                ", boissonsChaudes=" + boissonsChaudes +
                ", boissonsFraiches=" + boissonsFraiches +
                ", sandwichs=" + sandwichs +
                ", confiseries=" + confiseries +
                ", creditEnCours=" + creditEnCours +
                ", fermeActive=" + fermeActive +
                ", distributeursActive=" + distributeursActive +
                ", distributeurBCActive=" + distributeurBCActive +
                ", distributeurBFActive=" + distributeurBFActive +
                ", distributeurCoActive=" + distributeurCoActive +
                ", distributeurSaActive=" + distributeurSaActive +
                ", poulailler1=" + poulailler1 +
                ", poulailler2=" + poulailler2 +
                ", poulailler3=" + poulailler3 +
                ", poulailler4=" + poulailler4 +
                '}';
    }

    /**
     * renvoi true si le joueur a assez d'argent pour la depense
     *
     * @return
     */
    public boolean isArgent(BigDecimal montantAchat) {
        // on verifie si l'argent est dispo
        int comparaison = this.argent.compareTo(montantAchat);
        if (comparaison >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Methode permettant de depenser de l'argent
     * @param montantDepense
     */
    public void depenser(BigDecimal montantDepense) {
        this.setArgent(this.getArgent().subtract(montantDepense));
    }


}
