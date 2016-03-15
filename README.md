# iAdvize_Back_End_Test
API REST / Scraping pour iAdvize // Scala - Redis

Bonjour :) 

Comme convenu, vous pourrez trouver dans ce projet le scraper pour VDM et l'API REST pour accéder aux données. 

Tout mon projet est réalisé en Scala et Finatra (Framework HTTP basé sur TwitterServer et Finagle) et j'utilise 
Redis pour stocker les données. 

Les fonctiollaités suivantes ont été implémentées : 
  - Scraper pour VDM ✔
  - Stockage des donées ✔ 
  - Récupération de toutes les données (/posts) ✔
  - Récupération avecc filtres (author, from, to) --> Possibilité de les combiner ✔ 
  - Récupération via l'ID ✔
  
Les tests qui vont avec sont dans le dossier "test" (obviously ? :)) 

Pour lancer l'app :
  - Démarrez une instance Redis
  - Clonez le repo
  - Lancez "sbt run" à la racine (nécessite une installation de Sbt et un SDK Scala) 
  
Je pense n'avoir rien oublié, j'ai rajouté une Regex pour vérifier les dates saisies et il est possible de re-scraper VDM 
en saisisannt "/scraping". 

Dans l'éventualité où il y aurait un problème, je suis disponilbe par mail ! 

Pierre. 
