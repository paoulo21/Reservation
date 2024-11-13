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
    ARRAY[1, 2, 3, 4, 5, 6, 7],  -- enabledDays
    30,                         -- maxPerSlot
    60,                         -- minutesBetweenSlots
    '11:00:00',                -- start
    '18:00:00',                -- end
    'Piscine'                  -- name
);
INSERT INTO Constraints (enabled_days, max_per_slot, minutes_between_slots, start, end, name)
VALUES (
    ARRAY[1, 2, 3, 4, 5],  -- enabledDays
    1,                         -- maxPerSlot
    15,                         -- minutesBetweenSlots
    '13:00:00',                -- start
    '20:00:00',                -- end
    'Medecin'                  -- name
);