## Présentation du cahier charges de AuctionsApp

### Besoins fonctionnels
L'objectif ici est de développer **_AuctionsApp_**, une maquette d'application de vente aux enchères (_Auctions_ en anglais). L'application devant se conformer au style architectural _Full Stack_, mais nous intéressons ici au  **_Back End_**  du stack.
 
La page d'accueil principale de l'application expose un ensemble de produits pour les enchères. Sur chaque produit, on précise la durée de l'enchère ainsi que le prix minimum demandé par le dépositaire et également le montant de la surchère la plus élevée, à ce jour.
Les clients intéressés par l'un ou l'autre des produits doivent formuler une offre qui dépasse la valeur de l'enchère actuelle pour ce produit. Cette offre ne pourra en aucun cas dépasser la limite du solde possédé par le client. Au terme de la période de l'enchère, le produit sera accordée au client plus offrant.  


Le back office cible doit offrir une API web permettant aux clients de s'inscrire, de se renseigner sur les produits, et de déposer des offres. On offrira également un ensemble de points d'accès pour des fonctions statistiques comme par exemple:
- Le nombre d'offres pour un produit
- Le client ayant déposé la meilleure offre pour un produit
- Valeur de la meilleure offre pour un produit 

### Modèle du domaine
Le diagramme de classes ci-dessous montre les objets du domaine et leurs associations.

 ![UML, Modele du domaine](etc/Encheres_domain.jpg)

- La classe **_Client_** représente un client de l'application. Il possède un _username_ et un _password_ et peut avoir  des rôles applicatifs qui correspondent à des autorisations en termes de contrôle d'accès.  
Chaque utilisateur endosse, par défaut, le rôle _USER_. 
- La classe **_Role_** représente rôle au sens de la `Role-Based Access Control` (RBAC). 
- La classe **_Produit_** représente un produit mis à la surenchère. Il est caractérisé notamment par son nom, son prix initial, et la durée de sa mise aux enchères.
- La classe **_Offre_** est une classe centrale pour la logique d'enchères. Elle est une charnière entre les client en compétition et les produits exposés.

### Architeture du Back-End
Le back-end est organisé selon le modèle l'architecture en couches logiques. Le stack comporte en effet les trois couches classiques qui sont faiblement couplées (_repository_, _service_, et _rest_).  La _domain_ contient les objets du domaines utiles aux trois layers citées précédemment. _security_ contient la configuration de Spring Security (_authentification_ et _autorization_)' et adopte le modèle de l'authentification Basic et plus précisément les **_jetons Basic_**.  
  
   ![Architecture de l'application](etc/Couches_logiques.jpg )

### L'API Web (Contrôleurs REST)
L'API Web se compose des contrôleurs suivants:
- **_ProduitsResource_** (accessible sur : _/produits_)expose les opérations de gestion de produits.
- **_ClientsResource_** permet d'inscrire les nouveaux utilisateurs (sur _/register_) et de vérifier l'état de login de ces derniers (_/checklogin_). _checklogin_ s'occupe de vérifier que le token Basic mis dans le header d'autorisation permet bien d'authentifier l'utilisateur et il retournera le non de l'utilisateur en cas de succès. Le cas échéant on demandera au client de soumettre un token basic valide.
- **_OffresResource_**  (accessible sur : _/offres_)expose les opérations de dépôt et d'information sur les offres de surenchère.
- **_StatisticsResource_**  (accessible sur : _/statistics_) expose les opérations de statistiques.
   ![Les controleurs REST](etc/ControleursREST.jpg )
   
### Test du Back End
#### Initialisations
Afin de démarrer les tests, préparons un jeu de deux clients (_user1/password_ et _user2/password_) et un jeu de trois produits. Une telle initialisation pourra être faite via le CommandLineRunner ci-dessus.  

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
#### Tests de l'API `ClientResource`
- **Enregistrement d'un nouveau client**

  - Point d'accès : **_/register_**, Verbe: **_POST_**
  - Body de la requête : Contenu JSON de la forme `{"username":"un_nom","password":"un_password" }`

  Exemple de test avec PostMan
  ![Test Register](etc/test_register.jpg )
   
- **Vérification du token de login d'un utilisateur**  
  - Point d'accès : **_/checklogin_**, Verbe: **_GET_**
  - Positionner le header Authorization à Basic dXNlcjE6cGFzc3dvcmQ= (à titre d'exemple pour le client user1:password) 
   (Le base64 correspond à l'encodage en **Base64** de la chaine **username:password** . Astuce: Pour encoder et décoder en Base64, utiliser l'un des outils online telque _https://www.base64encode.org/_)

  Exemple de test avec PostMan pour vérifier le token de user1/password
  ![Test CheckLogin](etc/test_checklogin.jpg )  
   

#### Tests de l'API `OffresResource`
- **Soumission d'une offre pour un produit**
  - Point d'accès : **_/offres/{idProduit}/{montantOffre}_**, Verbe: **_POST_** (Le verbe aurait pu être un GET ici puisque les données de l'offre dans données dans la Query string)
  - Positionner le header Authorization de manière à ce qu'il contienne le token Basic du client. Ce token est utile pour authentifier et reconnaître le client soumissionnaire de l'offre.
    
  Exemple de test avec PostMan pour que user1 soumette une offre de 600 euros pour le produit 6.
  ![Test SoumissionOffre](etc/test_soumission_offre.jpg )
    
    
- **Consultation de mes offres pour un produit**
  - Point d'accès : **_/stats/offres/{idProduit}_**, Verbe: **_GET_** 
  - Positionner le header Authorization de manière à ce qu'il contienne le token Basic du client. Ce token est utile pour authentifier le client.
       
  Exemple de test avec PostMan pour avoir la liste des offres de l'utilisateur connecté pour le produit 6.
      ![Test Mes Offres](etc/test_mesOffres.jpg ) 
   

#### Tests de l'API `StatisticsResource`
- **La meilleure offre pour un produit**
  - Point d'accès : **_/offres/{idProduit}/best_**, Verbe: **_GET_** 
  - Positionner le header Authorization de manière à ce qu'il contienne le token Basic du client. Ce token est utile pour authentifier le client.
    
  Exemple de test avec PostMan pour afficher la meilleure offre pour le produit 6 (tous utilisateurs confondus).
  ![Test Stat meilleure offre](etc/test_stat_meilleureOffre.jpg )
    
   
- **Liste de toutes les offres pour un produit**
  - Point d'accès : **_/offres/{idProduit}/{montantOffre}_**, Verbe: **_GET_** 
  - Positionner le header Authorization de manière à ce qu'il contienne le token Basic du client. Ce token est utile pour authentifier et reconnaître le client soumissionnaire de l'offre.
    
  Exemple de test avec PostMan pour listes toutes les offres pour le produit 5 (tous utilisateurs confondus).
  ![Test Stat toutes les offres](etc/test_stat_liste_offres.jpg )
    
   