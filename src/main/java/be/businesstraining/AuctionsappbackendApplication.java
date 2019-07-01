package be.businesstraining;

import be.businesstraining.domain.Client;
import be.businesstraining.domain.Produit;
import be.businesstraining.domain.Role;
import be.businesstraining.repository.ClientsRepository;
import be.businesstraining.repository.ProduitsRepository;
import be.businesstraining.rest.ClientsResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

@SpringBootApplication
public class AuctionsappbackendApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientsResource.class);

    public static void main(String[] args) {
        SpringApplication.run(AuctionsappbackendApplication.class, args);
    }
    @Bean
    BCryptPasswordEncoder encoder() {
        return 	new BCryptPasswordEncoder();
    }

	@Bean
    CommandLineRunner runIt(ClientsRepository clientsRepo, ProduitsRepository produitsRepository) {
		return args -> {
            try {
                Client client1 = new Client("user1", encoder().encode("password"), true, null, new BigDecimal("1000"), null);
                Role role1 = new Role("USER", new HashSet<>(Arrays.asList(client1)));
                client1.setRoles(new HashSet<>(Arrays.asList(role1)));

                Client client2 = new Client("user2", encoder().encode("password"), true, null,new BigDecimal("1000"),null);
                Role role2 = new Role("ADMIN", new HashSet<>(Arrays.asList(client2)));
                client2.setRoles(new HashSet<>(Arrays.asList(role2)));

                clientsRepo.saveAll(Arrays.asList(client1, client2));

                Produit produit1 = new Produit("Laptop DELL i3 RAM 8Go", new BigDecimal("500"),LocalDateTime.now(),Duration.ofDays(15),null);
                Produit produit2 = new Produit("Samsung S7 8Go", new BigDecimal("400"),LocalDateTime.now(),Duration.ofDays(15),null);
                Produit produit3 = new Produit("HP LaserJet Couleur", new BigDecimal("300"),LocalDateTime.now(),Duration.ofDays(15),null);

                produitsRepository.saveAll(Arrays.asList(produit1, produit2, produit3));

                System.out.println("Fin de l'initialisation par CommandLineRunner ...");
            } catch (Exception ex) {
              LOGGER.error("Exception rencontrée lors de l'initialisation par CommandLineRunner : "+ex);
            }
        };
	}


}
