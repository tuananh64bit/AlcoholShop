-- Alcohol Shop Database Schema
-- Create tables and sample data for H2 fallback
CREATE TABLE IF NOT EXISTS category (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    image VARCHAR(255),
    category_id INT NOT NULL,
    alcohol_percentage DECIMAL(5,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_account (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    fullname VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
    birth_date DATE NOT NULL,
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS contact_message (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    subject VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert minimal categories and products for testing
INSERT INTO category (name) SELECT 'Whiskey' WHERE NOT EXISTS (SELECT 1 FROM category WHERE name='Whiskey');
INSERT INTO category (name) SELECT 'Vodka' WHERE NOT EXISTS (SELECT 1 FROM category WHERE name='Vodka');

INSERT INTO product (code, name, description, price, stock, image, category_id, alcohol_percentage)
SELECT 'P001', 'Sample Whiskey', 'Test product', 49.99, 10, 'placeholder.jpg', (SELECT id FROM category WHERE name='Whiskey' LIMIT 1), 40.0
WHERE NOT EXISTS (SELECT 1 FROM product WHERE code='P001');

INSERT INTO user_account (username, fullname, email, password_hash, role, birth_date, address)
SELECT 'admin', 'Administrator', 'admin@alcoholshop.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'ADMIN', '1990-01-01', 'Local Admin'
WHERE NOT EXISTS (SELECT 1 FROM user_account WHERE username='admin');

INSERT INTO contact_message (name, email, phone, subject, message)
SELECT 'Health Check', 'health@local', '+000', 'Init', 'Initial seed' WHERE NOT EXISTS (SELECT 1 FROM contact_message WHERE email='health@local');

