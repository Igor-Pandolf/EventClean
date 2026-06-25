package java10x.EventClean.infra.mapper;

import java10x.EventClean.core.entities.Evento;
import java10x.EventClean.core.enums.TipoEvento;
import java10x.EventClean.infra.persistence.EventoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários - EventoEntityMapper")
class EventoEntityMapperTest {

    private EventoEntityMapper eventoEntityMapper;

    private final LocalDateTime dataInicio = LocalDateTime.of(2026, 8, 10, 9, 0);
    private final LocalDateTime dataFim    = LocalDateTime.of(2026, 8, 10, 17, 0);

    @BeforeEach
    void setUp() {
        eventoEntityMapper = new EventoEntityMapper();
    }

    // ── toEntity ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("toEntity: deve mapear todos os campos do domínio para a entidade JPA")
    void deveMappearDominioParaEntidade() {
        Evento evento = new Evento(
                1L, "Workshop Clean Code", "Boas práticas",
                dataInicio, dataFim,
                "abc123", "Sala B", "Org A",
                80, TipoEvento.WORKSHOP
        );

        EventoEntity resultado = eventoEntityMapper.toEntity(evento);

        assertNotNull(resultado);
        assertEquals(1L,                  resultado.getId());
        assertEquals("Workshop Clean Code", resultado.getNome());
        assertEquals("Boas práticas",      resultado.getDescricao());
        assertEquals(dataInicio,           resultado.getDataInicio());
        assertEquals(dataFim,              resultado.getDataFim());
        assertEquals("abc123",             resultado.getIdentificador());
        assertEquals("Sala B",             resultado.getLocalEvento());
        assertEquals("Org A",              resultado.getOrganizador());
        assertEquals(80,                   resultado.getCapacidade());
        assertEquals(TipoEvento.WORKSHOP,  resultado.getTipoEvento());
    }

    @Test
    @DisplayName("toEntity: deve preservar id nulo para novos eventos")
    void deveMappearDominioComIdNuloParaEntidade() {
        Evento evento = new Evento(
                null, "Palestra Spring", "Conteúdo Spring",
                dataInicio, dataFim,
                null, "Auditório", "DevOrg",
                120, TipoEvento.PALESTRA
        );

        EventoEntity resultado = eventoEntityMapper.toEntity(evento);

        assertNull(resultado.getId());
        assertNull(resultado.getIdentificador());
        assertEquals("Palestra Spring", resultado.getNome());
    }

    // ── toDomain ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("toDomain: deve mapear todos os campos da entidade JPA para o domínio")
    void deveMappearEntidadeParaDominio() {
        EventoEntity entity = new EventoEntity(
                2L, "Música ao Vivo", "Show cover",
                dataInicio, dataFim,
                "mus001", "Teatro Municipal", "Show Org",
                500, TipoEvento.MUSICA
        );

        Evento resultado = eventoEntityMapper.toDomain(entity);

        assertNotNull(resultado);
        assertEquals(2L,                resultado.id());
        assertEquals("Música ao Vivo",  resultado.nome());
        assertEquals("Show cover",      resultado.descricao());
        assertEquals(dataInicio,         resultado.dataInicio());
        assertEquals(dataFim,            resultado.dataFim());
        assertEquals("mus001",           resultado.identificador());
        assertEquals("Teatro Municipal", resultado.localEvento());
        assertEquals("Show Org",         resultado.organizador());
        assertEquals(500,                resultado.capacidade());
        assertEquals(TipoEvento.MUSICA,  resultado.tipoEvento());
    }

    @Test
    @DisplayName("toDomain: deve manter consistência em mapeamento de ida e volta (round-trip)")
    void deveMantaConsistenciaEmRoundTrip() {
        Evento eventoOriginal = new Evento(
                3L, "Seminário DevOps", "CI/CD na prática",
                dataInicio, dataFim,
                "sem999", "Online", "DevOps Team",
                200, TipoEvento.SEMINARIO
        );

        // Domínio → Entidade → Domínio
        EventoEntity entity       = eventoEntityMapper.toEntity(eventoOriginal);
        Evento       eventoFinal  = eventoEntityMapper.toDomain(entity);

        assertEquals(eventoOriginal.id(),           eventoFinal.id());
        assertEquals(eventoOriginal.nome(),          eventoFinal.nome());
        assertEquals(eventoOriginal.descricao(),     eventoFinal.descricao());
        assertEquals(eventoOriginal.dataInicio(),    eventoFinal.dataInicio());
        assertEquals(eventoOriginal.dataFim(),       eventoFinal.dataFim());
        assertEquals(eventoOriginal.identificador(), eventoFinal.identificador());
        assertEquals(eventoOriginal.localEvento(),   eventoFinal.localEvento());
        assertEquals(eventoOriginal.organizador(),   eventoFinal.organizador());
        assertEquals(eventoOriginal.capacidade(),    eventoFinal.capacidade());
        assertEquals(eventoOriginal.tipoEvento(),    eventoFinal.tipoEvento());
    }
}
