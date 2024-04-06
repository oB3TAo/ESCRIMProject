-- Drop the existing schema if it exists
DROP DATABASE IF EXISTS escrim;

-- Create a new schema called ESCRIM
CREATE SCHEMA escrim;

-- Use the ESCRIM schema
USE escrim;

-- Create tables in the ESCRIM schema

CREATE TABLE Fournisseur
(
    ID_Fournisseur    INT AUTO_INCREMENT PRIMARY KEY,
    Nom               VARCHAR(255),
    Adresse           VARCHAR(255),
    Telephone         VARCHAR(20),
    Email             VARCHAR(255),
    Contact_Principal VARCHAR(255)
);

CREATE TABLE users
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE personnel
(
    id    INT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(50) NOT NULL,
    role  VARCHAR(50) NOT NULL,
    items VARCHAR(50) NOT NULL
);

CREATE TABLE Avion
(
    ID_Avion                  INT AUTO_INCREMENT PRIMARY KEY,
    Modele                    VARCHAR(255),
    Capacite                  INT,
    Immatriculation           VARCHAR(100),
    Statut                    VARCHAR(50),
    Date_Derniere_Maintenance DATE
);

CREATE TABLE Categorie
(
    ID_Categorie  INT AUTO_INCREMENT PRIMARY KEY,
    Nom_Categorie VARCHAR(255),
    Description   VARCHAR(255)
);

CREATE TABLE Emplacement
(
    ID_Emplacement  INT AUTO_INCREMENT PRIMARY KEY,
    Nom_Emplacement VARCHAR(255),
    Capacite        INT
);

CREATE TABLE Colis
(
    ID_Colis       INT AUTO_INCREMENT PRIMARY KEY,
    Nom            VARCHAR(255),
    Poids          DECIMAL(10, 2),
    Volume         DECIMAL(10, 2),
    Date_Envoi     DATE,
    Statut         VARCHAR(60),
    ID_Emplacement INT NOT NULL,
    FOREIGN KEY (ID_Emplacement) REFERENCES Emplacement(ID_Emplacement)
);

CREATE TABLE Materiel
(
    ID_Materiel           INT AUTO_INCREMENT PRIMARY KEY,
    Nom                   VARCHAR(255),
    Categorie             INT NOT NULL,
    Quantite_Disponible   INT,
    Date_Achat            DATE,
    Derniere_Verification DATE,
    Date_Peremption       DATE,
    Fournisseur           INT NOT NULL,
    Cout                  NUMERIC(10, 2),
    ID_Colis              INT NOT NULL,
    FOREIGN KEY (ID_Colis) REFERENCES Colis(ID_Colis),
    FOREIGN KEY (Fournisseur) REFERENCES Fournisseur(ID_Fournisseur)
);

CREATE TABLE PersonnelLogistique
(
    ID_Personnel INT AUTO_INCREMENT PRIMARY KEY,
    Nom          VARCHAR(255),
    Fonction     VARCHAR(100),
    Specialite   VARCHAR(255),
    Contacts     VARCHAR(255),
    Identifiant  VARCHAR(255),
    Mot_de_passe VARCHAR(255)
);

CREATE TABLE Maintenance
(
    ID_Maintenance             INT AUTO_INCREMENT PRIMARY KEY,
    ID_Materiel                INT NOT NULL,
    Date_Maintenance           DATE,
    Type_Maintenance           VARCHAR(100),
    Description                VARCHAR(255),
    Statut                     VARCHAR(50),
    Date_Prochaine_Maintenance DATE,
    ID_Personnel               INT NOT NULL,
    FOREIGN KEY (ID_Materiel) REFERENCES Materiel(ID_Materiel),
    FOREIGN KEY (ID_Personnel) REFERENCES PersonnelLogistique(ID_Personnel)
);

CREATE TABLE Medicament
(
    ID_Medicament   INT AUTO_INCREMENT PRIMARY KEY,
    Nom_Commercial  VARCHAR(255),
    Nom_Generique   VARCHAR(255),
    Dose            VARCHAR(100),
    Forme           VARCHAR(100),
    Quantite_Stock  INT,
    Date_Peremption DATE,
    Prix_Unitaire   DECIMAL(10, 2)
);

CREATE TABLE Patient
(
    ID_Patient             INT AUTO_INCREMENT PRIMARY KEY,
    Nom                    VARCHAR(255),
    Date_de_Naissance      DATE,
    Sexe                   CHAR(1),
    Numero_Securite_Social VARCHAR(15),
    Adresse                VARCHAR(255),
    Numero_Telephone       VARCHAR(20),
    Email                  VARCHAR(255),
    Traitement_en_Cours    TEXT
);

CREATE TABLE PersonnelMedical
(
    ID_PersonnelMedical INT AUTO_INCREMENT PRIMARY KEY,
    Nom                 VARCHAR(255),
    Fonction            VARCHAR(100),
    Specialite          VARCHAR(255),
    Telephone           VARCHAR(20),
    Email               VARCHAR(255),
    ID_Patient          INT NOT NULL,
    FOREIGN KEY (ID_Patient) REFERENCES Patient(ID_Patient)
);

CREATE TABLE Utilisation
(
    ID_Utilisation    INT AUTO_INCREMENT PRIMARY KEY,
    ID_Materiel       INT NOT NULL,
    Date_Debut        DATE,
    Date_Fin          DATE,
    Destination       VARCHAR(255),
    Quantite_Utilisee INT,
    ID_Personnel      INT NOT NULL,
    FOREIGN KEY (ID_Materiel) REFERENCES Materiel(ID_Materiel),
    FOREIGN KEY (ID_Personnel) REFERENCES PersonnelMedical(ID_PersonnelMedical)
);

CREATE TABLE Commande
(
    ID_Commande           INT AUTO_INCREMENT PRIMARY KEY,
    Date_Commande         DATE,
    Date_Livraison_Prevue DATE,
    Statut_Commande       VARCHAR(50),
    Montant_Total         DECIMAL(10, 2),
    ID_Fournisseur        INT NOT NULL,
    ID_Personnel          INT NOT NULL,
    ID_Materiel           INT NOT NULL,
    ID_Medicament         INT NOT NULL,
    FOREIGN KEY (ID_Fournisseur) REFERENCES Fournisseur(ID_Fournisseur),
    FOREIGN KEY (ID_Personnel) REFERENCES PersonnelLogistique(ID_Personnel),
    FOREIGN KEY (ID_Materiel) REFERENCES Materiel(ID_Materiel),
    FOREIGN KEY (ID_Medicament) REFERENCES Medicament(ID_Medicament)
);
