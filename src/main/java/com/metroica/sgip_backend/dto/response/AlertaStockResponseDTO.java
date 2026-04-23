package com.metroica.sgip_backend.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AlertaStockResponseDTO {
    private UUID id;
    private String productoNombre;
    private Integer stockAlGenerar;
    private Integer puntoPedidoReferencia;
    private String estado;
    private LocalDateTime fechaGenerada;
}