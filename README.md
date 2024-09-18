# ESCRIM: Logiciel de Gestion pour Hôpital de Campagne

## Contexte
ESCRIM est un hôpital de campagne conçu pour intervenir rapidement lors de crises humanitaires ou médicales. Ce projet, réalisé dans le cadre du cursus à l'École des Mines d'Alès, vise à optimiser la gestion des ressources médicales et logistiques. L'application développée est une solution CRUD (Create, Read, Update, Delete) permettant de gérer le personnel, les utilisateurs, les patients, les commandes, et les produits de manière efficace et sécurisée.

## Objectifs du Logiciel
- **Gestion des Stocks** : Suivi en temps réel des médicaments, y compris les dates de péremption, et du matériel médical.
- **Logistique** : Optimisation des ressources en fonction des capacités logistiques (ex : avion, palettes).
- **Gestion du Personnel et des Patients** : Administration des profils utilisateurs (médecins, infirmiers, techniciens) et suivi des patients avec une interface ergonomique et adaptée aux urgences.
- **Ergonomie Intuitive** : Interface simple, rapide à utiliser, spécialement conçue pour des situations critiques.

## Fonctionnalités Clés
1. **Écran de Connexion** : Authentification sécurisée des utilisateurs.
2. **Gestion du Personnel** : Suivi des rôles, distinctions de postes, et gestion des accès.
3. **Gestion des Patients** : Enregistrement des informations, suivi des traitements et historique médical.
4. **Gestion des Ressources** : Visualisation des stocks de médicaments, gestion des commandes et suivi logistique.

## Exigences Techniques
- **Technologies Utilisées** :
  - **Java** : Langage principal pour la logique métier.
  - **JavaFX** : Utilisé pour créer l'interface utilisateur avec **Scene Builder**.
  - **MySQL** : Gestion de la base de données pour stocker les informations des utilisateurs, patients, et ressources.
  - **FXML** : Description des interfaces graphiques pour JavaFX.

## Modélisation et Cas d'Utilisation
Le projet suit une approche itérative avec des modèles de domaine clairs :
- **Diagramme des Entités-Relations** : Modélisation des relations entre les médicaments, équipements, et utilisateurs.
- **Cas d'Utilisation** : Gérer les stocks, ajouter ou mettre à jour les patients, planifier les ressources logistiques.

## Plan d'Améliorations Futures
- **Réactivité en Temps Réel** : Amélioration des temps de réponse pour les données critiques.
- **Distinction des Postes** : Raffinement des rôles et des privilèges utilisateurs.
- **Amélioration de l'Ergonomie** : Simplification et optimisation de l'interface pour une utilisation en situation de stress.
- **Extension des Concepts** : Ajout de nouvelles fonctionnalités pour enrichir la gestion des ressources et des patients.

## Utilisation du Projet
### Installation :
1. **Prérequis** : Java 11 ou une version supérieure.
2. **Cloner le dépôt** : `git clone https://github.com/username/escrim-hospital.git`
3. **Compilation et Lancement** : Utilisez un IDE compatible avec JavaFX, tel que **IntelliJ** ou **Eclipse**, pour compiler et exécuter le projet.
