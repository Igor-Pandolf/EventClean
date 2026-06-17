package java10x.EventClean.infra.gateway;

import java10x.EventClean.core.entities.Evento;
import java10x.EventClean.core.gateway.EventoGateway;
import java10x.EventClean.infra.mapper.EventoDtoMapper;
import java10x.EventClean.infra.mapper.EventoEntityMapper;
import java10x.EventClean.infra.persistence.EventoEntity;
import java10x.EventClean.infra.persistence.EventoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventoRepositoryGateway implements EventoGateway {

    private final EventoRepository eventoRepository;
    private final EventoEntityMapper eventoEntityMapper;

    public EventoRepositoryGateway(EventoRepository eventoRepository, EventoEntityMapper eventoEntityMapper, EventoDtoMapper eventoDtoMapper) {
        this.eventoRepository = eventoRepository;
        this.eventoEntityMapper = eventoEntityMapper;
    }

    @Override
    public Evento criarEvento(Evento evento) {
        if (eventoRepository.existsByIdentificador(evento.identificador())) {
            throw new IllegalArgumentException("Já existe um evento cadastrado com este identificador!");
        }

        EventoEntity eventoEntity = eventoEntityMapper.toEntity(evento);
        EventoEntity novoEvento = eventoRepository.save(eventoEntity);
        return eventoEntityMapper.toDomain(novoEvento);
    }

    @Override
    public List<Evento> buscarEventos() {
        List<EventoEntity> eventos = eventoRepository.findAll();
        return eventos.stream()
                .map(eventoEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
