package com.dass21.crud.dto;

import java.time.LocalDateTime;

public record ParticipanteRespostaDTO(Long participante_id, int idade, String genero, LocalDateTime data_resposta,
                                      int pontuacao_total_ansiedade, String escala_ansiedade,
                                      int pontuacao_total_depressao, String escala_depressao,
                                      int pontuacao_total_estresse, String escala_estresse) {

}
