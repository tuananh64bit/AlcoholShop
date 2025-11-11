-- Alcohol Shop Database Schema
-- Create database
CREATE DATABASE IF NOT EXISTS alcohol_shop 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE alcohol_shop;

-- Categories table
CREATE TABLE category (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Products table
CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    image VARCHAR(255),
    category_id INT NOT NULL,
    alcohol_percentage DECIMAL(5,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
);

-- User accounts table
CREATE TABLE user_account (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    fullname VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'CUSTOMER') NOT NULL DEFAULT 'CUSTOMER',
    birth_date DATE NOT NULL,
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Orders table
CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE CASCADE
);

-- Order items table
CREATE TABLE order_item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

-- Cart items table (optional session-sync)
CREATE TABLE cart_item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_product (user_id, product_id)
);

-- Contact messages table
CREATE TABLE contact_message (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    subject VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert categories
INSERT INTO category (name) VALUES 
('Whiskey'),
('Vodka'),
('Wine'),
('Beer'),
('Liqueur');

-- Insert products (30+ products)
-- Whiskey products
INSERT INTO product (code, name, description, price, stock, image, category_id, alcohol_percentage) VALUES
('P001', 'Johnnie Walker Black Label', 'Premium blended Scotch whisky with rich, complex flavors', 89.99, 50, 'whiskey1.jpg', 1, 40.0),
('P002', 'Macallan 12 Year Old', 'Single malt Scotch whisky aged in sherry oak casks', 129.99, 30, 'whiskey2.jpg', 1, 40.0),
('P003', 'Jack Daniel\'s Old No. 7', 'Classic Tennessee whiskey with smooth, mellow taste', 29.99, 100, 'whiskey3.jpg', 1, 40.0),
('P004', 'Glenfiddich 15 Year Old', 'Single malt Scotch whisky with honey and oak notes', 79.99, 40, 'whiskey4.jpg', 1, 40.0),
('P005', 'Jameson Irish Whiskey', 'Triple-distilled Irish whiskey with smooth finish', 34.99, 80, 'whiskey5.jpg', 1, 40.0),
('P006', 'Crown Royal Deluxe', 'Canadian whisky with vanilla and oak flavors', 39.99, 60, 'whiskey6.jpg', 1, 40.0);

-- Vodka products
INSERT INTO product (code, name, description, price, stock, image, category_id, alcohol_percentage) VALUES
('P007', 'Grey Goose Vodka', 'Premium French vodka with smooth, clean taste', 49.99, 45, 'vodka1.jpg', 2, 40.0),
('P008', 'Beluga Gold Line', 'Russian premium vodka with exceptional smoothness', 89.99, 25, 'vodka2.jpg', 2, 40.0),
('P009', 'Absolut Vodka', 'Swedish vodka with pure, clean character', 24.99, 70, 'vodka3.jpg', 2, 40.0),
('P010', 'Ketel One Vodka', 'Dutch vodka with crisp, clean taste', 32.99, 55, 'vodka4.jpg', 2, 40.0),
('P011', 'Tito\'s Handmade Vodka', 'American craft vodka made from corn', 28.99, 65, 'vodka5.jpg', 2, 40.0),
('P012', 'Ciroc Vodka', 'French vodka made from grapes', 34.99, 40, 'vodka6.jpg', 2, 40.0);

-- Wine products
INSERT INTO product (code, name, description, price, stock, image, category_id, alcohol_percentage) VALUES
('P013', 'Dom Pérignon Champagne', 'Prestige cuvée champagne with fine bubbles', 199.99, 15, 'wine1.jpg', 3, 12.5),
('P014', 'Château Margaux 2015', 'Bordeaux red wine with complex aromas', 299.99, 10, 'wine2.jpg', 3, 13.5),
('P015', 'Kendall Jackson Chardonnay', 'California white wine with buttery notes', 19.99, 80, 'wine3.jpg', 3, 13.0),
('P016', 'Barolo DOCG 2018', 'Italian red wine with tannic structure', 49.99, 35, 'wine4.jpg', 3, 14.0),
('P017', 'Sauvignon Blanc Marlborough', 'New Zealand white wine with crisp acidity', 16.99, 60, 'wine5.jpg', 3, 12.5),
('P018', 'Rioja Reserva 2016', 'Spanish red wine aged in oak barrels', 24.99, 45, 'wine6.jpg', 3, 13.5);

-- Beer products
INSERT INTO product (code, name, description, price, stock, image, category_id, alcohol_percentage) VALUES
('P019', 'Heineken Lager', 'Dutch pilsner with crisp, refreshing taste', 8.99, 120, 'beer1.jpg', 4, 5.0),
('P020', 'Stella Artois', 'Belgian lager with smooth, balanced flavor', 9.99, 100, 'beer2.jpg', 4, 5.0),
('P021', 'Guinness Draught', 'Irish stout with creamy head and roasted flavor', 12.99, 80, 'beer3.jpg', 4, 4.2),
('P022', 'Corona Extra', 'Mexican lager with light, crisp taste', 7.99, 90, 'beer4.jpg', 4, 4.5),
('P023', 'Budweiser', 'American lager with clean, refreshing taste', 6.99, 110, 'beer5.jpg', 4, 5.0),
('P024', 'Blue Moon Belgian White', 'Wheat beer with orange peel and coriander', 10.99, 70, 'beer6.jpg', 4, 5.4);

-- Liqueur products
INSERT INTO product (code, name, description, price, stock, image, category_id, alcohol_percentage) VALUES
('P025', 'Baileys Irish Cream', 'Cream liqueur with Irish whiskey and chocolate', 24.99, 50, 'liqueur1.jpg', 5, 17.0),
('P026', 'Grand Marnier Cordon Rouge', 'Orange liqueur with cognac base', 39.99, 30, 'liqueur2.jpg', 5, 40.0),
('P027', 'Kahlúa Coffee Liqueur', 'Mexican coffee liqueur with vanilla notes', 19.99, 60, 'liqueur3.jpg', 5, 20.0),
('P028', 'Cointreau Orange Liqueur', 'French triple sec with orange flavor', 29.99, 40, 'liqueur4.jpg', 5, 40.0),
('P029', 'Amaretto Disaronno', 'Italian almond liqueur with sweet taste', 22.99, 45, 'liqueur5.jpg', 5, 28.0),
('P030', 'Chambord Raspberry Liqueur', 'French raspberry liqueur with royal taste', 34.99, 25, 'liqueur6.jpg', 5, 16.5);

-- Insert admin user (password: Admin@123 - hashed with BCrypt)
-- BCrypt hash for "Admin@123" is: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi
INSERT INTO user_account (username, fullname, email, password_hash, role, birth_date, address) VALUES
('admin', 'Administrator', 'admin@alcoholshop.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'ADMIN', '1990-01-01', '123 Admin Street, City, Country');

-- Insert sample customer
INSERT INTO user_account (username, fullname, email, password_hash, role, birth_date, address) VALUES
('customer1', 'John Doe', 'john.doe@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'CUSTOMER', '1995-05-15', '456 Customer Avenue, City, Country');

-- Insert sample contact message
INSERT INTO contact_message (name, email, phone, subject, message) VALUES
('Jane Smith', 'jane.smith@email.com', '+1234567890', 'Product Inquiry', 'I would like to know more about your whiskey collection. Do you have any rare bottles available?');

-- Create indexes for better performance
CREATE INDEX idx_product_category ON product(category_id);
CREATE INDEX idx_product_code ON product(code);
CREATE INDEX idx_user_username ON user_account(username);
CREATE INDEX idx_user_email ON user_account(email);
CREATE INDEX idx_order_user ON orders(user_id);
CREATE INDEX idx_order_item_order ON order_item(order_id);
CREATE INDEX idx_cart_user ON cart_item(user_id);
CREATE INDEX idx_contact_created ON contact_message(created_at);

