package com.imhungry.backend;

import static org.assertj.core.api.Assertions.assertThat;

import com.imhungry.backend.controller.CollageController;
import com.imhungry.backend.controller.ListController;
import com.imhungry.backend.controller.RecipeController;
import com.imhungry.backend.controller.RestaurantController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BackendApplicationTests {

	@Autowired
	private CollageController collageController;

	@Autowired
	private ListController listController;

	@Autowired
	private RecipeController recipeController;

	@Autowired
	private RestaurantController restaurantController;

	@Test
	public void contextLoads() {
		assertThat(collageController).isNotNull();
		assertThat(listController).isNotNull();
		assertThat(recipeController).isNotNull();
		assertThat(restaurantController).isNotNull();
	}

}


