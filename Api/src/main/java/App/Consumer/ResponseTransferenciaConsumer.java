package App.Consumer;

import App.Domain.ResponseAuthorization;
import App.Infra.Exceptions.EntityNotFoundException;
import App.Infra.Persistence.Entity.ClienteEntity;
import App.Infra.Persistence.Entity.ContaEntity;
import App.Infra.Persistence.Entity.TransferenciaEnviadaEntity;
import App.Infra.Persistence.Entity.TransferenciaRecebidaEntity;
import App.Infra.Persistence.Enum.STATUSTRANSFERENCIA;
import App.Infra.Persistence.Repository.ClienteRepository;
import App.Infra.Persistence.Repository.ContaRepository;
import App.Infra.Persistence.Repository.TransferenciaEnviadaRepository;
import App.Infra.Persistence.Repository.TransferenciaRecebidaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;

@Component
public class ResponseTransferenciaConsumer {

    private final ClienteRepository clienteRepository;
    private final ContaRepository contaRepository;
    private final TransferenciaEnviadaRepository transferenciaEnviadaRepository;
    private final TransferenciaRecebidaRepository transferenciaRecebidaRepository;
    Locale localBrasil = new Locale("pt", "BR");

    public ResponseTransferenciaConsumer(ClienteRepository clienteRepository, ContaRepository contaRepository, TransferenciaEnviadaRepository transferenciaEnviadaRepository, TransferenciaRecebidaRepository transferenciaRecebidaRepository) {
        this.clienteRepository = clienteRepository;
        this.contaRepository = contaRepository;
        this.transferenciaEnviadaRepository = transferenciaEnviadaRepository;
        this.transferenciaRecebidaRepository = transferenciaRecebidaRepository;
    }

    @RabbitListener(queues = { "authorization-response-queue"})
    public void AtualizaSaldo (@Payload Message message) throws JsonProcessingException
    {
        try
        {
            String dados = (String) message.getPayload();
            ObjectMapper mapper =new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ResponseAuthorization response = mapper.readValue(dados, ResponseAuthorization.class);
            if(response.authorization() == true)
            {
                TransferenciaEnviadaEntity transferenciaEnviada = transferenciaEnviadaRepository.findBycodigo(response.codigo()).orElseThrow(
                        ()-> new EntityNotFoundException()
                );
                TransferenciaRecebidaEntity transferenciaRecebida = transferenciaRecebidaRepository.findBycodigo(response.codigo()).orElseThrow(
                        ()-> new EntityNotFoundException()
                );
                ClienteEntity payer = clienteRepository.findByemail(response.emailpayer()).orElseThrow(
                        ()-> new EntityNotFoundException()
                );
                    ClienteEntity payee = clienteRepository.findByemail(response.emailpayee()).orElseThrow(
                            ()-> new EntityNotFoundException()
                    );
                if(response.valor() < payer.getContaEntity().getSaldo())
                {
                    ContaEntity contaEntityPayer = contaRepository.findById(payer.getContaEntity().getId()).orElseThrow(
                            ()-> new EntityNotFoundException()
                    );
                    ContaEntity contaEntityPayee = contaRepository.findById(payee.getContaEntity().getId()).orElseThrow(
                            ()-> new EntityNotFoundException()
                    );
                    contaEntityPayee.setSaldo(contaEntityPayee.getSaldo() - response.valor());
                    contaEntityPayer.setSaldo(contaEntityPayer.getSaldo() + response.valor());
                    payee.setTimeStamp(LocalDateTime.now());
                    payer.setTimeStamp(LocalDateTime.now());
                    transferenciaRecebida.setStatustransferencia(STATUSTRANSFERENCIA.AUTORIZADA);
                    transferenciaRecebida.setTimeStamp(LocalDateTime.now());
                    transferenciaEnviada.setStatustransferencia(STATUSTRANSFERENCIA.AUTORIZADA);
                    transferenciaEnviada.setTimeStamp(LocalDateTime.now());
                    transferenciaRecebidaRepository.save(transferenciaRecebida);
                    transferenciaEnviadaRepository.save(transferenciaEnviada);
                    clienteRepository.save(payee);
                    clienteRepository.save(payer);
                }
            }
            else
            {
                TransferenciaEnviadaEntity transferenciaEnviada = transferenciaEnviadaRepository.findBycodigo(response.codigo()).orElseThrow(
                        ()-> new EntityNotFoundException()
                );
                TransferenciaRecebidaEntity transferenciaRecebida = transferenciaRecebidaRepository.findBycodigo(response.codigo()).orElseThrow(
                        ()-> new EntityNotFoundException()
                );
                transferenciaRecebida.setStatustransferencia(STATUSTRANSFERENCIA.NAO_AUTORIZADA);
                transferenciaRecebida.setTimeStamp(LocalDateTime.now());
                transferenciaEnviada.setStatustransferencia(STATUSTRANSFERENCIA.NAO_AUTORIZADA);
                transferenciaEnviada.setTimeStamp(LocalDateTime.now());
                transferenciaRecebidaRepository.save(transferenciaRecebida);
                transferenciaEnviadaRepository.save(transferenciaEnviada);
            }
        }
        catch (Exception e)
        {
            e.getMessage();
        }
    }
}
