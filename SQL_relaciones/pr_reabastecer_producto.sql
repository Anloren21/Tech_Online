DELIMITER //

CREATE PROCEDURE pr_reabastecer_producto(
    IN p_id INT,
    IN p_cantidad INT
)
BEGIN

    IF p_cantidad <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La cantidad debe ser positiva';
    END IF;

    UPDATE productos
    SET stock = stock + p_cantidad
    WHERE id = p_id;

END //

DELIMITER ;

CALL pr_reabastecer_producto(2, 5);

DELIMITER ;

CALL pr_reabastecer_producto(2, -3);

DELIMITER ;

SELECT p.id, p.nombre, p.stock
FROM productos p 
WHERE id = 2;
