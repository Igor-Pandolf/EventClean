package java10x.EventClean.infra.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java10x.EventClean.core.enums.TipoEvento;

import java.time.LocalDateTime;

@Schema(description = "Objeto de transferência de dados (DTO) que representa um evento")
public record EventoDto(
        @Schema(description = "Identificador único do evento gerado pelo banco de dados", example = "1")
        Long id,

        @Schema(description = "Nome do evento", example = "Workshop de Clean Architecture")
        String nome,

        @Schema(description = "Descrição detalhada do evento", example = "Aprenda a estruturar seu código Java seguindo os princípios de Clean Architecture.")
        String descricao,

        @Schema(description = "Data e hora de início do evento", example = "2026-06-20T09:00:00")
        LocalDateTime dataInicio,

        @Schema(description = "Data e hora de término do evento", example = "2026-06-20T17:00:00")
        LocalDateTime dataFim,

        @Schema(description = "Identificador único do evento gerado automaticamente (UUID/Hash)", example = "evt_9b1deb4d")
        String identificador,

        @Schema(description = "Local onde o evento será realizado", example = "Auditório Principal ou Online")
        String localEvento,

        @Schema(description = "Nome do organizador do evento", example = "Java10x Team")
        String organizador,

        @Schema(description = "Capacidade máxima de participantes", example = "150")
        Integer capacidade,

        @Schema(description = "Tipo do evento (WORKSHOP, PALESTRA, MUSICA, SEMINARIO)", example = "WORKSHOP")
        TipoEvento tipoEvento) {
}
