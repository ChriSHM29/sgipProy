package com.metroica.sgip_backend.productos;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarInventario() {
        return ResponseEntity.ok(productoService.listarInventario());
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(@RequestBody @Valid ProductoCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crearProducto(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(@PathVariable UUID id, @RequestBody @Valid ProductoCreateDTO dto) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable UUID id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
