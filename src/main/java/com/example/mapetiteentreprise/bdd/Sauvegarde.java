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
    private int livraison1Active;
    private int livraison2Active;
    private int livraison3Active;
    private int livraison4Active;
    private int livraison5Active;
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
    private int poulailler1; // correspond au type de poulailler
    private int poulailler2;
    private int poulailler3;
    private int poulailler4;

    // nombre de points de livraison
    private int nbLivraison1;
    private int nbLivraison2;
    private int nbLivraison3;
    private int nbLivraison4;
    private int nbLivraison5;
    // nombre de courses de livraisons à récupérer
    private long nbCourses1;
    private long nbCourses2;
    private long nbCourses3;
    private long nbCourses4;
    private long nbCourses5;
    // barres de progressions des Livraisons
    private double etatProgressLivraison1;
    private double etatProgressLivraison2;
    private double etatProgressLivraison3;
    private double etatProgressLivraison4;
    private double etatProgressLivraison5;

    // partie usines
    // activation des usines de textile
    private int usineTextileActive1;
    private int usineTextileActive2;
    private int usineTextileActive3;
    private int usineTextileActive4;

    // nombre d'usines de textile
    private int nbUsinesTextile1;
    private int nbUsinesTextile2;
    private int nbUsinesTextile3;
    private int nbUsinesTextile4;

    // nombre de marchandises fabriquées en usine de textile
    private long nbMarchandisesUsineTextile1;
    private long nbMarchandisesUsineTextile2;
    private long nbMarchandisesUsineTextile3;
    private long nbMarchandisesUsineTextile4;

    // barres de progression usines
    private double etatProgressUsineTextile1;
    private double etatProgressUsineTextile2;
    private double etatProgressUsineTextile3;
    private double etatProgressUsineTextile4;

    public Sauvegarde(int id, String pseudo, BigDecimal argent, long numJourDeco, int heureDeco, double progressJour, int nbPoules, long nbOeufs, int fermeActive, int distributeursActive, int distributeurBCActive, int distributeurBFActive, int distributeurCoActive, int distributeurSaActive, int livraison1Active, int livraison2Active, int livraison3Active, int livraison4Active, int livraison5Active, double etatProgressOeuf, LocalDateTime dateDeco, int nbDistributeurBC, int nbDistributeurBF, int nbDistributeurSa, int nbDistributeurCo, long nbMarchandisesBC, long nbMarchandisesBF, long nbMarchandisesSa, long nbMarchandisesCo, double etatProgressBC, double etatProgressBF, double etatProgressSa, double etatProgressCo, LocalDateTime dateDebutJeu, int poulailler1, int poulailler2, int poulailler3, int poulailler4, int nbLivraison1, int nbLivraison2, int nbLivraison3, int nbLivraison4, int nbLivraison5, long nbCourses1, long nbCourses2, long nbCourses3, long nbCourses4, long nbCourses5, double etatProgressLivraison1, double etatProgressLivraison2, double etatProgressLivraison3, double etatProgressLivraison4, double etatProgressLivraison5, int usineTextileActive1, int usineTextileActive2, int usineTextileActive3, int usineTextileActive4, int nbUsinesTextile1, int nbUsinesTextile2, int nbUsinesTextile3, int nbUsinesTextile4, long nbMarchandisesUsineTextile1, long nbMarchandisesUsineTextile2, long nbMarchandisesUsineTextile3, long nbMarchandisesUsineTextile4, double etatProgressUsineTextile1, double etatProgressUsineTextile2, double etatProgressUsineTextile3, double etatProgressUsineTextile4) {
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
        this.livraison1Active = livraison1Active;
        this.livraison2Active = livraison2Active;
        this.livraison3Active = livraison3Active;
        this.livraison4Active = livraison4Active;
        this.livraison5Active = livraison5Active;
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
        this.poulailler1 = poulailler1;
        this.poulailler2 = poulailler2;
        this.poulailler3 = poulailler3;
        this.poulailler4 = poulailler4;
        this.nbLivraison1 = nbLivraison1;
        this.nbLivraison2 = nbLivraison2;
        this.nbLivraison3 = nbLivraison3;
        this.nbLivraison4 = nbLivraison4;
        this.nbLivraison5 = nbLivraison5;
        this.nbCourses1 = nbCourses1;
        this.nbCourses2 = nbCourses2;
        this.nbCourses3 = nbCourses3;
        this.nbCourses4 = nbCourses4;
        this.nbCourses5 = nbCourses5;
        this.etatProgressLivraison1 = etatProgressLivraison1;
        this.etatProgressLivraison2 = etatProgressLivraison2;
        this.etatProgressLivraison3 = etatProgressLivraison3;
        this.etatProgressLivraison4 = etatProgressLivraison4;
        this.etatProgressLivraison5 = etatProgressLivraison5;
        this.usineTextileActive1 = usineTextileActive1;
        this.usineTextileActive2 = usineTextileActive2;
        this.usineTextileActive3 = usineTextileActive3;
        this.usineTextileActive4 = usineTextileActive4;
        this.nbUsinesTextile1 = nbUsinesTextile1;
        this.nbUsinesTextile2 = nbUsinesTextile2;
        this.nbUsinesTextile3 = nbUsinesTextile3;
        this.nbUsinesTextile4 = nbUsinesTextile4;
        this.nbMarchandisesUsineTextile1 = nbMarchandisesUsineTextile1;
        this.nbMarchandisesUsineTextile2 = nbMarchandisesUsineTextile2;
        this.nbMarchandisesUsineTextile3 = nbMarchandisesUsineTextile3;
        this.nbMarchandisesUsineTextile4 = nbMarchandisesUsineTextile4;
        this.etatProgressUsineTextile1 = etatProgressUsineTextile1;
        this.etatProgressUsineTextile2 = etatProgressUsineTextile2;
        this.etatProgressUsineTextile3 = etatProgressUsineTextile3;
        this.etatProgressUsineTextile4 = etatProgressUsineTextile4;
    }

    public Sauvegarde(String pseudo, BigDecimal argent, long numJourDeco, int heureDeco, double progressJour, int nbPoules, long nbOeufs, int fermeActive, int distributeursActive, int distributeurBCActive, int distributeurBFActive, int distributeurCoActive, int distributeurSaActive, int livraison1Active, int livraison2Active, int livraison3Active, int livraison4Active, int livraison5Active, double etatProgressOeuf, LocalDateTime dateDeco, int nbDistributeurBC, int nbDistributeurBF, int nbDistributeurSa, int nbDistributeurCo, long nbMarchandisesBC, long nbMarchandisesBF, long nbMarchandisesSa, long nbMarchandisesCo, double etatProgressBC, double etatProgressBF, double etatProgressSa, double etatProgressCo, LocalDateTime dateDebutJeu, int poulailler1, int poulailler2, int poulailler3, int poulailler4, int nbLivraison1, int nbLivraison2, int nbLivraison3, int nbLivraison4, int nbLivraison5, long nbCourses1, long nbCourses2, long nbCourses3, long nbCourses4, long nbCourses5, double etatProgressLivraison1, double etatProgressLivraison2, double etatProgressLivraison3, double etatProgressLivraison4, double etatProgressLivraison5, int usineTextileActive1, int usineTextileActive2, int usineTextileActive3, int usineTextileActive4, int nbUsinesTextile1, int nbUsinesTextile2, int nbUsinesTextile3, int nbUsinesTextile4, long nbMarchandisesUsineTextile1, long nbMarchandisesUsineTextile2, long nbMarchandisesUsineTextile3, long nbMarchandisesUsineTextile4, double etatProgressUsineTextile1, double etatProgressUsineTextile2, double etatProgressUsineTextile3, double etatProgressUsineTextile4) {
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
        this.livraison1Active = livraison1Active;
        this.livraison2Active = livraison2Active;
        this.livraison3Active = livraison3Active;
        this.livraison4Active = livraison4Active;
        this.livraison5Active = livraison5Active;
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
        this.poulailler1 = poulailler1;
        this.poulailler2 = poulailler2;
        this.poulailler3 = poulailler3;
        this.poulailler4 = poulailler4;
        this.nbLivraison1 = nbLivraison1;
        this.nbLivraison2 = nbLivraison2;
        this.nbLivraison3 = nbLivraison3;
        this.nbLivraison4 = nbLivraison4;
        this.nbLivraison5 = nbLivraison5;
        this.nbCourses1 = nbCourses1;
        this.nbCourses2 = nbCourses2;
        this.nbCourses3 = nbCourses3;
        this.nbCourses4 = nbCourses4;
        this.nbCourses5 = nbCourses5;
        this.etatProgressLivraison1 = etatProgressLivraison1;
        this.etatProgressLivraison2 = etatProgressLivraison2;
        this.etatProgressLivraison3 = etatProgressLivraison3;
        this.etatProgressLivraison4 = etatProgressLivraison4;
        this.etatProgressLivraison5 = etatProgressLivraison5;
        this.usineTextileActive1 = usineTextileActive1;
        this.usineTextileActive2 = usineTextileActive2;
        this.usineTextileActive3 = usineTextileActive3;
        this.usineTextileActive4 = usineTextileActive4;
        this.nbUsinesTextile1 = nbUsinesTextile1;
        this.nbUsinesTextile2 = nbUsinesTextile2;
        this.nbUsinesTextile3 = nbUsinesTextile3;
        this.nbUsinesTextile4 = nbUsinesTextile4;
        this.nbMarchandisesUsineTextile1 = nbMarchandisesUsineTextile1;
        this.nbMarchandisesUsineTextile2 = nbMarchandisesUsineTextile2;
        this.nbMarchandisesUsineTextile3 = nbMarchandisesUsineTextile3;
        this.nbMarchandisesUsineTextile4 = nbMarchandisesUsineTextile4;
        this.etatProgressUsineTextile1 = etatProgressUsineTextile1;
        this.etatProgressUsineTextile2 = etatProgressUsineTextile2;
        this.etatProgressUsineTextile3 = etatProgressUsineTextile3;
        this.etatProgressUsineTextile4 = etatProgressUsineTextile4;
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

    public int getLivraison5Active() {
        return livraison5Active;
    }

    public void setLivraison5Active(int livraison5Active) {
        this.livraison5Active = livraison5Active;
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

    public int getNbLivraison1() {
        return nbLivraison1;
    }

    public void setNbLivraison1(int nbLivraison1) {
        this.nbLivraison1 = nbLivraison1;
    }

    public int getNbLivraison2() {
        return nbLivraison2;
    }

    public void setNbLivraison2(int nbLivraison2) {
        this.nbLivraison2 = nbLivraison2;
    }

    public int getNbLivraison3() {
        return nbLivraison3;
    }

    public void setNbLivraison3(int nbLivraison3) {
        this.nbLivraison3 = nbLivraison3;
    }

    public int getNbLivraison4() {
        return nbLivraison4;
    }

    public void setNbLivraison4(int nbLivraison4) {
        this.nbLivraison4 = nbLivraison4;
    }

    public int getNbLivraison5() {
        return nbLivraison5;
    }

    public void setNbLivraison5(int nbLivraison5) {
        this.nbLivraison5 = nbLivraison5;
    }

    public long getNbCourses1() {
        return nbCourses1;
    }

    public void setNbCourses1(long nbCourses1) {
        this.nbCourses1 = nbCourses1;
    }

    public long getNbCourses2() {
        return nbCourses2;
    }

    public void setNbCourses2(long nbCourses2) {
        this.nbCourses2 = nbCourses2;
    }

    public long getNbCourses3() {
        return nbCourses3;
    }

    public void setNbCourses3(long nbCourses3) {
        this.nbCourses3 = nbCourses3;
    }

    public long getNbCourses4() {
        return nbCourses4;
    }

    public void setNbCourses4(long nbCourses4) {
        this.nbCourses4 = nbCourses4;
    }

    public long getNbCourses5() {
        return nbCourses5;
    }

    public void setNbCourses5(long nbCourses5) {
        this.nbCourses5 = nbCourses5;
    }

    public double getEtatProgressLivraison1() {
        return etatProgressLivraison1;
    }

    public void setEtatProgressLivraison1(double etatProgressLivraison1) {
        this.etatProgressLivraison1 = etatProgressLivraison1;
    }

    public double getEtatProgressLivraison2() {
        return etatProgressLivraison2;
    }

    public void setEtatProgressLivraison2(double etatProgressLivraison2) {
        this.etatProgressLivraison2 = etatProgressLivraison2;
    }

    public double getEtatProgressLivraison3() {
        return etatProgressLivraison3;
    }

    public void setEtatProgressLivraison3(double etatProgressLivraison3) {
        this.etatProgressLivraison3 = etatProgressLivraison3;
    }

    public double getEtatProgressLivraison4() {
        return etatProgressLivraison4;
    }

    public void setEtatProgressLivraison4(double etatProgressLivraison4) {
        this.etatProgressLivraison4 = etatProgressLivraison4;
    }

    public double getEtatProgressLivraison5() {
        return etatProgressLivraison5;
    }

    public void setEtatProgressLivraison5(double etatProgressLivraison5) {
        this.etatProgressLivraison5 = etatProgressLivraison5;
    }

    public int getUsineTextileActive1() {
        return usineTextileActive1;
    }

    public void setUsineTextileActive1(int usineTextileActive1) {
        this.usineTextileActive1 = usineTextileActive1;
    }

    public int getUsineTextileActive2() {
        return usineTextileActive2;
    }

    public void setUsineTextileActive2(int usineTextileActive2) {
        this.usineTextileActive2 = usineTextileActive2;
    }

    public int getUsineTextileActive3() {
        return usineTextileActive3;
    }

    public void setUsineTextileActive3(int usineTextileActive3) {
        this.usineTextileActive3 = usineTextileActive3;
    }

    public int getUsineTextileActive4() {
        return usineTextileActive4;
    }

    public void setUsineTextileActive4(int usineTextileActive4) {
        this.usineTextileActive4 = usineTextileActive4;
    }

    public int getNbUsinesTextile1() {
        return nbUsinesTextile1;
    }

    public void setNbUsinesTextile1(int nbUsinesTextile1) {
        this.nbUsinesTextile1 = nbUsinesTextile1;
    }

    public int getNbUsinesTextile2() {
        return nbUsinesTextile2;
    }

    public void setNbUsinesTextile2(int nbUsinesTextile2) {
        this.nbUsinesTextile2 = nbUsinesTextile2;
    }

    public int getNbUsinesTextile3() {
        return nbUsinesTextile3;
    }

    public void setNbUsinesTextile3(int nbUsinesTextile3) {
        this.nbUsinesTextile3 = nbUsinesTextile3;
    }

    public int getNbUsinesTextile4() {
        return nbUsinesTextile4;
    }

    public void setNbUsinesTextile4(int nbUsinesTextile4) {
        this.nbUsinesTextile4 = nbUsinesTextile4;
    }

    public long getNbMarchandisesUsineTextile1() {
        return nbMarchandisesUsineTextile1;
    }

    public void setNbMarchandisesUsineTextile1(long nbMarchandisesUsineTextile1) {
        this.nbMarchandisesUsineTextile1 = nbMarchandisesUsineTextile1;
    }

    public long getNbMarchandisesUsineTextile2() {
        return nbMarchandisesUsineTextile2;
    }

    public void setNbMarchandisesUsineTextile2(long nbMarchandisesUsineTextile2) {
        this.nbMarchandisesUsineTextile2 = nbMarchandisesUsineTextile2;
    }

    public long getNbMarchandisesUsineTextile3() {
        return nbMarchandisesUsineTextile3;
    }

    public void setNbMarchandisesUsineTextile3(long nbMarchandisesUsineTextile3) {
        this.nbMarchandisesUsineTextile3 = nbMarchandisesUsineTextile3;
    }

    public long getNbMarchandisesUsineTextile4() {
        return nbMarchandisesUsineTextile4;
    }

    public void setNbMarchandisesUsineTextile4(long nbMarchandisesUsineTextile4) {
        this.nbMarchandisesUsineTextile4 = nbMarchandisesUsineTextile4;
    }

    public double getEtatProgressUsineTextile1() {
        return etatProgressUsineTextile1;
    }

    public void setEtatProgressUsineTextile1(double etatProgressUsineTextile1) {
        this.etatProgressUsineTextile1 = etatProgressUsineTextile1;
    }

    public double getEtatProgressUsineTextile2() {
        return etatProgressUsineTextile2;
    }

    public void setEtatProgressUsineTextile2(double etatProgressUsineTextile2) {
        this.etatProgressUsineTextile2 = etatProgressUsineTextile2;
    }

    public double getEtatProgressUsineTextile3() {
        return etatProgressUsineTextile3;
    }

    public void setEtatProgressUsineTextile3(double etatProgressUsineTextile3) {
        this.etatProgressUsineTextile3 = etatProgressUsineTextile3;
    }

    public double getEtatProgressUsineTextile4() {
        return etatProgressUsineTextile4;
    }

    public void setEtatProgressUsineTextile4(double etatProgressUsineTextile4) {
        this.etatProgressUsineTextile4 = etatProgressUsineTextile4;
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
                ", livraison1Active=" + livraison1Active +
                ", livraison2Active=" + livraison2Active +
                ", livraison3Active=" + livraison3Active +
                ", livraison4Active=" + livraison4Active +
                ", livraison5Active=" + livraison5Active +
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
                ", poulailler1=" + poulailler1 +
                ", poulailler2=" + poulailler2 +
                ", poulailler3=" + poulailler3 +
                ", poulailler4=" + poulailler4 +
                ", nbLivraison1=" + nbLivraison1 +
                ", nbLivraison2=" + nbLivraison2 +
                ", nbLivraison3=" + nbLivraison3 +
                ", nbLivraison4=" + nbLivraison4 +
                ", nbLivraison5=" + nbLivraison5 +
                ", nbCourses1=" + nbCourses1 +
                ", nbCourses2=" + nbCourses2 +
                ", nbCourses3=" + nbCourses3 +
                ", nbCourses4=" + nbCourses4 +
                ", nbCourses5=" + nbCourses5 +
                ", etatProgressLivraison1=" + etatProgressLivraison1 +
                ", etatProgressLivraison2=" + etatProgressLivraison2 +
                ", etatProgressLivraison3=" + etatProgressLivraison3 +
                ", etatProgressLivraison4=" + etatProgressLivraison4 +
                ", etatProgressLivraison5=" + etatProgressLivraison5 +
                ", usineTextileActive1=" + usineTextileActive1 +
                ", usineTextileActive2=" + usineTextileActive2 +
                ", usineTextileActive3=" + usineTextileActive3 +
                ", usineTextileActive4=" + usineTextileActive4 +
                ", nbUsinesTextile1=" + nbUsinesTextile1 +
                ", nbUsinesTextile2=" + nbUsinesTextile2 +
                ", nbUsinesTextile3=" + nbUsinesTextile3 +
                ", nbUsinesTextile4=" + nbUsinesTextile4 +
                ", nbMarchandisesUsineTextile1=" + nbMarchandisesUsineTextile1 +
                ", nbMarchandisesUsineTextile2=" + nbMarchandisesUsineTextile2 +
                ", nbMarchandisesUsineTextile3=" + nbMarchandisesUsineTextile3 +
                ", nbMarchandisesUsineTextile4=" + nbMarchandisesUsineTextile4 +
                ", etatProgressUsineTextile1=" + etatProgressUsineTextile1 +
                ", etatProgressUsineTextile2=" + etatProgressUsineTextile2 +
                ", etatProgressUsineTextile3=" + etatProgressUsineTextile3 +
                ", etatProgressUsineTextile4=" + etatProgressUsineTextile4 +
                '}';
    }
}
