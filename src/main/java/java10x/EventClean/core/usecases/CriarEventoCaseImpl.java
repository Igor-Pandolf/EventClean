package java10x.EventClean.core.usecases;

import java10x.EventClean.core.entities.Evento;
import java10x.EventClean.core.gateway.EventoGateway;
import java10x.EventClean.infra.exception.DuplicateEventException;

public class CriarEventoCaseImpl implements CriarEventoCase {

    private final EventoGateway eventoGateway;

    public CriarEventoCaseImpl(EventoGateway eventoGateway) {
        this.eventoGateway = eventoGateway;
    }

    @Override
    public Evento execute(Evento evento) {
        if (eventoGateway.existePorIdentificador(evento.identificador())){
            throw new DuplicateEventException("Já existe um evento cadastrado com o identificador: " + evento.identificador());
        }

        return eventoGateway.criarEvento(evento);
    }
}
