-- Drop the existing schema if it exists
DROP DATABASE IF EXISTS escrim;

-- Create a new schema called ESCRIM
CREATE SCHEMA escrim;

-- Use the ESCRIM schema
USE escrim;

-- Create tables in the ESCRIM schema

CREATE TABLE Category (
                           ID_Category  INT(10) NOT NULL AUTO_INCREMENT,
                           Name VARCHAR(255),
                           Description   VARCHAR(255),
                           PRIMARY KEY (ID_Category)
);

CREATE TABLE Commande (
                          ID_Commande           INT(11) NOT NULL AUTO_INCREMENT,
                          Date_Commande         DATE,
                          Date_Livraison_Prevue DATE,
                          Statut_Commande       VARCHAR(50),
                          Montant_Total         NUMERIC(10, 2),
                          ID_Fournisseur        INT(10) NOT NULL,
                          ID_Personnel          INT(10) NOT NULL,
                          ID_Produit            INT(11) NOT NULL,
                          PRIMARY KEY (ID_Commande)
);

CREATE TABLE Fournisseur (
                             ID_Fournisseur    INT(10) NOT NULL AUTO_INCREMENT,
                             Nom               VARCHAR(255),
                             Adresse           VARCHAR(255),
                             Telephone         VARCHAR(20),
                             Email             VARCHAR(255),
                             Contact_Principal VARCHAR(255),
                             PRIMARY KEY (ID_Fournisseur)
);

CREATE TABLE Patient (
                         ID_Patient             INT(11) NOT NULL AUTO_INCREMENT,
                         Nom                    VARCHAR(255),
                         Date_de_Naissance      DATE,
                         Sexe                   CHAR(1),
                         Numero_Securite_Social VARCHAR(15),
                         Adresse                VARCHAR(255),
                         Numero_Telephone       VARCHAR(20),
                         Email                  VARCHAR(255),
                         Traitement_en_Cours    TEXT,
                         Diagnostic             VARCHAR(255),
                         Statut                 VARCHAR(255),
                         ID_Personnel           INT(10) NOT NULL,
                         PRIMARY KEY (ID_Patient)
);

CREATE TABLE Personnel (
                           ID_Personnel INT(10) NOT NULL AUTO_INCREMENT,
                           Nom          VARCHAR(255),
                           Email        VARCHAR(255),
                           Telephone    VARCHAR(20),
                           Statut       VARCHAR(255),
                           Specialite   VARCHAR(255),  -- Used for specialization
                           Type         VARCHAR(255),  -- New column to identify personnel type
                           PRIMARY KEY (ID_Personnel)
);

CREATE TABLE Produit (
                         Nom              VARCHAR(255),
                         ID_Category     INT(10) NOT NULL,
                         Poids            FLOAT,
                         Quantite         INT(11),
                         DateDePeremption DATE,
                         Type_Prod        VARCHAR(255),  -- New column to identify personnel type
                         ID_Produit       INT(11) NOT NULL AUTO_INCREMENT,
                         PRIMARY KEY (ID_Produit)
);

CREATE TABLE User (
                      Username     VARCHAR(255) NOT NULL,
                      Password     VARCHAR(255),
                      Role         VARCHAR(255),
                      ID_Personnel INT(10) NOT NULL,
                      PRIMARY KEY (Username)
);

ALTER TABLE Commande
    ADD CONSTRAINT FK_Commande_Fournisseur FOREIGN KEY (ID_Fournisseur) REFERENCES Fournisseur (ID_Fournisseur);

ALTER TABLE User
    ADD CONSTRAINT FK_User_Personnel FOREIGN KEY (ID_Personnel) REFERENCES Personnel (ID_Personnel);

ALTER TABLE Commande
    ADD CONSTRAINT FK_Commande_Personnel FOREIGN KEY (ID_Personnel) REFERENCES Personnel (ID_Personnel);

ALTER TABLE Commande
    ADD CONSTRAINT FK_Commande_Produit FOREIGN KEY (ID_Produit) REFERENCES Produit (ID_Produit);

ALTER TABLE Produit
    ADD CONSTRAINT FK_Produit_Categorie FOREIGN KEY (ID_Category) REFERENCES category (ID_Category);

ALTER TABLE Patient
    ADD CONSTRAINT FK_Patient_Personnel FOREIGN KEY (ID_Personnel) REFERENCES Personnel (ID_Personnel);

