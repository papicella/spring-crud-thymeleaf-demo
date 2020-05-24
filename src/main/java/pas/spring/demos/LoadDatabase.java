package pas.spring.demos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pas.spring.demos.entities.User;
import pas.spring.demos.repositories.UserRepository;

@Configuration
@Slf4j
public class LoadDatabase {

    @Bean
    CommandLineRunner initDB(UserRepository userRepository) {
        return args -> {
            log.info("Pre loading " + userRepository.save(new User("Pas", "pasa@vmware.com")));
            log.info("Pre loading " + userRepository.save(new User("Lucia", "lucia78@rocks.com")));
            log.info("Pre loading " + userRepository.save(new User("Lucas", "lucas@rocks.com")));
            log.info("Pre loading " + userRepository.save(new User("Siena", "siena@rocks.com")));
        };
    }
}
