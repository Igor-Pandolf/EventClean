package java10x.EventClean.infra.dtos;

import java10x.EventClean.core.enums.TipoEvento;

import java.time.LocalDateTime;

public record EventoDto(Long id,
                        String nome,
                        String descricao,
                        LocalDateTime dataInicio,
                        LocalDateTime dataFim,
                        String identificador,
                        String localEvento,
                        String organizador,
                        Integer capacidade,
                        TipoEvento tipoEvento) {
}
