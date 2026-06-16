package java10x.EventClean.infra.persistence;

import jakarta.persistence.*;
import java10x.EventClean.core.enums.TipoEvento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Eventos")
public class EventoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String identificador;
    private String localEvento;
    private String organizador;
    private Integer capacidade;

    @Enumerated(EnumType.STRING)
    private TipoEvento tipoEvento;
}
