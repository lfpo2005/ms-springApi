package lfcode.api.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MsSpringApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsSpringApiApplication.class, args);
		//System.out.println(new BCryptPasswordEncoder().encode("197197")); //gerando senha cryptografada de teste
	}

}
