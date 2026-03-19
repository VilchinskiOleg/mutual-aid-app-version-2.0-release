#!/bin/bash
set -e

DB1="apiDB"
DB2="message-chat"

# Function to check if a database exists
function database_exists() {
  psql -U "$POSTGRES_USER" -tAc "SELECT 1 FROM pg_database WHERE datname='$1'" | grep -q 1
}

# Create database for Order-Service if it doesn't exist :
if ! database_exists "$DB1"; then
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE "$DB1";
EOSQL
fi

# Create database for Message-Chat-Service if it doesn't exist :
if ! database_exists "$DB2"; then
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE "$DB2";
EOSQL
fi
