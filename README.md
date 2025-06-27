
#  TicketJO - Backend API (Spring Boot)

Ce projet constitue l'API sécurisée pour l'application de billetterie des Jeux Olympiques 2024.  
Il gère l'ensemble des opérations : création de billets, authentification, paiements via Stripe, et sécurité OAuth 2.0.

##  Fonctionnalités

 API REST sécurisée (Spring Security)  
 Authentification par OAuth 2.0 (Google, GitHub...)  
 Gestion des billets avec stock et capacité  
 Webhooks Stripe pour suivi des paiements  
 Base de données relationnelle (ex: PostgreSQL, MySQL)  
 Développé en Java Spring Boot  

---

##  Prérequis techniques

- Java 17 minimum  
- Maven  
- Un compte Stripe avec accès à la clé secrète  
- Clients OAuth configurés (Google, GitHub...)  
- Base de données compatible (PostgreSQL recommandé)  

---

##  Structure du projet

```
ticketjo-backend/
├── src/                   # Code source Java
├── pom.xml                # Dépendances Maven
├── .env                   # Variables d'environnement (ne pas livrer tel quel)
├── README.md              # Ce guide
```

---

##  Configuration (.env)

**Exemple minimal :**

```env
SERVER_PORT=8080
DATABASE_URL=jdbc:postgresql://localhost:5432/ticketjo
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=motdepasse

# Stripe
STRIPE_SECRET_KEY=sk_test_xxxxxxxxxxxxxxxxxxxxxx
STRIPE_WEBHOOK_SECRET=whsec_xxxxxxxxxxxxxxxxxxx

# OAuth 2.0
OAUTH_CLIENT_ID=xxxxxxxxxxxxxxxxxxxxxxxxxxxx
OAUTH_CLIENT_SECRET=xxxxxxxxxxxxxxxxxxxxxxxx
```

**Explications :**  
- `STRIPE_SECRET_KEY` : Clé secrète Stripe côté serveur (retrouvable sur le dashboard Stripe)  
- `STRIPE_WEBHOOK_SECRET` : Clé secrète pour vérifier les webhooks Stripe  
- `OAUTH_CLIENT_ID / SECRET` : Informations fournies par Google, GitHub, etc. pour connecter les utilisateurs via OAuth  

---

##  Lancer le backend en développement

### 1. Import du projet

Ouvrez le projet dans votre IDE favori (Eclipse, IntelliJ, VSCode)  
Assurez-vous que Maven télécharge les dépendances.

### 2. Configuration de la BDD et des clés

Créez un fichier `.env` à la racine en suivant le modèle ci-dessus.

### 3. Exécuter

```bash
mvn spring-boot:run
```

Le backend démarre par défaut sur : [http://localhost:8080](http://localhost:8080)

---

##  Sécurité et Authentification

- Authentification OAuth 2.0 côté utilisateur  
- Gestion des tokens JWT pour sécuriser les échanges  
- Les routes `/api/tickets` et `/api/paiements` nécessitent un token valide  

---

##  Paiement Stripe

L'API gère :  
- Création d'une session de paiement côté serveur  
- Réception des webhooks pour valider les paiements  
- Protection des données sensibles : seules les clés publiques Stripe sont exposées au frontend  

---

##  Build et Production

Pour générer le `.jar` :

```bash
mvn clean package
```

Lancez ensuite :

```bash
java -jar target/ticketjo-backend.jar
```

---

##  Notes

- Le `.env` doit être protégé et jamais versionné  
- Adaptez la configuration pour correspondre à votre environnement de production  
- Le frontend doit connaître uniquement `VITE_STRIPE_PUBLIC_KEY` et l'URL du backend  

---

##  Copyright

© 2024 TicketJO. Tous droits réservés.
