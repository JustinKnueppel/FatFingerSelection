CREATE TABLE IF NOT EXISTS TRIAL (
    id INT AUTO_INCREMENT PRIMARY KEY,
    selection_type VARCHAR(50),
    duration BIGINT,
    distance_from_center DOUBLE,
    circle_radius FLOAT,
    space_between FLOAT
) 
