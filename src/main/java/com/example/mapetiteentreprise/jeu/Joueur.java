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
    private int livraison1Active;
    private int livraison2Active;
    private int livraison3Active;
    private int livraison4Active;
    private int livraison5Active;
    private LivraisonScooter livraisonScooter;
    private LivraisonCamionette livraisonCamionette;
    private LivraisonPetitCamion livraisonPetitCamion;
    private LivraisonPoidsLourd livraisonPoidsLourd;
    private LivraisonAvion livraisonAvion;

    public Joueur(String pseudo, BigDecimal argent, Ferme ferme, BoissonsChaudes boissonsChaudes, BoissonsFraiches boissonsFraiches, Sandwichs sandwichs, Confiseries confiseries, CreditEnCours creditEnCours, int fermeActive, int distributeursActive, int distributeurBCActive, int distributeurBFActive, int distributeurCoActive, int distributeurSaActive, int poulailler1, int poulailler2, int poulailler3, int poulailler4, int livraison1Active, int livraison2Active, int livraison3Active, int livraison4Active, int livraison5Active, LivraisonScooter livraisonScooter, LivraisonCamionette livraisonCamionette, LivraisonPetitCamion livraisonPetitCamion, LivraisonPoidsLourd livraisonPoidsLourd, LivraisonAvion livraisonAvion) {
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
        this.livraison1Active = livraison1Active;
        this.livraison2Active = livraison2Active;
        this.livraison3Active = livraison3Active;
        this.livraison4Active = livraison4Active;
        this.livraison5Active = livraison5Active;
        this.livraisonScooter = livraisonScooter;
        this.livraisonCamionette = livraisonCamionette;
        this.livraisonPetitCamion = livraisonPetitCamion;
        this.livraisonPoidsLourd = livraisonPoidsLourd;
        this.livraisonAvion = livraisonAvion;
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

    public int getLivraison1Active() {
        return livraison1Active;
    }

    public void setLivraison1Active(int livraison1Active) {
        this.livraison1Active = livraison1Active;
    }

    public int getLivraison2Active() {
        return livraison2Active;
    }

    public void setLivraison2Active(int livraison2Active) {
        this.livraison2Active = livraison2Active;
    }

    public int getLivraison3Active() {
        return livraison3Active;
    }

    public void setLivraison3Active(int livraison3Active) {
        this.livraison3Active = livraison3Active;
    }

    public int getLivraison4Active() {
        return livraison4Active;
    }

    public void setLivraison4Active(int livraison4Active) {
        this.livraison4Active = livraison4Active;
    }

    public LivraisonScooter getLivraisonScooter() {
        return livraisonScooter;
    }

    public void setLivraisonScooter(LivraisonScooter livraisonScooter) {
        this.livraisonScooter = livraisonScooter;
    }

    public LivraisonPetitCamion getLivraisonPetitCamion() {
        return livraisonPetitCamion;
    }

    public void setLivraisonPetitCamion(LivraisonPetitCamion livraisonPetitCamion) {
        this.livraisonPetitCamion = livraisonPetitCamion;
    }

    public LivraisonCamionette getLivraisonCamionette() {
        return livraisonCamionette;
    }

    public void setLivraisonCamionette(LivraisonCamionette livraisonCamionette) {
        this.livraisonCamionette = livraisonCamionette;
    }

    public LivraisonPoidsLourd getLivraisonPoidsLourd() {
        return livraisonPoidsLourd;
    }

    public void setLivraisonPoidsLourd(LivraisonPoidsLourd livraisonPoidsLourd) {
        this.livraisonPoidsLourd = livraisonPoidsLourd;
    }

    public LivraisonAvion getLivraisonAvion() {
        return livraisonAvion;
    }

    public void setLivraisonAvion(LivraisonAvion livraisonAvion) {
        this.livraisonAvion = livraisonAvion;
    }

    public int getLivraison5Active() {
        return livraison5Active;
    }

    public void setLivraison5Active(int livraison5Active) {
        this.livraison5Active = livraison5Active;
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
                ", livraisonScooter=" + livraisonScooter +
                ", livraisonPetitCamion=" + livraisonPetitCamion +
                ", livraisonCamionette=" + livraisonCamionette +
                ", livraisonPoidsLourd=" + livraisonPoidsLourd +
                ", livraison1Active=" + livraison1Active +
                ", livraison2Active=" + livraison2Active +
                ", livraison3Active=" + livraison3Active +
                ", livraison4Active=" + livraison4Active +
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

    /**
     * methode generale pour acheter un service de livraison
     *
     * @param montantAchat
     * @return true si ok, false si pas assez d'argent
     */
    public Boolean acheter(BigDecimal montantAchat) {
        // on verifie l'argent dispo
        // si c'est le cas on retire l'argent
        if (this.isArgent(montantAchat)) {
            this.depenser(montantAchat);
            return true;
        } else {
            return false;
        }
    }

}
