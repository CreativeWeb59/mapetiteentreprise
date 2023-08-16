package com.example.mapetiteentreprise.actions;

import com.example.mapetiteentreprise.jeu.Poulaillers;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

public class Outils {
    /**
     * Mise en forme du pseudo :
     * on enleve tous les espaces
     * nombre mini de caratères : 3
     * nombre maxi de caratères : 20
     * Caracteres autorisés : alphabet de a à z + chiffres de 0 à 9
     */
    public static Resultat testPseudo(String chaine, int min, int max){
        if(chaine.length()>0){
            String chaineATraiter = miseEnFormePseudo(chaine);

            // Test taille de la chaine
            Boolean taille = isTailleChaine(chaine, min, max);

            // Test du contenu de la chaine
            Boolean contenu = isAlphabet(chaineATraiter);

            if (taille && contenu) {
                return new Resultat(chaineATraiter, true);
            } else {
                return new Resultat("Le pseudo n'est pas valide", false);
            }
        } else {
            return new Resultat("Veuillez inscrire votre pseudo", false);
        }
    }

//            return (taille && contenu);


    /**
     * On passe la chaine tout en majuscule
     * on enlève tous les espaces
     * @param chaine
     * @return
     */
    public static String miseEnFormePseudo(String chaine){
        // tout minuscule
        chaine = chaine.toLowerCase();

        // enleve les caractères espace, les tabulations et les retours à la ligne
        String chaineTransformee = chaine.replaceAll("\\s", "");

        // enleve les espaces
        return chaineTransformee;
    }

    /**
     * la chaine doit contenir les caractères de l'alphabet
     * et doit commencer forcement par une lettre : cad les 26 premiers caractères de la liste
     * @param chaine
     * @return
     */
    public static Boolean isAlphabet(String chaine){
        String[] alphabet = new String[]{"a", "b","c","d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
                "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        // premier test sur le premier caractère qui doit être une lettre
        Boolean premierTest = false;
        String letrre1 = chaine.substring(0, 1);
        for (int i = 0; i < 26; i++) {
            if(letrre1.equals(alphabet[i])){
                premierTest = true;
            }
        }
        // deuxieme test sur tous les caractères
        if (premierTest){
            for (char c : chaine.toCharArray()) {
                String caractere = String.valueOf(c);
                boolean caractereAutorise = false;
                for (String lettre : alphabet) {
                    if (caractere.equalsIgnoreCase(lettre)) {
                        caractereAutorise = true;
                        break;
                    }
                }
                if (!caractereAutorise) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * permet de verifer la taille d'une chaine de caractères
     * renvoi false si la taille n'est pas comprise entre les deux valeurs min et max
     * @param chaine
     * @param max
     * @param min
     * @return
     */
    public static Boolean isTailleChaine(String chaine, int min, int max){
        if (chaine.length()>=min && chaine.length()<=max){
            return true;
        }
        return false;
    }

    /**
     * Formate les nombres décimaux avec séparateur de milliers
     * @return
     */
    public static DecimalFormat getDecimalFormatWithSpaceSeparator() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator(' '); // Espace pour le séparateur de milliers
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        return decimalFormat;
    }

    /**
     * Calcule la difference entre deux dates
     * @param date1
     * @param date2
     * @return un long representant des secondes écoulées entre la date2 et la date1
     */
    public static long differenceEntreDeuxDates(LocalDateTime date1, LocalDateTime date2){
        return ChronoUnit.SECONDS.between(date2, date1);
    }

    /**
     * Affichage du message d'erreur pendant un certain laps de temps donnée
     *
     * @param labelErreur le label qui doit être affiché temporairement
     * @param messageErreur le message à inscrire
     * @param delai délai d'affichage du message
     */
    public static void afficherMessageTemporaire(Label labelErreur, String messageErreur, int delai) {
        // affiche le label pour la gestion des erreurs
        labelErreur.setVisible(true);

        labelErreur.setText(messageErreur); // Afficher le message dans le label

        // Créer une PauseTransition pour la durée spécifiée
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(delai));
        pauseTransition.setOnFinished(event -> {
            labelErreur.setText(""); // Effacer le contenu du label après le délai
            labelErreur.setVisible(false); // cache a nouveau le label d'erreur
        });

        pauseTransition.play(); // Démarrer la temporisation
    }

    /**
     * Donne une position X pour centrer un objet (exemple bouton)
     * par rapport à un un autre objet (par exemple image)
     * @param imagePosX
     * @param imageWidth
     * @param widthBtn
     * @return
     */
    public static double centragePosX(double imagePosX, double imageWidth, double widthBtn){
        return (imagePosX + (imageWidth / 2) - (widthBtn / 2));
    }

    /**
     * Recupere la capacite totale en poule de tous les poulaillers
     * en fonction de leur index
     * @return
     */
    public static int capaciteMaxPoulaillers(List<Poulaillers> poulaillersList, int poulailler1, int poulailler2, int poulailler3, int poulailler4){
        int capacite1 = poulaillersList.get(poulailler1).getCapacite();
        int capacite2 = poulaillersList.get(poulailler2).getCapacite();
        int capacite3 = poulaillersList.get(poulailler3).getCapacite();
        int capacite4 = poulaillersList.get(poulailler4).getCapacite();
        return capacite1 + capacite2 + capacite3 + capacite4;
    }

    /**
     * Permet modifier le texte du label donné en paramètre
     * @param label
     */
    public static void setLabel(Label label, String texte){
        label.setText(texte);
    }
}
