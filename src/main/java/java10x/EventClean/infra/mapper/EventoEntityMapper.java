package java10x.EventClean.infra.mapper;

import java10x.EventClean.core.entities.Evento;
import java10x.EventClean.infra.dtos.EventoDto;
import java10x.EventClean.infra.persistence.EventoEntity;
import org.springframework.stereotype.Component;

@Component
public class EventoEntityMapper {

    public EventoEntity toEntity(Evento evento) {
        return new EventoEntity(
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

    public Evento toDomain(EventoEntity evento) {
        return new Evento(
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
