package be.businesstraining.repository;

import be.businesstraining.domain.Client;
import be.businesstraining.domain.Offre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface ClientsRepository extends JpaRepository<Client,Long> {

    Client findByUsername(String username);
    Set<Offre> findAllByUsername(String username);

    @Query(value = "From Client c where c.solde = (select max(c.solde) from Client c)")
    Set<Client> clientAyantLeMeilleurSolde() ;

}
