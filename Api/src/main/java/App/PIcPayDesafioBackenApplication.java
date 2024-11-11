package App;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
@OpenAPIDefinition(info = @Info(title = "Desafio Backend picpay",
		version = "1",
		description = "Desafio técnico picpay presente no link https://github.com/PicPay/picpay-desafio-backend. "))
public class PIcPayDesafioBackenApplication {

	public static void main(String[] args) {
		SpringApplication.run(PIcPayDesafioBackenApplication.class, args);
	}

}
