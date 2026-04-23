package com.metroica.sgip_backend.model.operativas;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SERIAL en Postgres
    private Integer id;

    @Column(nullable = false, unique = true, length = 120)
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String nombre;

    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "padre_id")
    private Categoria padre; // Relación recursiva para subcategorías

    @Column(nullable = false)
    private Boolean activa = true;
}