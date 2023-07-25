package com.example.mapetiteentreprise.jeu;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreditEnCours {
    private BigDecimal montantPret;
    private BigDecimal coutPret;
    private BigDecimal montantRembourse;
    private BigDecimal mensualite;
    private int nbMMensualite;
    private int cycleMensualite;
    private int termine; // 0 credit en cours, 1 credit termine
    private LocalDateTime dateDebutCredit;
    private LocalDateTime dateDerniereMensualite;

    public CreditEnCours(BigDecimal montantPret, BigDecimal coutPret, BigDecimal montantRembourse, BigDecimal mensualite, int nbMMensualite, int cycleMensualite, int termine, LocalDateTime dateDebutCredit, LocalDateTime dateDerniereMensualite) {
        this.montantPret = montantPret;
        this.coutPret = coutPret;
        this.montantRembourse = montantRembourse;
        this.mensualite = mensualite;
        this.nbMMensualite = nbMMensualite;
        this.cycleMensualite = cycleMensualite;
        this.termine = termine;
        this.dateDebutCredit = dateDebutCredit;
        this.dateDerniereMensualite = dateDerniereMensualite;
    }

    public BigDecimal getMontantPret() {
        return montantPret;
    }

    public void setMontantPret(BigDecimal montantPret) {
        this.montantPret = montantPret;
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

    public LocalDateTime getDateDebutCredit() {
        return dateDebutCredit;
    }

    public void setDateDebutCredit(LocalDateTime dateDebutCredit) {
        this.dateDebutCredit = dateDebutCredit;
    }

    public LocalDateTime getDateDerniereMensualite() {
        return dateDerniereMensualite;
    }

    public void setDateDerniereMensualite(LocalDateTime dateDerniereMensualite) {
        this.dateDerniereMensualite = dateDerniereMensualite;
    }

    /**
     * Calcule la mensualite en cours
     * @return
     */

    public void mensualiteEnCours(){
        // nb mensualite : nbMMensualite

        BigDecimal montantRestantDu = coutPret.subtract(montantRembourse);

    }

    /**
     * Calcule le nombre de mensualites payees
     */
    public void nbMensualiteEffectuee(){
//        BigDecimal montantPaye = coutPret.subtract(montantRembourse);
        BigDecimal nbMensualitesPayes = montantRembourse.divide(mensualite);
//        System.out.println("Montant paye : " + montantPaye);
        System.out.println("Mensualite : " + mensualite);
        System.out.println("nombre mensualites payees " + nbMensualitesPayes);
    }

    @Override
    public String toString() {
        return "CreditEnCours{" +
                "montantPret=" + montantPret +
                ", coutPret=" + coutPret +
                ", montantRembourse=" + montantRembourse +
                ", mensualite=" + mensualite +
                ", nbMMensualite=" + nbMMensualite +
                ", cycleMensualite=" + cycleMensualite +
                ", termine=" + termine +
                ", dateDebutCredit=" + dateDebutCredit +
                '}';
    }
}
