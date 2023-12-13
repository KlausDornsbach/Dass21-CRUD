package com.dass21.crud.repository;

import com.dass21.crud.domain.Participante;
import com.dass21.crud.domain.Resposta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RespostaRepository extends JpaRepository<Resposta, Long> {
    Optional<Resposta> findByParticipante(Participante participante);
    Optional<Resposta> findRespostaById(Long id);
}
