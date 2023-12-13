package com.dass21.crud.service;

import com.dass21.crud.domain.Condicao;
import com.dass21.crud.domain.Participante;
import com.dass21.crud.domain.Resposta;
import com.dass21.crud.dto.ParticipanteRespostaDTO;
import com.dass21.crud.repository.ParticipanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipanteService {
    @Autowired
    ParticipanteRepository participanteRepository;

    @Autowired
    RespostaService respostaService;
    public Participante saveParticipante(Participante participante) {
        return participanteRepository.save(participante);
    }

    public Optional<Participante> getParticipante(Long id) {
        return participanteRepository.findParticipanteById(id);
    }

    public List<Participante> getAllParticipantes() {
        return participanteRepository.findAll();
    }

    public List<ParticipanteRespostaDTO> getAllParticipanteRespostaDTO(List<Participante> participantes) {
        return participantes.stream()
                .map(participante -> this.constructParticipanteRespostaDTO(participante,
                        respostaService.findRespostaByParticipante(participante).get()))
                .toList();
    }

    public ParticipanteRespostaDTO constructParticipanteRespostaDTO(Participante participante, Resposta resposta) {
        return new ParticipanteRespostaDTO(
                participante.getId(), participante.getIdade(), participante.getGenero(), resposta.getDataResposta(),
                resposta.getPontuacaoTotalAnsiedade(),
                respostaService.getScale(Condicao.ANSIEDADE, resposta.getPontuacaoTotalAnsiedade()),
                resposta.getPontuacaoTotalDepressao(),
                respostaService.getScale(Condicao.DEPRESSAO, resposta.getPontuacaoTotalDepressao()),
                resposta.getPontuacaoTotalEstresse(),
                respostaService.getScale(Condicao.ESTRESSE, resposta.getPontuacaoTotalEstresse())
        );
    }
}
