package be.businesstraining.service;

import be.businesstraining.domain.Client;
import be.businesstraining.domain.Offre;
import be.businesstraining.domain.Produit;
import be.businesstraining.repository.ClientsRepository;
import be.businesstraining.repository.OffresRepository;
import be.businesstraining.repository.ProduitsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OffresServiceImpl implements OffresService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OffresServiceImpl.class);

    private ClientsRepository clientsRepository;
    private OffresRepository offresRepository;
    private ProduitsRepository produitsRepository;

    public OffresServiceImpl(ClientsRepository clientsRepository,
                             OffresRepository offresRepository,
                             ProduitsRepository produitsRepository ) {
        this.clientsRepository = clientsRepository;
        this.offresRepository = offresRepository;
        this.produitsRepository = produitsRepository;
    }

    @Override
    public void deposerOffre(String username, Long idProduit,
                             @PathVariable BigDecimal montantOffre) {
        try {
            // Récupérer le Cient offrant
            Client client = clientsRepository.findByUsername(username);

            // Récupérer le produit objet de l'offre
            Produit produit = produitsRepository.findById(idProduit).orElse(null);

            // Vérifier que le montant de l'offre est inférieur au solde du client            //
            // et que l'offre est supérieure au prix minimum demandé le produit
//            if ((client.getSolde().compareTo(montantOffre)<0)||
//                (montantOffre.compareTo(produit.getPrixMinimumDemande())<0)) {
//                LOGGER.info("Il y a une incohérence au niveau de la valeur de l'offre");
//                throw new Exception("Incohérence au niveau de la valeur de l'offre");
//            }

            // Crére l'objet de la nouvelle Offre
            Offre offre = new Offre(client, produit, montantOffre, LocalDateTime.now());

            // Ajouter la nouvelle offre à la liste des offres du client
            Set<Offre> offresDuClient = client.getOffres();
            offresDuClient.add(offre);
            client.setOffres(offresDuClient);
            LOGGER.info("Ajout de l'offre à la liste des offres du client");

            // Ajouter la nouvelle offre à la liste des offres relatives à un produit

            Set<Offre> offresDuProduit = produit.getOffres();
            offresDuProduit.add(offre);
            produit.setOffres(offresDuProduit);
            LOGGER.info("Ajout de l'offre à la liste des offres relative au produit");
            // Actualiser le solde du client en lui ôtant le montant de l'offre
            //client.setSolde(client.getSolde().subtract(montantOffre));
            // Synchroniser les objets persistants
            offresRepository.save(offre);

        } catch (Exception ex) {
            LOGGER.error("Exception lors du dépôt de l'Offre: " + ex.getMessage());
        }
    }

    @Override
    public Set<Offre> toutesLesOffresParUtilisateur(String username) {
        Set<Offre> a_retourner = new HashSet<>();
        try {
            a_retourner = offresRepository.findAllByClient_Username(username);
            LOGGER.info("********* Le nombre d offres de : " + username + " est : "+ a_retourner.size() );
        }catch (Exception ex){
            LOGGER.error("******** Exception lors de la consultation de mes offres: " + ex.getMessage());
        }
        return  a_retourner;
    }

    @Override
    public Set<Offre> toutesLesOffresParUtilisateurParProduit(String username, Long idProduit) {

        Set<Offre> a_retourner = new HashSet<>();
        try {
            a_retourner = offresRepository.findAllByClient_UsernameAndProduit_Id(username,idProduit);
            LOGGER.info("********* Le nombre d offres de : " + username + " est : "+ a_retourner.size() );
        }catch (Exception ex){
            LOGGER.error("******** Exception lors de la consultation de mes offres: " + ex.getMessage());
        }
        return  a_retourner;
    }
}
