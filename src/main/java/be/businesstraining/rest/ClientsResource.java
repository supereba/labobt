package be.businesstraining.rest;


import be.businesstraining.domain.Client;
import be.businesstraining.domain.Offre;
import be.businesstraining.domain.Role;
import be.businesstraining.repository.ClientsRepository;
import be.businesstraining.repository.RolesRepository;
import be.businesstraining.service.OffresService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;

@RestController
public class ClientsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientsResource.class);

    private ClientsRepository clientsRepository;
    private RolesRepository roleRepository;
    private OffresService offresService;


    public ClientsResource(ClientsRepository clientsRepository,
                           RolesRepository roleRepository,
                           OffresService offresService) {
        this.clientsRepository = clientsRepository;
        this.roleRepository = roleRepository;
        this.offresService = offresService;

    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Client client) {
        // LOGGER.info(">>>>> RECU The username " + client.getUsername() + " - "+client.getPassword());
        try {
            Client resultUser = clientsRepository.findByUsername(client.getUsername());
            // Tester si le nom d'utilisatur est déjà réservé
            if (resultUser != null) {
                LOGGER.info("The username " + client.getUsername() + " is already taken !");
                return new ResponseEntity<String>("Username already taken: ", HttpStatus.CONFLICT);

            } else {
                // - Ajouter l'utilisateur à la BDD
                // Role role1 = new Role("USER", new HashSet<>(Arrays.asList(user)));
                Role role1 = roleRepository.findByRole("USER");   //new Role("USER", new HashSet<>(Arrays.asList(user)));
                LOGGER.info(">>>>>>>>   >>>> Role NULL ???: " + (role1 == null));
                client.setPassword(new BCryptPasswordEncoder().encode(client.getPassword()));
                client.setEnabled(true);
                client.setRoles(new HashSet<>(Arrays.asList(role1)));
                clientsRepository.save(client);
                LOGGER.info("The username " + client.getUsername() + " has been added to the database !");

                return new ResponseEntity<String>("Success de l'enregistrement", HttpStatus.CREATED);

            }
        } catch (Exception ex) {
            LOGGER.error("Exception lors de l'enregistrement de l'utlisateur:"+ ex);
            return new ResponseEntity<String>("Erreur lors de l'enregistrement : " + ex.getMessage(), HttpStatus.CONFLICT);
            // throw new Exception("Exception lors de l'enregistrement de l'utlisateur : "+ex.getMessage());
        }
    }

    @GetMapping("/checklogin")
    public ResponseEntity<?> login(Principal user) {

        if (user != null)
            return new ResponseEntity<String>(user.getName() + ": Authenticated successfully", HttpStatus.OK);
        else
            return new ResponseEntity<String>("Please add your basic token in the Authorization Header",
                    HttpStatus.UNAUTHORIZED);
    }
}
