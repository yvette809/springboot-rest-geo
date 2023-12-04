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

CREATE TABLE place (
                       id INT AUTO_INCREMENT ,
                       name VARCHAR(50) NOT NULL,
                       category_id INT NOT NULL,
                       user_id VARCHAR(50) NOT NULL,
                       visible BOOLEAN NOT NULL DEFAULT true,
                       date_created DATETIME,
                       date_modified DATETIME,
                       description VARCHAR(255) NOT NULL,
                       coordinate POINT NOT NULL,
                       PRIMARY KEY (id),
                       FOREIGN KEY (category_id) REFERENCES category(id)

) ENGINE=InnoDB;

INSERT INTO place (name, category_id, user_id, visible,date_created, date_modified, description, coordinate) VALUES
                                                                                                                 ('Place 1', 1, 'yvette', true,  NOW(), NOW(), 'Description for Place 1', POINT(10.123, 20.456)),
                                                                                                                 ('Place 2', 2, 'admin', false,NOW(), NOW(), 'Description for Place 2', POINT(30.789, 40.012)),
                                                                                                                 ('Place 3', 3, 'user', false, NOW(), NOW(), 'Description for Place 3', POINT(50.345, 60.678)),
                                                                                                                 ('Place 4', 4, 'yvette', true, NOW(), NOW(), 'Description for Place 4', POINT(50.345, 60.678));

SELECT id, name, ST_AsText(coordinate) AS coordinates_text FROM place;


# table for users



