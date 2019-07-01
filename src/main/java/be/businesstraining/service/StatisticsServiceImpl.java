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

@Service
public class StatisticsServiceImpl implements  StatisticsService{
    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    private ClientsRepository clientsRepository;
    private OffresRepository offresRepository;
    private ProduitsRepository produitsRepository;

    public StatisticsServiceImpl(ClientsRepository clientsRepository,
                             OffresRepository offresRepository,
                             ProduitsRepository produitsRepository ) {
        this.clientsRepository = clientsRepository;
        this.offresRepository = offresRepository;
        this.produitsRepository = produitsRepository;
    }

    @Override
    public Offre laMeilleureOffrePourUnProduit(Long idProduit) {
        Produit produit = produitsRepository.findById(idProduit).orElse(null);
        return offresRepository.laMeilleureOffrePourUnProduit(produit);
    }

    @Override
    public Client leClientAyantLeMeilleurSolde() {
        Client client = clientsRepository.clientAyantLeMeilleurSolde().iterator().next();
       return  client;
    }
}
