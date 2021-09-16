DROP DATABASE expensetrackerdb;
DROP USER admin;
CREATE USER admin WITH PASSWORD 'user123';
CREATE DATABASE expensetrackerdb with template=template0 owner=admin;
\connect expensetrackerdb;
ALTER DEFAULT PRIVILEGES GRANT ALL ON tables TO admin;
ALTER DEFAULT PRIVILEGES GRANT ALL ON sequences TO admin;

CREATE TABLE etracker_users(
    user_id integer primary key not null,
    first_name varchar(20) not null,
    last_name varchar(20) not null,
    email varchar(30) not null,
    password text not null
);

CREATE TABLE etracker_categories(
    category_id integer primary key not null,
    user_id integer not null,
    title varchar(20) not null,
    description varchar(50) not null
);

ALTER TABLE etracker_categories add constraint cat_users_fk
foreign key(user_id) references etracker_users(user_id);

CREATE TABLE etracker_transactions(
    transaction_id integer primary key not null,
    category_id integer not null,
    user_id integer not null,
    amount numeric(10, 2) not null,
    note varchar(50) not null,
    transaction_datetimestamp Timestamp not null
);

ALTER TABLE etracker_transactions add constraint trans_cat_fk
foreign key(category_id) references etracker_categories(category_id);

ALTER TABLE etracker_transactions add constraint trans_users_fk
foreign key(user_id) references etracker_users(user_id);


CREATE SEQUENCE etracker_users_seq increment 1 start 1;
CREATE SEQUENCE etracker_categories_seq increment 1 start 1;
CREATE SEQUENCE etracker_transactions_seq increment 1 start 1000;







