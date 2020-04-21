CREATE TABLE IF NOT EXISTS SELECTION (
    id INT AUTO_INCREMENT PRIMARY KEY,
    selection_type VARCHAR(50),
    duration INT,
    distance_from_center INT,
    circle_radius FLOAT,
    space_between INT
) 
