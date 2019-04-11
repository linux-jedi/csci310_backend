package com.imhungry.backend;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = Restaurant.class, name = "restaurant"),
		@JsonSubTypes.Type(value = Recipe.class, name = "recipe")
})
public interface ListItem {
	String getId();
}
