/**
 * The Microservice01Application class for run spring application.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Microservice01Application {

    public static void main(String[] args) {
        SpringApplication.run(Microservice01Application.class, args);
    }
}