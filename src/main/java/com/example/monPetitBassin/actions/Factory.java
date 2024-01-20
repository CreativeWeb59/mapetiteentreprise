package com.example.monPetitBassin.actions;

import com.example.monPetitBassin.jeu.Bassin;
import com.example.monPetitBassin.jeu.Poisson;
import com.example.monPetitBassin.jeu.TypePoisson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

public class Factory {
    public static ArrayList<Bassin>createBassinsDefaut(){
        String[] nomsBassins = {
                "Bassin 1", "Bassin 2", "Bassin 3", "Bassin 4", "Bassin 5",
                "Bassin 6", "Bassin 7", "Bassin 8", "Bassin 9", "Bassin 10",
                "Bassin 11", "Bassin 12", "Bassin 13", "Bassin 14", "Bassin 15", "Bassin 16"
        };
        ArrayList<Bassin> listeBassin = new ArrayList<>();

        // Remplissage de l'ArrayList avec des objets Poisson
        BigDecimal tarif = BigDecimal.valueOf(0);
        int capacite = 100;
        boolean actif = true;
        for (String nom : nomsBassins) {
            Bassin bassin = new Bassin(nom, new Random().nextInt(8,14), tarif, 0, capacite, actif);
            listeBassin.add(bassin);
            tarif = tarif.add(BigDecimal.valueOf(100));
            capacite += 100;
            actif =false;
        }

        return listeBassin;
    }

    public static ArrayList<TypePoisson> listTypePoissons(){
        TypePoisson eauFroide = new TypePoisson("Eau froide", 10, 18, 1, 100, 6000);
        TypePoisson eauChaude = new TypePoisson("Eau chaude", 22, 28, 1, 100, 6000);
        TypePoisson eauSalee = new TypePoisson("Eau salée", 22, 28, 1, 100, 6000);

        ArrayList<TypePoisson>liste = new ArrayList<>();
        liste.add(eauFroide);
        liste.add(eauChaude);
        liste.add(eauSalee);
        return liste;
    }

    /**
     * cree une liste de 40 poissons d'eau froide avec leurs caracteristiques
     * @param typePoisson
     * @return
     */
    public static ArrayList<Poisson> listePoissonsEauFroide(TypePoisson typePoisson) {
        String[] nomsPoissons = {
                "Truite brune", "Omble de fontaine", "Ombre commun", "Saumon atlantique", "Omble chevalier",
                "Brochet", "Ombre de rivière", "Ombre de lac", "Lotte", "Brochet",
                "Perche", "Sandre", "Silure glane", "Black bass", "Bar européen",
                "Carpe commune", "Carpe miroir", "Corégone", "Tanche", "Gardon",
                "Ablette", "Rotengle", "Brème", "Hotu", "Chevaine",
                "Épinoche", "Goujon", "Lamproie","Loche commune", "Barbeau méridional",
                "Loche d'étang",  "Perche soleil", "Rotengle", "Vandoise", "Saumon de rivière",
                "Sardine", "Tanche", "Vairon", "Anguille", "Ablette"
        };
        ArrayList<Poisson> listePoissons = new ArrayList<>();

        // Remplissage de l'ArrayList avec des objets Poisson
        for (String nom : nomsPoissons) {
            Poisson poisson = new Poisson(nom, typePoisson, new Random().nextInt(8,14), new Random().nextInt(15,24), 100, 0);
            listePoissons.add(poisson);
        }

        return listePoissons;
    }
    /**
     * cree une liste de 30 poissons d'eau chaude avec leurs caracteristiques
     * @param typePoisson
     * @return
     */
    public static ArrayList<Poisson> listePoissonsEauChaude(TypePoisson typePoisson) {
        String[] nomsPoissons = {
                "Guppy",  "Tétra néon", "Molly", "Platy", "Combattant (Betta)",
                "Danio zébré", "Barbus cerise","Cichlidé nain (Ramirezi)", "Cichlidé à bande dorsale", "Colisa lalia (Gourami nain)",
                "Poisson-ange", "Corydoras", "Ancistrus","Gourami perlé", "Poisson-chat de verre",
                "Cichlidé du Texas",  "Poisson-archer", "Poisson-chat de Synodontis", "Tétra empereur", "Loche clown",
                "Barbus tigre", "Botia macracantha (Loche clown zèbre)","Barbus de Sumatra", "Discus", "Cichlidé du lac Malawi",
                "Cichlidé du lac Tanganyika", "Gourami miel", "Poisson-éléphant", "Killies"
        };
        ArrayList<Poisson> listePoissons = new ArrayList<>();

        // Remplissage de l'ArrayList avec des objets Poisson
        for (String nom : nomsPoissons) {
            Poisson poisson = new Poisson(nom, typePoisson, new Random().nextInt(12,20), new Random().nextInt(20,32), 100, 0);
            listePoissons.add(poisson);
        }

        return listePoissons;
    }
    /**
     * cree une liste de 30 poissons d'eau salée avec leurs caracteristiques
     * @param typePoisson
     * @return
     */
    public static ArrayList<Poisson> listePoissonsEauSalee(TypePoisson typePoisson) {
        String[] nomsPoissons = {
                "Poisson-clown (Amphiprion ocellaris)",
                "Naso unicornis (Licorne des mers)",
                "Thon rouge (Thunnus thynnus)",
                "Poisson-ange empereur (Pomacanthus imperator)",
                "Poisson-lion (Pterois volitans)",
                "Requin-marteau (Sphyrna spp.)",
                "Poisson-scie à long nez (Pristis pristis)",
                "Poisson-papillon à bandelette (Chaetodon fasciatus)",
                "Girelle paon (Thalassoma pavo)",
                "Napoléon (Cheilinus undulatus)",
                "Poisson-chirurgien bleu (Paracanthurus hepatus)",
                "Diodon (Diodon spp.)",
                "Séraphin de mer (Holacanthus tricolor)",
                "Poisson-ange à front barré (Pomacanthus arcuatus)",
                "Poisson-arlequin (Cirrhilabrus spp.)", "Poisson-trompette (Aulostomus maculatus)",
                "Raie manta (Manta birostris)", "Poisson-crocodile (Papilloculiceps longiceps)",
                "Poisson-archer rayé (Toxotes jaculatrix)", "Poisson-globe (Arothron spp.)",
                "Dorade royale (Sparus aurata)", "Poisson-lime (Synodus synodus)",
                "Mérou (Epinephelus spp.)", "Morue (Gadus morhua)",
                "Poisson-grenouille (Antennarius spp.)", "Poisson-licorne (Naso spp.)",
                "Poisson-globe porc-épic (Diodon holocanthus)", "Poisson-hachette (Hatchetfish)",
                "Poisson-coffre (Ostraciidae)", "Poisson-ange nain (Centropyge spp.)"
        };
        ArrayList<Poisson> listePoissons = new ArrayList<>();

        // Remplissage de l'ArrayList avec des objets Poisson
        for (String nom : nomsPoissons) {
            Poisson poisson = new Poisson(nom, typePoisson, new Random().nextInt(8,14), new Random().nextInt(15,24), 100, 0);
            listePoissons.add(poisson);
        }

        return listePoissons;
    }
}
