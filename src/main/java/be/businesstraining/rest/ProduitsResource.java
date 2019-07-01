package be.businesstraining.rest;

import be.businesstraining.domain.Produit;
import be.businesstraining.repository.ProduitsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produits")
public class ProduitsResource {

    private ProduitsRepository produitsRepository;

    public ProduitsResource(ProduitsRepository produitsRepository) {
        this.produitsRepository = produitsRepository;
    }

    @GetMapping
    public List<Produit> getAllProduits(){
        return  produitsRepository.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Produit> getDetailProduit(@PathVariable("id") Long id){
        Optional<Produit> resultat =  produitsRepository.findById(id);
        if (resultat.isPresent())
            return new ResponseEntity<>(resultat.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public  ResponseEntity<Produit> create( @RequestBody Produit c){

        if (!produitsRepository.findById(c.getId()).isPresent())
        {
            produitsRepository.save(c);
            return new ResponseEntity<Produit>(c, HttpStatus.CREATED);
        } else
            return new ResponseEntity<Produit>(HttpStatus.NOT_ACCEPTABLE);

    }
    @PutMapping
    public  ResponseEntity<Produit> update(@RequestBody Produit c){

        try {
            produitsRepository.save(c);
            return new ResponseEntity<Produit>(c, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<Produit>(HttpStatus.NOT_ACCEPTABLE);
        }

    }
    @DeleteMapping(path= "/{id}" )
    public  ResponseEntity<Produit> deleteById(@PathVariable("id") Long id) {
        Optional<Produit> resultat =  produitsRepository.findById(id);
        if (resultat.isPresent()) {
            produitsRepository.delete(resultat.get());
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }

        else
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }


}
