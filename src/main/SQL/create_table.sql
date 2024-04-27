-- Drop the existing schema if it exists
DROP DATABASE IF EXISTS escrim;

-- Create a new schema called ESCRIM
CREATE SCHEMA escrim;

-- Use the ESCRIM schema
USE escrim;

-- Create a table for storing different categories
CREATE TABLE Category
(
    ID_Category INT(10) NOT NULL AUTO_INCREMENT,
    Name        VARCHAR(255),
    Description VARCHAR(255),
    PRIMARY KEY (ID_Category)
);

-- Create a table for storing commands or orders
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

-- Create a table for storing supplier information
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

-- Create a table for storing patient information
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

-- Create a table for hospital and logistical personnel
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

-- Create a table for storing product information
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

-- Create a table for user management
CREATE TABLE User
(
    Username     VARCHAR(255) NOT NULL,
    Password     VARCHAR(255),
    Role         VARCHAR(255),
    ID_Personnel INT(10)      NOT NULL,
    PRIMARY KEY (Username)
);

-- Define foreign key constraints with explicit names
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
       ('Category3', 'Description3'),
       ('Category4', 'Description4'),
       ('Category5', 'Description5');

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
VALUES ('Paracetamol', 1, 0.5, 100, '2025-12-31', 'Medicament'),
       ('Stethoscope', 4, 0.9, 20, NULL, 'Materiel'),
       ('Amoxicillin', 1, 0.75, 50, '2025-11-30', 'Medicament'),
       ('Surgical Kit', 5, 0.2, 10, NULL, 'Materiel'),
       ('Omeprazole', 1, 1.0, 30, '2025-10-31', 'Medicament');

-- Populate Patient table
INSERT INTO Patient (Nom, Date_de_Naissance, Sexe, Numero_Securite_Social, Adresse, Numero_Telephone, Email,
                     Traitement_en_Cours, Diagnostic, Statut, ID_Personnel)
VALUES ('John Doe', '1980-05-15', 'M', '1234567890', '123 Main Street, City, Country', '1234567890',
        'john.doe@example.com', 'Pain Management', 'Chronic Pain', 'Active', 3),
       ('Jane Smith', '1990-10-25', 'F', '9876543210', '456 Elm Street, City, Country', '9876543210',
        'jane.smith@example.com', 'Antibiotic Treatment', 'Bacterial Infection', 'Active', 3),
       ('Michael Johnson', '1975-03-10', 'M', '4561237890', '789 Oak Street, City, Country', '4561237890',
        'michael.johnson@example.com', 'GERD Management', 'Gastroesophageal Reflux Disease', 'Active', 3);

-- Populate Commande table
INSERT INTO Commande (Date_Commande, Date_Livraison_Prevue, Statut_Commande, Montant_Total, ID_Fournisseur,
                      ID_Personnel, ID_Produit)
VALUES ('2024-04-23', '2024-04-30', 'Pending', 500.00, 1, 2, 1),
       ('2024-04-24', '2024-04-30', 'Pending', 150.00, 2, 2, 2),
       ('2024-04-25', '2024-05-02', 'Pending', 200.00, 3, 2, 3);


-- Populate User table
INSERT INTO User (Username, Password, Role, ID_Personnel)
VALUES ('admin', 'admin123', 'Admin', 1),
       ('logisticien', 'logi123', 'Logisticien', 2),
       ('medecin', 'med123', 'Medecin', 3),
       ('pharmacien', 'pharma123', 'Pharmacien', 1);
