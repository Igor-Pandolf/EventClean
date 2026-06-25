package java10x.EventClean.core.usecases;

import java10x.EventClean.core.entities.Evento;
import java10x.EventClean.core.enums.TipoEvento;
import java10x.EventClean.core.gateway.EventoGateway;
import java10x.EventClean.infra.exception.EventNotExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários - FiltrarIdentificadorCaseImpl")
class FiltrarIdentificadorCaseImplTest {

    @Mock
    private EventoGateway eventoGateway;

    @InjectMocks
    private FiltrarIdentificadorCaseImpl filtrarIdentificadorCase;

    private Evento eventoPorIdentificador;

    @BeforeEach
    void setUp() {
        eventoPorIdentificador = new Evento(
                1L,
                "Seminário de Arquitetura",
                "Aprenda padrões avançados",
                LocalDateTime.of(2026, 10, 1, 10, 0),
                LocalDateTime.of(2026, 10, 1, 18, 0),
                "xyz789",
                "Centro de Convenções",
                "Tech Corp",
                300,
                TipoEvento.SEMINARIO
        );
    }

    @Test
    @DisplayName("Deve retornar evento ao filtrar por identificador existente")
    void deveRetornarEventoPorIdentificadorExistente() {
        when(eventoGateway.filtrarIdentificador("xyz789")).thenReturn(eventoPorIdentificador);

        Evento resultado = filtrarIdentificadorCase.execute("xyz789");

        assertNotNull(resultado);
        assertEquals("xyz789", resultado.identificador());
        assertEquals("Seminário de Arquitetura", resultado.nome());
        assertEquals(TipoEvento.SEMINARIO, resultado.tipoEvento());

        verify(eventoGateway, times(1)).filtrarIdentificador("xyz789");
    }

    @Test
    @DisplayName("Deve propagar EventNotExistsException quando identificador não existe")
    void devePropararExcecaoQuandoIdentificadorNaoExiste() {
        when(eventoGateway.filtrarIdentificador("invalido"))
                .thenThrow(new EventNotExistsException("Evento com o identificador invalido não existe!"));

        EventNotExistsException exception = assertThrows(EventNotExistsException.class,
                () -> filtrarIdentificadorCase.execute("invalido"));

        assertTrue(exception.getMessage().contains("invalido"));
        verify(eventoGateway, times(1)).filtrarIdentificador("invalido");
    }

    @Test
    @DisplayName("Deve delegar ao gateway exatamente uma vez com o identificador correto")
    void deveDelegarAoGatewayUmaVez() {
        when(eventoGateway.filtrarIdentificador("xyz789")).thenReturn(eventoPorIdentificador);

        filtrarIdentificadorCase.execute("xyz789");

        verify(eventoGateway, times(1)).filtrarIdentificador("xyz789");
        verifyNoMoreInteractions(eventoGateway);
    }

    @Test
    @DisplayName("Deve retornar exatamente o objeto retornado pelo gateway")
    void deveRetornarExatamenteOObjetoDoGateway() {
        when(eventoGateway.filtrarIdentificador("xyz789")).thenReturn(eventoPorIdentificador);

        Evento resultado = filtrarIdentificadorCase.execute("xyz789");

        assertEquals(eventoPorIdentificador, resultado);
    }
}
