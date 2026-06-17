package java10x.EventClean.core.usecases;

import java10x.EventClean.core.entities.Evento;

import java.util.List;

public interface BuscarEventoCase {
    
    List<Evento> execute();
}
