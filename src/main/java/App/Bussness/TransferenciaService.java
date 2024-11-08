package App.Bussness;

import App.Domain.ResponseAuthorization;
import App.Domain.TransferenciaResponse;
import App.Infra.Exceptions.EntityNotFoundException;
import App.Infra.Exceptions.IllegalActionException;
import App.Infra.Exceptions.NullargumentsException;
import App.Infra.Gateway.TransferenciaGateway;
import App.Infra.Persistence.Entity.ClienteEntity;
import App.Infra.Persistence.Enum.TIPOCADASTRO;
import App.Infra.Persistence.Repository.ClienteRepository;
import App.Infra.Persistence.Repository.LoginRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

@Service
public class TransferenciaService implements TransferenciaGateway {


    private final ClienteRepository clienteRepository;
    private final LoginRepository loginRepository;
    Locale localBrasil = new Locale("pt", "BR");
    public TransferenciaService(ClienteRepository clienteRepository, LoginRepository loginRepository) {
        this.clienteRepository = clienteRepository;
        this.loginRepository = loginRepository;
    }

    @Override
    public ResponseEntity<TransferenciaResponse> novaTransferencia(Long payer,
                                                                   Long payee,
                                                                   Double valor)
    {
        {
            try
            {
               if(valor < 0){throw  new IllegalActionException("Valor Invalido");}
               if(payer != null &&
                  payee != null &&
                  valor != null)
               {
                   ClienteEntity depositante = clienteRepository.findById(payer).orElseThrow(
                               ()-> new EntityNotFoundException()
                   );
                   if(depositante.getTipocadastro() == TIPOCADASTRO.EMPRESA)
                   {throw new IllegalActionException("Ação não permitida a logistas.");}
                   if(valor < depositante.getSaldo())
                   {
                       ClienteEntity receptor = clienteRepository.findById(payee).orElseThrow(
                               ()-> new EntityNotFoundException()
                       );
                       ResponseAuthorization authorization = ProcessaTransferencia();
                       if(authorization.autorization() == true)
                       {
                           receptor.setSaldo(receptor.getSaldo() + valor);
                           depositante.setSaldo(depositante.getSaldo() - valor);
                           receptor.setTimeStamp(LocalDateTime.now());
                           depositante.setTimeStamp(LocalDateTime.now());
                           clienteRepository.save(depositante);
                           clienteRepository.save(receptor);
                           TransferenciaResponse response = new TransferenciaResponse(depositante.getNomeCompleto(),
                                   receptor.getNomeCompleto(),
                                   valor,
                                   LocalDateTime.now());
                           return new ResponseEntity<>(response, HttpStatus.OK);
                       }
                       else
                       {throw new IllegalActionException("Transferencia não autorizada.");}
                   }
                   else
                   { throw new IllegalActionException("Saldo insuficiente.");}
               }
               else
               { throw new NullargumentsException();}
            }
            catch (Exception e)
            {
                e.getMessage();
            }
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }


    public ResponseAuthorization ProcessaTransferencia()
    {
        try
        {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> resp =
                    restTemplate
                            .getForEntity("https://util.devi.tools/api/v2/authorize",Map.class);
            System.out.println(resp);
            if(resp.getStatusCode() == HttpStatus.OK)
            {
                ResponseAuthorization responseAuthorization = new ResponseAuthorization(Boolean.TRUE);
                //System.out.println("ok");
                return responseAuthorization;
            }
            else
            {throw new IllegalActionException("Ops, algo deu errado");}
        }
        catch (Exception e)
        {
            e.getMessage();
        }
        return null;
    }


}
