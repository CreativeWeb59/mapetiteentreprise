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

        // recupere le nombre de retard de paiement
        // si c'est egal à 0
        // alors on modifie les dates
        // sinon on modifie juste la date de paiement d'un remboursement

        if (nbRetardMensualite(jourEnCours) == 0) {
            // date de prochaine mensualite
            this.setDateProchaineMensualite(dateProchaineMensualite(jourEnCours)); // paiement normal
            this.setDateDerniereMensualite(jourEnCours);
        } else if ((nbRetardMensualite(jourEnCours) < 0)) {
            // cas du joueur qui paie en avance
            this.setDateProchaineMensualite(getDateProchaineMensualite() + cycleMensualite); // paiement en avance
            this.setDateDerniereMensualite(jourEnCours);
        }

        // manque maj date derniere mensualite
    }

    /**
     * Rembourse le crédit entierement
     */
    public void rembourserCredit(long jourEnCours) {
        // montant restant a payer
        this.setMontantRembourse(getCoutPret());
        // date de prochaine mensualite
        this.setDateDerniereMensualite(jourEnCours);
        this.setDateProchaineMensualite(jourEnCours);
        this.setTermine(1);
    }


    /**
     * Calcule la date de la prochaine mensualite
     * par rapport au nombre d'echeance de crédit obligatoires
     * et au jourEnCours
     *
     * @param jourEnCours
     * @return
     */
    public long dateProchaineMensualite(long jourEnCours) {
        // date de prochaine mensualite
        // egale ((debut du pret + mensualites obligtoires) * cycle +1)
        long resultat = ((this.getDateDebutCredit() + this.nombreEcheancesObligatoires(jourEnCours)) * this.getCycleMensualite() + 1);
        System.out.println("Date prochaine mensualite (resultat) " + resultat);
        return resultat;
    }

    /**
     * Calcule le nombre de mensualites en retard
     */
    public long nbRetardMensualite(long jourEnCours) {
        long nbMensualitesPayees = nombreMensualitesPayees();
        long nbEcheancesObligatoires = nombreEcheancesObligatoires(jourEnCours);

        return nbEcheancesObligatoires - nbMensualitesPayees;
    }

    /**
     * Calcule le nombre de mensualites deja payées
     */
    public long nombreMensualitesPayees() {
        // divise montant paye par valeur mensualite => nb de mensualites payées
        System.out.println("Nombre de mensualites payées : " + getMontantRembourse().divide(getMensualite()).intValue());
        return (getMontantRembourse().divide(getMensualite()).intValue());
    }

    /**
     * Renvoi le nombre d'echeance minimum a effectuer au credi suivant la date du jour
     * prends la date du debut du credit
     *
     * @param jourEnCours
     * @return
     */
    public long nombreEcheancesObligatoires(long jourEnCours) {
        // prends la date du debut du credit
        // prends le jour en cours - date debut credit et divise par le cyle => trouve le nombre d'echeances obligatoires

        System.out.println("Nombre d'échéances obligatoires : " + (jourEnCours - this.getDateDebutCredit()) / this.getCycleMensualite());
        return (jourEnCours - this.getDateDebutCredit()) / this.getCycleMensualite();
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
