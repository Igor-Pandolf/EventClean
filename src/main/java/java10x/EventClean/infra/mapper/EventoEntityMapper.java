package java10x.EventClean.infra.mapper;

import java10x.EventClean.core.entities.Evento;
import java10x.EventClean.infra.dtos.EventoDto;
import java10x.EventClean.infra.persistence.EventoEntity;
import org.springframework.stereotype.Component;

@Component
public class EventoEntityMapper {

    public EventoEntity toEntity(EventoDto eventoDto) {
        return new EventoEntity(
                eventoDto.id(),
                eventoDto.nome(),
                eventoDto.descricao(),
                eventoDto.dataInicio(),
                eventoDto.dataFim(),
                eventoDto.identificador(),
                eventoDto.localEvento(),
                eventoDto.organizador(),
                eventoDto.capacidade(),
                eventoDto.tipoEvento()
        );
    }

    public EventoDto toDto(EventoEntity evento) {
        return new EventoDto(
                evento.getId(),
                evento.getNome(),
                evento.getDescricao(),
                evento.getDataInicio(),
                evento.getDataFim(),
                evento.getIdentificador(),
                evento.getLocalEvento(),
                evento.getOrganizador(),
                evento.getCapacidade(),
                evento.getTipoEvento()
        );
    }
}
