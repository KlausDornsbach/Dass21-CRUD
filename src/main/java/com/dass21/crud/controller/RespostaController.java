package com.dass21.crud.controller;

import com.dass21.crud.domain.Participante;
import com.dass21.crud.domain.Resposta;
import com.dass21.crud.dto.RespostaDTO;
import com.dass21.crud.service.ParticipanteService;
import com.dass21.crud.service.RespostaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                return new ResponseEntity<>("Participante com id "+
                        newResposta.id().toString() +" Nao existe, resposta nao salva na DB", HttpStatus.BAD_REQUEST);
            }

            resposta = new Resposta(participante.get(), newResposta);
            respostaService.saveResposta(resposta);
        } catch (Exception e) {
            return new ResponseEntity<>("Nao foi possivel criar a resposta na DB", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Sucesso na criacao de resposta, id: " + resposta.getId(), HttpStatus.ACCEPTED);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<String> handlePutResposta(@PathVariable Long id, @RequestBody RespostaDTO respostaDTO) {
        Optional<Resposta> respostaDb = respostaService.getResposta(id);
        if (respostaDb.isEmpty()) {
            return new ResponseEntity<>("Nao foi achado uma resposta com o id especificado", HttpStatus.BAD_REQUEST);
        }
        respostaDb.get().update(respostaDTO);
        respostaService.saveResposta(respostaDb.get());
        return new ResponseEntity<>("Resposta atualizada", HttpStatus.ACCEPTED);
    }
}
