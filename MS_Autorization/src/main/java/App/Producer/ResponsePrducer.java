package App.Producer;

import App.Dto.ResponseAuthorization;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResponsePrducer {

    @Autowired
    private AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public void integrar (ResponseAuthorization response) throws JsonProcessingException {
        amqpTemplate.convertAndSend(
                "authorization-response-queue",
                "authorization-response-queue-rout-key",
                //response
                objectMapper.writeValueAsString(response)
        );
    }

}
