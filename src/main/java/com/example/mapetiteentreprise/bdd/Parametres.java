package com.example.mapetiteentreprise.bdd;

import java.math.BigDecimal;

public class Parametres {
    private int id;
    private int nbPoules = 1;
    private BigDecimal tarifPoule = BigDecimal.valueOf(20.00);
    private BigDecimal tarifOeuf = BigDecimal.valueOf(0.30);
    private BigDecimal taxeOeuf = BigDecimal.valueOf(0.10);
    private BigDecimal argentDepart = BigDecimal.valueOf(0.00);
    private int fermeActive = 0; // valeur true
    private int distributeursActive = 0;
    private int distributeurBCActive =0;
    private int distributeurBFActive = 0;
    private int distributeurCoActive = 0;
    private int distributeurSaActive = 0;
    private int lavageActive = 0;
    private int vitessePonteOeuf = 60;

    // partie des distributeurs
    private BigDecimal prixDistributeurBC = BigDecimal.valueOf(1500);
    private BigDecimal prixDistributeurBF = BigDecimal.valueOf(2000);
    private BigDecimal prixDistributeurSa = BigDecimal.valueOf(4000);
    private BigDecimal prixDistributeurCo = BigDecimal.valueOf(3000);

    // nombre des distributeurs
    private int nbDistributeurBC = 0;
    private int nbDistributeurBF = 0;
    private int nbDistributeurSa = 0;
    private int nbDistributeurCo = 0;

    // nombre de marchandises contenues dans les distributeurs
    private int nbMarchandisesBC = 0;
    private int nbMarchandisesBF = 0;
    private int nbMarchandisesSa = 0;
    private int nbMarchandisesCo = 0;

    // valeur des marchandises contenues dans les distributeurs
    private BigDecimal prixMarchandiseBC = BigDecimal.valueOf(15.00);
    private BigDecimal prixMarchandiseBF = BigDecimal.valueOf(20.00);
    private BigDecimal prixMarchandiseSa = BigDecimal.valueOf(40.00);
    private BigDecimal prixMarchandiseCo = BigDecimal.valueOf(30.00);

    private int vitesseBC = 60;
    private int vitesseBF = 60;
    private int vitesseSa = 60;
    private int vitesseCo = 60;

    // partie des livraisons
    private int livraisonActive01 = 0;
    private int livraisonActive02 = 0;
    private int livraisonActive03 = 0;
    private int livraisonActive04 = 0;




    public Parametres() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getTarifPoule() {
        return tarifPoule;
    }

    public void setTarifPoule(BigDecimal tarifPoule) {
        this.tarifPoule = tarifPoule;
    }

    public BigDecimal getTarifOeuf() {
        return tarifOeuf;
    }

    public void setTarifOeuf(BigDecimal tarifOeuf) {
        this.tarifOeuf = tarifOeuf;
    }

    public BigDecimal getTaxeOeuf() {
        return taxeOeuf;
    }

    public void setTaxeOeuf(BigDecimal taxeOeuf) {
        this.taxeOeuf = taxeOeuf;
    }

    public BigDecimal getArgentDepart() {
        return argentDepart;
    }

    public void setArgentDepart(BigDecimal argentDepart) {
        this.argentDepart = argentDepart;
    }

    public int getNbPoules() {
        return nbPoules;
    }

    public void setNbPoules(int nbPoules) {
        this.nbPoules = nbPoules;
    }

    public int getFermeActive() {
        return fermeActive;
    }

    public void setFermeActive(int fermeActive) {
        this.fermeActive = fermeActive;
    }

    public int getVitessePonteOeuf() {
        return vitessePonteOeuf;
    }

