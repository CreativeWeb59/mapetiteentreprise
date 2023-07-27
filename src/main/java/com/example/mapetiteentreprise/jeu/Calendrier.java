package com.example.mapetiteentreprise.jeu;

import com.example.mapetiteentreprise.actions.Outils;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Classe qui gere l'affichage du calendrier et la gestion des journées du jeu
 * Affichage de 4 semaines de 7 jours et des "mois" de 30 jours
 * On conserve juste l'historique de la semaine, on recree une semaine chaque "lundi" ou jour multiple de 7 + 1
 * Barre de progression independante des autres
 * commence le jour du début du jeu : 60 secondes = 1 jour en temps de jeu
 * <p>
 * 1er jour = jour 1
 * jour en cours = O
 * jours precedents = X
 * jour 30 => banquier => remboursement du credit
 */
public class Calendrier {
    private LocalDateTime dateDebutJeu;
    private int dureeJour = 600; // pour 60 secondes
    private int nbJoursSemaine = 7;
    private int nbJoursMois = 30; // 30 + 1 => correspond à l'arrivee du banquier
    private long semaine1EnCours[] = new long[]{1, 2, 3, 4, 5, 6, 7}; // contiendra les jours de la premiere semaine semaine à afficher sur le calendrier
    private long semaine2EnCours[] = new long[]{8, 9, 10, 11, 12, 13, 14}; // contiendra les jours de la premiere deuxieme semaine à afficher sur le calendrier

    public Calendrier(LocalDateTime dateDebutJeu) {
        this.dateDebutJeu = dateDebutJeu;
    }

    public LocalDateTime getDateDebutJeu() {
        return dateDebutJeu;
    }

    public void setDateDebutJeu(LocalDateTime dateDebutJeu) {
        this.dateDebutJeu = dateDebutJeu;
    }

    public int getDureeJour() {
        return dureeJour;
    }

    public void setDureeJour(int dureeJour) {
        this.dureeJour = dureeJour;
    }

    public int getNbJoursSemaine() {
        return nbJoursSemaine;
    }

    public void setNbJoursSemaine(int nbJoursSemaine) {
        this.nbJoursSemaine = nbJoursSemaine;
    }

    public int getNbJoursMois() {
        return nbJoursMois;
    }

    public void setNbJoursMois(int nbJoursMois) {
        this.nbJoursMois = nbJoursMois;
    }


    public long[] getSemaine1EnCours() {
        return semaine1EnCours;
    }

    /**
     * Met en place la premiere semaine du calendrier a afficher
     */
    public void setSemainesEnCours() {
        // recupere le numero de jour actuel
        long jourEnCours = this.getJourEnCours();
        long borneDebut; // chaque borne debut est un multiple de 7 + 1
        long borneFin; // chaque borne de fin est un multiple de 7

        borneFin = jourEnCours / 7;
        if (jourEnCours < 7) {
            borneDebut = 1;
            borneFin = 7;
        } else if (jourEnCours % 7 == 0) { // fin de la semaine
            borneDebut = jourEnCours - 6;
            borneFin = jourEnCours;
        } else if ((jourEnCours - 1) % 7 == 0) { // premier jour de la semaine
            borneDebut = jourEnCours;
            borneFin = jourEnCours + 6;
        } else {
            borneDebut = jourEnCours - (jourEnCours % 7) + 1; // milieu de la semaine
            borneFin = borneDebut + 6;
        }

        // mise en place des valeurs semaine 1
        for (int i = 0; i < 7; i++) {
            this.semaine1EnCours[i] = borneDebut;
            borneDebut++;
        }

        // mise en place des valeurs semaine 2
        for (int i = 0; i < 7; i++) {
            this.semaine2EnCours[i] = borneDebut;
            borneDebut++;
        }


    }


