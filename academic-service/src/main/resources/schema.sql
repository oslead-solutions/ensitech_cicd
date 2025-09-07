CREATE TABLE academic_year (
    id INT PRIMARY KEY AUTO_INCREMENT,
    label VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    ended_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
CREATE TABLE period (
    id INT PRIMARY KEY AUTO_INCREMENT,
    entitled VARCHAR(100) NOT NULL,
    started_at DATE,
    ended_at DATE,
    type_period VARCHAR(50),
    updated TIMESTAMP,
    created_at TIMESTAMP,
    academic_year_id INT,
    FOREIGN KEY (academic_year_id) REFERENCES academic_year(id)
);
