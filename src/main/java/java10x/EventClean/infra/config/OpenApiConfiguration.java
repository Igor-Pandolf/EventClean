package java10x.EventClean.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Desafio ITAU + Java10X")
                                .description("Api desafio tecnico do ITAU + Java10X")
                                .version("1.0.0")
                );
    }
}
