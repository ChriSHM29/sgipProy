package com.metroica.sgip_backend.dto.request;

import lombok.Data;
import java.util.UUID;

@Data
public class MovimientoRequestDTO {
    private UUID productoId;
    private String tipo;     // Recibirá "SALIDA" o "ENTRADA"
    private Integer cantidad;
    private String motivo;
}