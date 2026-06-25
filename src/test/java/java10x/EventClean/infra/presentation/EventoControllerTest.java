package java10x.EventClean.infra.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java10x.EventClean.core.entities.Evento;
import java10x.EventClean.core.enums.TipoEvento;
import java10x.EventClean.core.usecases.BuscarEventoCase;
import java10x.EventClean.core.usecases.CriarEventoCase;
import java10x.EventClean.core.usecases.FiltrarIdentificadorCase;
import java10x.EventClean.infra.dtos.EventoDto;
import java10x.EventClean.infra.exception.EventNotExistsException;
import java10x.EventClean.infra.exception.ManualIdentifierNotAllowedException;
import java10x.EventClean.infra.mapper.EventoDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventoController.class)
@DisplayName("Testes unitários - EventoController")
class EventoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CriarEventoCase criarEventoCase;

    @MockitoBean
    private BuscarEventoCase buscarEventoCase;

    @MockitoBean
    private FiltrarIdentificadorCase filtrarIdentificadorCase;

    @MockitoBean
    private EventoDtoMapper eventoDtoMapper;

    private ObjectMapper objectMapper;

    private final LocalDateTime dataInicio = LocalDateTime.of(2026, 8, 10, 9, 0);
    private final LocalDateTime dataFim    = LocalDateTime.of(2026, 8, 10, 17, 0);

    private EventoDto eventoDto;
    private Evento    eventoDomain;
    private EventoDto eventoDtoResposta;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        eventoDto = new EventoDto(
                null, "Workshop Clean Architecture", "Aprenda Clean Architecture",
                dataInicio, dataFim,
                null, "Auditório Principal", "Java10x Team",
                150, TipoEvento.WORKSHOP
        );

        eventoDomain = new Evento(
                null, "Workshop Clean Architecture", "Aprenda Clean Architecture",
                dataInicio, dataFim,
                null, "Auditório Principal", "Java10x Team",
                150, TipoEvento.WORKSHOP
        );

        eventoDtoResposta = new EventoDto(
                1L, "Workshop Clean Architecture", "Aprenda Clean Architecture",
                dataInicio, dataFim,
                "cln001", "Auditório Principal", "Java10x Team",
                150, TipoEvento.WORKSHOP
        );
    }

    // ── POST /api/v1/criarevento ──────────────────────────────────────────

    @Test
    @DisplayName("POST /criarevento: deve retornar 200 e mensagem de sucesso ao criar evento")
    void deveCriarEventoComSucesso() throws Exception {
        Evento eventoSalvo = new Evento(
                1L, "Workshop Clean Architecture", "Aprenda Clean Architecture",
                dataInicio, dataFim,
                "cln001", "Auditório Principal", "Java10x Team",
                150, TipoEvento.WORKSHOP
        );

        when(eventoDtoMapper.toDomain(any(EventoDto.class))).thenReturn(eventoDomain);
        when(criarEventoCase.execute(eventoDomain)).thenReturn(eventoSalvo);
        when(eventoDtoMapper.toDto(eventoSalvo)).thenReturn(eventoDtoResposta);

        mockMvc.perform(post("/api/v1/criarevento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Mensagem:']").value("Evento cadastrado com sucesso no nosso banco de dados"))
                .andExpect(jsonPath("$.['Dados do evento:']").exists());

        verify(criarEventoCase, times(1)).execute(eventoDomain);
    }

    @Test
    @DisplayName("POST /criarevento: deve retornar 422 quando identificador é enviado manualmente")
    void deveRetornar422QuandoIdentificadorEnviado() throws Exception {
        when(eventoDtoMapper.toDomain(any(EventoDto.class))).thenReturn(eventoDomain);
        when(criarEventoCase.execute(any()))
                .thenThrow(new ManualIdentifierNotAllowedException(
                        "O identificador é gerado automaticamente pelo sistema e não deve ser enviado!"));

        mockMvc.perform(post("/api/v1/criarevento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventoDto)))
                .andExpect(status().isUnprocessableEntity());
    }

    // ── GET /api/v1/buscarevento ──────────────────────────────────────────

    @Test
    @DisplayName("GET /buscarevento: deve retornar 200 com lista de eventos")
    void deveListarEventosComSucesso() throws Exception {
        Evento eventoSalvo = new Evento(
                1L, "Workshop Clean Architecture", "Aprenda Clean Architecture",
                dataInicio, dataFim,
                "cln001", "Auditório Principal", "Java10x Team",
                150, TipoEvento.WORKSHOP
        );

        when(buscarEventoCase.execute()).thenReturn(List.of(eventoSalvo));
        when(eventoDtoMapper.toDto(eventoSalvo)).thenReturn(eventoDtoResposta);

        mockMvc.perform(get("/api/v1/buscarevento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("Workshop Clean Architecture"));

        verify(buscarEventoCase, times(1)).execute();
    }

    @Test
    @DisplayName("GET /buscarevento: deve retornar 200 com lista vazia quando não há eventos")
    void deveRetornarListaVaziaQuandoNaoHaEventos() throws Exception {
        when(buscarEventoCase.execute()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/buscarevento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ── GET /api/v1/identificador/{identificador} ─────────────────────────

    @Test
    @DisplayName("GET /identificador/{id}: deve retornar 200 com evento quando identificador existe")
    void deveRetornarEventoPorIdentificador() throws Exception {
        Evento eventoSalvo = new Evento(
                1L, "Workshop Clean Architecture", "Aprenda Clean Architecture",
                dataInicio, dataFim,
                "cln001", "Auditório Principal", "Java10x Team",
                150, TipoEvento.WORKSHOP
        );

        when(filtrarIdentificadorCase.execute("cln001")).thenReturn(eventoSalvo);
        when(eventoDtoMapper.toDto(eventoSalvo)).thenReturn(eventoDtoResposta);

        mockMvc.perform(get("/api/v1/identificador/cln001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identificador").value("cln001"))
                .andExpect(jsonPath("$.nome").value("Workshop Clean Architecture"));

        verify(filtrarIdentificadorCase, times(1)).execute("cln001");
    }

    @Test
    @DisplayName("GET /identificador/{id}: deve retornar 404 quando identificador não existe")
    void deveRetornar404QuandoIdentificadorNaoExiste() throws Exception {
        when(filtrarIdentificadorCase.execute("invalido"))
                .thenThrow(new EventNotExistsException("Evento com o identificador invalido não existe!"));

        mockMvc.perform(get("/api/v1/identificador/invalido"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /identificador/{id}: deve passar o identificador correto ao use case")
    void devePassarIdentificadorCorretoAoUseCase() throws Exception {
        String identificador = "abc123";
        Evento eventoEncontrado = new Evento(
                2L, "Palestra", "Desc", dataInicio, dataFim,
                identificador, "Sala A", "OrgY", 80, TipoEvento.PALESTRA
        );
        EventoDto eventoDtoEncontrado = new EventoDto(
                2L, "Palestra", "Desc", dataInicio, dataFim,
                identificador, "Sala A", "OrgY", 80, TipoEvento.PALESTRA
        );

        when(filtrarIdentificadorCase.execute(identificador)).thenReturn(eventoEncontrado);
        when(eventoDtoMapper.toDto(eventoEncontrado)).thenReturn(eventoDtoEncontrado);

        mockMvc.perform(get("/api/v1/identificador/" + identificador))
                .andExpect(status().isOk());

        verify(filtrarIdentificadorCase).execute(identificador);
        verifyNoMoreInteractions(filtrarIdentificadorCase);
    }
}
