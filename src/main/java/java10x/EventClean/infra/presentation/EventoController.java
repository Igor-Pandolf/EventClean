package java10x.EventClean.infra.presentation;

import java10x.EventClean.core.entities.Evento;
import java10x.EventClean.infra.dtos.EventoDto;
import java10x.EventClean.infra.persistence.EventoEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/")
public class EventoController {

    @PostMapping("criarevento")
    public String criarEvento(@RequestBody EventoDto evento) {
        return "Evento criado";
    }
}
