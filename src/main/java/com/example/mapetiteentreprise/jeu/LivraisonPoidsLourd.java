package com.example.mapetiteentreprise.jeu;

import java.math.BigDecimal;

public class LivraisonPoidsLourd extends Livraisons{
    private final BigDecimal prixVehicule = BigDecimal.valueOf(110000); // prix du scooter
    private final BigDecimal prixCourse = BigDecimal.valueOf(1100.00); // tarif de chaque course

    public LivraisonPoidsLourd(String nom, int nbVehicules, long nbCourses, double etatProgressLivraison) {
        super(nom, nbVehicules, nbCourses, etatProgressLivraison);
    }
}
