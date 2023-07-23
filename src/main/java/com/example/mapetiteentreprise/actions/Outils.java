package com.example.mapetiteentreprise.actions;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
        System.out.println("Taille de la chaine : " + chaine.length());
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
}
