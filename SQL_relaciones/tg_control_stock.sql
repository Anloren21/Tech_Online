DELIMITER //

CREATE TRIGGER tg_control_stock
BEFORE INSERT ON ventas 
FOR EACH ROW
BEGIN
    DECLARE v_stock INT;

    SELECT p.stock
    INTO v_stock
    FROM productos p
    WHERE p.id = NEW.producto_id;

    IF NEW.cantidad > v_stock THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stock insuficiente';

    ELSE

        UPDATE productos
        SET stock = stock - NEW.cantidad
        WHERE id = NEW.producto_id;

    END IF;

END //

DELIMITER ;

SELECT p.id, p.nombre, p.stock
FROM productos p;

DELIMITER ;

INSERT INTO ventas (producto_id, cantidad)
VALUES (13,10);

DELIMITER ;

SELECT
    v.id,
    p.nombre AS producto,
    v.cantidad,
    v.fecha
FROM ventas v
JOIN productos p
    ON p.id = v.producto_id;

SELECT
    v.id,
    p.nombre AS producto,
    CONCAT(FORMAT(p.precio, 2), ' €') AS precio_unitario,
    v.cantidad,
    CONCAT(FORMAT(p.precio * v.cantidad, 2), ' €') AS total,
    v.fecha
FROM ventas v
JOIN productos p
    ON p.id = v.producto_id;