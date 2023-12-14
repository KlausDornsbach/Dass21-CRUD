package com.dass21.crud.domain;

import com.dass21.crud.dto.RespostaDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "respostas")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resposta implements Comparable<Resposta> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "participante_id")
    private Participante participante;

    @Column(name = "data_resposta", nullable = false)
    private LocalDateTime dataResposta;

    @Override
    public int compareTo(Resposta r) {
        return getDataResposta().compareTo(r.getDataResposta());
    }

    @Column(name = "pontuacao_total_depressao", nullable = false)
    private int pontuacaoTotalDepressao;

    @Column(name = "pontuacao_total_ansiedade", nullable = false)
    private int pontuacaoTotalAnsiedade;

    @Column(name = "pontuacao_total_estresse", nullable = false)
    private int pontuacaoTotalEstresse;

    public Resposta(Participante participante, RespostaDTO respostaDTO) {
        this.participante = participante;
        this.dataResposta = LocalDateTime.now();
        this.pontuacaoTotalAnsiedade = respostaDTO.pontuacaoTotalAnsiedade();
        this.pontuacaoTotalDepressao = respostaDTO.pontuacaoTotalDepressao();
        this.pontuacaoTotalEstresse = respostaDTO.pontuacaoTotalEstresse();
    }

    public Resposta(Participante participante, Integer ansiedade, Integer depressao, Integer estresse) {
        this.participante = participante;
        this.dataResposta = LocalDateTime.now();
        this.pontuacaoTotalAnsiedade = ansiedade;
        this.pontuacaoTotalDepressao = depressao;
        this.pontuacaoTotalEstresse = estresse;
    }

    public void update(RespostaDTO respostaDTO) {
        this.dataResposta = LocalDateTime.now();
        this.pontuacaoTotalAnsiedade = respostaDTO.pontuacaoTotalAnsiedade();
        this.pontuacaoTotalDepressao = respostaDTO.pontuacaoTotalDepressao();
        this.pontuacaoTotalEstresse = respostaDTO.pontuacaoTotalEstresse();
    }
}

