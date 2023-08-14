package com.example.mapetiteentreprise.jeu;

import java.math.BigDecimal;

public class LivraisonCamionette  extends Livraisons{
    private final BigDecimal prixVehicule = BigDecimal.valueOf(20000); // prix du scooter
    private final BigDecimal prixCourse = BigDecimal.valueOf(200.00); // tarif de chaque course

    public LivraisonCamionette(String nom, int nbVehicules, long nbCourses, double etatProgressLivraison) {
        super(nom, nbVehicules, nbCourses, etatProgressLivraison);
    }
}
