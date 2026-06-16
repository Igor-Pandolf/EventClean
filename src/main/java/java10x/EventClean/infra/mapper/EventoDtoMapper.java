package java10x.EventClean.infra.mapper;

import java10x.EventClean.core.entities.Evento;
import java10x.EventClean.infra.dtos.EventoDto;
import java10x.EventClean.infra.persistence.EventoEntity;
import org.springframework.stereotype.Component;

@Component
public class EventoDtoMapper {

    public Evento toEntity(EventoDto eventoDto) {
            return new Evento(
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

    public EventoDto toDto(Evento evento) {
        return new EventoDto(
                evento.id(),
                evento.nome(),
                evento.descricao(),
                evento.dataInicio(),
                evento.dataFim(),
                evento.identificador(),
                evento.localEvento(),
                evento.organizador(),
                evento.capacidade(),
                evento.tipoEvento()
        );
    }
}
