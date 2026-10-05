package modelo;

import java.time.LocalDateTime;

public class Entrega {

    private int id;
    private int pedidoId;
    private int repartidorId;
    private LocalDateTime fechaHora;

    public Entrega() {
    }

    public Entrega(
            int id,
            int pedidoId,
            int repartidorId,
            LocalDateTime fechaHora
    ) {

        this.id = id;
        this.pedidoId = pedidoId;
        this.repartidorId = repartidorId;
        this.fechaHora = fechaHora;
    }

    public Entrega(
            int pedidoId,
            int repartidorId,
            LocalDateTime fechaHora
    ) {

        this.pedidoId = pedidoId;
        this.repartidorId = repartidorId;
        this.fechaHora = fechaHora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    public int getRepartidorId() {
        return repartidorId;
    }

    public void setRepartidorId(int repartidorId) {
        this.repartidorId = repartidorId;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(
            LocalDateTime fechaHora
    ) {

        this.fechaHora = fechaHora;
    }
}