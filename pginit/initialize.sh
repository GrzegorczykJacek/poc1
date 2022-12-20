#!/bin/bash

set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE SCHEMA coordinator;
    CREATE DATABASE "registrator";
    CREATE DATABASE "httphandler1";
EOSQL
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "registrator" <<-EOSQL
            CREATE SCHEMA registrator;
EOSQL
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "httphandler1" <<-EOSQL
            CREATE SCHEMA httphandler1;
EOSQL