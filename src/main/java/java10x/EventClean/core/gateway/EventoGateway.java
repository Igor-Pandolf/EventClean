package java10x.EventClean.core.gateway;

import java10x.EventClean.core.entities.Evento;

import java.util.List;

public interface EventoGateway {

    Evento criarEvento(Evento evento);
    boolean existePorIdentificador(String identificador);
    List<Evento> buscarEventos();
    Evento filtrarIdentificador(String identificador);

}
