package com.example.mapetiteentreprise.jeu;

import java.math.BigDecimal;

public class LivraisonPetitCamion extends Livraisons{
    private final BigDecimal prixVehicule = BigDecimal.valueOf(70000); // prix du scooter
    private final BigDecimal prixCourse = BigDecimal.valueOf(700.00); // tarif de chaque course

    public LivraisonPetitCamion(String nom, int nbVehicules, long nbCourses, double etatProgressLivraison) {
        super(nom, nbVehicules, nbCourses, etatProgressLivraison);
    }
}
