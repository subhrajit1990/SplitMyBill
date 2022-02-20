package bill.manager.splitmybill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Troublem@ker
 */

@SpringBootApplication(scanBasePackages = "bill.manager.*")
@EnableJpaRepositories(basePackages = "bill.manager.*")
@ComponentScan(basePackages ="bill.manager.*")
@EntityScan(basePackages = "bill.manager.*")
@EnableSwagger2
public class SplitmybillApplication {

	public static void main(String[] args) {
		SpringApplication.run(SplitmybillApplication.class, args);
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("https://sharemybill.herokuapp.com/");
			}
		};
	}


}
