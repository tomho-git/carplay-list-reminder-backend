-- Docker initialization script for PostgreSQL
-- This script ensures the database and user are set up properly

-- Create database if it doesn't exist
SELECT 'CREATE DATABASE carplay_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'carplay_db');

-- Connect to the database
\c carplay_db;

-- The main schema.sql will be executed by Spring Boot automatically
-- This file is just for Docker container initialization if needed