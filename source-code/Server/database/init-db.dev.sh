#!/bin/bash

set -e

# Using a shell init script to be able to use environment variable substitution
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER $SONAR_JDBC_USERNAME PASSWORD '$SONAR_JDBC_PASSWORD';
    CREATE DATABASE $SONAR_JDBC_DB OWNER $SONAR_JDBC_USERNAME;
EOSQL