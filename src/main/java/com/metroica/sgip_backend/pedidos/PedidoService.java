package com.metroica.sgip_backend.pedidos;

import com.metroica.sgip_backend.productos.Producto;
import com.metroica.sgip_backend.productos.ProductoRepository;
import com.metroica.sgip_backend.seguridad.Usuario;
import com.metroica.sgip_backend.seguridad.UsuarioRepository;
import com.metroica.sgip_backend.shared.enums.EstadoPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoDetalleRepository detalleRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public PedidoResponseDTO crearPedido(PedidoCreateDTO dto) {
        Usuario usuario = usuarioRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No hay usuarios registrados en el sistema"));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setCanal(dto.getCanal());
        pedido.setClienteNombre(dto.getClienteNombre());
        pedido.setClienteTelefono(dto.getClienteTelefono());
        pedido.setClienteDireccion(dto.getClienteDireccion());
        pedido.setObservaciones(dto.getObservaciones());
        pedido.setPrioridad(dto.getCanal().name().equals("DELIVERY") ? (short) 3 : (short) 5);

        pedidoRepository.save(pedido);

        for (PedidoItemDTO itemDTO : dto.getItems()) {
            Producto producto = productoRepository.findById(itemDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + itemDTO.getProductoId()));

            if (producto.getStockActual() < itemDTO.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para '" + producto.getNombre() + "'. Disponible: " + producto.getStockActual());
            }

            producto.setStockActual(producto.getStockActual() - itemDTO.getCantidad());
            productoRepository.save(producto);

            PedidoDetalle detalle = new PedidoDetalle();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(itemDTO.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecioVenta());

            detalleRepository.save(detalle);
        }

        Pedido creado = pedidoRepository.findById(pedido.getId()).orElseThrow();
        return toResponseDTO(creado);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> verColaPedidos() {
        return pedidoRepository.findColaPedidosActivos()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PedidoResponseDTO actualizarEstado(UUID id, String estado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + id));
        pedido.setEstado(EstadoPedido.valueOf(estado));
        pedidoRepository.save(pedido);
        return toResponseDTO(pedido);
    }

    private PedidoResponseDTO toResponseDTO(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setNumero(pedido.getNumero());
        dto.setCanal(pedido.getCanal());
        dto.setEstado(pedido.getEstado());
        dto.setPrioridad(pedido.getPrioridad().intValue());
        dto.setTotal(pedido.getTotal());
        dto.setFechaIngreso(pedido.getFechaIngreso());
        dto.setClienteNombre(pedido.getClienteNombre());
        return dto;
    }
}
