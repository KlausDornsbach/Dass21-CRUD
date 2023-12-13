package com.dass21.crud.dto;


import java.time.LocalDateTime;
public record ParticipanteRespostaDTO(Long participante_id, Integer idade, String genero, LocalDateTime data_resposta,
                                      Integer pontuacao_total_ansiedade, String escala_ansiedade,
                                      Integer pontuacao_total_depressao, String escala_depressao,
                                      Integer pontuacao_total_estresse, String escala_estresse) {

}
