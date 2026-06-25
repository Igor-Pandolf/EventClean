package java10x.EventClean.infra.mapper;

import java10x.EventClean.core.entities.Evento;
import java10x.EventClean.core.enums.TipoEvento;
import java10x.EventClean.infra.dtos.EventoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários - EventoDtoMapper")
class EventoDtoMapperTest {

    private EventoDtoMapper eventoDtoMapper;

    private final LocalDateTime dataInicio = LocalDateTime.of(2026, 8, 10, 9, 0);
    private final LocalDateTime dataFim    = LocalDateTime.of(2026, 8, 10, 17, 0);

    @BeforeEach
    void setUp() {
        eventoDtoMapper = new EventoDtoMapper();
    }

    // ── toDomain ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("toDomain: deve mapear todos os campos do DTO para o domínio corretamente")
    void deveMappearDtoParaDominio() {
        EventoDto dto = new EventoDto(
                1L, "Evento Teste", "Descrição do evento",
                dataInicio, dataFim,
                "abc123", "Sala A", "Organizador X",
                100, TipoEvento.WORKSHOP
        );

        Evento resultado = eventoDtoMapper.toDomain(dto);

        assertNotNull(resultado);
        assertEquals(1L,              resultado.id());
        assertEquals("Evento Teste",  resultado.nome());
        assertEquals("Descrição do evento", resultado.descricao());
        assertEquals(dataInicio,       resultado.dataInicio());
        assertEquals(dataFim,          resultado.dataFim());
        assertEquals("abc123",         resultado.identificador());
        assertEquals("Sala A",         resultado.localEvento());
        assertEquals("Organizador X",  resultado.organizador());
        assertEquals(100,              resultado.capacidade());
        assertEquals(TipoEvento.WORKSHOP, resultado.tipoEvento());
    }

    @Test
    @DisplayName("toDomain: deve preservar identificador nulo quando DTO não possui identificador")
    void deveMappearDtoSemIdentificador() {
        EventoDto dto = new EventoDto(
                null, "Novo Evento", "Desc",
                dataInicio, dataFim,
                null, "Online", "Java10x",
                50, TipoEvento.PALESTRA
        );

        Evento resultado = eventoDtoMapper.toDomain(dto);

        assertNull(resultado.id());
        assertNull(resultado.identificador());
        assertEquals("Novo Evento", resultado.nome());
    }

    // ── toDto ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("toDto: deve mapear todos os campos do domínio para o DTO corretamente")
    void deveMappearDominioParaDto() {
        Evento evento = new Evento(
                2L, "Palestra Java", "Palestra sobre Java 21",
                dataInicio, dataFim,
                "def456", "Auditório B", "Speaker Y",
                200, TipoEvento.PALESTRA
        );

        EventoDto resultado = eventoDtoMapper.toDto(evento);

        assertNotNull(resultado);
        assertEquals(2L,              resultado.id());
        assertEquals("Palestra Java", resultado.nome());
        assertEquals("Palestra sobre Java 21", resultado.descricao());
        assertEquals(dataInicio,       resultado.dataInicio());
        assertEquals(dataFim,          resultado.dataFim());
        assertEquals("def456",         resultado.identificador());
        assertEquals("Auditório B",    resultado.localEvento());
        assertEquals("Speaker Y",      resultado.organizador());
        assertEquals(200,              resultado.capacidade());
        assertEquals(TipoEvento.PALESTRA, resultado.tipoEvento());
    }

    @Test
    @DisplayName("toDto: deve manter consistência em mapeamento de ida e volta (round-trip)")
    void deveMantaConsistenciaEmRoundTrip() {
        EventoDto dtoOriginal = new EventoDto(
                3L, "Seminário", "Conteúdo avançado",
                dataInicio, dataFim,
                "ghi789", "Online", "Org Z",
                500, TipoEvento.SEMINARIO
        );

        // DTO → Domínio → DTO
        Evento dominio   = eventoDtoMapper.toDomain(dtoOriginal);
        EventoDto dtoFinal = eventoDtoMapper.toDto(dominio);

        assertEquals(dtoOriginal.id(),          dtoFinal.id());
        assertEquals(dtoOriginal.nome(),         dtoFinal.nome());
        assertEquals(dtoOriginal.descricao(),    dtoFinal.descricao());
        assertEquals(dtoOriginal.dataInicio(),   dtoFinal.dataInicio());
        assertEquals(dtoOriginal.dataFim(),      dtoFinal.dataFim());
        assertEquals(dtoOriginal.identificador(),dtoFinal.identificador());
        assertEquals(dtoOriginal.localEvento(),  dtoFinal.localEvento());
        assertEquals(dtoOriginal.organizador(),  dtoFinal.organizador());
        assertEquals(dtoOriginal.capacidade(),   dtoFinal.capacidade());
        assertEquals(dtoOriginal.tipoEvento(),   dtoFinal.tipoEvento());
    }
}
