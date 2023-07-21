package com.example.mapetiteentreprise.jeu;

import java.time.LocalDateTime;

public class BoissonsFraiches extends Distributeurs{
    public BoissonsFraiches(int nbDistributeurs, long nbMarchandises, double etatProgressDistributeur, LocalDateTime dateDeco) {
        super(nbDistributeurs, nbMarchandises, etatProgressDistributeur, dateDeco);
    }
}
