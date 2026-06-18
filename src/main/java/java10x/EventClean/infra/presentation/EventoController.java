package java10x.EventClean.infra.presentation;

import java10x.EventClean.core.entities.Evento;
import java10x.EventClean.core.usecases.BuscarEventoCase;
import java10x.EventClean.core.usecases.CriarEventoCase;
import java10x.EventClean.core.usecases.FiltrarIdentificadorCase;
import java10x.EventClean.infra.dtos.EventoDto;
import java10x.EventClean.infra.mapper.EventoDtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/")
public class EventoController {

    private final CriarEventoCase criarEventoCase;
    private final BuscarEventoCase buscarEventoCase;
    private final FiltrarIdentificadorCase filtrarIdentificadorCase;
    private final EventoDtoMapper eventoDtoMapper;

    public EventoController(CriarEventoCase criarEventoCase, BuscarEventoCase buscarEventoCase, FiltrarIdentificadorCase filtrarIdentificadorCase, EventoDtoMapper eventoDtoMapper) {
        this.criarEventoCase = criarEventoCase;
        this.buscarEventoCase = buscarEventoCase;
        this.filtrarIdentificadorCase = filtrarIdentificadorCase;
        this.eventoDtoMapper = eventoDtoMapper;
    }


    @PostMapping("criarevento")
    public ResponseEntity<Map<String, Object>> criarEvento(@RequestBody EventoDto eventoDto) {
        Evento novoEvento = criarEventoCase.execute(eventoDtoMapper.toDomain(eventoDto));
        Map<String, Object> response = new HashMap<>();
        response.put("Mensagem:", "Evento cadastrado com sucesso no nosso banco de dados");
        response.put("Dados do evento:", eventoDtoMapper.toDto(novoEvento));

        return ResponseEntity.ok(response);
    }

    @GetMapping("buscarevento")
    public List<EventoDto> listarEvento() {
        List<Evento> eventos = buscarEventoCase.execute();
        return eventos.stream()
                .map(eventoDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("identificador/{identificador}")
    public ResponseEntity<EventoDto> filtrarIdentificador(@PathVariable String identificador){
        Evento evento = filtrarIdentificadorCase.execute(identificador);
        return ResponseEntity.ok(eventoDtoMapper.toDto(evento));
    }
}
