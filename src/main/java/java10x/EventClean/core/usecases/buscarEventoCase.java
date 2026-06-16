package java10x.EventClean.core.usecases;

import java10x.EventClean.core.entities.Evento;

public interface buscarEventoCase {
    
    public Evento execute(Long id);
}
