package com.metroica.sgip_backend.service.operativas;

import com.metroica.sgip_backend.dto.response.ProductoResponseDTO;
import com.metroica.sgip_backend.mapper.operativas.ProductoMapper;
import com.metroica.sgip_backend.repository.operativo.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    /**
     * Obtiene el listado completo de productos del inventario.
     * @Transactional(readOnly = true) optimiza el rendimiento porque le dice a
     * Postgres que no haremos INSERT ni UPDATE, solo lectura.
     */
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarInventario() {
        return productoRepository.findAll()
                .stream()
                .map(productoMapper::toResponseDTO) // Aquí entra en acción MapStruct
                .collect(Collectors.toList());
    }
}