package fontys.IA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "fontys.IA.repositories")
public class MovieServiceApp {
    public static void main(String[] args) {

        SpringApplication.run(MovieServiceApp.class, args);
    }
}