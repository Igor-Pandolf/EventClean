package java10x.EventClean.core.usecases;

import java10x.EventClean.core.entities.Evento;
import java10x.EventClean.core.gateway.EventoGateway;
import java10x.EventClean.infra.exception.DuplicateEventException;
import java10x.EventClean.infra.exception.EventNotExistsException;

public class FiltrarIdentificadorCaseImpl implements FiltrarIdentificadorCase{

    private final EventoGateway eventoGateway;

    public FiltrarIdentificadorCaseImpl(EventoGateway eventoGateway) {
        this.eventoGateway = eventoGateway;
    }


    @Override
    public Evento execute(String identificador) {
        if (!eventoGateway.existePorIdentificador(identificador)){
            throw new EventNotExistsException("Evento com o identificador " + identificador + " não existe!");
        }

        return eventoGateway.filtrarIdentificador(identificador);
    }
}
