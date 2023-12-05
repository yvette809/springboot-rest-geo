CREATE DATABASE IF NOT EXISTS map;
USE map;

DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS place;


# create table category
CREATE TABLE category (
                          id INT AUTO_INCREMENT ,
                          name VARCHAR(50) NOT NULL,
                          symbol VARCHAR(50) NOT NULL,
                          description VARCHAR(255) NOT NULL,
                          PRIMARY KEY (id)
);



INSERT INTO category ( name, symbol, description) VALUES
                                                      ('park', '🏞️', 'Very relaxed park for families'),
                                                      ( 'beach', '🏖️', 'Enjoy the summer here'),
                                                      ('resort', '⛵', 'Good place for meditation'),
                                                      ('mountain', '🏔️', 'A little adventure is intriguing');

# create places table

INSERT INTO place (name, category_id, user_id, visible, date_created, date_modified, description, coordinate)
VALUES
    ('Place 1', 1, 'yvette', true, NOW(), NOW(), 'Description for Place 1', ST_GeomFromText('POINT(20.456 10.123)', 4326)),
    ('Place 2', 2, 'admin', false, NOW(), NOW(), 'Description for Place 2', ST_GeomFromText('POINT(25.789 15.678)', 4326)),
    ('Place 3', 1, 'user', true, NOW(), NOW(), 'Description for Place 3', ST_GeomFromText('POINT(30.567 12.345)', 4326)),
    ('Place 4', 3, 'yvette', true, NOW(), NOW(), 'Description for Place 4', ST_GeomFromText('POINT(18.234 22.789)', 4326)),
    ('Place 5', 2, 'yvette', false, NOW(), NOW(), 'Description for Place 5', ST_GeomFromText('POINT(16.789 25.123)', 4326));






# table for users



