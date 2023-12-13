package com.dass21.crud.domain;

import com.dass21.crud.dto.ParticipanteDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "participantes")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Participante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private int idade;

    @Column(nullable = false)
    private String genero;

    public Participante(ParticipanteDTO participanteDTO) {
        this.nome = participanteDTO.nome();
        this.idade = participanteDTO.idade();
        this.genero = participanteDTO.genero();
    }
}
