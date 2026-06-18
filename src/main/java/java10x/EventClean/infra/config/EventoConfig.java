package java10x.EventClean.infra.config;

import java10x.EventClean.core.usecases.*;
import java10x.EventClean.infra.gateway.EventoRepositoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 1. @Configuration avisa ao Spring que esta classe contém definições de "receitas" (beans)
// que ele precisa ler e jogar para dentro do seu ecossistema assim que a aplicação sobe.
@Configuration
public class EventoConfig {

    // 2. @Bean diz ao Spring: "Gerencie o objeto retornado por este método".
    // A partir de agora, qualquer classe que pedir um 'CriarEventoCase' no construtor
    // (como o seu EventoController) vai receber essa instância que criamos aqui.
    @Bean
    public CriarEventoCase criarEventoCase(EventoRepositoryGateway gateway) {

        // 3. Injeção de Dependência Automática:
        // O Spring olha para o parâmetro (EventoRepositoryGateway) e procura no próprio sistema
        // se já existe um componente de banco criado. Ele acha o seu Repository/Gateway da infra
        // e passa automaticamente aqui para dentro.

        // 4. Instanciação Manual (A mágica da Clean Architecture):
        // Como o seu 'CriarEventoCaseImpl' é uma classe Java pura no Core (sem nenhuma anotação),
        // nós o instanciamos manualmente usando o 'new', passando a dependência que o Spring achou.
        return new CriarEventoCaseImpl(gateway);

        // O Spring guarda o objeto retornado dentro do seu "Coração" (ApplicationContext)
        // pronto para ser usado pelo seu Controller!
    }

    @Bean
    public BuscarEventoCase buscarEventoCase(EventoRepositoryGateway gateway) {
        return new BuscarEventoCaseImpl(gateway);
    }

    @Bean
    public FiltrarIdentificadorCase filtrarIdentificadorCase(EventoRepositoryGateway gateway) {
        return new FiltrarIdentificadorCaseImpl(gateway);
    }
}
