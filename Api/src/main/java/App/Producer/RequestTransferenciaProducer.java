package App.Producer;

import App.Domain.TransferenciaResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestTransferenciaProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public void integrar (TransferenciaResponse response) throws JsonProcessingException {
        amqpTemplate.convertAndSend(
                "authorization-request-queue",
                "authorization-request-queue-rout-key",
                objectMapper.writeValueAsString(response)
        );
    }

}
