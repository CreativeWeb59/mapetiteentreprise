package com.example.mapetiteentreprise.jeu;

import java.time.LocalDateTime;

public class Confiseries extends Distributeurs{
    public Confiseries(int nbDistributeurs, long nbMarchandises, double etatProgressDistributeur, LocalDateTime dateDeco) {
        super(nbDistributeurs, nbMarchandises, etatProgressDistributeur, dateDeco);
    }
}
