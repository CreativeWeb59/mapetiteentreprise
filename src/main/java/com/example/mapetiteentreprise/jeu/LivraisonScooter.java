package com.example.mapetiteentreprise.jeu;

import java.math.BigDecimal;

public class LivraisonScooter extends Livraisons{
    private final BigDecimal prixVehicule = BigDecimal.valueOf(5000); // prix du scooter
    private final BigDecimal prixCourse = BigDecimal.valueOf(50.00); // tarif de chaque course

    public LivraisonScooter(String nom, int nbVehicules, long nbCourses, double etatProgressLivraison) {
        super(nom, nbVehicules, nbCourses, etatProgressLivraison);
    }
}
