package com.example.mapetiteentreprise.jeu;

import com.example.mapetiteentreprise.bdd.*;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;

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
}
