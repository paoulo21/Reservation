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

INSERT INTO Utilisateur (nom, prenom) VALUES ('Doe', 'John');
INSERT INTO Utilisateur (nom, prenom) VALUES ('Smith', 'Alice');

INSERT INTO Reservation (id_utilisateur, date_heure) VALUES (1, '2024-11-20 14:00:00');
INSERT INTO Reservation (id_utilisateur, date_heure) VALUES (2, '2024-11-21 15:30:00');