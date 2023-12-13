package com.dass21.crud;

import com.dass21.crud.controller.ParticipanteController;
import com.dass21.crud.controller.RespostaController;
import com.dass21.crud.domain.Condicao;
import com.dass21.crud.domain.Participante;
import com.dass21.crud.domain.Resposta;
import com.dass21.crud.dto.ParticipanteDTO;
import com.dass21.crud.dto.ParticipanteRespostaDTO;
import com.dass21.crud.dto.RespostaDTO;
import com.dass21.crud.repository.ParticipanteRepository;
import com.dass21.crud.repository.RespostaRepository;
import com.dass21.crud.service.RespostaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TestControllers {
    @Autowired
    ParticipanteController participanteController;
    @Autowired
    RespostaController respostaController;
    @Autowired
    RespostaRepository respostaRepository;
    @Autowired
    ParticipanteRepository participanteRepository;
    @Autowired
    RespostaService respostaService;

    @BeforeEach
    void resetDB() {
        respostaRepository.deleteAll();
        participanteRepository.deleteAll();
    }

    @Test
    void addParticipanteShouldAddToDB() {
        long beforeCount = participanteRepository.count();
        ParticipanteDTO participanteDTO = new ParticipanteDTO("user", 20, "Masculino");
        participanteController.handleAddParticipante(participanteDTO);
        assertEquals(beforeCount + 1, participanteRepository.count());
    }

    @Test
    void getAllParticipantesWorksWithOne() {
        // setup
        ParticipanteDTO participanteDTO = new ParticipanteDTO("user", 20, "Masculino");
        Participante savedParticipante = participanteRepository.save(new Participante(participanteDTO));

        RespostaDTO respostaDTO = new RespostaDTO(savedParticipante.getId(), 0, 0, 0);
        Resposta savedResposta = respostaRepository.save(new Resposta(savedParticipante, respostaDTO));

        // verification
        ResponseEntity<List<ParticipanteRespostaDTO>> response = participanteController.handleGetAllParticipantesRespostas();

        assertEquals(response.getStatusCode(), HttpStatus.ACCEPTED);

        ParticipanteRespostaDTO participanteRespostaDTO = new ParticipanteRespostaDTO(
                savedParticipante.getId(), savedParticipante.getIdade(), savedParticipante.getGenero(), savedResposta.getDataResposta(),
                0, respostaService.getScale(Condicao.ANSIEDADE, savedResposta.getPontuacaoTotalAnsiedade()),
                0, respostaService.getScale(Condicao.DEPRESSAO, savedResposta.getPontuacaoTotalDepressao()),
                0, respostaService.getScale(Condicao.ESTRESSE, savedResposta.getPontuacaoTotalEstresse()));
        ParticipanteRespostaDTO returned = Objects.requireNonNull(response.getBody()).get(0);

        // only test some random properties, can't test the whole thing because localDateTime floating point precision
        // gets in the way
        assertEquals(returned.participante_id(), participanteRespostaDTO.participante_id());
        assertEquals(returned.idade(), participanteRespostaDTO.idade());
        assertEquals(returned.pontuacao_total_ansiedade(), participanteRespostaDTO.pontuacao_total_ansiedade());
        assertEquals(returned.escala_depressao(), participanteRespostaDTO.escala_depressao());
    }

    @Test
    void getAllParticipantesShouldNotFailIfAParticipanteHasNoResposta() {
        // setup
        ParticipanteDTO participanteDTO = new ParticipanteDTO("user", 20, "Masculino");
        ParticipanteDTO participanteDTO2 = new ParticipanteDTO("user1", 21, "Feminino");
        Participante savedParticipante = participanteRepository.save(new Participante(participanteDTO));
        participanteRepository.save(new Participante(participanteDTO2));
        RespostaDTO respostaDTO = new RespostaDTO(savedParticipante.getId(), 0, 0, 0);
        respostaRepository.save(new Resposta(savedParticipante, respostaDTO));

        // verification
        ResponseEntity<List<ParticipanteRespostaDTO>> response = participanteController.handleGetAllParticipantesRespostas();

        assertEquals(response.getStatusCode(), HttpStatus.ACCEPTED);
    }

    @Test
    void updateSpecificParticipante() {
        // setup
        ParticipanteDTO participanteDTO = new ParticipanteDTO("user", 20, "Masculino");
        Participante savedParticipante = participanteRepository.save(new Participante(participanteDTO));
        long beforeCount = participanteRepository.count();

        ParticipanteDTO newParticipanteDTO = new ParticipanteDTO("newUser", 30, "Feminino");
        ResponseEntity<String> response = participanteController.handlePutParticipante(savedParticipante.getId(),
                newParticipanteDTO);

        Participante newlySaved = participanteRepository.findParticipanteById(savedParticipante.getId()).get();

        // verification
        assertEquals(response.getStatusCode(), HttpStatus.ACCEPTED);

        // asserts we don't save nor delete no more participantes
        assertEquals(beforeCount, participanteRepository.count());

        assertEquals(newlySaved.getGenero(), newParticipanteDTO.genero());
        assertEquals(newlySaved.getIdade(), newParticipanteDTO.idade());
        assertEquals(newlySaved.getNome(), newParticipanteDTO.nome());
    }

    @Test
    void updateSpecificResposta() {
        ParticipanteDTO participanteDTO = new ParticipanteDTO("user", 20, "Masculino");
        Participante savedParticipante = participanteRepository.save(new Participante(participanteDTO));

        RespostaDTO respostaDTO = new RespostaDTO(savedParticipante.getId(), 0, 0, 0);
        Resposta savedResposta = respostaRepository.save(new Resposta(savedParticipante, respostaDTO));
        long beforeCount = respostaRepository.count();

        RespostaDTO newRespostaDTO = new RespostaDTO(savedParticipante.getId(), 10, 10, 10);

        ResponseEntity<String> response = respostaController.handlePutResposta(savedResposta.getId(),
                newRespostaDTO);

        Resposta newlySaved = respostaRepository.findRespostaById(savedResposta.getId()).get();

        // verification
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

        // asserts we don't save nor delete no more participantes
        assertEquals(beforeCount, respostaRepository.count());

        assertEquals(newlySaved.getParticipante().getId(), newRespostaDTO.id());
        assertEquals(newlySaved.getPontuacaoTotalEstresse(), newRespostaDTO.pontuacaoTotalEstresse());
        assertEquals(newlySaved.getPontuacaoTotalDepressao(), newRespostaDTO.pontuacaoTotalDepressao());
        assertEquals(newlySaved.getPontuacaoTotalAnsiedade(), newRespostaDTO.pontuacaoTotalAnsiedade());
    }

    @Test
    void getSpecificParticipante() {
        ParticipanteDTO participanteDTO = new ParticipanteDTO("user", 20, "Masculino");
        Participante savedParticipante = participanteRepository.save(new Participante(participanteDTO));

        RespostaDTO respostaDTO = new RespostaDTO(savedParticipante.getId(), 0, 0, 0);
        Resposta savedResposta = respostaRepository.save(new Resposta(savedParticipante, respostaDTO));

        ResponseEntity<ParticipanteRespostaDTO> response = participanteController.handleGetSingleParticipante(savedParticipante.getId());

        // verification
        assertEquals(response.getStatusCode(), HttpStatus.ACCEPTED);
        ParticipanteRespostaDTO returned = Objects.requireNonNull(response.getBody());
        assertEquals(returned.idade(), savedParticipante.getIdade());
        assertEquals(returned.pontuacao_total_depressao(), savedResposta.getPontuacaoTotalDepressao());
    }

    @Test
    void getSpecificParticipanteDoesNotExist() {
        ResponseEntity<ParticipanteRespostaDTO> response = participanteController.handleGetSingleParticipante(0L);

        // verification
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
