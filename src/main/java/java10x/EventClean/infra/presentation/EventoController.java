package java10x.EventClean.infra.presentation;

import java10x.EventClean.core.entities.Evento;
import java10x.EventClean.core.usecases.CriarEventoCase;
import java10x.EventClean.core.usecases.CriarEventoCaseImpl;
import java10x.EventClean.infra.dtos.EventoDto;
import java10x.EventClean.infra.mapper.EventoDtoMapper;
import java10x.EventClean.infra.persistence.EventoEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/")
public class EventoController {

    private final CriarEventoCase criarEventoCase;
    private final EventoDtoMapper eventoDtoMapper;

    public EventoController(CriarEventoCase criarEventoCase, EventoDtoMapper eventoDtoMapper) {
        this.criarEventoCase = criarEventoCase;
        this.eventoDtoMapper = eventoDtoMapper;
    }


    @PostMapping("criarevento")
    public EventoDto criarEvento(@RequestBody EventoDto eventoDto) {
        Evento novoEvento = criarEventoCase.execute(eventoDtoMapper.toEntity(eventoDto));
        return eventoDtoMapper.toDto(novoEvento);
    }

    @GetMapping
    public String listarEvento() {
        return "lista de eventos";
    }
}
