deop table if exists Constraints;
CREATE TABLE Constraints (
    id SERIAL PRIMARY KEY,
    enabled_days INTEGER[],
    max_per_slot INTEGER NOT NULL,
    minutes_between_slots BIGINT NOT NULL,
    start TIME NOT NULL,
    end TIME NOT NULL,
    name VARCHAR(255) NOT NULL
);
INSERT INTO Constraints (enabled_days, max_per_slot, minutes_between_slots, start, end, name)
VALUES (
    ARRAY[1, 2, 3, 4, 5, 6, 7],
    30,                        
    60,                         
    '11:00:00',                
    '18:00:00',              
    'Piscine'                 
);
INSERT INTO Constraints (enabled_days, max_per_slot, minutes_between_slots, start, end, name)
VALUES (
    ARRAY[1, 2, 3, 4, 5], 
    1,                        
    15,                     
    '13:00:00',             
    '20:00:00',             
    'Medecin'              
);