package com.metroica.sgip_backend.dto.response;

import com.metroica.sgip_backend.model.enums.CanalPedido;
import com.metroica.sgip_backend.model.enums.EstadoPedido;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PedidoResponseDTO {
    private UUID id;
    private Integer numero; // El ticket legible
    private CanalPedido canal;
    private EstadoPedido estado;
    private Integer prioridad; // Para pintar de rojo los urgentes en React
    private BigDecimal total;
    private LocalDateTime fechaIngreso;
    private String clienteNombre;
}