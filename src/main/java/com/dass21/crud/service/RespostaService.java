package com.dass21.crud.service;

import com.dass21.crud.domain.Condicao;
import com.dass21.crud.domain.Participante;
import com.dass21.crud.domain.Resposta;
import com.dass21.crud.dto.RespostaDTO;
import com.dass21.crud.repository.RespostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RespostaService {
    @Autowired
    RespostaRepository respostaRepository;
    public void saveResposta(Resposta resposta) {
        respostaRepository.save(resposta);
    }

    public Optional<Resposta> getResposta(Long id) {
        return respostaRepository.findRespostaById(id);
    }

    // this method returns the last Resposta on the DB based on data_resposta
    public Optional<Resposta> findRespostaByParticipante(Participante p) {
        ArrayList<Resposta> respostas = respostaRepository.findByParticipante(p);
        Collections.sort(respostas);
        if (respostas.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(respostas.get(respostas.size() - 1));
    }

    public boolean verifyScoreValidity(RespostaDTO respostaDTO) {
        return respostaDTO.pontuacaoTotalAnsiedade() >= 0
                && respostaDTO.pontuacaoTotalDepressao() >= 0
                && respostaDTO.pontuacaoTotalEstresse() >= 0;
    }

    public String getScale(Condicao condicao, int score) {
        List<String> symptomsArray = Arrays.asList(
                "0 - Sem sintomas",
                "1 - Sintomas leves",
                "2 - Sintomas moderados",
                "3 - Sintomas graves",
                "4 - Sintomas muito graves"
        );
        String stringScale = "";
        switch (condicao) {
            case DEPRESSAO:
                if (0 <= score && score <= 4) {
                    stringScale = symptomsArray.get(0);
                } else if (5 <= score && score <= 6) {
                    stringScale = symptomsArray.get(1);
                } else if (7 <= score && score <= 10) {
                    stringScale = symptomsArray.get(2);
                } else if (11 <= score && score <= 13) {
                    stringScale = symptomsArray.get(3);
                } else if (score >= 14) {
                    stringScale = symptomsArray.get(4);
                }
                break;
            case ANSIEDADE:
                if (0 <= score && score <= 3) {
                    stringScale = symptomsArray.get(0);
                } else if (4 <= score && score <= 5) {
                    stringScale = symptomsArray.get(1);
                } else if (6 <= score && score <= 7) {
                    stringScale = symptomsArray.get(2);
                } else if (8 <= score && score <= 9) {
                    stringScale = symptomsArray.get(3);
                } else if (score >= 10) {
                    stringScale = symptomsArray.get(4);
                }
                break;
            case ESTRESSE:
                if (0 <= score && score <= 7) {
                    stringScale = symptomsArray.get(0);
                } else if (8 <= score && score <= 9) {
                    stringScale = symptomsArray.get(1);
                } else if (10 <= score && score <= 12) {
                    stringScale = symptomsArray.get(2);
                } else if (13 <= score && score <= 16) {
                    stringScale = symptomsArray.get(3);
                } else if (score >= 17) {
                    stringScale = symptomsArray.get(4);
                }
                break;
        }
        return stringScale;
    }
}
