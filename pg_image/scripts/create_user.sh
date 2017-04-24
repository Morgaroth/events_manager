#!/usr/bin/env bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE USER events_manager_user with LOGIN ENCRYPTED PASSWORD 'ala123';
    CREATE DATABASE events;
    GRANT ALL PRIVILEGES ON DATABASE events TO events_manager_user;
EOSQL