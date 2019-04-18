package com.imhungry.backend;

import com.imhungry.backend.controller.RestaurantController;
import com.imhungry.backend.sourcer.CollageBuilder;
import com.imhungry.backend.sourcer.RecipeSourcer;
import com.imhungry.backend.sourcer.RestaurantSourcer;
import com.imhungry.backend.utils.MockupUtilityMethods;
import org.mockito.Mockito;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Arrays;

import static org.mockito.Mockito.when;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableCaching
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
		createRestaurantMocks(restaurantSourcer);

		return restaurantSourcer;
	}

	@Bean
	@Scope("singleton")
	@Profile("dev")
	public RecipeSourcer getMockRecipeSourcer() throws Exception {
		RecipeSourcer recipeSourcer = Mockito.mock(RecipeSourcer.class);
		createRecipeMocks(recipeSourcer);

		return recipeSourcer;
	}

	@Bean
	@Scope("singleton")
	@Profile("dev")
	public CollageBuilder getMockCollageBuilder() throws Exception {
		CollageBuilder collageBuilder = Mockito.mock(CollageBuilder.class);
		createCollageMocks(collageBuilder);
		return collageBuilder;
	}

	private void createRestaurantMocks(RestaurantSourcer restaurantSourcer) throws Exception {
		when(restaurantSourcer.getRestaurantDetails("ChIJW-yJPuPHwoARGh0NU_IeYpI"))
				.thenReturn(MockupUtilityMethods.getNorthernCafe());
		when(restaurantSourcer.getRestaurantDetails("ChIJRaPCphDHwoARRKD4kcOtCA0"))
				.thenReturn(MockupUtilityMethods.getHabitBurger());

		when(restaurantSourcer.searchRestaurants("chinese", 5, RestaurantController.milesToMeters(3)))
				.thenReturn(MockupUtilityMethods.getFiveChineseRestaurants());
		when(restaurantSourcer.searchRestaurants("chinese", 100, RestaurantController.milesToMeters(3)))
				.thenReturn(MockupUtilityMethods.getFiveChineseRestaurants());

		when(restaurantSourcer.searchRestaurants("burger", 5, RestaurantController.milesToMeters(3)))
				.thenReturn(MockupUtilityMethods.getFiveBurgerRestaurants());
		when(restaurantSourcer.searchRestaurants("burger", 30, RestaurantController.milesToMeters(3)))
				.thenReturn(MockupUtilityMethods.getThirtyBurgerRestaurants());

		when(restaurantSourcer.searchRestaurants("burger", 2, RestaurantController.milesToMeters(1)))
				.thenCallRealMethod();

		when(restaurantSourcer.searchRestaurants("burger", 5, RestaurantController.milesToMeters(0.1)))
				.thenCallRealMethod();
		when(restaurantSourcer.searchRestaurants("burger", 5, RestaurantController.milesToMeters(1)))
				.thenCallRealMethod();
		when(restaurantSourcer.searchRestaurants("burger", 2, RestaurantController.milesToMeters(4)))
				.thenCallRealMethod();
		when(restaurantSourcer.getRestaurantDetails("ChIJdzrHbse4woARwbth0qmfStw"))
				.thenCallRealMethod();
	}

	private void createRecipeMocks(RecipeSourcer recipeSourcer) throws Exception {
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
	}

	private void createCollageMocks(CollageBuilder collageBuilder) throws IOException {
		when(collageBuilder.getUrls("chinese" + " food", 10))
				.thenReturn(MockupUtilityMethods.getImageURLsChineseFood());
		when(collageBuilder.getUrls("burger" + " food", 10))
				.thenReturn(MockupUtilityMethods.getImageURLsBurgerFood());
		when(collageBuilder.getUrls("burger-TEST" + " food", 10))
				.thenCallRealMethod();
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
		return new BCryptPasswordEncoder();
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