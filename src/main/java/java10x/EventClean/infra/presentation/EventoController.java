package java10x.EventClean.infra.presentation;

import java10x.EventClean.core.entities.Evento;
import java10x.EventClean.core.usecases.BuscarEventoCase;
import java10x.EventClean.core.usecases.CriarEventoCase;
import java10x.EventClean.infra.dtos.EventoDto;
import java10x.EventClean.infra.mapper.EventoDtoMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/")
public class EventoController {

    private final CriarEventoCase criarEventoCase;
    private final BuscarEventoCase buscarEventoCase;
    private final EventoDtoMapper eventoDtoMapper;

    public EventoController(CriarEventoCase criarEventoCase, BuscarEventoCase buscarEventoCase, EventoDtoMapper eventoDtoMapper) {
        this.criarEventoCase = criarEventoCase;
        this.buscarEventoCase = buscarEventoCase;
        this.eventoDtoMapper = eventoDtoMapper;
    }


    @PostMapping("criarevento")
    public EventoDto criarEvento(@RequestBody EventoDto eventoDto) {
        Evento novoEvento = criarEventoCase.execute(eventoDtoMapper.toDomain(eventoDto));
        return eventoDtoMapper.toDto(novoEvento);
    }

    @GetMapping
    public List<EventoDto> listarEvento() {
        List<Evento> eventos = buscarEventoCase.execute();
        return eventos.stream()
                .map(eventoDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}
