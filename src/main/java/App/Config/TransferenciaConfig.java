package App.Config;

import App.Infra.Gateway.TransferenciaGateway;
import App.Infra.UseCase.Transferencia.UseCaseTransferenciaPost;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransferenciaConfig {


    @Bean
    UseCaseTransferenciaPost useCaseTransferenciaPost(TransferenciaGateway transferenciaGateway)
    { return new UseCaseTransferenciaPost(transferenciaGateway);}

}
