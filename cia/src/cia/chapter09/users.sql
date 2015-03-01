CREATE TABLE IF NOT EXISTS USERS (
  id integer NOT NULL PRIMARY KEY,
  login varchar(256),
  first_name varchar(256),
  last_name varchar(256),
  password varchar(256),
  email_address varchar(256)
);

CREATE TABLE IF NOT EXISTS CHARGES (
  id integer not null primary key,
  user_id integer,
  amount_dollars integer,
  amount_cents integer,
  category varchar(256),
  vendor_name varchar(256),
  date datetime
);