CREATE OR REPLACE VIEW v_stock_bajo AS
SELECT 
	p.id, 
	p.nombre, 
	CONCAT(FORMAT(p.precio, 2), ' €') AS precio,
    p.stock
FROM productos p
WHERE p.stock < 5;

SELECT *
FROM v_stock_bajo;

