package App.Consumer;

import App.Dto.ResponseAuthorization;
import App.Dto.TransferenciaResponse;
import App.Exception.IllegalActionException;
import App.Producer.ResponsePrducer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Component
public class AutorizationConsumer {

    private final ResponsePrducer prducer;
    private ObjectMapper mapper =new ObjectMapper();
    private RestTemplate restTemplate = new RestTemplate();

    public AutorizationConsumer(ResponsePrducer prducer) {
        this.prducer = prducer;
    }

    @RabbitListener(queues = { "authorization-requestTransferencia-queue"})
    public void ProcessaTransferencia(@Payload Message message) throws IOException
    {
        try
        {
            if(message != null)
            {
                String dados = (String) message.getPayload();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                TransferenciaResponse transferenciaResponse = mapper.readValue(dados, TransferenciaResponse.class);
                if(transferenciaResponse != null) {
                    ResponseEntity<Map> resp =
                            restTemplate
                                    .getForEntity("https://util.devi.tools/api/v8/authorize",Map.class);
                    if(resp.getStatusCode() == HttpStatus.OK)
                    {
                        ResponseAuthorization responseAuthorization = new ResponseAuthorization(transferenciaResponse.payer(),
                                transferenciaResponse.emailpayer(),
                                transferenciaResponse.payee(),
                                transferenciaResponse.emailpayee(),
                                transferenciaResponse.codigo(),
                                transferenciaResponse.valor(),
                                Boolean.TRUE);
                        prducer.integrar(responseAuthorization);
                    }
                    else
                    {
                        ResponseAuthorization responseAuthorization = new ResponseAuthorization(transferenciaResponse.payer(),
                                transferenciaResponse.emailpayer(),
                                transferenciaResponse.payee(),
                                transferenciaResponse.emailpayee(),
                                transferenciaResponse.codigo(),
                                transferenciaResponse.valor(),
                                Boolean.FALSE);
                        prducer.integrar(responseAuthorization);
                    }
                }
                else
                { throw new IllegalActionException("Ops algo errado, verifique os dados...");}
            }
        }
        catch (Exception e)
        {
            e.getMessage();
        }
    }



}
