package com.dass21.crud.controller;

import com.dass21.crud.domain.Condicao;
import com.dass21.crud.domain.Participante;
import com.dass21.crud.domain.Resposta;
import com.dass21.crud.dto.ParticipanteDTO;
import com.dass21.crud.dto.ParticipanteRespostaDTO;
import com.dass21.crud.service.ParticipanteService;
import com.dass21.crud.service.RespostaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participante")
public class ParticipanteController {
    @Autowired
    ParticipanteService participanteService;

    @Autowired
    RespostaService respostaService;

    @PostMapping("/adicionar")
    public ResponseEntity<String> handleAddParticipante(@RequestBody ParticipanteDTO newParticipante) {
        Participante participante = new Participante(newParticipante);
        try {
            participante = participanteService.saveParticipante(participante);
        } catch (Exception e) {
            return new ResponseEntity<>("erro, participante nao salvo", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("criou participante com id: " + participante.getId(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ParticipanteRespostaDTO>> handleGetAllParticipantesRespostas() {
        List<Participante> participantes = participanteService.getAllParticipantes();
        List<ParticipanteRespostaDTO> respostas;
        try {
            respostas = participantes.stream()
                    .map(participante -> this.constructParticipanteRespostaDTO(participante, respostaService.findRespostaByParticipante(participante))).toList();
        } catch (Exception e) {
            throw e;
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(respostas, HttpStatus.ACCEPTED);
    }

    private ParticipanteRespostaDTO constructParticipanteRespostaDTO(Participante participante, Resposta resposta) {
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