    public void setVitessePonteOeuf(int vitessePonteOeuf) {
        this.vitessePonteOeuf = vitessePonteOeuf;
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

    public void setDistributeurCoActive(int ditributeurCoActive) {
        this.distributeurCoActive = ditributeurCoActive;
    }

    public int getDistributeurSaActive() {
        return distributeurSaActive;
    }

    public void setDistributeurSaActive(int distributeurSaActive) {
        this.distributeurSaActive = distributeurSaActive;
    }

    public int getLivraisonActive() {
        return livraisonActive;
    }

    public void setLivraisonActive(int livraisonActive) {
        this.livraisonActive = livraisonActive;
    }

    public int getLavageActive() {
        return lavageActive;
    }

    public void setLavageActive(int lavageActive) {
        this.lavageActive = lavageActive;
    }

    public int getDistributeursActive() {
        return distributeursActive;
    }

    public void setDistributeursActive(int distributeursActive) {
        this.distributeursActive = distributeursActive;
    }

    public BigDecimal getPrixDistributeurBC() {
        return prixDistributeurBC;
    }

    public void setPrixDistributeurBC(BigDecimal prixDistributeurBC) {
        this.prixDistributeurBC = prixDistributeurBC;
    }

    public BigDecimal getPrixDistributeurBF() {
        return prixDistributeurBF;
    }

    public void setPrixDistributeurBF(BigDecimal prixDistributeurBF) {
        this.prixDistributeurBF = prixDistributeurBF;
    }

    public BigDecimal getPrixDistributeurSa() {
        return prixDistributeurSa;
    }

    public void setPrixDistributeurSa(BigDecimal prixDistributeurSa) {
        this.prixDistributeurSa = prixDistributeurSa;
    }

    public BigDecimal getPrixDistributeurCo() {
        return prixDistributeurCo;
    }

    public void setPrixDistributeurCo(BigDecimal prixDistributeurCo) {
        this.prixDistributeurCo = prixDistributeurCo;
    }

    public int getNbDistributeurBC() {
        return nbDistributeurBC;
    }

    public void setNbDistributeurBC(int nbDistributeurBC) {
        this.nbDistributeurBC = nbDistributeurBC;
    }

    public int getNbDistributeurBF() {
        return nbDistributeurBF;
    }

    public void setNbDistributeurBF(int nbDistributeurBF) {
        this.nbDistributeurBF = nbDistributeurBF;
    }

    public int getNbDistributeurSa() {
        return nbDistributeurSa;
    }

    public void setNbDistributeurSa(int nbDistributeurSa) {
        this.nbDistributeurSa = nbDistributeurSa;
    }

    public int getNbDistributeurCo() {
        return nbDistributeurCo;
    }

    public void setNbDistributeurCo(int nbDistributeurCo) {
        this.nbDistributeurCo = nbDistributeurCo;
    }

    public int getNbMarchandisesBC() {
        return nbMarchandisesBC;
    }

    public void setNbMarchandisesBC(int nbMarchandisesBC) {
        this.nbMarchandisesBC = nbMarchandisesBC;
    }

    public int getNbMarchandisesBF() {
        return nbMarchandisesBF;
    }

    public void setNbMarchandisesBF(int nbMarchandisesBF) {
        this.nbMarchandisesBF = nbMarchandisesBF;
    }

    public int getNbMarchandisesSa() {
        return nbMarchandisesSa;
    }

    public void setNbMarchandisesSa(int nbMarchandisesSa) {
        this.nbMarchandisesSa = nbMarchandisesSa;
    }

    public int getNbMarchandisesCo() {
        return nbMarchandisesCo;
    }

    public void setNbMarchandisesCo(int nbMarchandisesCo) {
        this.nbMarchandisesCo = nbMarchandisesCo;
    }

    public BigDecimal getPrixMarchandiseBC() {
        return prixMarchandiseBC;
    }

    public void setPrixMarchandiseBC(BigDecimal prixMarchandiseBC) {
        this.prixMarchandiseBC = prixMarchandiseBC;
    }

    public BigDecimal getPrixMarchandiseBF() {
        return prixMarchandiseBF;
    }

    public void setPrixMarchandiseBF(BigDecimal prixMarchandiseBF) {
        this.prixMarchandiseBF = prixMarchandiseBF;
    }

    public BigDecimal getPrixMarchandiseSa() {
        return prixMarchandiseSa;
    }

    public void setPrixMarchandiseSa(BigDecimal prixMarchandiseSa) {
        this.prixMarchandiseSa = prixMarchandiseSa;
    }

    public BigDecimal getPrixMarchandiseCo() {
        return prixMarchandiseCo;
    }

    public void setPrixMarchandiseCo(BigDecimal prixMarchandiseCo) {
        this.prixMarchandiseCo = prixMarchandiseCo;
    }

    public int getVitesseBC() {
        return vitesseBC;
    }

    public void setVitesseBC(int vitesseBC) {
        this.vitesseBC = vitesseBC;
    }

    public int getVitesseBF() {
        return vitesseBF;
    }

    public void setVitesseBF(int vitesseBF) {
        this.vitesseBF = vitesseBF;
    }

    public int getVitesseSa() {
        return vitesseSa;
    }

    public void setVitesseSa(int vitesseSa) {
        this.vitesseSa = vitesseSa;
    }

    public int getVitesseCo() {
        return vitesseCo;
    }

    public void setVitesseCo(int vitesseCo) {
        this.vitesseCo = vitesseCo;
    }

    /**
     * recupere le prix de vente et
     * renvoi le montant de la taxe
     * @param nbOeufs
     * @return
     */
    public BigDecimal calulTaxe(long nbOeufs){
        return BigDecimal.valueOf(nbOeufs).multiply(this.taxeOeuf);
    }

    @Override
    public String toString() {
        return "Parametres{" +
                "id=" + id +
                ", nbPoules=" + nbPoules +
                ", tarifPoule=" + tarifPoule +
                ", tarifOeuf=" + tarifOeuf +
                ", taxeOeuf=" + taxeOeuf +
                ", argentDepart=" + argentDepart +
                ", fermeActive=" + fermeActive +
                ", distributeursActive=" + distributeursActive +
                ", distributeurBCActive=" + distributeurBCActive +
                ", distributeurBFActive=" + distributeurBFActive +
                ", distributeurCoActive=" + distributeurCoActive +
                ", distributeurSaActive=" + distributeurSaActive +
                ", livraisonActive=" + livraisonActive +
                ", lavageActive=" + lavageActive +
                ", vitessePonteOeuf=" + vitessePonteOeuf +
                ", prixDistributeurBC=" + prixDistributeurBC +
                ", prixDistributeurBF=" + prixDistributeurBF +
                ", prixDistributeurSa=" + prixDistributeurSa +
                ", prixDistributeurCo=" + prixDistributeurCo +
                ", nbDistributeurBC=" + nbDistributeurBC +
                ", nbDistributeurBF=" + nbDistributeurBF +
                ", nbDistributeurSa=" + nbDistributeurSa +
                ", nbDistributeurCo=" + nbDistributeurCo +
                ", nbMarchandisesBC=" + nbMarchandisesBC +
                ", nbMarchandisesBF=" + nbMarchandisesBF +
                ", nbMarchandisesSa=" + nbMarchandisesSa +
                ", nbMarchandisesCo=" + nbMarchandisesCo +
                ", prixMarchandiseBC=" + prixMarchandiseBC +
                ", prixMarchandiseBF=" + prixMarchandiseBF +
                ", prixMarchandiseSa=" + prixMarchandiseSa +
                ", prixMarchandiseCo=" + prixMarchandiseCo +
                ", vitesseBC=" + vitesseBC +
                ", vitesseBF=" + vitesseBF +
                ", vitesseSa=" + vitesseSa +
                ", vitesseCo=" + vitesseCo +
                '}';
    }
}
