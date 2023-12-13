package com.dass21.crud.repository;

import com.dass21.crud.domain.Participante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
    Optional<Participante> findParticipanteById(Long id);
}
