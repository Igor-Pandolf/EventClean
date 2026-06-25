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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários - CriarEventoCaseImpl")
class CriarEventoCaseImplTest {

    @Mock
    private EventoGateway eventoGateway;

    @InjectMocks
    private CriarEventoCaseImpl criarEventoCase;

    private Evento eventoValido;
    private Evento eventoSalvo;

    @BeforeEach
    void setUp() {
        eventoValido = new Evento(
                null,
                "Workshop de Clean Architecture",
                "Aprenda a estruturar seu código Java",
                LocalDateTime.of(2026, 8, 10, 9, 0),
                LocalDateTime.of(2026, 8, 10, 17, 0),
                null,
                "Auditório Principal",
                "Java10x Team",
                150,
                TipoEvento.WORKSHOP
        );

        eventoSalvo = new Evento(
                1L,
                "Workshop de Clean Architecture",
                "Aprenda a estruturar seu código Java",
                LocalDateTime.of(2026, 8, 10, 9, 0),
                LocalDateTime.of(2026, 8, 10, 17, 0),
                "abc123",
                "Auditório Principal",
                "Java10x Team",
                150,
                TipoEvento.WORKSHOP
        );
    }

    @Test
    @DisplayName("Deve criar um evento com sucesso delegando ao gateway")
    void deveCriarEventoComSucesso() {
        when(eventoGateway.criarEvento(eventoValido)).thenReturn(eventoSalvo);

        Evento resultado = criarEventoCase.execute(eventoValido);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Workshop de Clean Architecture", resultado.nome());
        assertEquals("abc123", resultado.identificador());
        assertEquals(TipoEvento.WORKSHOP, resultado.tipoEvento());

        verify(eventoGateway, times(1)).criarEvento(eventoValido);
    }

    @Test
    @DisplayName("Deve retornar exatamente o evento retornado pelo gateway")
    void deveRetornarEventoDoGateway() {
        when(eventoGateway.criarEvento(eventoValido)).thenReturn(eventoSalvo);

        Evento resultado = criarEventoCase.execute(eventoValido);

        assertEquals(eventoSalvo, resultado);
    }

    @Test
    @DisplayName("Deve propagar exceção lançada pelo gateway")
    void devePropagarExcecaoDoGateway() {
        when(eventoGateway.criarEvento(eventoValido))
                .thenThrow(new RuntimeException("Erro no banco de dados"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> criarEventoCase.execute(eventoValido));

        assertEquals("Erro no banco de dados", exception.getMessage());
        verify(eventoGateway, times(1)).criarEvento(eventoValido);
    }

    @Test
    @DisplayName("Deve chamar o gateway exatamente uma vez")
    void deveChamarGatewayUmaVez() {
        when(eventoGateway.criarEvento(eventoValido)).thenReturn(eventoSalvo);

        criarEventoCase.execute(eventoValido);

        verify(eventoGateway, times(1)).criarEvento(eventoValido);
        verifyNoMoreInteractions(eventoGateway);
    }
}
