package com.imhungry.backend;

import org.mockito.Mockito;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;

import static org.mockito.Mockito.when;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
	
	@Bean
	@Scope("singleton")
	@Profile("dev")
	public RestaurantSourcer getMockRestaurantSourcer() throws Exception {
		RestaurantSourcer restaurantSourcer = Mockito.mock(RestaurantSourcer.class);
		when(restaurantSourcer.getRestaurantDetails("ChIJW-yJPuPHwoARGh0NU_IeYpI"))
                .thenReturn(MockupUtilityMethods.getSingleRestaurant());
		when(restaurantSourcer.searchRestaurants("chinese", 5, 10000))
                .thenReturn(MockupUtilityMethods.getFiveChineseRestaurants());
		return restaurantSourcer;
	}

	@Bean
	@Scope("singleton")
	@Profile("dev")
	public RecipeSourcer getMockRecipeSourcer() throws Exception {
		RecipeSourcer recipeSourcer = Mockito.mock(RecipeSourcer.class);
		when(recipeSourcer.getRecipe("573147"))
				.thenReturn(MockupUtilityMethods.getSingleRecipe());
		when(recipeSourcer.getRecipes("chinese", 5))
				.thenReturn(MockupUtilityMethods.getFiveChineseRecipes());
		return recipeSourcer;
	}

	@Bean
	@Scope("singleton")
	@Profile("dev")
	public CollageBuilder getMockCollageBuilder() throws Exception {
		CollageBuilder collageBuilder = Mockito.mock(CollageBuilder.class);
		when(collageBuilder.getUrls("chinese" + " food", 10))
				.thenReturn(MockupUtilityMethods.getImageURLsChineseFood());

		return collageBuilder;
	}

	@Bean
	@Scope("singleton")
	@Profile("prod")
	public RestaurantSourcer restaurantSourcer() {
		return new RestaurantSourcer();
	}

	@Bean
	@Scope("singleton")
	@Profile("prod")
	public CollageBuilder getCollageBuilder() {
		return new CollageBuilder();
	}

	@Bean
	@Scope("singleton")
	@Profile("prod")
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