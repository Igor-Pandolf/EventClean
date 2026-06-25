package java10x.EventClean.infra.gateway;

import java10x.EventClean.core.entities.Evento;
import java10x.EventClean.core.enums.TipoEvento;
import java10x.EventClean.infra.exception.EventNotExistsException;
import java10x.EventClean.infra.exception.ManualIdentifierNotAllowedException;
import java10x.EventClean.infra.mapper.EventoEntityMapper;
import java10x.EventClean.infra.persistence.EventoEntity;
import java10x.EventClean.infra.persistence.EventoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários - EventoRepositoryGateway")
class EventoRepositoryGatewayTest {

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private EventoEntityMapper eventoEntityMapper;

    @InjectMocks
    private EventoRepositoryGateway eventoRepositoryGateway;

    private final LocalDateTime dataInicio = LocalDateTime.of(2026, 8, 10, 9, 0);
    private final LocalDateTime dataFim    = LocalDateTime.of(2026, 8, 10, 17, 0);

    private Evento eventoDomain;
    private EventoEntity eventoEntity;
    private EventoEntity eventoEntitySalvo;

    @BeforeEach
    void setUp() {
        // Evento domínio sem identificador (simulando criação)
        eventoDomain = new Evento(
                null, "Workshop TDD", "Aprenda TDD na prática",
                dataInicio, dataFim,
                null, "Sala C", "Org TDD",
                60, TipoEvento.WORKSHOP
        );

        // Entidade JPA correspondente (sem identificador ainda)
        eventoEntity = new EventoEntity(
                null, "Workshop TDD", "Aprenda TDD na prática",
                dataInicio, dataFim,
                null, "Sala C", "Org TDD",
                60, TipoEvento.WORKSHOP
        );

        // Entidade salva com ID e identificador gerados
        eventoEntitySalvo = new EventoEntity(
                1L, "Workshop TDD", "Aprenda TDD na prática",
                dataInicio, dataFim,
                "tdd001", "Sala C", "Org TDD",
                60, TipoEvento.WORKSHOP
        );
    }

    // ── criarEvento ───────────────────────────────────────────────────────

    @Test
    @DisplayName("criarEvento: deve criar evento com sucesso quando identificador é nulo")
    void deveCriarEventoComSucesso() {
        Evento eventoSalvoDomain = new Evento(
                1L, "Workshop TDD", "Aprenda TDD na prática",
                dataInicio, dataFim,
                "tdd001", "Sala C", "Org TDD",
                60, TipoEvento.WORKSHOP
        );

        when(eventoEntityMapper.toEntity(eventoDomain)).thenReturn(eventoEntity);
        when(eventoRepository.existsByIdentificadorIgnoreCase(anyString())).thenReturn(false);
        when(eventoRepository.save(eventoEntity)).thenReturn(eventoEntitySalvo);
        when(eventoEntityMapper.toDomain(eventoEntitySalvo)).thenReturn(eventoSalvoDomain);

        Evento resultado = eventoRepositoryGateway.criarEvento(eventoDomain);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertNotNull(resultado.identificador());
        verify(eventoRepository, times(1)).save(eventoEntity);
    }

    @Test
    @DisplayName("criarEvento: deve lançar ManualIdentifierNotAllowedException quando identificador é enviado")
    void deveLancarExcecaoQuandoIdentificadorForEnviado() {
        Evento eventoComIdentificador = new Evento(
                null, "Evento Inválido", "Desc",
                dataInicio, dataFim,
                "manual123", "Local", "Org",
                50, TipoEvento.PALESTRA
        );

        assertThrows(ManualIdentifierNotAllowedException.class,
                () -> eventoRepositoryGateway.criarEvento(eventoComIdentificador));

        verify(eventoRepository, never()).save(any());
    }

    @Test
    @DisplayName("criarEvento: deve gerar novo identificador quando o primeiro já existe (colisão)")
    void deveGerarNovoIdentificadorQuandoOPrimeiroJaExiste() {
        Evento eventoSalvoDomain = new Evento(
                1L, "Workshop TDD", "Aprenda TDD na prática",
                dataInicio, dataFim,
                "tdd001", "Sala C", "Org TDD",
                60, TipoEvento.WORKSHOP
        );

        when(eventoEntityMapper.toEntity(eventoDomain)).thenReturn(eventoEntity);
        // Primeira chamada → colisão; segunda chamada → livre
        when(eventoRepository.existsByIdentificadorIgnoreCase(anyString()))
                .thenReturn(true)
                .thenReturn(false);
        when(eventoRepository.save(eventoEntity)).thenReturn(eventoEntitySalvo);
        when(eventoEntityMapper.toDomain(eventoEntitySalvo)).thenReturn(eventoSalvoDomain);

        Evento resultado = eventoRepositoryGateway.criarEvento(eventoDomain);

        assertNotNull(resultado);
        // existsByIdentificadorIgnoreCase deve ter sido chamado pelo menos duas vezes
        verify(eventoRepository, atLeast(2)).existsByIdentificadorIgnoreCase(anyString());
    }

    // ── gerarIdentificador ────────────────────────────────────────────────

    @Test
    @DisplayName("gerarIdentificador: deve retornar uma string com 6 caracteres alfanuméricos")
    void deveGerarIdentificadorComSeisCaracteres() {
        String identificador = eventoRepositoryGateway.gerarIdentificador();

        assertNotNull(identificador);
        assertEquals(6, identificador.length());
        assertTrue(identificador.matches("[a-zA-Z0-9]+"));
    }

