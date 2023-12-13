package com.dass21.crud.repository;

import com.dass21.crud.domain.Participante;
import com.dass21.crud.domain.Resposta;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RespostaRepository extends JpaRepository<Resposta, Long> {
    Resposta findByParticipante(Participante participante);
}
