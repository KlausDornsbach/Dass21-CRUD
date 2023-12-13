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
import java.util.Optional;

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
            respostas = participanteService.getAllParticipanteRespostaDTO(participantes);
        } catch (Exception e) {
            throw e;
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(respostas, HttpStatus.ACCEPTED);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<String> handlePutParticipante(@PathVariable Long id, @RequestBody ParticipanteDTO participante) {
        Optional<Participante> participanteDb = participanteService.getParticipante(id);
        if (participanteDb.isEmpty()) {
            return new ResponseEntity<>("nao foi achado um participante com o id especificado", HttpStatus.BAD_REQUEST);
        }
        participanteDb.get().update(participante);
        participanteService.saveParticipante(participanteDb.get());
        return new ResponseEntity<>("Participante e resposta atualizados", HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipanteRespostaDTO> handleGetSingleParticipante(@PathVariable Long id) {
        Optional<Participante> participanteDb = participanteService.getParticipante(id);

        if (participanteDb.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        Optional<Resposta> respostaDb = respostaService.findRespostaByParticipante(participanteDb.get());

        if (respostaDb.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        ParticipanteRespostaDTO response = participanteService.constructParticipanteRespostaDTO(participanteDb.get(), respostaDb.get());
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
