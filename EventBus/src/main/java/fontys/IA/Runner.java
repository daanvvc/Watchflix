package fontys.IA;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

	private final RabbitTemplate rabbitTemplate;

	public Runner( RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	@Override
	public void run(String... args) throws Exception {
		// Without this the queue isn't binded to the routing key for some reason
		 System.out.println("Sending message...");
		 rabbitTemplate.convertAndSend("amq.topic", "movie-routing-key", "Hello from RabbitMQ!");
	}
}
