-- Drop the existing schema if it exists
DROP DATABASE IF EXISTS escrim;

-- Create a new schema called ESCRIM
CREATE SCHEMA escrim;

-- Use the ESCRIM schema
USE escrim;

-- Create tables in the ESCRIM schema

CREATE TABLE Category
(
    ID_Category INT(10) NOT NULL AUTO_INCREMENT,
    Name        VARCHAR(255),
    Description VARCHAR(255),
    PRIMARY KEY (ID_Category)
);

CREATE TABLE Commande
(
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

CREATE TABLE Fournisseur
(
    ID_Fournisseur    INT(10) NOT NULL AUTO_INCREMENT,
    Nom               VARCHAR(255),
    Adresse           VARCHAR(255),
    Telephone         VARCHAR(20),
    Email             VARCHAR(255),
    Contact_Principal VARCHAR(255),
    PRIMARY KEY (ID_Fournisseur)
);

CREATE TABLE Patient
(
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

CREATE TABLE Personnel
(
    ID_Personnel INT(10) NOT NULL AUTO_INCREMENT,
    Nom          VARCHAR(255),
    Email        VARCHAR(255),
    Telephone    VARCHAR(20),
    Statut       VARCHAR(255),
    Specialite   VARCHAR(255), -- Used for specialization
    Type         VARCHAR(255), -- New column to identify personnel type
    PRIMARY KEY (ID_Personnel)
);

CREATE TABLE Produit
(
    Nom              VARCHAR(255),
    ID_Category      INT(10) NOT NULL,
    Poids            FLOAT,
    Quantite         INT(11),
    DateDePeremption DATE,
    Type_Prod        VARCHAR(255), -- New column to identify personnel type
    ID_Produit       INT(11) NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (ID_Produit)
);

CREATE TABLE User
(
    Username     VARCHAR(255) NOT NULL,
    Password     VARCHAR(255),
    Role         VARCHAR(255),
    ID_Personnel INT(10)      NOT NULL,
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

-- Populate Category table
INSERT INTO Category (Name, Description)
VALUES ('Category1', 'Description1'),
       ('Category2', 'Description2'),
       ('Category3', 'Description3');

-- Populate Fournisseur table
INSERT INTO Fournisseur (Nom, Adresse, Telephone, Email, Contact_Principal)
VALUES ('Supplier1', 'Address1', '1234567890', 'supplier1@example.com', 'Contact1'),
       ('Supplier2', 'Address2', '0987654321', 'supplier2@example.com', 'Contact2'),
       ('Supplier3', 'Address3', '1112223333', 'supplier3@example.com', 'Contact3');

-- Populate Personnel table
INSERT INTO Personnel (Nom, Email, Telephone, Statut, Specialite, Type)
VALUES ('Person1', 'person1@example.com', '1234567890', 'Statut1', 'Specialite1', 'Pharmacien'),
       ('Person2', 'person2@example.com', '0987654321', 'Statut2', 'Specialite2', 'Logisticien'),
       ('Person3', 'person3@example.com', '1112223333', 'Statut3', 'Specialite3', 'Medecin');

-- Populate Produit table
INSERT INTO Produit (Nom, ID_Category, Poids, Quantite, DateDePeremption, Type_Prod)
VALUES ('Product1', 1, 100.50, 50, '2024-12-31', 'Type1'),
       ('Product2', 2, 75.25, 30, '2024-11-30', 'Type2'),
       ('Product3', 3, 120.75, 20, '2024-10-31', 'Type3');

-- Populate User table
INSERT INTO User (Username, Password, Role, ID_Personnel)
VALUES ('admin', 'admin123', 'Admin', 1),
       ('logisticien', 'logi123', 'Logisticien', 2),
       ('medecin', 'med123', 'Medecin', 3),
       ('pharmacien', 'pharma123', 'Pharmacien', 1);
