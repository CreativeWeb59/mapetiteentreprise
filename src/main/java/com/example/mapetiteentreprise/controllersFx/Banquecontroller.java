package com.example.mapetiteentreprise.controllersFx;

import com.example.mapetiteentreprise.Main;
import com.example.mapetiteentreprise.actions.Outils;
import com.example.mapetiteentreprise.bdd.ConnectionBdd;
import com.example.mapetiteentreprise.bdd.Credits;
import com.example.mapetiteentreprise.bdd.CreditsService;
import com.example.mapetiteentreprise.jeu.CreditEnCours;
import com.example.mapetiteentreprise.jeu.Jeu;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Banquecontroller {
    private Jeu jeu;
    private final String monnaie = " €";
    private final String separationTexte = System.getProperty("line.separator");
    // pattern des nombre décimaux
    private final DecimalFormat decimalFormat = Outils.getDecimalFormatWithSpaceSeparator();
    @FXML
    private Label labelAccueil, labelAPayer, labelMontant, labelInterets, labelTotal, labelRestantDu, labelPrelevement, labelMessageFinDuPret,
            labelCredit;
    @FXML
    Button retourMenu, btnRembourser, btnRembourserTout, btnPret;
    @FXML
    Pane paneCreditEnCours, paneNouveauCredit, paneProgress;
    @FXML
    private ProgressBar progressOeufs, progressBC, progressBF, progressSa, progressCo,
            progressScooter, progressCamionette, progressPetitCamion, progressPoidsLourd, progressAvion,
            progressTextile1, progressTextile2, progressTextile3, progressTextile4,
            progressJouets1, progressJouets2, progressJouets3, progressJouets4;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private ConnectionBdd connectionBdd = new ConnectionBdd();
    private Credits credits = new Credits();
    private CreditsService creditsService;

    // attributs pur le prêt
    private final BigDecimal pourcentageInterets = BigDecimal.valueOf(0.08);
    private final int nbMensualites = 30;
    private final int cycleMensualite = 7;

    public void startFenetre(Jeu jeu) {
        // Recuperation du jeu
        this.jeu = jeu;
        setLabelAccueil();
        // affiche le crédit en cours ou la possibilité de prendre un nouveau crédit
        affichagePanneau();

        // barres de progression
        demarrageProgress();

        // demarrage des usines
        demarrageUsinesTextile();
        demarrageUsinesJouets();

        // affichage des barres de progression (mode dev)
        jeu.afficheProgression(paneProgress);

    }

    public void retourGestion(Event event) {
        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();
        // sauvegarde bdd
        sauveBdd();

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("gestion.fxml"));
            root = loader.load();
            GestionController gestionController = loader.getController();
            // on renvoi les infos a la fenetre suivante (tout est dans l'instance jeu)

            gestionController.debutJeu(jeu);
        } catch (Exception e) {
            System.out.println(e);
        }

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Action a executé lors de la fermeture de la fentre avec la croix : sauvegarde
     *
     * @param event
     */
    public void onWindowClose(WindowEvent event) {
        // fermeture des barres, enregistrement + stop et sauvegarde date deco
        fermetureProgress();
        // sauvegarde bdd
        sauveBdd();
    }

    public void setLabelAccueil(){
        String formattedString = decimalFormat.format(jeu.getJoueur().getArgent()) + monnaie;
        labelAccueil.setText("Montant en banque : " + formattedString);
    }
    /**
     * Initialise les labels du crédit
     */
    public void affichageCredit(){
        setLabelMontant();
        setLabelAPayer();
        setLabelTotal();
        setLabelRestantDu();
        setLabelInterets();
        setLabelPrelevement();
        isMensualite();
        isCreditComplet();
        setLabelAccueil();
    }

    /**
     * Fait le choix entre afficher le detail du credit ou effectuer un nouveau crédit
     */
    public void affichagePanneau(){
        if(isCredit()){
            paneCreditEnCours.setVisible(true);
            paneNouveauCredit.setVisible(false);
            affichageCredit();
        } else {
            paneCreditEnCours.setVisible(false);
            paneNouveauCredit.setVisible(true);
            propositionPret();
        }
    }

    /**
     * Mise en place label a payer
     * indique le numero du jour qu'il faudra payer la mensualité
     */
    public void setLabelAPayer(){
        // calcul mensualites en retard
        long nbRetardMensualite = jeu.getJoueur().getCreditEnCours().nbRetardMensualite(jeu.getCalendrier().getNumJour());
        long prochainPrelevement = jeu.getJoueur().getCreditEnCours().getDateProchaineMensualite();

        String texte;
        // gestion si echeances en retard
        if (nbRetardMensualite <= 0) {
            texte = "" + prochainPrelevement;
        } else {
            texte = "Vous avez " + nbRetardMensualite + " écheance(s) en retard" + separationTexte;
            texte += "A payer immédiatement";

            // Arrivee du banquier
            // A faire Blocage des boutons d'achat
        }

        this.labelAPayer.setText(texte);
    }

    /**
     * Mise en place label a payer
     */
    public void setLabelMontant(){
        String formattedString = decimalFormat.format(jeu.getJoueur().getCreditEnCours().getMontantPret()) + monnaie;
        this.labelMontant.setText(formattedString);
    }

    /**
     * Mise en place label a payer
     */
    public void setLabelInterets(){
        BigDecimal mInterets = jeu.getJoueur().getCreditEnCours().getCoutPret().subtract(jeu.getJoueur().getCreditEnCours().getMontantPret());
        String formattedString = decimalFormat.format(mInterets) + monnaie;
        this.labelInterets.setText(formattedString);
    }

    /**
     * Mise en place label a payer
     */
    public void setLabelTotal(){
        String formattedString = decimalFormat.format(jeu.getJoueur().getCreditEnCours().getCoutPret()) + monnaie;
        labelTotal.setText(formattedString);
    }
    /**
     * Mise en place label a payer
     */
    public void setLabelRestantDu(){
        BigDecimal restantDu = jeu.getJoueur().getCreditEnCours().getCoutPret().subtract(jeu.getJoueur().getCreditEnCours().getMontantRembourse());
        String formattedString = decimalFormat.format(restantDu) + monnaie;
        labelRestantDu.setText(formattedString);
    }
    /**
     * Mise en place label a payer
     */
    public void setLabelPrelevement(){
        String formattedString = decimalFormat.format(jeu.getJoueur().getCreditEnCours().getMensualite()) + monnaie;
        labelPrelevement.setText(formattedString);
    }

    /**
     * Renvoi true si credit en cours
     * @return
     */
    public boolean isCredit(){
        CreditEnCours creditEnCours = jeu.getJoueur().getCreditEnCours();
        if (creditEnCours != null) {
            if(jeu.getJoueur().getCreditEnCours().getTermine() == 0){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * gere quand aucun prêt en cours
     * propose un nouveau avec plusieurs solutions de prêts
     */
    public void propositionPret(){
        // calcul des valeurs necessaires
        BigDecimal montantAPreter = montantAPreter();
        BigDecimal coutPret = montantAPreter.add(montantAPreter.multiply(BigDecimal.valueOf(0.08)));
        BigDecimal mensualite = coutPret.divide(BigDecimal.valueOf(nbMensualites));

        long jourEnCours = jeu.getCalendrier().getNumJour();
        long dateProchaineMensualite = jourEnCours + cycleMensualite;
        long datePreavis = jourEnCours + (nbMensualites * cycleMensualite) + 2;

        String formattedStringMontantAPreter = decimalFormat.format(montantAPreter) + monnaie;
        String formattedStringCoutPret = decimalFormat.format(coutPret) + monnaie;
        String formattedStringMensualite = decimalFormat.format(mensualite) + monnaie;


        String texte = "Montant du prêt : " + formattedStringMontantAPreter + separationTexte;
        texte += nbMensualites + " Mensualites de " + formattedStringMensualite + separationTexte;
        texte += "Cout total du crédit : " + formattedStringCoutPret +  separationTexte;
        texte += "Première mensualité le jour " + dateProchaineMensualite;
        setLabelPret(texte);
    }

    /**
     * Renvoi le montant du pret à preter
     * @return
     */
    public BigDecimal montantAPreter(){
        return jeu.valeurEntreprise().divide(BigDecimal.valueOf(2));
    }

    public void setLabelPret(String texte){
        labelCredit.setText(texte);
    }

    /**
     * start les 2 barres de progression : progress oeuf et progress jour
     *  + les barres des distributeurs s'ils sont actifs
     */
    public void demarrageProgress(){
        // recuperation de l'etat des barres de progression
        double vitesseOeuf = jeu.getParametres().getVitessePonteOeuf() - (jeu.getParametres().getVitessePonteOeuf() * jeu.getJoueur().getFerme().getEtatProgressOeuf());

        jeu.getJoueur().getFerme().progressBarStartOeuf(1, jeu.getParametres().getVitessePonteOeuf(), vitesseOeuf, progressOeufs);
        jeu.getCalendrier().progressHeure(1, jeu.getParametres().getVitessePonteOeuf(), vitesseOeuf);

        // demarrage des distributeurs
        demarrageDistributeurs();

        // demarrage des livraisons
        demarrageLivraisons();

        // demarrage des usines
        demarrageUsinesTextile();
    }
    /**
     * Demarre les usines lorsqu'elles sont actives
     */
    public void demarrageUsinesTextile() {
        if (Outils.isActif(jeu.getJoueur().getUsineTextilePetite().getUsineActive())) {
            // recupertaion etat barre de progression usine de textil petite
            double vitesseUsineTextile1 = jeu.getJoueur().getUsineTextilePetite().getVitesseUsine() - (jeu.getJoueur().getUsineTextilePetite().getVitesseUsine() * jeu.getJoueur().getUsineTextilePetite().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineTextilePetite().progressBarStartUsineTextile(1, this.jeu.getJoueur().getUsineTextilePetite().getVitesseUsine(), vitesseUsineTextile1, progressTextile1);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineTextileMoyenne().getUsineActive())) {
            // recupertaion etat barre de progression usine de textil moyenne
            double vitesseUsineTextile2 = jeu.getJoueur().getUsineTextileMoyenne().getVitesseUsine() - (jeu.getJoueur().getUsineTextileMoyenne().getVitesseUsine() * jeu.getJoueur().getUsineTextileMoyenne().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineTextileMoyenne().progressBarStartUsineTextile(1, this.jeu.getJoueur().getUsineTextileMoyenne().getVitesseUsine(), vitesseUsineTextile2, progressTextile2);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineTextileGrande().getUsineActive())) {
            // recupertaion etat barre de progression usine de textil grande
            double vitesseUsineTextile3 = jeu.getJoueur().getUsineTextileGrande().getVitesseUsine() - (jeu.getJoueur().getUsineTextileGrande().getVitesseUsine() * jeu.getJoueur().getUsineTextileGrande().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineTextileGrande().progressBarStartUsineTextile(1, this.jeu.getJoueur().getUsineTextileGrande().getVitesseUsine(), vitesseUsineTextile3, progressTextile3);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineTextileEnorme().getUsineActive())) {
            // recupertaion etat barre de progression usine de textil enorme
            double vitesseUsineTextile4 = jeu.getJoueur().getUsineTextileEnorme().getVitesseUsine() - (jeu.getJoueur().getUsineTextileEnorme().getVitesseUsine() * jeu.getJoueur().getUsineTextileEnorme().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineTextileEnorme().progressBarStartUsineTextile(1, this.jeu.getJoueur().getUsineTextileEnorme().getVitesseUsine(), vitesseUsineTextile4, progressTextile4);
        }
    }
    /**
     * Demarre les usines de jouets lorsqu'elles sont actives
     */
    public void demarrageUsinesJouets() {
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsPetite().getUsineActive())) {
            // recupertaion etat barre de progression usine de jouets petite
            double vitesseUsineJouets1 = jeu.getJoueur().getUsineJouetsPetite().getVitesseUsine() - (jeu.getJoueur().getUsineJouetsPetite().getVitesseUsine() * jeu.getJoueur().getUsineJouetsPetite().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineJouetsPetite().progressBarStartUsineJouets(1, this.jeu.getJoueur().getUsineJouetsPetite().getVitesseUsine(), vitesseUsineJouets1, progressJouets1);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsMoyenne().getUsineActive())) {
            // recupertaion etat barre de progression usine de jouets moyenne
            double vitesseUsineJouets2 = jeu.getJoueur().getUsineJouetsMoyenne().getVitesseUsine() - (jeu.getJoueur().getUsineJouetsMoyenne().getVitesseUsine() * jeu.getJoueur().getUsineJouetsMoyenne().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineJouetsMoyenne().progressBarStartUsineJouets(1, this.jeu.getJoueur().getUsineJouetsMoyenne().getVitesseUsine(), vitesseUsineJouets2, progressJouets2);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsGrande().getUsineActive())) {
            // recupertaion etat barre de progression usine de jouets grande
            double vitesseUsineJouets3 = jeu.getJoueur().getUsineJouetsGrande().getVitesseUsine() - (jeu.getJoueur().getUsineJouetsGrande().getVitesseUsine() * jeu.getJoueur().getUsineJouetsGrande().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineJouetsGrande().progressBarStartUsineJouets(1, this.jeu.getJoueur().getUsineJouetsGrande().getVitesseUsine(), vitesseUsineJouets3, progressJouets3);
        }
        if (Outils.isActif(jeu.getJoueur().getUsineJouetsEnorme().getUsineActive())) {
            // recupertaion etat barre de progression usine de jouets enorme
            double vitesseUsineJouets4 = jeu.getJoueur().getUsineJouetsEnorme().getVitesseUsine() - (jeu.getJoueur().getUsineJouetsEnorme().getVitesseUsine() * jeu.getJoueur().getUsineJouetsEnorme().getEtatProgressUsine());
            this.jeu.getJoueur().getUsineJouetsEnorme().progressBarStartUsineJouets(1, this.jeu.getJoueur().getUsineJouetsEnorme().getVitesseUsine(), vitesseUsineJouets4, progressJouets4);
        }
    }
    /**
     * Fermeture des barres de progression : enregistrement de l'état + stop des barres de progress
     * Sauvegarde date deco
     */
    public void fermetureProgress(){
        // sauvegarde des barres de progression
        this.jeu.getJoueur().getFerme().setEtatProgressOeuf(this.progressOeufs.getProgress());

        // on recupere l'etat de la barre de progression des distributeurs
        this.jeu.getJoueur().getBoissonsChaudes().setEtatProgressDistributeur(this.progressBC.getProgress());
        this.jeu.getJoueur().getBoissonsFraiches().setEtatProgressDistributeur(this.progressBF.getProgress());
        this.jeu.getJoueur().getConfiseries().setEtatProgressDistributeur(this.progressCo.getProgress());
        this.jeu.getJoueur().getSandwichs().setEtatProgressDistributeur(this.progressSa.getProgress());

        // on recupere les barres de progression des livraisons
        this.jeu.getJoueur().getLivraisonScooter().setEtatProgressLivraison(this.progressScooter.getProgress());
        this.jeu.getJoueur().getLivraisonCamionette().setEtatProgressLivraison(this.progressCamionette.getProgress());
        this.jeu.getJoueur().getLivraisonPetitCamion().setEtatProgressLivraison(this.progressPetitCamion.getProgress());
        this.jeu.getJoueur().getLivraisonPoidsLourd().setEtatProgressLivraison(this.progressPoidsLourd.getProgress());
        this.jeu.getJoueur().getLivraisonAvion().setEtatProgressLivraison(this.progressAvion.getProgress());

        // on recupere les barres de progression des usines de textile
        this.jeu.getJoueur().getUsineTextilePetite().setEtatProgressUsine(this.progressTextile1.getProgress());
        this.jeu.getJoueur().getUsineTextileMoyenne().setEtatProgressUsine(this.progressTextile2.getProgress());
        this.jeu.getJoueur().getUsineTextileGrande().setEtatProgressUsine(this.progressTextile3.getProgress());
        this.jeu.getJoueur().getUsineTextileEnorme().setEtatProgressUsine(this.progressTextile4.getProgress());

        // on recupere les barres de progression des usines de jouets
        this.jeu.getJoueur().getUsineJouetsPetite().setEtatProgressUsine(this.progressJouets1.getProgress());
        this.jeu.getJoueur().getUsineJouetsMoyenne().setEtatProgressUsine(this.progressJouets2.getProgress());
        this.jeu.getJoueur().getUsineJouetsGrande().setEtatProgressUsine(this.progressJouets3.getProgress());
        this.jeu.getJoueur().getUsineJouetsEnorme().setEtatProgressUsine(this.progressJouets4.getProgress());

        // on stoppe les barres de progression
        jeu.getJoueur().getFerme().progressBarStop();
        jeu.getCalendrier().progressBarStop();
        jeu.getJoueur().getBoissonsChaudes().progressBarStop();
        jeu.getJoueur().getBoissonsFraiches().progressBarStop();
        jeu.getJoueur().getConfiseries().progressBarStop();
        jeu.getJoueur().getSandwichs().progressBarStop();
        jeu.getJoueur().getLivraisonScooter().progressBarStop();
        jeu.getJoueur().getLivraisonCamionette().progressBarStop();
        jeu.getJoueur().getLivraisonPetitCamion().progressBarStop();
        jeu.getJoueur().getLivraisonPoidsLourd().progressBarStop();
        jeu.getJoueur().getLivraisonAvion().progressBarStop();
        jeu.getJoueur().getUsineTextilePetite().progressBarStop();
        jeu.getJoueur().getUsineTextileMoyenne().progressBarStop();
        jeu.getJoueur().getUsineTextileGrande().progressBarStop();
        jeu.getJoueur().getUsineTextileEnorme().progressBarStop();
        jeu.getJoueur().getUsineJouetsPetite().progressBarStop();
        jeu.getJoueur().getUsineJouetsMoyenne().progressBarStop();
        jeu.getJoueur().getUsineJouetsGrande().progressBarStop();
        jeu.getJoueur().getUsineJouetsEnorme().progressBarStop();
    }
    /**
     * Sauvegarde de la base de donnees
     */
    public void sauveBdd(){
        System.out.println("fermeture fenetre : Sauvegarde");
        try {
            this.jeu.sauvegardejeu();
            this.jeu.sauvegardeCredit();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    /**
     * gere le bouton pour rembourser une mensualite du credit
     */
    public void onRembourserMensualite() {
        // teste si assez argent en banque
        if (jeu.getJoueur().isArgent(jeu.getJoueur().getCreditEnCours().getMensualite())) {
            // enleve le montant de la banque
            jeu.getJoueur().depenser(jeu.getJoueur().getCreditEnCours().getMensualite());
            // met a jour le credit
            // met a jour le montant du credit rembourse
            jeu.getJoueur().getCreditEnCours().payerMensualite(jeu.getCalendrier().getNumJour());
            // met a jour la prochaine date du reglement

            System.out.println("remboursement");
            this.affichageCredit();
            setLabelAccueil();
        } else {
            System.out.println("Vous n'avez pas assez d'argent pour rembourser le credit");
        }
    }

    /**
     * verifie si assez d'argent pour rembourser le prêt
     */
    public void isMensualite() {
        CreditEnCours creditEnCours = jeu.getJoueur().getCreditEnCours();
        if (creditEnCours != null) {
            if (jeu.getJoueur().isArgent(jeu.getJoueur().getCreditEnCours().getMensualite())) {
                btnRembourser.setVisible(true);
                btnRembourser.setDisable(false);
            } else {
                btnRembourser.setVisible(true);
                btnRembourser.setDisable(true);
            }
        }
    }

    /**
     * gere le bouton pour rembourser une mensualite du credit
     */
    public void onRembourserTout() {
        // teste si assez argent en banque
        BigDecimal montantRestant = jeu.getJoueur().getCreditEnCours().getCoutPret().subtract(jeu.getJoueur().getCreditEnCours().getMontantRembourse());
        if (jeu.getJoueur().isArgent(montantRestant)) {
            // enleve le montant de la banque
            jeu.getJoueur().depenser(montantRestant);
            // met a jour le credit
            // met a jour le montant du credit rembourse
            jeu.getJoueur().getCreditEnCours().rembourserCredit(jeu.getCalendrier().getNumJour());
            System.out.println("remboursement complet");

            setLabelAccueil();
            // chanqge de panneau d'affichage
            paneCreditEnCours.setVisible(false);
            paneNouveauCredit.setVisible(true);

            // affichage message temporaire dans le label : labelMessageFinDuPret
            Outils.afficherMessageTemporaire(labelMessageFinDuPret, "Vous venez de rembourser votre prêt", 3000);
            propositionPret();
        } else {
            System.out.println("Vous n'avez pas assez d'argent pour rembourser entierement le credit");
        }
    }

    /**
     * verifie si assez d'argent pour rembourser le prêt
     */
    public void isCreditComplet() {
        CreditEnCours creditEnCours = jeu.getJoueur().getCreditEnCours();
        if (creditEnCours != null) {
            BigDecimal montantRestant = jeu.getJoueur().getCreditEnCours().getCoutPret().subtract(jeu.getJoueur().getCreditEnCours().getMontantRembourse());
            if (jeu.getJoueur().isArgent(montantRestant)) {
                btnRembourserTout.setVisible(true);
                btnRembourserTout.setDisable(false);
            } else {
                btnRembourserTout.setVisible(true);
                btnRembourserTout.setDisable(true);
            }
        }
    }

    /**
     * Click sur le bouton de prêt
     * recupere les infos du type de prêt
     * calcule la somme pretable par rapport à la valeur de l'entreprise
     */
    public void onBtnPret(){
        // calcul de la somme à preter
        BigDecimal montantAPreter = montantAPreter();

        // calcul des valeurs necessaires
        BigDecimal coutPret = montantAPreter.add(montantAPreter.multiply(BigDecimal.valueOf(0.08)));
        BigDecimal mensualite = coutPret.divide(BigDecimal.valueOf(nbMensualites));
        long jourEnCours = jeu.getCalendrier().getNumJour();
        long dateProchaineMensualite = jourEnCours + cycleMensualite;
        long datePreavis = jourEnCours + (nbMensualites * cycleMensualite) + 2;

        CreditEnCours creditEnCours = jeu.getJoueur().getCreditEnCours();
        System.out.println("joueur : " + jeu.getJoueur().getCreditEnCours());
        System.out.println("Credit : " + creditEnCours);
        // verifie si un credit existe déja
        if (creditEnCours == null) {
            // creation du pret
            creditEnCours = new CreditEnCours(montantAPreter, coutPret, BigDecimal.valueOf(0), mensualite, nbMensualites, cycleMensualite, 0,
                    jourEnCours, jourEnCours, dateProchaineMensualite, datePreavis, 0);
            // on l'ajoute au joueur
            jeu.getJoueur().setCreditEnCours(creditEnCours);

            // on ajoute la somme dans la banque du joueur
            jeu.getJoueur().setArgent(jeu.getJoueur().getArgent().add(montantAPreter));

            // on ajoute dans la table credits
            connectionBdd.connect();

            creditsService = new CreditsService(connectionBdd);
            try {
                credits = new Credits(jeu.getJoueur().getPseudo(), montantAPreter, coutPret, BigDecimal.valueOf(0), mensualite, 30, cycleMensualite, 0, jourEnCours, jourEnCours, dateProchaineMensualite, datePreavis, 0);
                creditsService.addCredit(credits);
                System.out.println("Ajout du credit en bdd");
            } catch (Exception e){
                System.out.println("Erreur de creation du crédit");
            }
            connectionBdd.close();

            // maj des labels
            affichagePanneau();

        } else {
            System.out.println("Vous avez déja un crédit en cours, terminez votre crédit avant d'en faire un nouveau");
        }
    }
    /**
     * Permet d'ajuster les distributeurs et les demarrer s'ils sont actifs
     */
    public void demarrageDistributeurs(){
        // Demmarage des distributueurs
        // Boissons chaudes
        if (jeu.getJoueur().getDistributeursActive() == 1 && jeu.getJoueur().getBoissonsChaudes().getNbDistributeurs() > 0) {
            double vitesse = jeu.getParametres().getVitesseBC() - (jeu.getParametres().getVitesseBC() * jeu.getJoueur().getBoissonsChaudes().getEtatProgressDistributeur());
            this.jeu.getJoueur().getBoissonsChaudes().progressBarStartBC(1, jeu.getParametres().getVitesseBC(), vitesse, progressBC);
        }

        // Boissons fraiches
        if (jeu.getJoueur().getDistributeursActive() == 1 && jeu.getJoueur().getBoissonsFraiches().getNbDistributeurs() > 0) {
            double vitesse = jeu.getParametres().getVitesseBF() - (jeu.getParametres().getVitesseBF() * jeu.getJoueur().getBoissonsFraiches().getEtatProgressDistributeur());
            this.jeu.getJoueur().getBoissonsFraiches().progressBarStartBF(1, jeu.getParametres().getVitesseBF(), vitesse, progressBF);
        }

        // Confiseries
        if (jeu.getJoueur().getDistributeursActive() == 1 && jeu.getJoueur().getConfiseries().getNbDistributeurs() > 0) {
            double vitesse = jeu.getParametres().getVitesseCo() - (jeu.getParametres().getVitesseCo() * jeu.getJoueur().getConfiseries().getEtatProgressDistributeur());
            this.jeu.getJoueur().getConfiseries().progressBarStartCo(1, jeu.getParametres().getVitesseCo(), vitesse, progressCo);
        }

        // Sandwichs
        if (jeu.getJoueur().getDistributeursActive() == 1 && jeu.getJoueur().getSandwichs().getNbDistributeurs() > 0) {
            double vitesse = jeu.getParametres().getVitesseSa() - (jeu.getParametres().getVitesseSa() * jeu.getJoueur().getSandwichs().getEtatProgressDistributeur());
            this.jeu.getJoueur().getSandwichs().progressBarStartSa(1, jeu.getParametres().getVitesseSa(), vitesse, progressSa);
        }
    }

    /**
     * Demarrage des barres de progression des livraisons
     */
    public void demarrageLivraisons() {
        if(jeu.getJoueur().getLivraison1Active() == 1){
            // recuperation de l'etat de la barre de progression pour la livraison en scooter
            double vitesseScooter = jeu.getJoueur().getLivraisonScooter().getVitesseLivraion() - (jeu.getJoueur().getLivraisonScooter().getVitesseLivraion() * jeu.getJoueur().getLivraisonScooter().getEtatProgressLivraison());
            System.out.println("Vitesse scooter : " + vitesseScooter);
            this.jeu.getJoueur().getLivraisonScooter().progressBarStartScooter(1, jeu.getJoueur().getLivraisonScooter().getVitesseLivraion(), vitesseScooter, progressScooter);
        }
        if(jeu.getJoueur().getLivraison2Active() == 1){
            // recuperation de l'etat de la barre de progression pour la livraison en camionette
            double vitesseCamionette = jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion() - (jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion() * jeu.getJoueur().getLivraisonCamionette().getEtatProgressLivraison());
            System.out.println("Vitesse camionette : " + vitesseCamionette);
            this.jeu.getJoueur().getLivraisonCamionette().progressBarStartCamionette(1, jeu.getJoueur().getLivraisonCamionette().getVitesseLivraion(), vitesseCamionette, progressCamionette);
        }
        if(jeu.getJoueur().getLivraison3Active() == 1){
            // recuperation de l'etat de la barre de progression pour la livraison en petit camion
            double vitessePetitCamion = jeu.getJoueur().getLivraisonPetitCamion().getVitesseLivraion() - (jeu.getJoueur().getLivraisonPetitCamion().getVitesseLivraion() * jeu.getJoueur().getLivraisonPetitCamion().getEtatProgressLivraison());
            System.out.println("Vitesse petit camion : " + vitessePetitCamion);
            this.jeu.getJoueur().getLivraisonPetitCamion().progressBarStartPetitCamion(1, jeu.getJoueur().getLivraisonPetitCamion().getVitesseLivraion(), vitessePetitCamion, progressPetitCamion);
        }
        if(jeu.getJoueur().getLivraison4Active() == 1){
            // recuperation de l'etat de la barre de progression pour la livraison en poids lours
            double vitessePoidsLourd = jeu.getJoueur().getLivraisonPoidsLourd().getVitesseLivraion() - (jeu.getJoueur().getLivraisonPoidsLourd().getVitesseLivraion() * jeu.getJoueur().getLivraisonPoidsLourd().getEtatProgressLivraison());
            System.out.println("Vitesse poids lourd : " + vitessePoidsLourd);
            this.jeu.getJoueur().getLivraisonPoidsLourd().progressBarStartPoidsLourd(1, jeu.getJoueur().getLivraisonPoidsLourd().getVitesseLivraion(), vitessePoidsLourd, progressPoidsLourd);
        }
        if(jeu.getJoueur().getLivraison5Active() == 1){
            // recuperation de l'etat de la barre de progression pour la livraison en avion
            double vitesseAvion = jeu.getJoueur().getLivraisonAvion().getVitesseLivraion() - (jeu.getJoueur().getLivraisonAvion().getVitesseLivraion() * jeu.getJoueur().getLivraisonAvion().getEtatProgressLivraison());
            System.out.println("Vitesse avion : " + vitesseAvion);
            this.jeu.getJoueur().getLivraisonAvion().progressBarStartAvion(1, jeu.getJoueur().getLivraisonAvion().getVitesseLivraion(), vitesseAvion, progressAvion);
        }
    }
}
