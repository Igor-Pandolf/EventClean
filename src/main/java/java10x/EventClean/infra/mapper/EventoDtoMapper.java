package java10x.EventClean.infra.mapper;

import java10x.EventClean.infra.dtos.EventoDto;
import java10x.EventClean.infra.persistence.EventoEntity;
import org.springframework.stereotype.Component;

@Component
public class EventoDtoMapper {

    public EventoEntity map(EventoDto eventoDto) {
        EventoEntity eventoEntity = new EventoEntity();
        eventoEntity.setId(eventoDto.id());
        eventoEntity.setNome(eventoDto.nome());
        eventoEntity.setDescricao(eventoDto.descricao());
        eventoEntity.setDataInicio(eventoDto.dataInicio());
        eventoEntity.setDataFim(eventoDto.dataFim());
        eventoEntity.setIdentificador(eventoDto.identificador());
        eventoEntity.setLocalEvento(eventoDto.localEvento());
        eventoEntity.setOrganizador(eventoDto.organizador());
        eventoEntity.setCapacidade(eventoDto.capacidade());
        eventoEntity.setTipoEvento(eventoDto.tipoEvento());

        return eventoEntity;
    }

    public EventoDto map(EventoEntity eventoEntity) {
        return new EventoDto(
                eventoEntity.getId(),
                eventoEntity.getNome(),
                eventoEntity.getDescricao(),
                eventoEntity.getDataInicio(),
                eventoEntity.getDataFim(),
                eventoEntity.getIdentificador(),
                eventoEntity.getLocalEvento(),
                eventoEntity.getOrganizador(),
                eventoEntity.getCapacidade(),
                eventoEntity.getTipoEvento()
        );
    }
}
