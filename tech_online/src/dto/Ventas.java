package dto;

import java.time.LocalDateTime;

public class Ventas {

    private int id;
    private int productoId;
    private int cantidad;
    private LocalDateTime fecha;

    public Ventas() {
    }

    public Ventas(int productoId, int cantidad) {
        this.productoId = productoId;
        this.cantidad = cantidad;
    }

    public Ventas(int id, int productoId, int cantidad, LocalDateTime fecha) {
        this.id = id;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Ventas [id=" + id
                + ", productoId=" + productoId
                + ", cantidad=" + cantidad
                + ", fecha=" + fecha + "]";
    }
}
