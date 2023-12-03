CREATE DATABASE IF NOT EXISTS map;
USE map;

DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS place;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS authorities;

#
CREATE TABLE users (
                       user_id  VARCHAR(50) NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       enabled TINYINT NOT NULL DEFAULT 1,
                       PRIMARY KEY (user_id)
);



CREATE TABLE authorities (

                             user_id VARCHAR(50) NOT NULL ,
                             role VARCHAR(50) NOT NULL,
                             UNIQUE KEY authorities_idx_1(user_id,role),
                             CONSTRAINT authorities_ibfk_1
                                 FOREIGN KEY (user_id) REFERENCES users(user_id)
);





INSERT INTO users(user_id, password)
VALUES
    ('Yvette', '{bcrypt}$2a$12$LHN/4NoBabGTzqYICNOOauikwFRxkmjiRK0L1nW6ReXYtZe.oVQ7q'),
    ('Favour', '{bcrypt}$2a$12$6XSbkRkkKUsjFrV6qAHxCeZspwLxlv0oAOHtO6/0vzdrkBEvjWgDu'),
    ('Nathan', '{bcrypt}$2a$12$lP.FyqW4CjlcDPlzULW/7ePU.OrkEi56CcCyH440DVZ6OJGxuEFZ2');


INSERT INTO authorities(user_id, role)
VALUES
    ('Yvette', 'ROLE_ADMIN'),
    ('Favour', 'ROLE_USER'),
    ('Nathan', 'ROLE_USER');



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
                       FOREIGN KEY (category_id) REFERENCES category(id),
                       FOREIGN KEY (user_id) REFERENCES users(user_id)
) ENGINE=InnoDB;

INSERT INTO place (name, category_id, user_id, visible,date_created, date_modified, description, coordinate) VALUES
                                                                                                                 ('Place 1', 1, 'Yvette', true,  NOW(), NOW(), 'Description for Place 1', POINT(10.123, 20.456)),
                                                                                                                 ('Place 2', 2, 'Nathan', false,NOW(), NOW(), 'Description for Place 2', POINT(30.789, 40.012)),
                                                                                                                 ('Place 3', 3, 'Favour', false, NOW(), NOW(), 'Description for Place 3', POINT(50.345, 60.678));

SELECT id, name, ST_AsText(coordinate) AS coordinates_text FROM place;


# table for users



