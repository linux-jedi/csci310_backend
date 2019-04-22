package com.imhungry.backend.sourcer;

import com.imhungry.backend.model.Ingredient;
import lombok.Getter;

import java.util.Optional;

public class IngredientParser {

	@Getter
	private String ingredientValue;

	@Getter
	private Double quantity;

	public String getIngredientString() {
		return String.valueOf(quantity) + " " + ingredientValue;
	}

	public IngredientParser(String unparsed) {
		separate(unparsed);
	}

	private void separate(String unparsed) {
		unparsed = unparsed.trim();
		String numberPatternString = "^([\\d|.|½|¼]*)";
		unparsed = unparsed.replaceAll(numberPatternString, "$1-/-");
		String[] splitString = unparsed.split("-/-");

		if (splitString.length == 1 || splitString[0].equals("")) {
			quantity = null;
			setIngredientValue(unparsed.substring(3));
		} else {
			String number = splitString[0];
			setIngredientValue(splitString[1]);

			quantity = parseNum(number);


		}
	}

	private Double parseNum(String number) {
		String newNumber = number.replaceAll("(½)", ".5").replaceAll("(¼)", ".25");
		return Double.parseDouble(newNumber);
	}

	private void setIngredientValue(String ingredientValue) {
		this.ingredientValue = ingredientValue.trim();
	}

	public static Ingredient collateIngredients(Ingredient collateInto, Ingredient collateFrom) {
		if (collateFrom == null) return collateInto;

		Double quantity1 = Optional.ofNullable(collateInto.getQuantity()).orElse((double) 0);
		Double quantity2 = Optional.ofNullable(collateFrom.getQuantity()).orElse((double) 0);

		collateInto.setQuantity(quantity1 + quantity2);
		collateInto.setChecked(collateFrom.isChecked() || collateInto.isChecked());

		return collateInto;
	}
}
