-- CarPlay List Reminder Database Schema
-- This script creates the database tables if they don't exist

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    phone_number VARCHAR(20),
    profile_picture_url TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create families table
CREATE TABLE IF NOT EXISTS families (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    family_code VARCHAR(50) UNIQUE NOT NULL,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE
);

-- Create family_members table
CREATE TABLE IF NOT EXISTS family_members (
    id BIGSERIAL PRIMARY KEY,
    family_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'MEMBER')),
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    invited_by BIGINT,
    FOREIGN KEY (family_id) REFERENCES families(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (invited_by) REFERENCES users(id) ON DELETE SET NULL,
    UNIQUE(family_id, user_id)
);

-- Create shopping_lists table
CREATE TABLE IF NOT EXISTS shopping_lists (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    family_id BIGINT NOT NULL,
    created_by BIGINT NOT NULL,
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (family_id) REFERENCES families(id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE
);

-- Create shopping_list_items table
CREATE TABLE IF NOT EXISTS shopping_list_items (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    quantity INTEGER DEFAULT 1,
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    shopping_list_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (shopping_list_id) REFERENCES shopping_lists(id) ON DELETE CASCADE
);

-- Create indexes for better query performance
-- Users table indexes
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_is_active ON users(is_active);
CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);

-- Families table indexes
CREATE INDEX IF NOT EXISTS idx_families_family_code ON families(family_code);
CREATE INDEX IF NOT EXISTS idx_families_created_by ON families(created_by);
CREATE INDEX IF NOT EXISTS idx_families_created_at ON families(created_at);

-- Family members table indexes
CREATE INDEX IF NOT EXISTS idx_family_members_family_id ON family_members(family_id);
CREATE INDEX IF NOT EXISTS idx_family_members_user_id ON family_members(user_id);
CREATE INDEX IF NOT EXISTS idx_family_members_is_active ON family_members(is_active);
CREATE INDEX IF NOT EXISTS idx_family_members_role ON family_members(role);

-- Shopping lists table indexes
CREATE INDEX IF NOT EXISTS idx_shopping_lists_family_id ON shopping_lists(family_id);
CREATE INDEX IF NOT EXISTS idx_shopping_lists_created_by ON shopping_lists(created_by);
CREATE INDEX IF NOT EXISTS idx_shopping_lists_is_completed ON shopping_lists(is_completed);
CREATE INDEX IF NOT EXISTS idx_shopping_lists_created_at ON shopping_lists(created_at);

-- Shopping list items table indexes
CREATE INDEX IF NOT EXISTS idx_shopping_list_items_shopping_list_id ON shopping_list_items(shopping_list_id);
CREATE INDEX IF NOT EXISTS idx_shopping_list_items_is_completed ON shopping_list_items(is_completed);
CREATE INDEX IF NOT EXISTS idx_shopping_list_items_created_at ON shopping_list_items(created_at);

-- Create a function to automatically update the updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers to automatically update updated_at on record updates
DROP TRIGGER IF EXISTS update_users_updated_at ON users;
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_families_updated_at ON families;
CREATE TRIGGER update_families_updated_at
    BEFORE UPDATE ON families
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_shopping_lists_updated_at ON shopping_lists;
CREATE TRIGGER update_shopping_lists_updated_at
    BEFORE UPDATE ON shopping_lists
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_shopping_list_items_updated_at ON shopping_list_items;
CREATE TRIGGER update_shopping_list_items_updated_at
    BEFORE UPDATE ON shopping_list_items
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Insert some sample data (only if tables are empty)
-- Insert sample users
INSERT INTO users (username, email, first_name, last_name) 
SELECT 'john_doe', 'john@example.com', 'John', 'Doe' 
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'john_doe');

INSERT INTO users (username, email, first_name, last_name) 
SELECT 'jane_smith', 'jane@example.com', 'Jane', 'Smith' 
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'jane_smith');

