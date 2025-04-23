CREATE TABLE customer (
    customerid NUMBER GENERATED ALWAYS AS IDENTITY,
    cname VARCHAR2(50 CHAR) NOT NULL,
    email VARCHAR2(50 CHAR) NOT NULL,
    password VARCHAR2(100 CHAR) NOT NULL,
    phone NUMBER(10,0),
    address VARCHAR2(10 CHAR),
    PRIMARY KEY (customerid)
);

CREATE TABLE restaurant_admin (
    restaurant_id NUMBER GENERATED ALWAYS AS IDENTITY,
    restaurant_name VARCHAR2(100 CHAR) NOT NULL,
    admin_name VARCHAR2(50 CHAR) NOT NULL,
    email VARCHAR2(100 CHAR) NOT NULL,
    password VARCHAR2(100 CHAR) NOT NULL,
    PRIMARY KEY (id)
);
