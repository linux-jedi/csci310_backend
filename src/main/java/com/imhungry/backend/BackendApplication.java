package com.imhungry.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	@Scope("singleton")
	public RestaurantSourcer getRestaurantSourcer() {
		return new RestaurantSourcer();
	}

	@Bean
    @Scope("singleton")
    public CollageBuilder getCollageBuilder() {
        return new CollageBuilder();
    }

    @Bean
    @Scope("singleton")
    public RecipeSourcer getRecipeSourcer() {
        return new RecipeSourcer();
    }

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			System.out.println("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}

		};
	}

}
