package com.example.mapetiteentreprise.jeu;

import com.example.mapetiteentreprise.bdd.*;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Jeu {
    private Joueur joueur;
    private Sauvegarde sauvegarde;
    private Parametres parametres;
    private Calendrier calendrier;
    // Liste de barres de progression, par defaut
    // la 0 est toujours la progress jour
    // la 2 est celle de la ferme
    // la 3, 4, 5, 6 celle des distributeurs...
    private List<AnimationsBarresProgress> barresDeProgressions = new ArrayList<>();

    private List<Poulaillers> poulaillersList = new ArrayList<>(); // liste des poulaillers dispos

    public Jeu(Joueur joueur, Sauvegarde sauvegarde, Parametres parametres, Calendrier calendrier) {
        this.joueur = joueur;
        this.sauvegarde = sauvegarde;
        this.parametres = parametres;
        this.calendrier = calendrier;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public Sauvegarde getSauvegarde() {
        return sauvegarde;
    }

    public void setSauvegarde(Sauvegarde sauvegarde) {
        this.sauvegarde = sauvegarde;
    }

    public Parametres getParametres() {
        return parametres;
    }

    public void setParametres(Parametres parametres) {
        this.parametres = parametres;
    }

    public Calendrier getCalendrier() {
        return calendrier;
    }

    public void setCalendrier(Calendrier calendrier) {
        this.calendrier = calendrier;
    }

    public List<AnimationsBarresProgress> getBarresDeProgressions() {
        return barresDeProgressions;
    }

    /**
     * Ajoute une barre de progression
     * @param barresDeProgressions
     */
    public void setUneBarreDeProgression(AnimationsBarresProgress barresDeProgressions) {
        this.barresDeProgressions.add(barresDeProgressions);
    }

    /**
     * fait appel a une methode de la classe AnimationBarreProgress
     * @return
     */
    public void creerTimelineJournee(int cycle, double vitesse) {
        int index = 0;
        if (index >= 0 && index < barresDeProgressions.size()) {
            AnimationsBarresProgress barreDeProgression = barresDeProgressions.get(index);
            barreDeProgression.createProgressJournee(cycle, vitesse);
        }
    }

    public void playTimelineJournee(int cycle, double vitesse) {
        int index = 0;
        if (index >= 0 && index < barresDeProgressions.size()) {
            AnimationsBarresProgress barreDeProgression = barresDeProgressions.get(index);
            barreDeProgression.start();
            System.out.println("start progress");
        }
    }

    public void rePlayTimelineJournee(double value) {
        int index = 0;
        if (index >= 0 && index < barresDeProgressions.size()) {
            AnimationsBarresProgress barreDeProgression = barresDeProgressions.get(index);
            Platform.runLater(() -> barreDeProgression.updateProgressBar(value));
        }
    }

    @Override
    public String toString() {
        return "Jeu{" +
                "joueur=" + joueur +
                ", sauvegarde=" + sauvegarde +
                ", parametres=" + parametres +
                ", calendrier=" + calendrier +
                ", barresDeProgressions=" + barresDeProgressions +
                '}';
    }

    /**
     * Permet de sauvegarder la partie dans la Bdd
     */
    public void sauvegardejeu(ProgressBar progressOeufs, ProgressBar progressJour) {
        // sauvegarde des barres de progression
        getCalendrier().setProgressJour(progressJour.getProgress());
        getJoueur().getFerme().setEtatProgressOeuf(progressOeufs.getProgress());

        // mise a jour instance sauvegarde
        getSauvegarde().setArgent(getJoueur().getArgent());
        getSauvegarde().setNbPoules(getJoueur().getFerme().getNbPoules());
        getSauvegarde().setNbOeufs(getJoueur().getFerme().getNbOeufs());
        getSauvegarde().setEtatProgressOeuf(getJoueur().getFerme().getEtatProgressOeuf());

        getSauvegarde().setDateDeco(getJoueur().getFerme().getDateDeco());
        getSauvegarde().setFermeActive(getJoueur().getFermeActive());
        getSauvegarde().setDistributeursActive(getJoueur().getDistributeursActive());
        getSauvegarde().setDistributeurBCActive(getJoueur().getDistributeurBCActive());
        getSauvegarde().setDistributeurBFActive(getJoueur().getDistributeurBFActive());
        getSauvegarde().setDistributeurSaActive(getJoueur().getDistributeurSaActive());
        getSauvegarde().setDistributeurCoActive(getJoueur().getDistributeurCoActive());
        getSauvegarde().setNbDistributeurBC(getJoueur().getBoissonsChaudes().getNbDistributeurs());
        getSauvegarde().setNbDistributeurBF(getJoueur().getBoissonsFraiches().getNbDistributeurs());
        getSauvegarde().setNbDistributeurSa(getJoueur().getSandwichs().getNbDistributeurs());
        getSauvegarde().setNbDistributeurCo(getJoueur().getConfiseries().getNbDistributeurs());
        getSauvegarde().setNbMarchandisesBC(getJoueur().getBoissonsChaudes().getNbMarchandises());
        getSauvegarde().setNbMarchandisesBF(getJoueur().getBoissonsFraiches().getNbMarchandises());
        getSauvegarde().setNbMarchandisesSa(getJoueur().getSandwichs().getNbMarchandises());
        getSauvegarde().setNbMarchandisesCo(getJoueur().getConfiseries().getNbMarchandises());
        getSauvegarde().setEtatProgressBC(getJoueur().getBoissonsChaudes().getEtatProgressDistributeur());
        getSauvegarde().setEtatProgressBF(getJoueur().getBoissonsFraiches().getEtatProgressDistributeur());
        getSauvegarde().setEtatProgressSa(getJoueur().getSandwichs().getEtatProgressDistributeur());
        getSauvegarde().setEtatProgressCo(getJoueur().getConfiseries().getEtatProgressDistributeur());
        getSauvegarde().setNumJourDeco(getCalendrier().getNumJour());
        getSauvegarde().setHeureDeco(getCalendrier().getHeureActuelle());
        getSauvegarde().setProgressJour(getCalendrier().getProgressJour());
        getSauvegarde().setPoulailler1(getJoueur().getPoulailler1());
        getSauvegarde().setPoulailler2(getJoueur().getPoulailler2());
        getSauvegarde().setPoulailler3(getJoueur().getPoulailler3());
        getSauvegarde().setPoulailler4(getJoueur().getPoulailler4());

        System.out.println("Nouvelles valeurs a sauvegarder" + getSauvegarde());

        // sauvegarde dans la bdd
        ConnectionBdd connectionBdd = new ConnectionBdd();
        connectionBdd.connect();
        SauvegardeService sauvegardeService = new SauvegardeService(connectionBdd);
        try {
            sauvegardeService.majSauvegarde(getSauvegarde());
        } catch (Exception e) {
            System.out.println(e);
        }
        connectionBdd.close();
    }

    /**
     * Sauvegarde les modifications si crédit en cours
     */
    public void sauvegardeCredit() {
        if (getJoueur().getCreditEnCours() != null) {
            ConnectionBdd connectionBdd = new ConnectionBdd();
            connectionBdd.connect();
            Credits credits = new Credits();
            CreditsService creditsService = new CreditsService(connectionBdd);
            credits.setPseudo(getJoueur().getPseudo());
            credits.setMontantRembourse(getJoueur().getCreditEnCours().getMontantRembourse());
            credits.setDateDerniereMensualite(getJoueur().getCreditEnCours().getDateDerniereMensualite());
            credits.setTermine(getJoueur().getCreditEnCours().getTermine());
            credits.setMensualite(getJoueur().getCreditEnCours().getMensualite());
            credits.setDateProchaineMensualite(getJoueur().getCreditEnCours().getDateProchaineMensualite());
            credits.setDatePreavis(getJoueur().getCreditEnCours().getDatePreavis());
            credits.setBlocageDatePreavis(getJoueur().getCreditEnCours().getBlocageDatePreavis());
            try {
                creditsService.majCredit(credits);
            } catch (Exception e) {
                System.out.println(e);
            }
            connectionBdd.close();
        }
    }

    /**
     * Creation des poulaillers
     * 4 poulaillers + 1 inactif
     * et affectation dans la liste des poulaillers
     */
    public void createPoulaillers(){
        Poulaillers poulaillerInactif = new Poulaillers("Inactif",  BigDecimal.valueOf(0), 0);
        this.addPoulailler(poulaillerInactif);
        Poulaillers poulailler = new Poulaillers("Poulailler",  BigDecimal.valueOf(1000), 200);
        this.addPoulailler(poulailler);
        Poulaillers poulaillerPro = new Poulaillers("Poulailler pro", BigDecimal.valueOf(5000), 1000);
        this.addPoulailler(poulaillerPro);
        Poulaillers poulaillerMega = new Poulaillers("Méga poulailler", BigDecimal.valueOf(25000), 5000);
        this.addPoulailler(poulaillerMega);
        Poulaillers poulaillerIndustriel = new Poulaillers("Poulailler industriel", BigDecimal.valueOf(50000), 10000);
        this.addPoulailler(poulaillerIndustriel);

    }

    public List<Poulaillers> getPoulaillersList() {
        return poulaillersList;
    }

    public void setPoulaillersList(List<Poulaillers> poulaillersList) {
        this.poulaillersList = poulaillersList;
    }
    public void addPoulailler(Poulaillers poulaillers){
        this.poulaillersList.add(poulaillers);
    }
    /**
     * Calcule la valeur de l'entreprise
     */
    public BigDecimal valeurEntreprise(){
        BigDecimal valeurFermes = valeurFermes();
        System.out.println("Fermes" + valeurFermes);
        BigDecimal valeurPoules = valeurPoules();
        System.out.println("Poules" + valeurPoules);
        BigDecimal valeurDistributeurs = valeurDistributeurs();
        System.out.println("Distributeurs" + valeurDistributeurs);
        return valeurFermes.add(valeurDistributeurs.add(valeurPoules));
    }

    /**
     * Retourne la valeur de chaque poulailler
     * @return
     */
    public BigDecimal valeurFermes(){
        // recupere la valeur de chaque poulailler dans un tableau
        int[] index = new int[]{this.getJoueur().getPoulailler1(), this.getJoueur().getPoulailler2(), this.getJoueur().getPoulailler3(), this.getJoueur().getPoulailler4()};

        BigDecimal valeur = BigDecimal.valueOf(0);

        for (int i = 0; i < index.length; i++) {
            valeur = valeur.add(getPoulaillersList().get(index[i]).getPrixPoulailler());
        }
        return valeur;
    }

    /**
     * Renvoi la valeur des poules
     * @return
     */
    public BigDecimal valeurPoules(){
        int nbPoules = joueur.getFerme().getNbPoules();
        BigDecimal prixPoule = parametres.getTarifPoule();
        return prixPoule.multiply(BigDecimal.valueOf(nbPoules));
    }

    /**
     * Retourne la valeur de chaque distributeur
     * valeur distributer * nb distributeurs
     * @return
     */
    public BigDecimal valeurDistributeurs(){
        BigDecimal valeur = BigDecimal.valueOf(0);
        if(joueur.getDistributeurBCActive() == 1){
            int nbDistributeur = joueur.getBoissonsChaudes().getNbDistributeurs();
            BigDecimal prixDistributeur = parametres.getPrixDistributeurBC();
            BigDecimal valeurDistributeur = prixDistributeur.multiply(BigDecimal.valueOf(nbDistributeur));
            valeur = valeur.add(valeurDistributeur);
        }
        if(joueur.getDistributeurBFActive() == 1){
            int nbDistributeur = joueur.getBoissonsFraiches().getNbDistributeurs();
            BigDecimal prixDistributeur = parametres.getPrixDistributeurBF();
            BigDecimal valeurDistributeur = prixDistributeur.multiply(BigDecimal.valueOf(nbDistributeur));
            valeur = valeur.add(valeurDistributeur);
        }
        if(joueur.getDistributeurCoActive() == 1){
            int nbDistributeur = joueur.getConfiseries().getNbDistributeurs();
            BigDecimal prixDistributeur = parametres.getPrixDistributeurCo();
            BigDecimal valeurDistributeur = prixDistributeur.multiply(BigDecimal.valueOf(nbDistributeur));
            valeur = valeur.add(valeurDistributeur);
        }
        if(joueur.getDistributeurSaActive() == 1){
            int nbDistributeur = joueur.getSandwichs().getNbDistributeurs();
            BigDecimal prixDistributeur = parametres.getPrixDistributeurSa();
            BigDecimal valeurDistributeur = prixDistributeur.multiply(BigDecimal.valueOf(nbDistributeur));
            valeur = valeur.add(valeurDistributeur);
        }
        return valeur;
    }
}
