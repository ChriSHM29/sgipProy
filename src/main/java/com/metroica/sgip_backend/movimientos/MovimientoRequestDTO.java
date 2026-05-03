package com.metroica.sgip_backend.movimientos;

import lombok.Data;

import java.util.UUID;

@Data
public class MovimientoRequestDTO {
    private UUID productoId;
    private String tipo;
    private Integer cantidad;
    private String motivo;
}
