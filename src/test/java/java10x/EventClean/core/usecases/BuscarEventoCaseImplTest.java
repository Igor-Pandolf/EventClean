package java10x.EventClean.core.usecases;

import java10x.EventClean.core.entities.Evento;
import java10x.EventClean.core.enums.TipoEvento;
import java10x.EventClean.core.gateway.EventoGateway;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários - BuscarEventoCaseImpl")
class BuscarEventoCaseImplTest {

    @Mock
    private EventoGateway eventoGateway;

    @InjectMocks
    private BuscarEventoCaseImpl buscarEventoCase;

    private Evento evento1;
    private Evento evento2;

    @BeforeEach
    void setUp() {
        evento1 = new Evento(
                1L, "Workshop Java", "Evento de Java",
                LocalDateTime.of(2026, 8, 10, 9, 0),
                LocalDateTime.of(2026, 8, 10, 17, 0),
                "abc123", "Sala A", "Java10x", 100, TipoEvento.WORKSHOP
        );

        evento2 = new Evento(
                2L, "Palestra DevOps", "Evento de DevOps",
                LocalDateTime.of(2026, 9, 5, 14, 0),
                LocalDateTime.of(2026, 9, 5, 16, 0),
                "def456", "Auditório", "DevTeam", 200, TipoEvento.PALESTRA
        );
    }

    @Test
    @DisplayName("Deve retornar lista com todos os eventos")
    void deveRetornarListaComTodosEventos() {
        when(eventoGateway.buscarEventos()).thenReturn(List.of(evento1, evento2));

        List<Evento> resultado = buscarEventoCase.execute();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Workshop Java", resultado.get(0).nome());
        assertEquals("Palestra DevOps", resultado.get(1).nome());

        verify(eventoGateway, times(1)).buscarEventos();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há eventos cadastrados")
    void deveRetornarListaVaziaQuandoNaoHaEventos() {
        when(eventoGateway.buscarEventos()).thenReturn(Collections.emptyList());

        List<Evento> resultado = buscarEventoCase.execute();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(eventoGateway, times(1)).buscarEventos();
    }

    @Test
    @DisplayName("Deve delegar a busca ao gateway exatamente uma vez")
    void deveDelegarBuscaAoGatewayUmaVez() {
        when(eventoGateway.buscarEventos()).thenReturn(List.of(evento1));

        buscarEventoCase.execute();

        verify(eventoGateway, times(1)).buscarEventos();
        verifyNoMoreInteractions(eventoGateway);
    }

    @Test
    @DisplayName("Deve propagar exceção lançada pelo gateway")
    void devePropagarExcecaoDoGateway() {
        when(eventoGateway.buscarEventos())
                .thenThrow(new RuntimeException("Falha ao acessar o banco de dados"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> buscarEventoCase.execute());

        assertEquals("Falha ao acessar o banco de dados", exception.getMessage());
    }
}
