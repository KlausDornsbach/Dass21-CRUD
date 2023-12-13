package com.dass21.crud.controller;

import com.dass21.crud.domain.Participante;
import com.dass21.crud.domain.Resposta;
import com.dass21.crud.dto.RespostaDTO;
import com.dass21.crud.service.ParticipanteService;
import com.dass21.crud.service.RespostaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/resposta")
public class RespostaController {
    @Autowired
    RespostaService respostaService;
    @Autowired
    ParticipanteService participanteService;

    @PostMapping("/adicionar")
    public ResponseEntity<String> handleAddResposta(@RequestBody RespostaDTO newResposta) {
        Resposta resposta;
        try {
            Optional<Participante> participante = participanteService.getParticipante(newResposta.id());
            if (participante.isEmpty()) {
                return new ResponseEntity<>("participante com id "+
                        newResposta.id().toString() +" nao existe, resposta nao salva na DB", HttpStatus.BAD_REQUEST);
            }

            resposta = new Resposta(participante.get(), newResposta);
            respostaService.saveResposta(resposta);
        } catch (Exception e) {
            return new ResponseEntity<>("nao pode criar a resposta na DB", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("sucesso na criacao de resposta, id: " + resposta.getId(), HttpStatus.ACCEPTED);
    }
}
