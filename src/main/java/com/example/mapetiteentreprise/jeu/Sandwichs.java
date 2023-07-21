package com.example.mapetiteentreprise.jeu;

import java.time.LocalDateTime;

public class Sandwichs extends Distributeurs{
    public Sandwichs(int nbDistributeurs, long nbMarchandises, double etatProgressDistributeur, LocalDateTime dateDeco) {
        super(nbDistributeurs, nbMarchandises, etatProgressDistributeur, dateDeco);
    }
}
