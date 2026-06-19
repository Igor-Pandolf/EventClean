package java10x.EventClean.core.usecases;

import java10x.EventClean.core.entities.Evento;
import java10x.EventClean.core.gateway.EventoGateway;
import java10x.EventClean.infra.exception.EventNotExistsException;

public class FiltrarIdentificadorCaseImpl implements FiltrarIdentificadorCase{

    private final EventoGateway eventoGateway;

    public FiltrarIdentificadorCaseImpl(EventoGateway eventoGateway) {
        this.eventoGateway = eventoGateway;
    }


    @Override
    public Evento execute(String identificador) {
        return eventoGateway.filtrarIdentificador(identificador);
    }
}
