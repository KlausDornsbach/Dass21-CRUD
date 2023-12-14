package com.dass21.crud.controller;

import com.dass21.crud.domain.Participante;
import com.dass21.crud.domain.Resposta;
import com.dass21.crud.dto.FormDTO;
import com.dass21.crud.dto.ParticipanteRespostaDTO;
import com.dass21.crud.service.ParticipanteService;
import com.dass21.crud.service.RespostaService;
import com.dass21.crud.util.MyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class FullController {
    @Autowired
    ParticipanteService participanteService;
    @Autowired
    RespostaService respostaService;
    @PostMapping("/adicionar")
    public ResponseEntity<String> handlePostForm(@RequestBody FormDTO form) {
        Participante participante = new Participante(form.nome(), form.idade(), form.genero());
        participante = participanteService.saveParticipante(participante);
        Resposta resposta = new Resposta(participante, form.pontuacaoTotalAnsiedade(),
                form.pontuacaoTotalDepressao(), form.pontuacaoTotalEstresse());
        respostaService.saveResposta(resposta);

        return new ResponseEntity<>("Cadastrou resposta para participante de id: " + participante.getId(), HttpStatus.ACCEPTED);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> handlePutForm(@PathVariable Long id, @RequestBody FormDTO form) {
        Optional<Participante> optionalParticipante = participanteService.getParticipante(id);
        if (optionalParticipante.isEmpty()) {
            return new ResponseEntity<>("Nao achou participante de id " + id, HttpStatus.BAD_REQUEST);
        }
        Optional<Resposta> optionalResposta = respostaService.findRespostaByParticipante(optionalParticipante.get());
        if (optionalResposta.isEmpty()) {
            return new ResponseEntity<>("Nao achou resposta associada ao participante", HttpStatus.BAD_REQUEST);
        }
        Participante participante = optionalParticipante.get();
        Resposta resposta = optionalResposta.get();
        BeanUtils.copyProperties(form, participante, MyUtils.getNullPropertyNames(form));
        BeanUtils.copyProperties(form, resposta, MyUtils.getNullPropertyNames(form));
        participanteService.saveParticipante(participante);
        respostaService.saveResposta(resposta);
        return new ResponseEntity<>(List.of(participante, resposta), HttpStatus.ACCEPTED);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<ParticipanteRespostaDTO>> handleGetAllParticipantesRespostas() {
        List<Participante> participantes = participanteService.getAllParticipantes();
        List<ParticipanteRespostaDTO> respostas;
        try {
            respostas = participanteService.getAllParticipanteRespostaDTO(participantes);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(respostas, HttpStatus.ACCEPTED);
    }

}