-- Insert sample family
INSERT INTO families (name, description, family_code, created_by) 
SELECT 'Smith Family', 'Our family shopping lists', 'FAM001', u.id
FROM users u
WHERE u.username = 'john_doe'
AND NOT EXISTS (SELECT 1 FROM families WHERE name = 'Smith Family');

-- Insert family members
INSERT INTO family_members (family_id, user_id, role)
SELECT f.id, u.id, 'ADMIN'
FROM families f, users u
WHERE f.name = 'Smith Family' AND u.username = 'john_doe'
AND NOT EXISTS (SELECT 1 FROM family_members WHERE family_id = f.id AND user_id = u.id);

INSERT INTO family_members (family_id, user_id, role, invited_by)
SELECT f.id, u2.id, 'MEMBER', u1.id
FROM families f, users u1, users u2
WHERE f.name = 'Smith Family' AND u1.username = 'john_doe' AND u2.username = 'jane_smith'
AND NOT EXISTS (SELECT 1 FROM family_members WHERE family_id = f.id AND user_id = u2.id);

-- Insert sample shopping lists
INSERT INTO shopping_lists (name, family_id, created_by, is_completed) 
SELECT 'Grocery Shopping', f.id, u.id, FALSE
FROM families f, users u
WHERE f.name = 'Smith Family' AND u.username = 'john_doe'
AND NOT EXISTS (SELECT 1 FROM shopping_lists WHERE name = 'Grocery Shopping');

INSERT INTO shopping_lists (name, family_id, created_by, is_completed) 
SELECT 'Hardware Store', f.id, u.id, FALSE
FROM families f, users u
WHERE f.name = 'Smith Family' AND u.username = 'jane_smith'
AND NOT EXISTS (SELECT 1 FROM shopping_lists WHERE name = 'Hardware Store');

-- Insert sample items for the grocery list
INSERT INTO shopping_list_items (name, description, quantity, shopping_list_id, is_completed)
SELECT 'Milk', '2% milk', 1, sl.id, FALSE
FROM shopping_lists sl
WHERE sl.name = 'Grocery Shopping'
AND NOT EXISTS (
    SELECT 1 FROM shopping_list_items sli 
    WHERE sli.name = 'Milk' AND sli.shopping_list_id = sl.id
);

INSERT INTO shopping_list_items (name, description, quantity, shopping_list_id, is_completed)
SELECT 'Bread', 'Whole wheat bread', 1, sl.id, FALSE
FROM shopping_lists sl
WHERE sl.name = 'Grocery Shopping'
AND NOT EXISTS (
    SELECT 1 FROM shopping_list_items sli 
    WHERE sli.name = 'Bread' AND sli.shopping_list_id = sl.id
);

INSERT INTO shopping_list_items (name, description, quantity, shopping_list_id, is_completed)
SELECT 'Eggs', 'Large eggs', 1, sl.id, FALSE
FROM shopping_lists sl
WHERE sl.name = 'Grocery Shopping'
AND NOT EXISTS (
    SELECT 1 FROM shopping_list_items sli 
    WHERE sli.name = 'Eggs' AND sli.shopping_list_id = sl.id
);

INSERT INTO shopping_list_items (name, description, quantity, shopping_list_id, is_completed)
SELECT 'Screws', 'Wood screws 2 inch', 1, sl.id, FALSE
FROM shopping_lists sl
WHERE sl.name = 'Hardware Store'
AND NOT EXISTS (
    SELECT 1 FROM shopping_list_items sli 
    WHERE sli.name = 'Screws' AND sli.shopping_list_id = sl.id
);

INSERT INTO shopping_list_items (name, description, quantity, shopping_list_id, is_completed)
SELECT 'Drill bits', 'Set of drill bits', 1, sl.id, FALSE
FROM shopping_lists sl
WHERE sl.name = 'Hardware Store'
AND NOT EXISTS (
    SELECT 1 FROM shopping_list_items sli 
    WHERE sli.name = 'Drill bits' AND sli.shopping_list_id = sl.id
);