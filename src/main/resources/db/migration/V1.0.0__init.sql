CREATE TABLE city
(
    id BIGSERIAL NOT NULL,
    name TEXT NOT NULL,
    zip_code TEXT NOT NULL,
    department_code TEXT NOT NULL,
    lat REAL NOT NULL,
    lon REAL NOT NULL
);

INSERT INTO city (id, name, zip_code, department_code, lat, lon) VALUES (1, 'Aubagne', '13400', '13', 12, 8);
INSERT INTO city (id, name, zip_code, department_code, lat, lon) VALUES (2, 'Montpellier', '34000', '34', 19, 20);
