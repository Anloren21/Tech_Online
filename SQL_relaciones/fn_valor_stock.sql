DELIMITER //

CREATE FUNCTION fn_valor_stock(p_id INT)
RETURNS DECIMAL(12,2)
READS SQL DATA
BEGIN
    DECLARE v_valor DECIMAL(12,2);

    SELECT p.precio * p.stock
    INTO v_valor
    FROM productos p
    WHERE p.id = p_id;

    RETURN v_valor;
END

DELIMITER ;

SELECT CONCAT(FORMAT(fn_valor_stock(1), 2), ' €') AS valor_stock;

DELIMITER ;

SELECT
    p.id,
    p.nombre,
    CONCAT(FORMAT(p.precio, 2), ' €') AS precio,
    p.stock,
    CONCAT(FORMAT(fn_valor_stock(p.id), 2), ' €') AS valor_stock
FROM productos p;