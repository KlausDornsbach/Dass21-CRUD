package com.dass21.crud.dto;


import java.time.LocalDateTime;
public record ParticipanteRespostaDTO(Long participanteId, Integer idade, String genero, LocalDateTime dataResposta,
                                      Integer pontuacaoTotalAnsiedade, String escalaAnsiedade,
                                      Integer pontuacaoTotalDepressao, String escalaDepressao,
                                      Integer pontuacaoTotalEstresse, String escalaEstresse) {

}
