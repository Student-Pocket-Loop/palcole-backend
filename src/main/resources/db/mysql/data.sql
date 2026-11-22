-- INSERT INTO palcole.roles VALUES (1, "ADMIN"), (2, "USER");

-- INSERT INTO palcole.users (roles_role_id, username, email, password, account_expiration_date, credentials_expiration_date)
-- VALUES (1, "joaquin", "admin@localhost.es", "$2a$10$bOY/2//tFFoJwliab9gyHubB6kFEdhIS9SIFS4aeOcVqj3ZFQZVa.", "2999-12-31 23:59:59", "2999-12-31 23:59:59");

-- INSERT INTO palcole.users (roles_role_id, username, email, password, account_expiration_date, credentials_expiration_date)
-- VALUES (2, "manuel", "manuel@localhost.es", "$2a$10$bOY/2//tFFoJwliab9gyHubB6kFEdhIS9SIFS4aeOcVqj3ZFQZVa.", "2999-12-31 23:59:59", "2999-12-31 23:59:59");

/*
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE palcole.users;
TRUNCATE TABLE palcole.roles;
TRUNCATE TABLE palcole.promotions;
TRUNCATE TABLE palcole.messages;
TRUNCATE TABLE palcole.favorite_promotions;
TRUNCATE TABLE palcole.favorite_items;
TRUNCATE TABLE palcole.conversations;
TRUNCATE TABLE palcole.categories;
TRUNCATE TABLE palcole.academic_items;

SET FOREIGN_KEY_CHECKS = 1;
*/

USE palcole;

-- 1. ROLES
INSERT INTO roles (role_id, name) VALUES
  (1, 'ADMIN'),
  (2, 'USER');

-- 2. USERS
INSERT INTO users
  (user_id, roles_role_id, username, email, password, educational_center,
   create_date, account_expiration_date, is_locked, credentials_expiration_date, is_enabled)
VALUES
  (1, 1, 'joaquin', 'admin@localhost.es',
   '$2a$10$bOY/2//tFFoJwliab9gyHubB6kFEdhIS9SIFS4aeOcVqj3ZFQZVa.',
   'Santa Joaquina de Vedruna', '2026-09-01', '2999-12-31', 0, '2999-12-31', 1),
  (2, 2, 'manuel', 'manuel@localhost.es',
   '$2a$10$bOY/2//tFFoJwliab9gyHubB6kFEdhIS9SIFS4aeOcVqj3ZFQZVa.',
   'Santa Joaquina de Vedruna', '2026-09-01', '2999-12-31', 0, '2999-12-31', 1),
  (3, 2, 'lucia', 'lucia@localhost.es',
   '$2a$10$bOY/2//tFFoJwliab9gyHubB6kFEdhIS9SIFS4aeOcVqj3ZFQZVa. ',
   'Santa Joaquina de Vedruna', '2026-09-01', '2999-12-31', 0, '2999-12-31', 1);

-- 3. CATEGORIES
INSERT INTO categories (category_id, name) VALUES
  (1, 'Libros'),
  (2, 'Apuntes'),
  (3, 'Material de papelería'),
  (4, 'Tecnología');

-- 4. PROMOTIONS
INSERT INTO promotions
  (promotion_id, category_id, title, description, original_price, discount_price,
   discount_percentage, promo_code, start_date, end_date)
VALUES
  (1, 3, 'Vuelta al cole en papelería', 'Descuento en material escolar',
   20.00, 15.00, 25, 'VUELTA25', '2026-09-01', '2026-09-30'),
  (2, 4, 'Calculadoras científicas', 'Descuento para estudiantes',
   30.00, 24.00, 20, NULL, '2026-09-01', '2026-12-31'),
  (3, 3, 'Mochilas escolares', 'Descuento en mochilas',
   50.00, 40.00, 20, 'MOCHI20', '2026-09-01', '2026-10-15'),
  (4, 1, 'Libros de texto', 'Descuento en libros de ciclos formativos',
   100.00, 70.00, 30, 'LIBROS30', '2026-09-01', '2026-11-30');

-- 5. ACADEMIC_ITEMS (published_at toma CURRENT_TIMESTAMP por defecto)
INSERT INTO academic_items
  (item_id, student_id, category_id, title, description, price,
   item_condition, listing_type, sale_status, image_url)
VALUES
  (1, 2, 1, 'Libro de Programación', 'Apenas usado, sin subrayados',
   12.50, 'como_nuevo', 'venta', 'disponible', 'https://ejemplo.com/libro-programacion.jpg'),
  (2, 3, 4, 'Calculadora científica', 'Funciona perfectamente',
   10.00, 'usado', 'venta', 'reservado', 'https://ejemplo.com/calculadora.jpg'),
  (3, 2, 2, 'Apuntes de Bases de Datos', 'Resumen completo del curso',
   0.00, 'usado', 'regalo', 'disponible', 'https://ejemplo.com/apuntes-bd.jpg'),
  (4, 3, 3, 'Estuche completo', 'Lo cambio por una libreta A4',
   0.00, 'nuevo', 'intercambio', 'disponible', 'https://ejemplo.com/estuche.jpg'),
  (5, 1, 1, 'Diccionario de inglés', 'Algo deteriorado en la portada',
   3.00, 'deteriorado', 'venta', 'entregado', 'https://ejemplo.com/diccionario.jpg');

-- 6. FAVORITE_ITEMS
INSERT INTO favorite_items (user_id, item_id) VALUES
  (2, 2),
  (2, 4),
  (3, 1),
  (3, 3);

-- 7. FAVORITE_PROMOTIONS
INSERT INTO favorite_promotions (user_id, promotion_id) VALUES
  (2, 1),
  (2, 2),
  (3, 3);

-- 8. CONVERSATIONS (buyer_id es quien pregunta por el artículo de otro usuario)
INSERT INTO conversations (item_id, buyer_id) VALUES
  (1, 3),   -- lucia pregunta por el libro de manuel
  (2, 2),   -- manuel pregunta por la calculadora de lucia
  (3, 3);   -- lucia pregunta por los apuntes de manuel

-- 9. MESSAGES
INSERT INTO messages (conversation_id, sender_id, content) VALUES
  (1, 3, 'Hola, ¿sigue disponible el libro?'),
  (1, 2, 'Sí, lo tengo en clase los martes.'),
  (1, 3, 'Perfecto, el martes te lo compro.'),
  (2, 2, 'Hola, ¿podrías bajarme el precio de la calculadora?'),
  (2, 3, 'Te la dejo en 9 euros.'),
  (3, 3, '¿Me pasas los apuntes en papel o en PDF?');