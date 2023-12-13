package com.dass21.crud.service;

import com.dass21.crud.domain.Participante;
import com.dass21.crud.repository.ParticipanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipanteService {
    @Autowired
    ParticipanteRepository participanteRepository;
    public Participante saveParticipante(Participante participante) {
        return participanteRepository.save(participante);
    }

    public Optional<Participante> getParticipante(Long id) {
        return participanteRepository.findParticipanteById(id);
    }

    public List<Participante> getAllParticipantes() {
        return participanteRepository.findAll();
    }
}
