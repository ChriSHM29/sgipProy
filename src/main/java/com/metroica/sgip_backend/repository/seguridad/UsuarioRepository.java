package com.metroica.sgip_backend.repository.seguridad;

import com.metroica.sgip_backend.model.seguridad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    // Esencial para que Spring Security busque al usuario al hacer Login
    Optional<Usuario> findByEmail(String email);
}