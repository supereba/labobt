package be.businesstraining.service;

import be.businesstraining.domain.Offre;

import java.util.Set;

public interface ProduitsService {
    Set<Offre> toutesLesOffresParProduit (Long idProduit);
}
