package com.metroica.sgip_backend.pedidos;

import com.metroica.sgip_backend.shared.enums.CanalPedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PedidoCreateDTO {

    @NotNull(message = "Debe especificar si es LOCAL o DELIVERY")
    private CanalPedido canal;

    private String clienteNombre;
    private String clienteTelefono;
    private String clienteDireccion;
    private String observaciones;

    @NotEmpty(message = "El pedido no puede estar vacio")
    @Valid
    private List<PedidoItemDTO> items;
}
