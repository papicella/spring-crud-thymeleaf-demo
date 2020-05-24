package pas.spring.demos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SpringCrudThymeleafDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCrudThymeleafDemoApplication.class, args);
    }

}
