package be.businesstraining.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Produits")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private BigDecimal prixMinimumDemande;
    private LocalDateTime dateEnregistrement;
    private Duration dureeEnchere;

    public LocalDateTime getDateEnregistrement() {
        return dateEnregistrement;
    }

    public Produit() {
    }

    public Produit(String name, BigDecimal prixMinimumDemande, LocalDateTime dateEnregistrement, Duration dureeEnchere, Set<Offre> offres) {
        this.name = name;
        this.prixMinimumDemande = prixMinimumDemande;
        this.dateEnregistrement = dateEnregistrement;
        this.dureeEnchere = dureeEnchere;
        this.offres = offres;
    }

    public void setDateEnregistrement(LocalDateTime dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    @OneToMany(mappedBy = "produit", fetch = FetchType.LAZY)
    private Set<Offre> offres = new HashSet<>();

    public Set<Offre> getOffres() {
        return offres;
    }

    public void setOffres(Set<Offre> offres) {
        this.offres = offres;
    }

    public Produit(String name, BigDecimal prixMinimumDemande, Duration dureeEnchere, Set<Offre> offres) {
        this.name = name;
        this.prixMinimumDemande = prixMinimumDemande;
        this.dureeEnchere = dureeEnchere;
        this.offres = offres;
    }

    public Long getId() {
        return id;

    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrixMinimumDemande() {
        return prixMinimumDemande;
    }

    public void setPrixMinimumDemande(BigDecimal prixMinimumDemande) {
        this.prixMinimumDemande = prixMinimumDemande;
    }

    public Duration getDureeEnchere() {
        return dureeEnchere;
    }

    public void setDureeEnchere(Duration dureeEnchere) {
        this.dureeEnchere = dureeEnchere;
    }

}
