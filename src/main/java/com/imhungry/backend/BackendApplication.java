package com.imhungry.backend;

import org.mockito.Mockito;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static org.mockito.Mockito.when;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"com.imhungry.backend.repository"})
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
				.thenReturn(MockupUtilityMethods.getNorthernCafe());
		when(restaurantSourcer.getRestaurantDetails("ChIJRaPCphDHwoARRKD4kcOtCA0"))
				.thenReturn(MockupUtilityMethods.getHabitBurger());


		when(restaurantSourcer.searchRestaurants("chinese", 5, 10000))
				.thenReturn(MockupUtilityMethods.getFiveChineseRestaurants());
		when(restaurantSourcer.searchRestaurants("chinese", 100, 10000))
				.thenReturn(MockupUtilityMethods.getFiveChineseRestaurants());

		when(restaurantSourcer.searchRestaurants("burger", 5, 10000))
				.thenReturn(MockupUtilityMethods.getFiveBurgerRestaurants());
		when(restaurantSourcer.searchRestaurants("burger", 30, 10000))
				.thenReturn(MockupUtilityMethods.getThirtyBurgerRestaurants());

		when(restaurantSourcer.searchRestaurants("burger", 2, 10000))
				.thenCallRealMethod();
		when(restaurantSourcer.getRestaurantDetails("ChIJdzrHbse4woARwbth0qmfStw"))
				.thenCallRealMethod();

		return restaurantSourcer;
	}

	@Bean
	@Scope("singleton")
	@Profile("dev")
	public RecipeSourcer getMockRecipeSourcer() throws Exception {
		RecipeSourcer recipeSourcer = Mockito.mock(RecipeSourcer.class);
		when(recipeSourcer.getRecipe("573147"))
				.thenReturn(MockupUtilityMethods.getFriedRice());
		when(recipeSourcer.getRecipe("219871"))
				.thenReturn(MockupUtilityMethods.getAubgergineBurger());

		when(recipeSourcer.searchRecipes("chinese", 5))
				.thenReturn(MockupUtilityMethods.getFiveChineseRecipes());

		when(recipeSourcer.searchRecipes("burger", 5))
				.thenReturn(MockupUtilityMethods.getFiveBurgerRecipes());
		when(recipeSourcer.searchRecipes("burger", 30))
				.thenReturn(MockupUtilityMethods.getThirtyBurgerRecipes());

		when(recipeSourcer.searchRecipes("burger", 2))
				.thenCallRealMethod();
		when(recipeSourcer.getRecipe("219957")).thenCallRealMethod();

		return recipeSourcer;
	}

	@Bean
	@Scope("singleton")
	@Profile("dev")
	public CollageBuilder getMockCollageBuilder() throws Exception {
		CollageBuilder collageBuilder = Mockito.mock(CollageBuilder.class);
		when(collageBuilder.getUrls("chinese" + " food", 10))
				.thenReturn(MockupUtilityMethods.getImageURLsChineseFood());
		when(collageBuilder.getUrls("burger" + " food", 10))
				.thenReturn(MockupUtilityMethods.getImageURLsBurgerFood());
		when(collageBuilder.getUrls("burger-TEST" + " food", 10))
				.thenCallRealMethod();
		return collageBuilder;
	}

	@Bean
	@Scope("singleton")
	@Profile("prod")
	public RestaurantSourcer getRestaurantSourcer() {
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
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
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