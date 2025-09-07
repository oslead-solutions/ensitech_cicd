# Projet Ensitech - Architecture Microservices

Bienvenue dans le dépôt central du projet Ensitech. Ce projet a pour but de construire le système d'information de l'école en utilisant une architecture microservices moderne avec Spring Boot et Spring Cloud.

## Architecture Cible

Le système est décomposé en plusieurs microservices, chacun ayant sa propre responsabilité et sa propre base de données.

### Services d'Infrastructure

*   **`config-service` (Port 9999)**: Serveur de configuration centralisé qui lit les configurations depuis le dossier `config-repo`.
*   **`discovery-service` (Port 8761)**: Service de découverte (Eureka) qui permet aux autres services de se trouver.
*   **`gateway-service` (Port 8888)**: Point d'entrée unique (API Gateway) pour toutes les requêtes externes. **Toutes les requêtes du front-end doivent passer par ici.**

### Services Métier

*   **`user-service` (Port 8081)**: Gère les utilisateurs (étudiants, enseignants, etc.).
*   **`course-service` (Port 8082)**: Gère les cours, les matières et les associations avec les utilisateurs.
*   **`authentication-service` (Port 8083)**: Gère le login et les comptes utilisateurs.
*   **`training-service` (Port 8084)**: Gère les cours, les evalutions, les specialités et les associations entre evaluation et etudiants.

### Services à Ajouter

*   `academic-year-service`
*   `inscription-service`


## Prérequis

*   JDK 21 ou supérieur
*   Maven 3.8+
*   Votre IDE favori (IntelliJ IDEA, VS Code...)
*   Un client API comme Postman.

## Ordre de Lancement

L'ordre de démarrage des services est **crucial** :
1.  `config-service`
2.  `discovery-service`
3.  `user-service`
4.  `course-service`
5.   `authentication-service`
6. `training-service`
5.  `gateway-service`
6.  (Et ensuite les autres services que vous ajouterez)

Attendez que chaque service ait bien démarré avant de lancer le suivant.

---

## Tester les Endpoints via la Gateway

Toutes les requêtes de test doivent être faites sur le port de la passerelle (`8888`).

### Gestion des Utilisateurs (routé vers `user-service`)

*   **Lister tous les étudiants :**
    *   `GET http://localhost:8888/api/students`
*   **Lister tous les enseignants :**
    *   `GET http://localhost:8888/api/teachers`
*   **Créer un étudiant :**
    *   `POST http://localhost:8888/api/students`
    *   Body (JSON) : `{"firstName": "Nouveau", "lastName": "Etudiant", "email": "nouveau@email.com", "gender": "MALE"}`

### Gestion des Cours (routé vers `course-service`)

*   **Lister tous les cours (avec détails enrichis) :**
    *   `GET http://localhost:8888/api/courses`
*   **Créer un cours sans enseignant :**
    *   `POST http://localhost:8888/api/courses`
    *   Body (JSON) : `{"title": "Algorithmique", "coefficient": 3, "hours": 30}`
*   **Associer un enseignant à un cours :**
    *   `PUT http://localhost:8888/api/courses/{courseId}/teacher/{teacherId}`
    *   Exemple : `PUT http://localhost:8888/api/courses/2/teacher/1`
*   **Inscrire un étudiant à un cours :**
    *   `PUT http://localhost:8888/api/courses/{courseId}/students/{studentId}`
    *   Exemple : `PUT http://localhost:8880/api/courses/1/students/3`

---

## Guide pour Ajouter un Nouveau Microservice

Pour garantir la cohérence et la compatibilité de tous les services, nous utilisons un projet parent Maven. Chaque nouveau microservice doit hériter de ce parent.

Suivez ces étapes pour créer un nouveau service (par exemple, course-service) :

1. Créer la Structure du Projet
   Vous pouvez utiliser Spring Initializr pour générer la base du projet, mais vous devrez modifier manuellement le pom.xml après la création.

Créez un nouveau dossier course-service à la racine de ce projet.
Générez un projet Spring Boot à l'intérieur de ce dossier.
2. Configurer le pom.xml du nouveau service
   C'est l'étape la plus importante. Votre nouveau pom.xml doit hériter du pom.xml parent qui se trouve à la racine du projet (ensitech-final/pom.xml).

Voici un modèle pour le pom.xml de course-service :

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
<modelVersion>4.0.0</modelVersion>

    <!-- ÉTAPE 1: HÉRITAGE DU PARENT -->
    <!-- Cela garantit que vous utilisez les mêmes versions de Spring Boot, Spring Cloud, etc. -->
    <parent>
        <groupId>com.project.ensitech</groupId>
        <artifactId>ensitech-parent</artifactId>
        <version>1.0-SNAPSHOT</version>
        <!-- Le chemin relatif vers le pom.xml parent -->
        <relativePath>../pom.xml</relativePath>
    </parent>

    <!-- ÉTAPE 2: IDENTITÉ DU MICROSERVICE -->
    <artifactId>course-service</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>course-service</name>
    <description>Microservice pour la gestion des cours</description>

    <!-- ÉTAPE 3: DÉPENDANCES SPÉCIFIQUES -->
    <!-- Le parent gère déjà les versions. Vous n'avez qu'à ajouter les dépendances dont vous avez besoin. -->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <!-- Ajoutez ici d'autres dépendances comme la BDD, Lombok... -->
    </dependencies>

    <!-- ... le reste du build plugin reste le même ... -->
</project>
3. Mettre à jour le pom.xml parent
Pour que Maven reconnaisse votre nouveau service comme un module du projet global, ajoutez-le à la section <modules> du pom.xml qui se trouve à la racine (ensitech-final/pom.xml).

<!-- Dans ensitech-final/pom.xml -->
<modules>
    <module>user-service</module>
    <!-- Ajoutez votre nouveau service ici -->
    <module>course-service</module>
</modules>
4. Lancer et Tester
Vous pouvez maintenant lancer votre nouveau microservice. Il partagera automatiquement la configuration de base (versions, plugins...) avec les autres services, assurant ainsi la stabilité de l'ensemble de l'écosystème.

Comment ça marche ? (La magie du POM Parent)
Le pom.xml à la racine du projet agit comme un "chef d'orchestre".

<dependencyManagement>: Il définit une liste de toutes les dépendances possibles avec leurs versions exactes. Quand un microservice "enfant" (comme user-service) déclare une dépendance sans spécifier de version, il hérite automatiquement de la version définie par le parent. C'est ce qui évite les conflits de versions.
<pluginManagement>: De même, il définit la configuration des plugins Maven.
<modules>: Il liste tous les sous-projets (nos microservices), ce qui vous permet de compiler, tester ou packager tous les services en une seule commande depuis la racine (mvn clean install).