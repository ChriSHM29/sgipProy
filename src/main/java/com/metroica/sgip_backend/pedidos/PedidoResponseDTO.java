package com.metroica.sgip_backend.pedidos;

import com.metroica.sgip_backend.shared.enums.CanalPedido;
import com.metroica.sgip_backend.shared.enums.EstadoPedido;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PedidoResponseDTO {
    private UUID id;
    private Integer numero;
    private CanalPedido canal;
    private EstadoPedido estado;
    private Integer prioridad;
    private BigDecimal total;
    private LocalDateTime fechaIngreso;
    private String clienteNombre;
}
