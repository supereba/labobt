package be.businesstraining.repository;

import be.businesstraining.domain.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ProduitsRepository extends JpaRepository<Produit,Long> {

}