    @Test
    @DisplayName("gerarIdentificador: deve gerar identificadores diferentes em chamadas consecutivas (estatístico)")
    void deveGerarIdentificadoresDiferentes() {
        String id1 = eventoRepositoryGateway.gerarIdentificador();
        String id2 = eventoRepositoryGateway.gerarIdentificador();
        String id3 = eventoRepositoryGateway.gerarIdentificador();

        // Probabilidade de todos iguais é astronomicamente baixa
        assertFalse(id1.equals(id2) && id2.equals(id3),
                "Três identificadores consecutivos não deveriam ser todos iguais");
    }

    // ── existePorIdentificador ────────────────────────────────────────────

    @Test
    @DisplayName("existePorIdentificador: deve retornar true quando identificador existe")
    void deveRetornarTrueQuandoIdentificadorExiste() {
        when(eventoRepository.existsByIdentificadorIgnoreCase("abc123")).thenReturn(true);

        boolean resultado = eventoRepositoryGateway.existePorIdentificador("abc123");

        assertTrue(resultado);
        verify(eventoRepository, times(1)).existsByIdentificadorIgnoreCase("abc123");
    }

    @Test
    @DisplayName("existePorIdentificador: deve retornar false quando identificador não existe")
    void deveRetornarFalseQuandoIdentificadorNaoExiste() {
        when(eventoRepository.existsByIdentificadorIgnoreCase("xyz999")).thenReturn(false);

        boolean resultado = eventoRepositoryGateway.existePorIdentificador("xyz999");

        assertFalse(resultado);
    }

    @Test
    @DisplayName("existePorIdentificador: deve ser case-insensitive")
    void deveSerCaseInsensitive() {
        when(eventoRepository.existsByIdentificadorIgnoreCase("ABC123")).thenReturn(true);

        boolean resultado = eventoRepositoryGateway.existePorIdentificador("ABC123");

        assertTrue(resultado);
        verify(eventoRepository).existsByIdentificadorIgnoreCase("ABC123");
    }

    // ── buscarEventos ─────────────────────────────────────────────────────

    @Test
    @DisplayName("buscarEventos: deve retornar lista mapeada de todos os eventos")
    void deveRetornarListaMapeadaDeEventos() {
        EventoEntity entity2 = new EventoEntity(
                2L, "Palestra", "Desc", dataInicio, dataFim,
                "pal001", "Auditório", "OrgX", 100, TipoEvento.PALESTRA
        );
        Evento dominio2 = new Evento(
                2L, "Palestra", "Desc", dataInicio, dataFim,
                "pal001", "Auditório", "OrgX", 100, TipoEvento.PALESTRA
        );

        when(eventoRepository.findAll()).thenReturn(List.of(eventoEntitySalvo, entity2));
        when(eventoEntityMapper.toDomain(eventoEntitySalvo)).thenReturn(new Evento(
                1L, "Workshop TDD", "Aprenda TDD na prática",
                dataInicio, dataFim, "tdd001", "Sala C", "Org TDD", 60, TipoEvento.WORKSHOP
        ));
        when(eventoEntityMapper.toDomain(entity2)).thenReturn(dominio2);

        List<Evento> resultado = eventoRepositoryGateway.buscarEventos();

        assertEquals(2, resultado.size());
        verify(eventoRepository, times(1)).findAll();
        verify(eventoEntityMapper, times(2)).toDomain(any(EventoEntity.class));
    }

    @Test
    @DisplayName("buscarEventos: deve retornar lista vazia quando não há eventos")
    void deveRetornarListaVazia() {
        when(eventoRepository.findAll()).thenReturn(Collections.emptyList());

        List<Evento> resultado = eventoRepositoryGateway.buscarEventos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ── filtrarIdentificador ──────────────────────────────────────────────

    @Test
    @DisplayName("filtrarIdentificador: deve retornar evento quando identificador existe")
    void deveRetornarEventoPorIdentificador() {
        Evento eventoEsperado = new Evento(
                1L, "Workshop TDD", "Aprenda TDD na prática",
                dataInicio, dataFim, "tdd001", "Sala C", "Org TDD", 60, TipoEvento.WORKSHOP
        );

        when(eventoRepository.existsByIdentificadorIgnoreCase("tdd001")).thenReturn(true);
        when(eventoRepository.findByIdentificadorIgnoreCase("tdd001")).thenReturn(eventoEntitySalvo);
        when(eventoEntityMapper.toDomain(eventoEntitySalvo)).thenReturn(eventoEsperado);

        Evento resultado = eventoRepositoryGateway.filtrarIdentificador("tdd001");

        assertNotNull(resultado);
        assertEquals("tdd001", resultado.identificador());
        assertEquals("Workshop TDD", resultado.nome());
    }

    @Test
    @DisplayName("filtrarIdentificador: deve lançar EventNotExistsException quando identificador não existe")
    void deveLancarExcecaoQuandoIdentificadorNaoExiste() {
        when(eventoRepository.existsByIdentificadorIgnoreCase("inexistente")).thenReturn(false);

        EventNotExistsException exception = assertThrows(EventNotExistsException.class,
                () -> eventoRepositoryGateway.filtrarIdentificador("inexistente"));

        assertTrue(exception.getMessage().contains("inexistente"));
        verify(eventoRepository, never()).findByIdentificadorIgnoreCase(anyString());
    }
}
