INSERT INTO constraints_rules (enabled_days, max_per_slot, minutes_between_slots, start_Time, end_Time, name)
VALUES (
    '1,2,3,4,5,6,7', -- La liste est stockée sous forme de texte
    30,
    60,
    '11:00',
    '18:00',
    'Piscine'
);


INSERT INTO constraints_rules (enabled_days, max_per_slot, minutes_between_slots, start_Time, end_Time, name)
VALUES (
    '1,2,3,4,5', 
    1,                        
    15,                     
    '13:00',             
    '20:00',             
    'Medecin'              
);

INSERT INTO utilisateur (nom, prenom, role,mdp,email) VALUES ('test', 'test', 'Admin','098f6bcd4621d373cade4e832627b4f6','test@test.com');
INSERT INTO utilisateur (nom, prenom, role,mdp,email) VALUES ('Smith', 'Alice', 'User','tyui','user@user.com');


INSERT INTO Reservation (id_utilisateur, date_heure) VALUES (1, '2025-11-20 14:00:00');
INSERT INTO Reservation (id_utilisateur, date_heure) VALUES (2, '2024-11-21 15:30:00');