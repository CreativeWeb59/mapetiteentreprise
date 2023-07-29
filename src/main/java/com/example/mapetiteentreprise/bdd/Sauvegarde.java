package com.example.mapetiteentreprise.bdd;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Sauvegarde {
    private int id;
    private String pseudo;
    private BigDecimal argent;
    public long numJourDeco;
    public int heureDeco; // chiffre de 1 à 10 => 1 jour = 10 fois 60s ou 10 fois un progressOeuf
    public double progressJour;
    private int nbPoules;
    private long nbOeufs;
    private int fermeActive;
    private int distributeursActive;
    private int distributeurBCActive;
    private int distributeurBFActive;
    private int distributeurCoActive;
    private int distributeurSaActive;
    private int livraisonActive;
    private int lavageActive;
    private double etatProgressOeuf;
    private LocalDateTime dateDeco;
    // partie des distributeurs

    // nombre des distributeurs
    private int nbDistributeurBC;
    private int nbDistributeurBF;
    private int nbDistributeurSa;
    private int nbDistributeurCo;

    // nombre de marchandises contenues dans les distributeurs
    private long nbMarchandisesBC;
    private long nbMarchandisesBF;
    private long nbMarchandisesSa;
    private long nbMarchandisesCo;

    // barres de progressions des distributeurs
    private double etatProgressBC;
    private double etatProgressBF;
    private double etatProgressSa;
    private double etatProgressCo;

    // gestion du calendrier
    private LocalDateTime dateDebutJeu;

    public Sauvegarde(int id, String pseudo, BigDecimal argent, long numJourDeco, int heureDeco, double progressJour, int nbPoules, long nbOeufs, int fermeActive, int distributeursActive, int distributeurBCActive, int distributeurBFActive, int distributeurCoActive, int distributeurSaActive, int livraisonActive, int lavageActive, double etatProgressOeuf, LocalDateTime dateDeco, int nbDistributeurBC, int nbDistributeurBF, int nbDistributeurSa, int nbDistributeurCo, long nbMarchandisesBC, long nbMarchandisesBF, long nbMarchandisesSa, long nbMarchandisesCo, double etatProgressBC, double etatProgressBF, double etatProgressSa, double etatProgressCo, LocalDateTime dateDebutJeu) {
        this.id = id;
        this.pseudo = pseudo;
        this.argent = argent;
        this.numJourDeco = numJourDeco;
        this.heureDeco = heureDeco;
        this.progressJour = progressJour;
        this.nbPoules = nbPoules;
        this.nbOeufs = nbOeufs;
        this.fermeActive = fermeActive;
        this.distributeursActive = distributeursActive;
        this.distributeurBCActive = distributeurBCActive;
        this.distributeurBFActive = distributeurBFActive;
        this.distributeurCoActive = distributeurCoActive;
        this.distributeurSaActive = distributeurSaActive;
        this.livraisonActive = livraisonActive;
        this.lavageActive = lavageActive;
        this.etatProgressOeuf = etatProgressOeuf;
        this.dateDeco = dateDeco;
        this.nbDistributeurBC = nbDistributeurBC;
        this.nbDistributeurBF = nbDistributeurBF;
        this.nbDistributeurSa = nbDistributeurSa;
        this.nbDistributeurCo = nbDistributeurCo;
        this.nbMarchandisesBC = nbMarchandisesBC;
        this.nbMarchandisesBF = nbMarchandisesBF;
        this.nbMarchandisesSa = nbMarchandisesSa;
        this.nbMarchandisesCo = nbMarchandisesCo;
        this.etatProgressBC = etatProgressBC;
        this.etatProgressBF = etatProgressBF;
        this.etatProgressSa = etatProgressSa;
        this.etatProgressCo = etatProgressCo;
        this.dateDebutJeu = dateDebutJeu;
    }

    public Sauvegarde(String pseudo, BigDecimal argent, long numJourDeco, int heureDeco, double progressJour, int nbPoules, long nbOeufs, int fermeActive, int distributeursActive, int distributeurBCActive, int distributeurBFActive, int distributeurCoActive, int distributeurSaActive, int livraisonActive, int lavageActive, double etatProgressOeuf, LocalDateTime dateDeco, int nbDistributeurBC, int nbDistributeurBF, int nbDistributeurSa, int nbDistributeurCo, long nbMarchandisesBC, long nbMarchandisesBF, long nbMarchandisesSa, long nbMarchandisesCo, double etatProgressBC, double etatProgressBF, double etatProgressSa, double etatProgressCo, LocalDateTime dateDebutJeu) {
        this.pseudo = pseudo;
        this.argent = argent;
        this.numJourDeco = numJourDeco;
        this.heureDeco = heureDeco;
        this.progressJour = progressJour;
        this.nbPoules = nbPoules;
        this.nbOeufs = nbOeufs;
        this.fermeActive = fermeActive;
        this.distributeursActive = distributeursActive;
        this.distributeurBCActive = distributeurBCActive;
        this.distributeurBFActive = distributeurBFActive;
        this.distributeurCoActive = distributeurCoActive;
        this.distributeurSaActive = distributeurSaActive;
        this.livraisonActive = livraisonActive;
        this.lavageActive = lavageActive;
        this.etatProgressOeuf = etatProgressOeuf;
        this.dateDeco = dateDeco;
        this.nbDistributeurBC = nbDistributeurBC;
        this.nbDistributeurBF = nbDistributeurBF;
        this.nbDistributeurSa = nbDistributeurSa;
        this.nbDistributeurCo = nbDistributeurCo;
        this.nbMarchandisesBC = nbMarchandisesBC;
        this.nbMarchandisesBF = nbMarchandisesBF;
        this.nbMarchandisesSa = nbMarchandisesSa;
        this.nbMarchandisesCo = nbMarchandisesCo;
        this.etatProgressBC = etatProgressBC;
        this.etatProgressBF = etatProgressBF;
        this.etatProgressSa = etatProgressSa;
        this.etatProgressCo = etatProgressCo;
        this.dateDebutJeu = dateDebutJeu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public long getNbMarchandisesBC() {
        return nbMarchandisesBC;
    }

    public void setNbMarchandisesBC(long nbMarchandisesBC) {
        this.nbMarchandisesBC = nbMarchandisesBC;
    }

    public long getNbMarchandisesBF() {
        return nbMarchandisesBF;
    }

    public void setNbMarchandisesBF(long nbMarchandisesBF) {
        this.nbMarchandisesBF = nbMarchandisesBF;
    }

    public long getNbMarchandisesSa() {
        return nbMarchandisesSa;
    }

    public void setNbMarchandisesSa(long nbMarchandisesSa) {
        this.nbMarchandisesSa = nbMarchandisesSa;
    }

    public long getNbMarchandisesCo() {
        return nbMarchandisesCo;
    }

    public void setNbMarchandisesCo(long nbMarchandisesCo) {
        this.nbMarchandisesCo = nbMarchandisesCo;
    }

    public double getEtatProgressBC() {
        return etatProgressBC;
    }

    public void setEtatProgressBC(double etatProgressBC) {
        this.etatProgressBC = etatProgressBC;
    }

    public double getEtatProgressBF() {
        return etatProgressBF;
    }

    public void setEtatProgressBF(double etatProgressBF) {
        this.etatProgressBF = etatProgressBF;
    }

    public double getEtatProgressSa() {
        return etatProgressSa;
    }

    public void setEtatProgressSa(double etatProgressSa) {
        this.etatProgressSa = etatProgressSa;
    }

    public double getEtatProgressCo() {
        return etatProgressCo;
    }

    public void setEtatProgressCo(double etatProgressCo) {
        this.etatProgressCo = etatProgressCo;
    }

    public LocalDateTime getDateDebutJeu() {
        return dateDebutJeu;
    }

    public void setDateDebutJeu(LocalDateTime dateDebutJeu) {
        this.dateDebutJeu = dateDebutJeu;
    }

    public long getNumJourDeco() {
        return numJourDeco;
    }

    public void setNumJourDeco(long numJourDeco) {
        this.numJourDeco = numJourDeco;
    }

    public int getHeureDeco() {
        return heureDeco;
    }

    public void setHeureDeco(int heureDeco) {
        this.heureDeco = heureDeco;
    }

    public double getProgressJour() {
        return progressJour;
    }

    public void setProgressJour(double progressJour) {
        this.progressJour = progressJour;
    }

    @Override
    public String toString() {
        return "Sauvegarde{" +
                "id=" + id +
                ", pseudo='" + pseudo + '\'' +
                ", argent=" + argent +
                ", numJourDeco=" + numJourDeco +
                ", heureDeco=" + heureDeco +
                ", progressJour=" + progressJour +
                ", nbPoules=" + nbPoules +
                ", nbOeufs=" + nbOeufs +
                ", fermeActive=" + fermeActive +
                ", distributeursActive=" + distributeursActive +
                ", distributeurBCActive=" + distributeurBCActive +
                ", distributeurBFActive=" + distributeurBFActive +
                ", distributeurCoActive=" + distributeurCoActive +
                ", distributeurSaActive=" + distributeurSaActive +
                ", livraisonActive=" + livraisonActive +
                ", lavageActive=" + lavageActive +
                ", etatProgressOeuf=" + etatProgressOeuf +
                ", dateDeco=" + dateDeco +
                ", nbDistributeurBC=" + nbDistributeurBC +
                ", nbDistributeurBF=" + nbDistributeurBF +
                ", nbDistributeurSa=" + nbDistributeurSa +
                ", nbDistributeurCo=" + nbDistributeurCo +
                ", nbMarchandisesBC=" + nbMarchandisesBC +
                ", nbMarchandisesBF=" + nbMarchandisesBF +
                ", nbMarchandisesSa=" + nbMarchandisesSa +
                ", nbMarchandisesCo=" + nbMarchandisesCo +
                ", etatProgressBC=" + etatProgressBC +
                ", etatProgressBF=" + etatProgressBF +
                ", etatProgressSa=" + etatProgressSa +
                ", etatProgressCo=" + etatProgressCo +
                ", dateDebutJeu=" + dateDebutJeu +
                '}';
    }
}
