package java10x.EventClean.core.entities;

import java10x.EventClean.core.enums.TipoEvento;

import java.time.LocalDateTime;

public record Evento(Long id,
                     String nome,
                     String descricao,
                     LocalDateTime dataInicio,
                     LocalDateTime dataFim,
                     String identificador,
                     String localEvento,
                     String organizador,
                     TipoEvento tipoEvento) {
}
