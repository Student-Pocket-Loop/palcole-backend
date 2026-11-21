DROP DATABASE IF EXISTS palcole;

CREATE DATABASE IF NOT EXISTS palcole DEFAULT CHARACTER SET utf8mb4;
USE palcole;

-- 1. Users and authentication

CREATE TABLE IF NOT EXISTS roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    roles_role_id INT NOT NULL,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    educational_center VARCHAR(150) DEFAULT 'Santa Joaquina de Vedruna',
    create_date DATETIME DEFAULT (CURRENT_DATE),
account_expiration_date DATETIME,
is_locked BOOLEAN DEFAULT FALSE,
    credentials_expiration_date DATETIME,
    is_enabled BOOLEAN DEFAULT (TRUE),
    FOREIGN KEY (roles_role_id) REFERENCES roles (role_id) ON DELETE RESTRICT
);

-- --------------------------------------------------------
-- 2. CATEGORIES (For offers and school supplies)
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- --------------------------------------------------------
-- 4. PROMOTIONS AND STORE OFFERS
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS promotions (
    promotion_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    original_price DECIMAL(8,2) NOT NULL,
    discount_price DECIMAL(8,2) NOT NULL,
    discount_percentage INT NOT NULL,
    promo_code VARCHAR(50),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories (category_id)
);

-- --------------------------------------------------------
-- 5. ACADEMIC SUPPLIES MARKETPLACE (Second-hand)
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS academic_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    category_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(8,2) DEFAULT 0.00,
    item_condition ENUM('nuevo', 'como_nuevo', 'usado', 'deteriorado') NOT NULL,
    listing_type ENUM('venta', 'intercambio', 'regalo') NOT NULL DEFAULT 'venta',
    sale_status ENUM('disponible', 'reservado', 'entregado') NOT NULL DEFAULT 'disponible',
    published_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    image_url VARCHAR(150) NOT NULL,
    FOREIGN KEY (student_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories (category_id)
);

-- --------------------------------------------------------
-- 7. FAVORITES (Saved promotions or items)
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS favorite_items (
    user_id INT NOT NULL,
    item_id INT NOT NULL,
    primary key(user_id, item_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES academic_items (item_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS favorite_promotions (
    user_id INT NOT NULL,
    promotion_id INT NOT NULL,
    primary key(user_id, promotion_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (promotion_id) REFERENCES promotions (promotion_id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- 8. CHAT / MESSAGING FOR ON-CAMPUS DELIVERIES
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS conversations (
    conversation_id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT NOT NULL,
    buyer_id INT NOT NULL,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (item_id) REFERENCES academic_items (item_id) ON DELETE CASCADE,
    FOREIGN KEY (buyer_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    conversation_id INT NOT NULL,
    sender_id INT NOT NULL,
    content TEXT NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (conversation_id) REFERENCES conversations (conversation_id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES users (user_id)
);