    /**
     * Met en place la deuxième semaine du calendrier a afficher
     *
     * @param semaine2EnCours
     */
    public void setSemaine2EnCours(long[] semaine2EnCours) {
        this.semaine2EnCours = semaine2EnCours;
    }

    /**
     * Creation du calendrier
     */
    public void createCalendrier() {

    }


    /**
     * Creation de la semaine 1 a afficher
     * correspond a la semaine du jour en cours
     */
    public void createSemaine1() {

    }

    /**
     * Creation de la semaine 2 a afficher
     * correspond a la semaine du jour en cours
     */
    public void createSemaine2() {

    }

    /**
     * Renvoi le numero de jour actuel
     * calcule la differnce entre la date actuelle et la date de creation de la partie
     * divise par 60 (ou le nombre de secondes dans une journée)
     * pour recuperer un entier correspondant au numero de jour actuel
     *
     * @return
     */
    public long getJourEnCours() {
        LocalDateTime dateDuJours = LocalDateTime.now();
        long ecartEnSecondes = ChronoUnit.SECONDS.between(dateDebutJeu, dateDuJours);
        long jourEnCours = (ecartEnSecondes / this.dureeJour) + 1;
        return jourEnCours;
    }


    public void createSemaine1Calendrier(Pane paneParent, long jourBanquier) {
        double valeurX = 0;

        for (long jour : this.semaine1EnCours) {
            String laClasse = choixClasseCalendrier(jour, jourBanquier);
            createJourCalendrier(paneParent, jour, laClasse, valeurX);
            valeurX += 130;
        }
    }

    public void createSemaine2Calendrier(Pane paneParent, long jourBanquier) {
        double valeurX = 0;

        for (long jour : this.semaine2EnCours) {
            String laClasse = choixClasseCalendrier(jour, jourBanquier);
            createJourCalendrier(paneParent, jour, laClasse, valeurX);
            valeurX += 130;
        }
    }

    /**
     * Methode de creation d'un pane par jour de calendrier
     * ajoute des elements au pane : paneSemaine1
     * <Pane prefHeight="120.0" prefWidth="118.0" styleClass="fondsCalendrierCoche">
     * <children>
     * <Label alignment="CENTER" layoutY="10.0" prefHeight="29.0" prefWidth="120.0" styleClass="labelCalendrier" text="Jour 1" />
     * </children>
     * </Pane>
     * <p>
     * Choix de la classe pour l'image de fonds :
     * - case simple
     * - case avec croix pour jour déja passé
     * - case avec cercle pour jour en cours
     * - case avec le banquier pour le jour du passage du banquier
     */
    public void createJourCalendrier(Pane paneParent, long jourCalendrier, String laClasse, double valeurX) {
        Pane nouveauPane = new Pane();
        nouveauPane.setPrefSize(120.0, 120.0);
        nouveauPane.getStyleClass().add(laClasse);
        nouveauPane.setLayoutX(valeurX);

        Label nouveauLabel = new Label("Jour " + jourCalendrier);
        nouveauLabel.setAlignment(Pos.CENTER);
        nouveauLabel.setPrefSize(120.0, 29.0);
        nouveauLabel.getStyleClass().add("labelCalendrier");

        nouveauPane.getChildren().add(nouveauLabel);
        paneParent.getChildren().add(nouveauPane);
    }

    /**
     * Choisi la classe du calendrier a afficher
     * suivant le jour en cours
     */
    public String choixClasseCalendrier(long jourCalendrier, long jourBanquier) {
        long jourEnCours = this.getJourEnCours();
        if (jourCalendrier == jourBanquier) {
            return "fondsCalendrierBanquier";
        }
        if (jourEnCours == jourCalendrier) {
            return "fondsCalendrierCercle";
        } else if (jourEnCours < jourCalendrier) {
            return "fondsCalendrier";
        } else {
            return "fondsCalendrierCoche";
        }
    }


    /**
     * Modifie le montant du prelevement si c'est la derniere mensualite
     */


}
