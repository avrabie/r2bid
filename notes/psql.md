docker exec -it mypostgres

psql -U myuser mydatabase

List databases:
\l 
Connection info:
\conninfo

CREATE DATABASE mydatabase2;

To clear the screen:
\! clear

Create a table:
CREATE TABLE users (
    id GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL UNIQUE,
    age INT
);

\i /path/to/script.sql

Copy a file to a docker conatiner:
docker cp /path/to/file.txt mypostgres:/path/to/file.txt


Setting up kafka according to this article actually worked:
https://jskim1991.medium.com/docker-docker-compose-example-for-kafka-zookeeper-and-schema-registry-c516422532e7
