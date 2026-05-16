-- Contraseña: Admin1234! (bcrypt hash)
INSERT INTO usuarios (nombre, apellido, email, password_hash, rol) VALUES
('Admin', 'Sistema', 'admin@supermercado.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh.2', 'ADMIN');

-- Categorías padre
INSERT INTO categorias (nombre, descripcion) VALUES
('Almacén', 'Productos de almacén y despensa'),
('Bebidas', 'Todo tipo de bebidas'),
('Lácteos', 'Productos lácteos y refrigerados'),
('Carnes', 'Carnes y fiambres'),
('Limpieza', 'Productos de limpieza del hogar'),
('Perfumería', 'Cuidado personal e higiene'),
('Frutas y Verduras', 'Productos frescos'),
('Panadería', 'Pan y productos de panadería');

-- Subcategorías Almacén (id padre = 1)
INSERT INTO categorias (nombre, categoria_padre_id) VALUES
('Aceites', 1), ('Arroz', 1), ('Pastas', 1), ('Conservas', 1);

-- Subcategorías Bebidas (id padre = 2)
INSERT INTO categorias (nombre, categoria_padre_id) VALUES
('Aguas', 2), ('Gaseosas', 2), ('Jugos', 2), ('Vinos', 2);

-- Subcategorías Lácteos (id padre = 3)
INSERT INTO categorias (nombre, categoria_padre_id) VALUES
('Leches', 3), ('Yogures', 3), ('Quesos', 3);

-- Subcategorías Carnes (id padre = 4)
INSERT INTO categorias (nombre, categoria_padre_id) VALUES
('Vacuna', 4), ('Pollo', 4), ('Cerdo', 4);

-- Productos de ejemplo
INSERT INTO productos (nombre, descripcion, precio, stock, unidad, peso, categoria_id) VALUES
('Aceite de Girasol Natura 1.5L', 'Aceite de girasol de primera calidad', 2850.00, 100, 'LT', 1.5, 9),
('Arroz Largo Fino La Estrella 1kg', 'Arroz largo fino tipo 1', 1200.00, 200, 'KG', 1.0, 10),
('Fideos Fetuccini Matarazzo 500g', 'Fideos secos de sémola', 850.00, 150, 'GR', 0.5, 11),
('Agua Mineral Villavicencio 1.5L', 'Agua mineral sin gas', 650.00, 300, 'LT', 1.5, 13),
('Coca-Cola 2.25L', 'Gaseosa cola', 1800.00, 200, 'LT', 2.25, 14),
('Leche Entera La Serenísima 1L', 'Leche entera larga vida', 980.00, 250, 'LT', 1.0, 17),
('Yogur Natural Danone 190g', 'Yogur natural sin azúcar', 650.00, 120, 'GR', 0.19, 18);
