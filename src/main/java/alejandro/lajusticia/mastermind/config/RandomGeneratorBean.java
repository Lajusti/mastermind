package alejandro.lajusticia.mastermind.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class RandomGeneratorBean {

    @Bean
    public Random buildRandomBean() {
        return new Random();
    }

}
