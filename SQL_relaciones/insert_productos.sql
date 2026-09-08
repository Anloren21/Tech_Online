SELECT *
FROM productos;

INSERT INTO productos (nombre, precio, stock)
VALUES
('Teclado mecánico', 59.90, 12),
('Ratón gaming', 29.90, 4),
('Monitor 24 pulgadas', 149.99, 3),
('Disco SSD 1TB', 89.99, 15),
('Memoria RAM 16GB', 54.50, 8),
('Auriculares gaming', 44.90, 2),
('Webcam Full HD', 39.95, 6),
('Hub USB-C', 24.99, 1),
('Fuente de alimentación 650W', 74.90, 10),
('Tarjeta gráfica RTX 4060', 329.99, 4),
('Procesador Ryzen 5', 189.90, 7),
('Placa base ATX', 129.99, 5),
('Cable HDMI 2m', 12.90, 20),
('Adaptador USB WiFi', 19.90, 3),
('Disco duro externo 2TB', 84.99, 9);

SELECT
    p.nombre,
    CONCAT(FORMAT(precio, 2), ' €') AS precio,
    p.stock,
    CONCAT(FORMAT(precio * stock, 2), ' €') AS valor_stock
FROM productos p;

