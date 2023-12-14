package com.dass21.crud.controller;

import com.dass21.crud.domain.Condicao;
import com.dass21.crud.domain.Participante;
import com.dass21.crud.domain.Resposta;
import com.dass21.crud.dto.ParticipanteDTO;
import com.dass21.crud.dto.ParticipanteRespostaDTO;
import com.dass21.crud.service.ParticipanteService;
import com.dass21.crud.service.RespostaService;
import com.dass21.crud.util.MyUtils;
import org.springframework.beans.BeanUtils;
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

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<String> handlePutParticipante(@PathVariable Long id, @RequestBody ParticipanteDTO participante) {
        Optional<Participante> optionalParticipante = participanteService.getParticipante(id);
        if (optionalParticipante.isEmpty()) {
            return new ResponseEntity<>("nao foi achado um participante com o id especificado", HttpStatus.BAD_REQUEST);
        }
        Participante participanteDb = optionalParticipante.get();
        BeanUtils.copyProperties(participante, participanteDb, MyUtils.getNullPropertyNames(participante));
        participanteService.saveParticipante(participanteDb);
        return new ResponseEntity<>("Participante atualizado", HttpStatus.ACCEPTED);
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
