package com.metroica.sgip_backend.productos;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarInventario() {
        return productoRepository.findAll()
                .stream()
                .map(productoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductoResponseDTO crearProducto(ProductoCreateDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con ID: " + dto.getCategoriaId()));
        Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + dto.getProveedorId()));

        if (productoRepository.findBySku(dto.getSku()).isPresent()) {
            throw new RuntimeException("Ya existe un producto con el SKU: " + dto.getSku());
        }

        Producto producto = productoMapper.toEntity(dto);
        producto.setCategoria(categoria);
        producto.setProveedor(proveedor);

        productoRepository.save(producto);
        return productoMapper.toResponseDTO(producto);
    }

    @Transactional
    public ProductoResponseDTO actualizarProducto(UUID id, ProductoCreateDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        producto.setSku(dto.getSku());
        producto.setNombre(dto.getNombre());
        producto.setPrecioCosto(dto.getPrecioCosto());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setStockActual(dto.getStockActual());
        producto.setPuntoPedido(dto.getPuntoPedido());

        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoria no encontrada con ID: " + dto.getCategoriaId()));
            producto.setCategoria(categoria);
        }
        if (dto.getProveedorId() != null) {
            Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + dto.getProveedorId()));
            producto.setProveedor(proveedor);
        }

        productoRepository.save(producto);
        return productoMapper.toResponseDTO(producto);
    }

    @Transactional
    public void eliminarProducto(UUID id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        productoRepository.delete(producto);
    }
}
