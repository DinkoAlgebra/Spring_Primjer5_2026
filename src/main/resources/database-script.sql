DROP TABLE IF EXISTS Category;

CREATE TABLE Category
(
    id int PRIMARY KEY,
    name        VARCHAR(50)    NOT NULL,
    description VARCHAR(255)
);

DROP TABLE IF EXISTS Article;

CREATE TABLE Article
(
    id INT IDENTITY(1,1) PRIMARY KEY,
    name        VARCHAR(50)    NOT NULL,
    description VARCHAR(255),
    price       DECIMAL(10, 2) NOT NULL,
    categoryId  INT,
    FOREIGN KEY (categoryId) REFERENCES Category(id)
);

INSERT INTO Category(id, name, description)
VALUES(1, 'Cars and motorcycles', 'New and used');

INSERT INTO Category(id, name, description)
VALUES(2, 'Apartments and houses', 'New and used');

INSERT INTO ARTICLE(id, name, description, price, categoryId)
VALUES(1, 'Tesla Model Y', 'Electric car', 50000, 2);

INSERT INTO ARTICLE(id, name, description, price, categoryId)
VALUES(2, 'Apartment on the main square', 'Luxury apartment', 500000, 1);

INSERT INTO ARTICLE(id, name, description, price, categoryId)
VALUES(3, 'House on the beach', 'Vacation house', 5000000, 1);

INSERT INTO ARTICLE(id, name, description, price, categoryId)
VALUES(4, 'Oldtimer Mercedes X 1800', 'Vintage car', 100000, 2);

SELECT * FROM Article;