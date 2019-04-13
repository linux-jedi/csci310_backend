package com.imhungry.backend.data;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = Restaurant.class, name = "restaurant"),
		@JsonSubTypes.Type(value = Recipe.class, name = "recipe")
})
public interface ListItem {
	String getId();
}
