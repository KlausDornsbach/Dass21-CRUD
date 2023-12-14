package com.dass21.crud.controller;

import com.dass21.crud.domain.Participante;
import com.dass21.crud.domain.Resposta;
import com.dass21.crud.dto.RespostaDTO;
import com.dass21.crud.service.ParticipanteService;
import com.dass21.crud.service.RespostaService;
import com.dass21.crud.util.MyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
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

    @PutMapping("/atualizar")
    public ResponseEntity<String> handlePutResposta(@RequestBody RespostaDTO respostaDTO) {
        Optional<Participante> participante = participanteService.getParticipante(respostaDTO.id());
        Optional<Resposta> respostaOptional = respostaService.findRespostaByParticipante(participante.get());
        if (respostaOptional.isEmpty()) {
            return new ResponseEntity<>("Nao foi achado uma resposta com o id especificado", HttpStatus.BAD_REQUEST);
        }
        Resposta respostaDb = respostaOptional.get();
        BeanUtils.copyProperties(respostaDTO, respostaDb, MyUtils.getNullPropertyNames(respostaDTO));
        respostaService.saveResposta(respostaDb);
        return new ResponseEntity<>("Resposta atualizada", HttpStatus.ACCEPTED);
    }
}
