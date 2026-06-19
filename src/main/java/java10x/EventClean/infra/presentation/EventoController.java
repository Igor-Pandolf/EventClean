package java10x.EventClean.infra.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java10x.EventClean.core.entities.Evento;
import java10x.EventClean.core.usecases.BuscarEventoCase;
import java10x.EventClean.core.usecases.CriarEventoCase;
import java10x.EventClean.core.usecases.FiltrarIdentificadorCase;
import java10x.EventClean.infra.dtos.EventoDto;
import java10x.EventClean.infra.mapper.EventoDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Eventos", description = "Endpoints para gerenciamento e consulta de eventos")
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


    @Operation(
        summary = "Cadastra um novo evento",
        description = "Cria um novo evento no sistema. O identificador do evento é gerado automaticamente pelo sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Evento cadastrado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "422",
            description = "Não é permitido enviar um identificador manualmente ou recurso não encontrado",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Evento duplicado no banco de dados",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping("criarevento")
    public ResponseEntity<Map<String, Object>> criarEvento(@RequestBody EventoDto eventoDto) {
        Evento novoEvento = criarEventoCase.execute(eventoDtoMapper.toDomain(eventoDto));
        Map<String, Object> response = new HashMap<>();
        response.put("Mensagem:", "Evento cadastrado com sucesso no nosso banco de dados");
        response.put("Dados do evento:", eventoDtoMapper.toDto(novoEvento));

        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Lista todos os eventos",
        description = "Retorna uma lista com todos os eventos cadastrados no banco de dados."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de eventos retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventoDto.class))
        )
    })
    @GetMapping("buscarevento")
    public List<EventoDto> listarEvento() {
        List<Evento> eventos = buscarEventoCase.execute();
        return eventos.stream()
                .map(eventoDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(
        summary = "Busca evento por identificador",
        description = "Busca e retorna os detalhes de um evento específico baseado no seu identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Evento encontrado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventoDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Evento não encontrado para o identificador informado",
            content = @Content(mediaType = "application/json")
        )
    })
    @GetMapping("identificador/{identificador}")
    public ResponseEntity<EventoDto> filtrarIdentificador(
            @Parameter(description = "Identificador único do evento", required = true, example = "evt_9b1deb4d")
            @PathVariable String identificador){
        Evento evento = filtrarIdentificadorCase.execute(identificador);
        return ResponseEntity.ok(eventoDtoMapper.toDto(evento));
    }
}
