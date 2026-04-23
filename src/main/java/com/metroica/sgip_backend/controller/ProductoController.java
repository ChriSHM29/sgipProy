package com.metroica.sgip_backend.controller;

import com.metroica.sgip_backend.dto.response.ProductoResponseDTO;
import com.metroica.sgip_backend.service.operativas.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // Indica que esta clase responderá con JSON (no con vistas HTML)
@RequestMapping("/api/v1/productos") // La URL base para acceder a este controlador
@RequiredArgsConstructor
public class ProductoController {

    // Inyectamos el servicio que acabamos de crear (nunca inyectamos el repositorio aquí)
    private final ProductoService productoService;

    /**
     * Endpoint para listar todo el inventario.
     * Se accede haciendo una petición GET a http://localhost:8080/api/v1/productos
     */
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarInventario() {
        // Llamamos a la lógica de negocio
        List<ProductoResponseDTO> inventario = productoService.listarInventario();

        // Devolvemos la lista empaquetada en una respuesta HTTP 200 (OK)
        return ResponseEntity.ok(inventario);
    }
}