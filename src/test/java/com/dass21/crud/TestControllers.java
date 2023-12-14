package com.dass21.crud;

import com.dass21.crud.controller.FullController;
import com.dass21.crud.controller.ParticipanteController;
import com.dass21.crud.controller.RespostaController;
import com.dass21.crud.domain.Condicao;
import com.dass21.crud.domain.Participante;
import com.dass21.crud.domain.Resposta;
import com.dass21.crud.dto.FormDTO;
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
    FullController fullController;
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
        ResponseEntity<List<ParticipanteRespostaDTO>> response = fullController.handleGetAllParticipantesRespostas();

        assertEquals(response.getStatusCode(), HttpStatus.ACCEPTED);

        ParticipanteRespostaDTO participanteRespostaDTO = new ParticipanteRespostaDTO(
                savedParticipante.getId(), savedParticipante.getIdade(), savedParticipante.getGenero(), savedResposta.getDataResposta(),
                0, respostaService.getScale(Condicao.ANSIEDADE, savedResposta.getPontuacaoTotalAnsiedade()),
                0, respostaService.getScale(Condicao.DEPRESSAO, savedResposta.getPontuacaoTotalDepressao()),
                0, respostaService.getScale(Condicao.ESTRESSE, savedResposta.getPontuacaoTotalEstresse()));
        ParticipanteRespostaDTO returned = Objects.requireNonNull(response.getBody()).get(0);

        // only test some random properties, can't test the whole thing because localDateTime floating point precision
        // gets in the way
        assertEquals(returned.participanteId(), participanteRespostaDTO.participanteId());
        assertEquals(returned.idade(), participanteRespostaDTO.idade());
        assertEquals(returned.pontuacaoTotalAnsiedade(), participanteRespostaDTO.pontuacaoTotalAnsiedade());
        assertEquals(returned.escalaDepressao(), participanteRespostaDTO.escalaDepressao());
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
        ResponseEntity<List<ParticipanteRespostaDTO>> response = fullController.handleGetAllParticipantesRespostas();

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
        assertEquals(returned.pontuacaoTotalDepressao(), savedResposta.getPontuacaoTotalDepressao());
    }

    @Test
    void getSpecificParticipanteDoesNotExist() {
        ResponseEntity<ParticipanteRespostaDTO> response = participanteController.handleGetSingleParticipante(0L);

        // verification
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void addParticipanteWithForm() {
        long countBeforeParticipantes = participanteRepository.count();
        long countBeforeRespostas = respostaRepository.count();
        FormDTO formDTO = new FormDTO("test_name", 1, "test_gender", 0, 0, 0);
        ResponseEntity<String> response = fullController.handlePostForm(formDTO);

        assertEquals(response.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals(countBeforeParticipantes + 1, participanteRepository.count());
        assertEquals(countBeforeRespostas + 1, respostaRepository.count());
    }

    @Test
    void addMultipleRespostasReturnsLastOne() {
        ParticipanteDTO participanteDTO = new ParticipanteDTO("user", 20, "Masculino");
        Participante savedParticipante = participanteRepository.save(new Participante(participanteDTO));

        RespostaDTO respostaDTO = new RespostaDTO(savedParticipante.getId(), 0, 0, 0);
        respostaController.handleAddResposta(respostaDTO);

        RespostaDTO respostaDTO2 = new RespostaDTO(savedParticipante.getId(), 10, 10, 10);
        respostaController.handleAddResposta(respostaDTO2);

        ResponseEntity<ParticipanteRespostaDTO> response = participanteController.handleGetSingleParticipante(savedParticipante.getId());
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(10, response.getBody().pontuacaoTotalDepressao());
        assertEquals(10, response.getBody().pontuacaoTotalAnsiedade());
        assertEquals(10, response.getBody().pontuacaoTotalEstresse());
    }


    @Test
    void updateParticipanteWithForm() {
        FormDTO formDTO = new FormDTO("test_name", 1, "test_gender", 0, 0, 0);
        fullController.handlePostForm(formDTO);

        FormDTO formDTO1 = new FormDTO("other_gender");
        Long id = participanteRepository.findAll().get(0).getId();
        ResponseEntity<Object> response = fullController.handlePutForm(id, formDTO1);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        List<Object> returned = (List<Object>) response.getBody();
        Participante p = (Participante) returned.get(0);
        assertEquals("other_gender", p.getGenero());
    }

    @Test
    void updateRespostaWithForm() {
        FormDTO formDTO = new FormDTO("test_name", 1, "test_gender", 0, 0, 0);
        fullController.handlePostForm(formDTO);

        FormDTO formDTO1 = new FormDTO(10);
        Long id = participanteRepository.findAll().get(0).getId();
        ResponseEntity<Object> response = fullController.handlePutForm(id, formDTO1);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        List<Object> returned = (List<Object>) response.getBody();
        Resposta r = (Resposta) returned.get(1);
        assertEquals(10, r.getPontuacaoTotalEstresse());
    }
}
