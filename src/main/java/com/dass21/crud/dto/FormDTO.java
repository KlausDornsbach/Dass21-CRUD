package com.dass21.crud.dto;

import lombok.Data;

public record FormDTO(String nome, Integer idade, String genero, Integer pontuacaoTotalAnsiedade,
                      Integer pontuacaoTotalDepressao, Integer pontuacaoTotalEstresse) {

    public FormDTO(String genero) {
        this(null, null, genero, null, null, null);
    }

    public FormDTO(Integer pontuacaoTotalEstresse) {
        this(null, null, null, null, null, pontuacaoTotalEstresse);
    }
}
