package java10x.EventClean.core.entities;

import java10x.EventClean.core.enums.TipoEvento;

import java.time.LocalDateTime;

public record Evento(Long id,
                     String nome,
                     String descricao,
                     LocalDateTime inicio,
                     LocalDateTime fim,
                     String identificador,
                     String local,
                     String organizador,
                     TipoEvento tipoEvento) {
}
