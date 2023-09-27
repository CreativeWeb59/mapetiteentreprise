// usine textile :  petite usine : 500.000 €, maxi 50
//                  usine moyenne : 750.000 €
//                  grande usine : 1.000.000 €
//                  énorme usine : 1.500.000 €

// usine jouets

// usine agroalimentaire

// usine pharmaceutique

// => Petite, moyennne, grande, enorme


// usine composants informatiques

// page de gestion avec selection de chaque usine
// une page par usine

// presse, journaux
// petite imprimerie
// moyenne imprimerie
// grande imprimerie
// imprimerie industrielle, imprimerie commerciale de grande envergure, maison d'édition ou imprimerie de presse

// fabrication voitures


// modifier le blocage par le banquier pour qu'il apparaisse sur toute les pages
// voir quand la date est dépassé de renvoyer vers la page gestion

// creer un fichier des timelines pour chaque progress bar de façon à intégrer le fichier et non copier toutes les timelines

package com.example.mapetiteentreprise.jeu;

import com.example.mapetiteentreprise.bdd.*;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Jeu {
    private final boolean dev = true;
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
     *
     * @param barresDeProgressions
     */
    public void setUneBarreDeProgression(AnimationsBarresProgress barresDeProgressions) {
        this.barresDeProgressions.add(barresDeProgressions);
    }

    /**
     * fait appel a une methode de la classe AnimationBarreProgress
     *
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

    public boolean isDev() {
        return dev;
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
    public void sauvegardejeu() {
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
        getSauvegarde().setLivraison1Active(getJoueur().getLivraison1Active());
        getSauvegarde().setLivraison2Active(getJoueur().getLivraison2Active());
        getSauvegarde().setLivraison3Active(getJoueur().getLivraison3Active());
        getSauvegarde().setLivraison4Active(getJoueur().getLivraison4Active());
        getSauvegarde().setLivraison5Active(getJoueur().getLivraison5Active());
        getSauvegarde().setNbLivraison1(getJoueur().getLivraisonScooter().getNbVehicules());
        getSauvegarde().setNbLivraison2(getJoueur().getLivraisonCamionette().getNbVehicules());
        getSauvegarde().setNbLivraison3(getJoueur().getLivraisonPetitCamion().getNbVehicules());
        getSauvegarde().setNbLivraison4(getJoueur().getLivraisonPoidsLourd().getNbVehicules());
        getSauvegarde().setNbLivraison5(getJoueur().getLivraisonAvion().getNbVehicules());
        getSauvegarde().setNbCourses1(getJoueur().getLivraisonScooter().getNbCourses());
        getSauvegarde().setNbCourses2(getJoueur().getLivraisonCamionette().getNbCourses());
        getSauvegarde().setNbCourses3(getJoueur().getLivraisonPetitCamion().getNbCourses());
        getSauvegarde().setNbCourses4(getJoueur().getLivraisonPoidsLourd().getNbCourses());
        getSauvegarde().setNbCourses5(getJoueur().getLivraisonAvion().getNbCourses());
        getSauvegarde().setEtatProgressLivraison1(getJoueur().getLivraisonScooter().getEtatProgressLivraison());
        getSauvegarde().setEtatProgressLivraison2(getJoueur().getLivraisonCamionette().getEtatProgressLivraison());
        getSauvegarde().setEtatProgressLivraison3(getJoueur().getLivraisonPetitCamion().getEtatProgressLivraison());
        getSauvegarde().setEtatProgressLivraison4(getJoueur().getLivraisonPoidsLourd().getEtatProgressLivraison());
        getSauvegarde().setEtatProgressLivraison5(getJoueur().getLivraisonAvion().getEtatProgressLivraison());

        // textiles
        getSauvegarde().setUsineTextileActive1(getJoueur().getUsineTextilePetite().getUsineActive());
        getSauvegarde().setNbUsinesTextile1(getJoueur().getUsineTextilePetite().getNbUsines());
        getSauvegarde().setNbMarchandisesUsineTextile1(getJoueur().getUsineTextilePetite().getNbMarchandises());
        getSauvegarde().setEtatProgressUsineTextile1(getJoueur().getUsineTextilePetite().getEtatProgressUsine());

        getSauvegarde().setUsineTextileActive2(getJoueur().getUsineTextileMoyenne().getUsineActive());
        getSauvegarde().setNbUsinesTextile2(getJoueur().getUsineTextileMoyenne().getNbUsines());
        getSauvegarde().setNbMarchandisesUsineTextile2(getJoueur().getUsineTextileMoyenne().getNbMarchandises());
        getSauvegarde().setEtatProgressUsineTextile2(getJoueur().getUsineTextileMoyenne().getEtatProgressUsine());

        getSauvegarde().setUsineTextileActive3(getJoueur().getUsineTextileGrande().getUsineActive());
        getSauvegarde().setNbUsinesTextile3(getJoueur().getUsineTextileGrande().getNbUsines());
        getSauvegarde().setNbMarchandisesUsineTextile3(getJoueur().getUsineTextileGrande().getNbMarchandises());
        getSauvegarde().setEtatProgressUsineTextile3(getJoueur().getUsineTextileGrande().getEtatProgressUsine());

        getSauvegarde().setUsineTextileActive4(getJoueur().getUsineTextileEnorme().getUsineActive());
        getSauvegarde().setNbUsinesTextile4(getJoueur().getUsineTextileEnorme().getNbUsines());
        getSauvegarde().setNbMarchandisesUsineTextile4(getJoueur().getUsineTextileEnorme().getNbMarchandises());
        getSauvegarde().setEtatProgressUsineTextile4(getJoueur().getUsineTextileEnorme().getEtatProgressUsine());

        // jouets
        getSauvegarde().setUsineJouetsActive1(getJoueur().getUsineJouetsPetite().getUsineActive());
        getSauvegarde().setNbUsinesJouets1(getJoueur().getUsineJouetsPetite().getNbUsines());
        getSauvegarde().setNbMarchandisesUsineJouets1(getJoueur().getUsineJouetsPetite().getNbMarchandises());
        getSauvegarde().setEtatProgressUsineJouets1(getJoueur().getUsineJouetsPetite().getEtatProgressUsine());

        getSauvegarde().setUsineJouetsActive2(getJoueur().getUsineJouetsMoyenne().getUsineActive());
        getSauvegarde().setNbUsinesJouets2(getJoueur().getUsineJouetsMoyenne().getNbUsines());
        getSauvegarde().setNbMarchandisesUsineJouets2(getJoueur().getUsineJouetsMoyenne().getNbMarchandises());
        getSauvegarde().setEtatProgressUsineJouets2(getJoueur().getUsineJouetsMoyenne().getEtatProgressUsine());

        getSauvegarde().setUsineJouetsActive3(getJoueur().getUsineJouetsGrande().getUsineActive());
        getSauvegarde().setNbUsinesJouets3(getJoueur().getUsineJouetsGrande().getNbUsines());
        getSauvegarde().setNbMarchandisesUsineJouets3(getJoueur().getUsineJouetsGrande().getNbMarchandises());
        getSauvegarde().setEtatProgressUsineJouets3(getJoueur().getUsineJouetsGrande().getEtatProgressUsine());

        getSauvegarde().setUsineJouetsActive4(getJoueur().getUsineJouetsEnorme().getUsineActive());
        getSauvegarde().setNbUsinesJouets4(getJoueur().getUsineJouetsEnorme().getNbUsines());
        getSauvegarde().setNbMarchandisesUsineJouets4(getJoueur().getUsineJouetsEnorme().getNbMarchandises());
        getSauvegarde().setEtatProgressUsineJouets4(getJoueur().getUsineJouetsEnorme().getEtatProgressUsine());

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
     * 5 poulaillers + 1 inactif
     * et affectation dans la liste des poulaillers
     */
    public void createPoulaillers() {
        Poulaillers poulaillerInactif = new Poulaillers("Inactif", BigDecimal.valueOf(0), 0);
        this.addPoulailler(poulaillerInactif);
        Poulaillers poulailler = new Poulaillers("Poulailler", BigDecimal.valueOf(1000), 200);
        this.addPoulailler(poulailler);
        Poulaillers poulaillerPro = new Poulaillers("Poulailler pro", BigDecimal.valueOf(5000), 1000);
        this.addPoulailler(poulaillerPro);
        Poulaillers poulaillerMega = new Poulaillers("Méga poulailler", BigDecimal.valueOf(25000), 5000);
        this.addPoulailler(poulaillerMega);
        Poulaillers poulaillerIndustriel = new Poulaillers("Poulailler industriel", BigDecimal.valueOf(50000), 10000);
        this.addPoulailler(poulaillerIndustriel);
        Poulaillers poulaillerGeant = new Poulaillers("Poulailler géant", BigDecimal.valueOf(100000), 20000);
        this.addPoulailler(poulaillerGeant);
    }

    public List<Poulaillers> getPoulaillersList() {
        return poulaillersList;
    }

    public void setPoulaillersList(List<Poulaillers> poulaillersList) {
        this.poulaillersList = poulaillersList;
    }

    public void addPoulailler(Poulaillers poulaillers) {
        this.poulaillersList.add(poulaillers);
    }

    /**
     * Calcule la valeur de l'entreprise
     */
    public BigDecimal valeurEntreprise() {
        BigDecimal valeurFermes = valeurFermes();
        System.out.println("Fermes : " + valeurFermes);
        BigDecimal valeurPoules = valeurPoules();
        System.out.println("Poules : " + valeurPoules);
        BigDecimal valeurDistributeurs = valeurDistributeurs();
        System.out.println("Distributeurs : " + valeurDistributeurs);
        BigDecimal valeurLivraisons = ValeurLivraisons();
        System.out.println("Livraisons : " + ValeurLivraisons());
        BigDecimal valeurUsinesTextile = valeurUsinesTextile();
        System.out.println("Usines Textile : " + valeurUsinesTextile);
        BigDecimal valeursUsinesJouets = valeurUsinesJouets();
        System.out.println("Usines jouets : " + valeursUsinesJouets);
        return valeurFermes.add(valeurDistributeurs.add(valeurPoules.add(valeurLivraisons).add(valeurUsinesTextile)));
    }

    /**
     * Retourne la valeur de chaque poulailler
     *
     * @return
     */
    public BigDecimal valeurFermes() {
        // recupere la valeur de chaque poulailler dans un tableau
        int[] index = new int[]{this.getJoueur().getPoulailler1(), this.getJoueur().getPoulailler2(), this.getJoueur().getPoulailler3(), this.getJoueur().getPoulailler4()};
        // index[0] = 2 => prix poulailler[2]
        // index[1] = 0 => prix poulailler[0]
        // index[2] = 0 => prix poulailler[0]
        // index[3] = 0 => prix poulailler[0]

        BigDecimal valeur = BigDecimal.valueOf(0);

        for (int i = 0; i < index.length; i++) {
            for (int j = 0; j < index.length; j++) {
                if (index[i] >= j) {
                    System.out.println("Valeur de j : " + getPoulaillersList().get(j).getPrixPoulailler());
                    valeur = valeur.add(getPoulaillersList().get(j).getPrixPoulailler());
                }
            }
        }
        System.out.println("La valeur : " + valeur);
        return valeur;
    }

    /**
     * Renvoi la valeur des poules
     *
     * @return
     */
    public BigDecimal valeurPoules() {
        int nbPoules = joueur.getFerme().getNbPoules();
        BigDecimal prixPoule = parametres.getTarifPoule();
        return prixPoule.multiply(BigDecimal.valueOf(nbPoules));
    }

    /**
     * Retourne la valeur de chaque distributeur
     * valeur distributer * nb distributeurs
     *
     * @return
     */
    public BigDecimal valeurDistributeurs() {
        BigDecimal valeur = BigDecimal.valueOf(0);
        if (joueur.getDistributeurBCActive() == 1) {
            int nbDistributeur = joueur.getBoissonsChaudes().getNbDistributeurs();
            BigDecimal prixDistributeur = parametres.getPrixDistributeurBC();
            BigDecimal valeurDistributeur = prixDistributeur.multiply(BigDecimal.valueOf(nbDistributeur));
            valeur = valeur.add(valeurDistributeur);
        }
        if (joueur.getDistributeurBFActive() == 1) {
            int nbDistributeur = joueur.getBoissonsFraiches().getNbDistributeurs();
            BigDecimal prixDistributeur = parametres.getPrixDistributeurBF();
            BigDecimal valeurDistributeur = prixDistributeur.multiply(BigDecimal.valueOf(nbDistributeur));
            valeur = valeur.add(valeurDistributeur);
        }
        if (joueur.getDistributeurCoActive() == 1) {
            int nbDistributeur = joueur.getConfiseries().getNbDistributeurs();
            BigDecimal prixDistributeur = parametres.getPrixDistributeurCo();
            BigDecimal valeurDistributeur = prixDistributeur.multiply(BigDecimal.valueOf(nbDistributeur));
            valeur = valeur.add(valeurDistributeur);
        }
        if (joueur.getDistributeurSaActive() == 1) {
            int nbDistributeur = joueur.getSandwichs().getNbDistributeurs();
            BigDecimal prixDistributeur = parametres.getPrixDistributeurSa();
            BigDecimal valeurDistributeur = prixDistributeur.multiply(BigDecimal.valueOf(nbDistributeur));
            valeur = valeur.add(valeurDistributeur);
        }
        return valeur;
    }

    /**
     * Retourne la valeur de chaque distributeur
     * valeur distributer * nb distributeurs
     *
     * @return
     */
    public BigDecimal ValeurLivraisons() {
        BigDecimal valeur = BigDecimal.valueOf(0);
        if (joueur.getLivraison1Active() == 1) {
            int nbVehicules = joueur.getLivraisonScooter().getNbVehicules();
            BigDecimal prixVehicule = joueur.getLivraisonScooter().getPrixVehicule();
            BigDecimal valeurLivraison = prixVehicule.multiply(BigDecimal.valueOf(nbVehicules));
            valeur = valeur.add(valeurLivraison);
        }
        if (joueur.getLivraison2Active() == 1) {
            int nbVehicules = joueur.getLivraisonCamionette().getNbVehicules();
            BigDecimal prixVehicule = joueur.getLivraisonCamionette().getPrixVehicule();
            BigDecimal valeurLivraison = prixVehicule.multiply(BigDecimal.valueOf(nbVehicules));
            valeur = valeur.add(valeurLivraison);
        }
        if (joueur.getLivraison3Active() == 1) {
            int nbVehicules = joueur.getLivraisonPetitCamion().getNbVehicules();
            BigDecimal prixVehicule = joueur.getLivraisonPetitCamion().getPrixVehicule();
            BigDecimal valeurLivraison = prixVehicule.multiply(BigDecimal.valueOf(nbVehicules));
            valeur = valeur.add(valeurLivraison);
        }
        if (joueur.getLivraison4Active() == 1) {
            int nbVehicules = joueur.getLivraisonPoidsLourd().getNbVehicules();
            BigDecimal prixVehicule = joueur.getLivraisonPoidsLourd().getPrixVehicule();
            BigDecimal valeurLivraison = prixVehicule.multiply(BigDecimal.valueOf(nbVehicules));
            valeur = valeur.add(valeurLivraison);
        }
        if (joueur.getLivraison5Active() == 1) {
            int nbVehicules = joueur.getLivraisonAvion().getNbVehicules();
            BigDecimal prixVehicule = joueur.getLivraisonAvion().getPrixVehicule();
            BigDecimal valeurLivraison = prixVehicule.multiply(BigDecimal.valueOf(nbVehicules));
            valeur = valeur.add(valeurLivraison);
        }
        return valeur;
    }
    public BigDecimal valeurUsinesTextile(){
        BigDecimal valeur = BigDecimal.valueOf(0);
        valeur = valeurUsineTextile(joueur.getUsineTextilePetite());
        System.out.println("Valeur petite usine : " + valeur);
        valeur = valeur.add(valeurUsineTextile(joueur.getUsineTextileMoyenne()));
        System.out.println("Valeur moyenne usine : " + valeur);
        valeur = valeur.add(valeurUsineTextile(joueur.getUsineTextileGrande()));
        System.out.println("Valeur grande usine : " + valeur);
        valeur = valeur.add(valeurUsineTextile(joueur.getUsineTextileEnorme()));
        System.out.println("Valeur enorme usine : " + valeur);
        return valeur;
    }
    public BigDecimal valeurUsineTextile(UsineTextile usineTextile){
        BigDecimal valeur = BigDecimal.valueOf(0);
        if(joueur.getUsineTextilePetite().getUsineActive() == 1){
            int nbUsines = usineTextile.getNbUsines();
            BigDecimal prixUsine = usineTextile.getPrixUsine() ;
            valeur = prixUsine.multiply(BigDecimal.valueOf(nbUsines));
        }
        return valeur;
    }

    /**
     * Valeurs des usines de jouets
     * @return
     */
    public BigDecimal valeurUsinesJouets(){
        BigDecimal valeur = BigDecimal.valueOf(0);
        valeur = valeurUsineJouets(joueur.getUsineJouetsPetite());
        System.out.println("Valeur petite usine : " + valeur);
        valeur = valeur.add(valeurUsineJouets(joueur.getUsineJouetsMoyenne()));
        System.out.println("Valeur moyenne usine : " + valeur);
        valeur = valeur.add(valeurUsineJouets(joueur.getUsineJouetsGrande()));
        System.out.println("Valeur grande usine : " + valeur);
        valeur = valeur.add(valeurUsineJouets(joueur.getUsineJouetsEnorme()));
        System.out.println("Valeur enorme usine : " + valeur);
        return valeur;
    }
    public BigDecimal valeurUsineJouets(UsineJouets usineJouets){
        BigDecimal valeur = BigDecimal.valueOf(0);
        if(joueur.getUsineJouetsPetite().getUsineActive() == 1){
            int nbUsines = usineJouets.getNbUsines();
            BigDecimal prixUsine = usineJouets.getPrixUsine() ;
            valeur = prixUsine.multiply(BigDecimal.valueOf(nbUsines));
        }
        return valeur;
    }

    /**
     * Affiche les barres de progression en mode dev
     */
    public void afficheProgression(Pane paneProgress) {
        if (this.isDev()) {
            paneProgress.setVisible(true);
        } else {
            paneProgress.setVisible(false);
        }
    }
}
// pour la maj