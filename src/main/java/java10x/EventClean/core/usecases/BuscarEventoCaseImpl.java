package java10x.EventClean.core.usecases;

import java10x.EventClean.core.entities.Evento;
import java10x.EventClean.core.gateway.EventoGateway;

import java.util.List;

public class BuscarEventoCaseImpl implements BuscarEventoCase {

    private final EventoGateway eventoGateway;

    public BuscarEventoCaseImpl(EventoGateway eventoGateway) {
        this.eventoGateway = eventoGateway;
    }

    @Override
    public List<Evento> execute() {
        return eventoGateway.buscarEventos();
    }
}
