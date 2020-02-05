CREATE TABLE tlds (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(16) NOT NULL,
  price DOUBLE NOT NULL
  
);
 
INSERT INTO tlds (name, price) VALUES
  ('com', 8.99),
  ('club', 15.99),
  ('net', 9.99);

CREATE TABLE domains (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  tld VARCHAR(255) NOT NULL,
  unique(name,tld)
);

create index IF NOT EXISTS domains_name_idx on domains(name);

INSERT INTO domains (name, tld) VALUES
  ('existing', 'com');