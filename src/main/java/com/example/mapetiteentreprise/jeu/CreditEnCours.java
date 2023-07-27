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
    private long dateDebutCredit;
    private long dateDerniereMensualite;
    private long dateProchaineMensualite;
    private long datePreavis;
    private int blocageDatePreavis; // 0 pas encore attribué // 1 attribué

    public CreditEnCours(BigDecimal montantPret, BigDecimal coutPret, BigDecimal montantRembourse, BigDecimal mensualite,
                         int nbMMensualite, int cycleMensualite, int termine,
                         long dateDebutCredit, long dateDerniereMensualite, long dateProchaineMensualite, long datePreavis, int blocageDatePreavis) {
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

    public long getDateDebutCredit() {
        return dateDebutCredit;
    }

    public void setDateDebutCredit(long dateDebutCredit) {
        this.dateDebutCredit = dateDebutCredit;
    }

    public long getDateDerniereMensualite() {
        return dateDerniereMensualite;
    }

    public void setDateDerniereMensualite(long dateDerniereMensualite) {
        this.dateDerniereMensualite = dateDerniereMensualite;
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

    /**
     * Calcule la mensualite en cours
     *
     * @return
     */

    public void mensualiteEnCours() {
        // nb mensualite : nbMMensualite

        BigDecimal montantRestantDu = coutPret.subtract(montantRembourse);

    }




    /**
     * Calcule le jour de la prochaine mensualite
     *
     * @param dureeJour
     */
    public void majDateProchaineMensualite(int dureeJour) {
        int nbPrelevement = nbPrelevements();
        long ecartProchainPrelevement = (cycleMensualite * (nbPrelevement + 1) / dureeJour) + dateDebutCredit;

        System.out.println("jour prochaine mensualite : " + ecartProchainPrelevement);
        this.dateProchaineMensualite = ecartProchainPrelevement;
    }

    /**
     * retourne le nombre de prelevements effectues
     *
     * @return
     */
    public int nbPrelevements() {
        BigDecimal nbMensualitesPayes = montantRembourse.divide(mensualite);
        return nbMensualitesPayes.intValue();
    }

    public long getDateProchaineMensualite() {
        return dateProchaineMensualite;
    }

    public void setDateProchaineMensualite(long dateProchaineMensualite) {
        this.dateProchaineMensualite = dateProchaineMensualite;
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

    /**
     * Renvoi le montant restant a rembourser
     *
     * @return
     */
//    public BigDecimal montantEcheance(){
//        // montant total a rembourser
//        BigDecimal  aRembourser = this.coutPret.subtract(this.montantRembourse);
//
//        // compare si montant du credit restant superieur a la mensualite => remboursement mensualite
//        // sinon => remboursement restant du credit, et on déclenche le solde du credit
//
//        return mensualite;
//
//    }
    public void payerMensualite(long jourEnCours) {
        // ajoute le montant rembourse
        BigDecimal ajout = this.getMontantRembourse().add(this.mensualite);
        this.setMontantRembourse(ajout);

        // calcul prochaine echeance
        BigDecimal restantCredit = this.coutPret.subtract(this.montantRembourse);
        int comparaison = restantCredit.compareTo(mensualite);
        if (comparaison <= 0) {
            System.out.println("avant derniere echeance du pret");
            this.setMensualite(restantCredit);
        }

        // verifie si credit termine
        comparaison = this.coutPret.compareTo(this.montantRembourse);
        if (comparaison <= 0) {
            System.out.println("credit termine et paye");
            this.termine = 1;
        }

        // verfifie si date depassée sinon rembourse le crédit avec les bonnes dates pour les mensualites
        if(this.getDateProchaineMensualite() >= jourEnCours){
            this.setDateProchaineMensualite(this.getDateProchaineMensualite() + cycleMensualite);
            this.setDateDerniereMensualite(jourEnCours);
            this.setDatePreavis(jourEnCours + cycleMensualite + 2);
            this.setBlocageDatePreavis(1);
        } else {
            this.setDateDerniereMensualite(jourEnCours);
        }

        // modifie la date de la prochaine mensualite

        this.setDateProchaineMensualite(this.getDateDerniereMensualite() + cycleMensualite);
        this.setDateDerniereMensualite(jourEnCours);
    }

    /**
     * Calcule le nombre de mensualites en retard
     */
    public long nbRetardMensualite(long jourEnCours) {
        long datePrevueProchaineMensualite = this.getDateProchaineMensualite();
        System.out.println("date prevue prochaine mensualite " + this.getDateProchaineMensualite());
        System.out.println("Cycle mensualite : " + getCycleMensualite());
        if (datePrevueProchaineMensualite < jourEnCours) {
            long nbMensualitesAPayer = 0;
            while (datePrevueProchaineMensualite < jourEnCours) {
                datePrevueProchaineMensualite += getCycleMensualite();
                System.out.println(" date prochaine mensualite" + datePrevueProchaineMensualite);
                nbMensualitesAPayer ++ ;
            }

            System.out.println("retard de paiement de : " + nbMensualitesAPayer);
            return nbMensualitesAPayer;

        } else {
            System.out.println("Pas de retard de paiement");
            return 0;
        }
    }

    /**
     * Calcule le nombre de mensualites payees
     */
    public long nbMensualitesPayees() {
        return this.montantRembourse.divide(mensualite).longValue();
    }


    /**
     * Calcule le montant des mensualites en retard
     *
     * @param nbRetard
     * @return
     */
    public BigDecimal montantRetardMensualite(int nbRetard) {
        return BigDecimal.valueOf(10);
    }
}
