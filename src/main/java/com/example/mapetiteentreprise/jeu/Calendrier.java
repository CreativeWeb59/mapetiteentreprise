package com.example.mapetiteentreprise.jeu;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import java.time.LocalDateTime;

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
    private long semaine1EnCours[] = new long[]{1, 2, 3, 4, 5, 6, 7}; // contiendra les jours de la premiere semaine semaine à afficher sur le calendrier
    private long semaine2EnCours[] = new long[]{8, 9, 10, 11, 12, 13, 14}; // contiendra les jours de la premiere deuxieme semaine à afficher sur le calendrier
    private long numJour;
    private int heureActuelle; // chiffre de 1 à 10 => 1 jour = 10 fois 60s ou 10 fois un progressOeuf
    private double progressJour;
    private PieChart pieHorloge;
    private final int nbHeures = 10;
    private Timeline timelineHeure, timelineJour;


    public Calendrier(LocalDateTime dateDebutJeu, long numJour, int heureActuelle, double progressJour) {
        this.dateDebutJeu = dateDebutJeu;
        this.numJour = numJour;
        this.heureActuelle = heureActuelle;
        this.progressJour = progressJour;
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

    public double getProgressJour() {
        return progressJour;
    }

    public void setProgressJour(double progressJour) {
        this.progressJour = progressJour;
    }

    public int getHeureActuelle() {
        return heureActuelle;
    }

    public void setHeureActuelle(int heureActuelle) {
        this.heureActuelle = heureActuelle;
    }

    public long[] getSemaine1EnCours() {
        return semaine1EnCours;
    }

    public long getNumJour() {
        return numJour;
    }

    public void setNumJour(long numJour) {
        this.numJour = numJour;
    }

    public int getNbHeures() {
        return nbHeures;
    }

    /**
     * Met en place la premiere semaine du calendrier a afficher
     */
    public void setSemainesEnCours() {
        // recupere le numero de jour actuel
        long jourEnCours = this.getNumJour();
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
     * Renvoi le numero de jour actuel
     * calcule la differnce entre la date actuelle et la date de creation de la partie
     * divise par 60 (ou le nombre de secondes dans une journée)
     * pour recuperer un entier correspondant au numero de jour actuel
     *
     * @return N'est plus utilise depuis la version : pas de jeu en mode deco
     */
//    public long getJourEnCours() {
//        LocalDateTime dateDuJours = LocalDateTime.now();
//        long ecartEnSecondes = ChronoUnit.SECONDS.between(dateDebutJeu, dateDuJours);
//        long jourEnCours = (ecartEnSecondes / this.dureeJour) + 1;
//        return jourEnCours;
//    }
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
        long jourEnCours = this.getNumJour();
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

    public void createHorloge(PieChart pieHorlogeController) {
        this.pieHorloge = pieHorlogeController;
        // Masque les libellés
        this.pieHorloge.setLabelsVisible(false);
        // Désactive l'espacement entre les tranches
        this.pieHorloge.setLabelLineLength(0);

        // creation du tableau pour les heures
        PieChart.Data[] heures = new PieChart.Data[10];

        for (int i = 0; i < heures.length; i++) {
            heures[i] = new PieChart.Data("Heure " + (i), 10);
            this.pieHorloge.getData().add(heures[i]);
        }

        // Modifier la couleur des tranches en utilisant une couleur personnalisée
        setCouleurTranchesHorloge(heures);

        // Définir l'angle de départ de la première tranche à 90 degrés (commençant en haut du cercle)
        pieHorloge.setStartAngle(90);

        // rend le titre transparent
        pieHorloge.lookup(".chart-title").setStyle("-fx-background-color: transparent;");
    }

    public void setCouleurTranchesHorloge(PieChart.Data[] heures) {
        String[] customColors = couleursTranchesHoraires();
        for (int i = 0; i < heures.length; i++) {
            heures[i].getNode().setStyle("-fx-pie-color: " + customColors[i] + ";");
        }
    }

    /**
     * Permet de remplir le tableau des couleurs de l'horloge
     * suivant l'heure actuelle
     *
     * @return
     */
    public String[] couleursTranchesHoraires() {
        // tableau par defaut qui correspond à 1 heure
        String ecoulee = "#0F9DE8";
        String pasEcoulee = "white";

        String[] resultatCouleurs = {pasEcoulee, pasEcoulee, pasEcoulee, pasEcoulee, pasEcoulee, pasEcoulee, pasEcoulee, pasEcoulee, pasEcoulee, pasEcoulee, pasEcoulee};
        for (int i = 0; i < resultatCouleurs.length; i++) {
            if (this.heureActuelle > i) {
                resultatCouleurs[i] = ecoulee;
            } else {
                resultatCouleurs[i] = pasEcoulee;
            }
        }
        return resultatCouleurs;
    }

    /**
     * permet de modifier les couleurs au changement d'heure
     *
     * @param pieChart
     */
    public void modifyPieChartColors(PieChart pieChart) {
        // Vous pouvez appeler cette méthode pour modifier les couleurs du PieChart existant
        // en utilisant l'objet pieChart passé en paramètre.
        PieChart.Data[] heures = pieChart.getData().toArray(new PieChart.Data[0]);
        setCouleurTranchesHorloge(heures);
    }

    /**
     * Incremente le numero de jour
     * Remet le compteur des heures à 1
     */
    public void setJourSuivant() {
        this.setNumJour(this.getNumJour() + 1);
    }

    /**
     * permet d'ajouter une heure
     * au dessus de 10 passe à 1 heure
     * et incremente un jour
     */
    public void setIncrementHeure() {
        if (this.getHeureActuelle() >= this.nbHeures) {
            this.setHeureActuelle(0);
            this.setJourSuivant();
        }
        this.setHeureActuelle(this.getHeureActuelle() + 1);
    }

    /**
     * Timeline pour ajouter les heures et ensuite afficher le calendrier
     * ici pas de barre de progression : l'heure est basée sur la production de la ferme
     *
     * @param cycle                   : 0 pour cycle infini
     * @param vitesse                 : vitesse en secondes
     * @param paneSemaine1            : pane de la semaine 1 pour l'afficher
     * @param paneSemaine2            : pane de la semaine 2 pour l'afficher
     * @param prochaineDateMensualite : permet d'afficher la date de venue du banquier pour le paiement de la mensualité du crédit
     */
    public void progressHeureCalendrier(int cycle, double vitesse, double vitesseAjustement,  Pane paneSemaine1, Pane paneSemaine2, long prochaineDateMensualite) {
        timelineHeure = new Timeline(
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    System.out.println("heure terminée");
                    // incremente un jour et remet l'heure à 1
                    this.setIncrementHeure();
                    // mise à jour du calendrier
                    this.afficheCalendrier(paneSemaine1, paneSemaine2, prochaineDateMensualite); // uniquement sur la page gestionController
                })
        );
        timelineHeure.setOnFinished(event -> {
            if (cycle == 1) {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                System.out.println("suite");
                progressHeureCalendrier(cycle - 1, vitesse, vitesse, paneSemaine1, paneSemaine2, prochaineDateMensualite);
            }
        });
        if (cycle == 0) {
            timelineHeure.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineHeure.setCycleCount(cycle);
        }
        timelineHeure.play();
    }

    /**
     * Barre de progression pour ajouter les heures
     * N'affiche pas le calendrier
     * ici pas de barre de progression : l'heure est basée sur la production de la ferme
     *
     * @param cycle   : 0 pour cycle infini
     * @param vitesse : vitesse en secondes
     */
    public void progressHeure(int cycle, double vitesse, double vitesseAjustement) {
        timelineHeure = new Timeline(
                new KeyFrame(Duration.seconds(vitesseAjustement), e -> {
                    System.out.println("heure terminée");
                    // incremente un jour et remet l'heure à 1
                    this.setIncrementHeure();
                })
        );
        timelineHeure.setOnFinished(event -> {
            if (cycle == 1) {
                // recalcul de la vitesse suivant le niveau de la barre de progression
                System.out.println("suite");
                progressHeure(cycle - 1, vitesse, vitesse);
            }
        });
        if (cycle == 0) {
            timelineHeure.setCycleCount(Animation.INDEFINITE);
        } else {
            timelineHeure.setCycleCount(cycle);
        }
        timelineHeure.play();
    }

    /**
     * Permet d'afficher le calendrier sur la page Gestion
     *
     * @param paneSemaine1            : affiche la semaine 1
     * @param paneSemaine2            : affiche la semaine 2
     * @param prochaineDateMensualite si date prochaine mensualite superieure au jour en cours, affiche le banquier
     */
    public void afficheCalendrier(Pane paneSemaine1, Pane paneSemaine2, long prochaineDateMensualite) {
        // met en place la semaine en cours
        this.setSemainesEnCours();
        if (prochaineDateMensualite >= this.getNumJour()) {
            this.createSemaine1Calendrier(paneSemaine1, prochaineDateMensualite);
            this.createSemaine2Calendrier(paneSemaine2, prochaineDateMensualite);

        } else {
            this.createSemaine1Calendrier(paneSemaine1, 0);
            this.createSemaine2Calendrier(paneSemaine2, 0);
        }
    }

    /**
     * Permet de stopper la timeline Heure
     */
    public void progressBarStop() {
        if (timelineHeure != null) {
            timelineHeure.stop();
            timelineHeure = null;
            System.out.println("Arret de la barre de progression timelineHeure");
        }
    }

}
