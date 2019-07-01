package be.businesstraining.rest;

import be.businesstraining.domain.Offre;
import be.businesstraining.repository.ClientsRepository;
import be.businesstraining.repository.RolesRepository;
import be.businesstraining.service.OffresService;
import be.businesstraining.service.OffresServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/offres")
public class OffresResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(OffresResource.class);

    private ClientsRepository clientsRepository;
    private OffresService offresService;

    public OffresResource(ClientsRepository clientsRepository,
                          OffresService offresService) {
        this.clientsRepository = clientsRepository;
        this.offresService = offresService;

    }

    @PostMapping("/{idProduit}/{montantOffre}")
    public ResponseEntity<?> donnerOffre(@PathVariable Long idProduit,
                                   @PathVariable BigDecimal montantOffre,
                                   Principal user) {
        try {
            // Récupérer le client à partir du contexte de sécurité (via le raccourci principal) et lui ajouter l'offre
            String username = user.getName();
            offresService.deposerOffre(username, idProduit, montantOffre);
            LOGGER.info("Succes du dépôt de l'offre");
            return new ResponseEntity<String>("Success du dépôt", HttpStatus.OK);

        } catch (Exception ex) {
            LOGGER.error("Exception lors du dépôt de l'Offre: " + ex);
            return new ResponseEntity<String>("Erreur lors de l'ajout de l'Offre : " + ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> toutesMesOffres(Principal user) {
        try {
            // Récupérer le client à partir du contexte de sécurité (via le raccourci principal) et lui ajouter l'offre
            String me = user.getName();
            Set<Offre> offres =offresService.toutesLesOffresParUtilisateur(me);
            return (offres == null)?
                    new ResponseEntity<>(HttpStatus.NOT_FOUND):
                    new ResponseEntity<Set<Offre>>(offres, HttpStatus.OK);

        } catch (Exception ex) {
            LOGGER.error("Exception lors de la consultation de mes offres: " + ex);
            return new ResponseEntity<String>("Erreur lors de l'ajout de l'Offre : " + ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/me/{idProduit}")
    public ResponseEntity<?> toutesMesOffresPourUnProduit(@PathVariable Long idProduit, Principal user) {
        try {
            // Récupérer le client à partir du contexte de sécurité (via le raccourci principal) et lui ajouter l'offre
            String me = user.getName();
            Set<Offre> offres =offresService.toutesLesOffresParUtilisateurParProduit(me,idProduit);
            return (offres == null)?
                    new ResponseEntity<>(HttpStatus.NOT_FOUND):
                    new ResponseEntity<Set<Offre>>(offres, HttpStatus.OK);

        } catch (Exception ex) {
            LOGGER.error("Exception lors de la consultation de mes offres: " + ex);
            return new ResponseEntity<String>("Erreur lors de l'ajout de l'Offre : " + ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

}
