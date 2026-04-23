package com.metroica.sgip_backend.model.operativas;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

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
    @Size(min = 11, max = 11, message = "El RUC debe tener 11 dígitos")
    private String ruc; // Validación para RUC peruano

    private String contacto;
    private String telefono;
    private String email;
    private String direccion;

    @Column(name = "lead_time_dias", nullable = false) // Asumiendo que así se llama en Postgres, o borra el name si se llama lead_time_dias automáticamente
    private Integer leadTimeDias = 3;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}