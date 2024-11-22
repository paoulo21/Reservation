drop table if EXISTS Reservation;
drop table if EXISTS Utilisateur;
DROP TABLE IF EXISTS Constraints;
DROP TABLE IF EXISTS Crenaux;

CREATE TABLE Constraints (
    id SERIAL PRIMARY KEY,
    enabled_days INTEGER[],
    max_per_slot INTEGER NOT NULL,
    minutes_between_slots INTEGER NOT NULL,
    start TIME,
    "end" TIME,
    name VARCHAR(255) NOT NULL
);

INSERT INTO Constraints (enabled_days, max_per_slot, minutes_between_slots, start, "end", name)
VALUES (
    ARRAY[1, 2, 3, 4, 5, 6, 7],
    30,                        
    60,                         
    '11:00:00',                
    '18:00:00',              
    'Piscine'                 
);

INSERT INTO Constraints (enabled_days, max_per_slot, minutes_between_slots, start, "end", name)
VALUES (
    ARRAY[1, 2, 3, 4, 5], 
    1,                        
    15,                     
    '13:00:00',             
    '20:00:00',             
    'Medecin'              
);

CREATE TABLE Crenaux (
    dateTime TIMESTAMP PRIMARY KEY
);

CREATE TABLE Utilisateur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL
);

CREATE TABLE Reservation (
    id SERIAL PRIMARY KEY,
    id_utilisateur INTEGER REFERENCES Utilisateur(id),
    date_heure TIMESTAMP,
    CONSTRAINT fk_utilisateur FOREIGN KEY (id_utilisateur) REFERENCES Utilisateur(id)
);

INSERT INTO Utilisateur (nom, prenom) VALUES ('Doe', 'John');
INSERT INTO Utilisateur (nom, prenom) VALUES ('Smith', 'Alice');

INSERT INTO Reservation (id_utilisateur, date_heure) VALUES (1, '2024-11-20 14:00:00');
INSERT INTO Reservation (id_utilisateur, date_heure) VALUES (2, '2024-11-21 15:30:00');