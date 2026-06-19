package java10x.EventClean.infra.gateway;

import java10x.EventClean.core.entities.Evento;
import java10x.EventClean.core.gateway.EventoGateway;
import java10x.EventClean.infra.exception.EventNotExistsException;
import java10x.EventClean.infra.exception.ManualIdentifierNotAllowedException;
import java10x.EventClean.infra.mapper.EventoDtoMapper;
import java10x.EventClean.infra.mapper.EventoEntityMapper;
import java10x.EventClean.infra.persistence.EventoEntity;
import java10x.EventClean.infra.persistence.EventoRepository;
import org.apache.commons.lang3.RandomStringUtils;
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
        if (evento.identificador() != null) {
            throw new ManualIdentifierNotAllowedException("O identificador é gerado automaticamente pelo sistema e não deve ser enviado!");
        }

        String identificador = gerarIdentificador();

        while (existePorIdentificador(identificador)) {
            identificador = gerarIdentificador();
        }

        EventoEntity eventoEntity = eventoEntityMapper.toEntity(evento);
        eventoEntity.setIdentificador(identificador);
        EventoEntity novoEvento = eventoRepository.save(eventoEntity);
        return eventoEntityMapper.toDomain(novoEvento);
    }

    @Override
    public String gerarIdentificador() {
        return RandomStringUtils.randomAlphanumeric(6);
    }

    @Override
    public boolean existePorIdentificador(String identificador) {
        return eventoRepository.existsByIdentificadorIgnoreCase(identificador);
    }

    @Override
    public List<Evento> buscarEventos() {
        List<EventoEntity> eventos = eventoRepository.findAll();
        return eventos.stream()
                .map(eventoEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Evento filtrarIdentificador(String identificador) {
        if (!existePorIdentificador(identificador)){
            throw new EventNotExistsException("Evento com o identificador " + identificador + " não existe!");
        }

        return eventoEntityMapper.toDomain(eventoRepository.findByIdentificadorIgnoreCase(identificador));
    }
}
