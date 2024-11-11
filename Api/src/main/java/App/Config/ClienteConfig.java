package App.Config;

import App.Infra.Gateway.ClienteGateway;
import App.Infra.UseCase.Cliente.UseCaseCLienteDelete;
import App.Infra.UseCase.Cliente.UseCaseCLienteGet;
import App.Infra.UseCase.Cliente.UseCaseCLientePost;
import App.Infra.UseCase.Cliente.UseCaseCLientePut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClienteConfig {

    @Bean
    UseCaseCLienteGet useCaseCLienteGet(ClienteGateway clienteGateway)
    { return new UseCaseCLienteGet(clienteGateway);}

    @Bean
    UseCaseCLientePost useCaseCLientePost(ClienteGateway clienteGateway)
    { return new UseCaseCLientePost(clienteGateway);}

    @Bean
    UseCaseCLientePut useCaseCLientePut(ClienteGateway clienteGateway)
    {return  new UseCaseCLientePut(clienteGateway);}

    @Bean
    UseCaseCLienteDelete useCaseCLienteDelete(ClienteGateway clienteGateway)
    { return  new UseCaseCLienteDelete(clienteGateway);}

}
