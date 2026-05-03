package com.metroica.sgip_backend.productos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "proveedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "El nombre del proveedor es obligatorio")
    private String nombre;

    @Column(unique = true, length = 11)
    @Size(min = 11, max = 11, message = "El RUC debe tener 11 digitos")
    private String ruc;

    private String contacto;
    private String telefono;
    private String email;
    private String direccion;

    @Column(name = "lead_time_dias", nullable = false)
    private Integer leadTimeDias = 3;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
