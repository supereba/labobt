package be.businesstraining.service;

import be.businesstraining.domain.Offre;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.Set;

public interface OffresService {

    void deposerOffre(String username, Long idProduit,
                      @PathVariable BigDecimal montantOffre);

    Set<Offre> toutesLesOffresParUtilisateur (String username);
    Set<Offre> toutesLesOffresParUtilisateurParProduit (String username, Long productId);


}
