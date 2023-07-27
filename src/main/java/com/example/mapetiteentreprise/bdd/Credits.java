package com.example.mapetiteentreprise.bdd;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Credits {
    private int id;
    private String pseudo;
    private BigDecimal montantPret;
    private BigDecimal coutPret;
    private BigDecimal montantRembourse;
    private BigDecimal mensualite;
    private int nbMMensualite;
    private int cycleMensualite;
    private int termine; // 0 credit en cours, 1 credit termine
    private long dateDebutCredit; // correspond au jour du jeu auquel on fait le crédit
    private long dateDerniereMensualite;
    private long dateProchaineMensualite;
    private long datePreavis;
    private int blocageDatePreavis;

    public Credits() {
    }

    public Credits(int id, String pseudo, BigDecimal montantPret, BigDecimal coutPret, BigDecimal montantRembourse, BigDecimal mensualite,
                   int nbMMensualite, int cycleMensualite, int termine, long dateDebutCredit,
                   long dateDerniereMensualite, long dateProchaineMensualite, long datePreavis, int blocageDatePreavis) {
        this.id = id;
        this.pseudo = pseudo;
        this.montantPret = montantPret;
        this.coutPret = coutPret;
        this.montantRembourse = montantRembourse;
        this.mensualite = mensualite;
        this.nbMMensualite = nbMMensualite;
        this.cycleMensualite = cycleMensualite;
        this.termine = termine;
        this.dateDebutCredit = dateDebutCredit;
        this.dateDerniereMensualite = dateDerniereMensualite;
        this.dateProchaineMensualite = dateProchaineMensualite;
        this.datePreavis = datePreavis;
        this.blocageDatePreavis = blocageDatePreavis;
    }

    public Credits(String pseudo, BigDecimal montantPret, BigDecimal coutPret, BigDecimal montantRembourse, BigDecimal mensualite, int nbMMensualite, int cycleMensualite, int termine, long dateDebutCredit, long dateDerniereMensualite, long dateProchaineMensualite, long datePreavis, int blocageDatePreavis) {
        this.pseudo = pseudo;
        this.montantPret = montantPret;
        this.coutPret = coutPret;
        this.montantRembourse = montantRembourse;
        this.mensualite = mensualite;
        this.nbMMensualite = nbMMensualite;
        this.cycleMensualite = cycleMensualite;
        this.termine = termine;
        this.dateDebutCredit = dateDebutCredit;
        this.dateDerniereMensualite = dateDerniereMensualite;
        this.dateProchaineMensualite = dateProchaineMensualite;
        this.datePreavis = datePreavis;
        this.blocageDatePreavis = blocageDatePreavis;
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

    public BigDecimal getMontantPret() {
        return montantPret;
    }

    public void setMontantPret(BigDecimal montantPret) {
        this.montantPret = montantPret;
    }

    public BigDecimal getMensualite() {
        return mensualite;
    }

    public void setMensualite(BigDecimal mensualite) {
        this.mensualite = mensualite;
    }

    public int getNbMMensualite() {
        return nbMMensualite;
    }

    public void setNbMMensualite(int nbMMensualite) {
        this.nbMMensualite = nbMMensualite;
    }

    public int getCycleMensualite() {
        return cycleMensualite;
    }

    public void setCycleMensualite(int cycleMensualite) {
        this.cycleMensualite = cycleMensualite;
    }

    public int getTermine() {
        return termine;
    }

    public void setTermine(int termine) {
        this.termine = termine;
    }

    public long getDateDebutCredit() {
        return dateDebutCredit;
    }

    public void setDateDebutCredit(long dateDebutCredit) {
        this.dateDebutCredit = dateDebutCredit;
    }

    public BigDecimal getCoutPret() {
        return coutPret;
    }

    public void setCoutPret(BigDecimal coutPret) {
        this.coutPret = coutPret;
    }

    public BigDecimal getMontantRembourse() {
        return montantRembourse;
    }

    public void setMontantRembourse(BigDecimal montantRembourse) {
        this.montantRembourse = montantRembourse;
    }

    public long getDateDerniereMensualite() {
        return dateDerniereMensualite;
    }

    public void setDateDerniereMensualite(long dateDerniereMensualite) {
        this.dateDerniereMensualite = dateDerniereMensualite;
    }

    public long getDateProchaineMensualite() {
        return dateProchaineMensualite;
    }

    public void setDateProchaineMensualite(long dateProchaineMensualite) {
        this.dateProchaineMensualite = dateProchaineMensualite;
    }

    public long getDatePreavis() {
        return datePreavis;
    }

    public void setDatePreavis(long datePreavis) {
        this.datePreavis = datePreavis;
    }

    public int getBlocageDatePreavis() {
        return blocageDatePreavis;
    }

    public void setBlocageDatePreavis(int blocageDatePreavis) {
        this.blocageDatePreavis = blocageDatePreavis;
    }
}
