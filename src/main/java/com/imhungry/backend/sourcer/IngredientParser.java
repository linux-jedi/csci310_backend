package com.imhungry.backend.sourcer;

import com.imhungry.backend.model.Ingredient;
import lombok.Getter;

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

		if (splitString.length == 1 || splitString[0].length() == 0) {
			quantity = null;
			setIngredientValue(unparsed.substring(3));
		} else {
			String number = splitString[0];
			setIngredientValue(splitString[1]);

			if (number.charAt(0) == '½') quantity = 0.5;
			else if (number.charAt(0) == '¼') quantity = 0.25;
			else quantity = Double.parseDouble(number);
		}
	}

	private void setIngredientValue(String ingredientValue) {
		this.ingredientValue = ingredientValue.trim();
	}

	public static Ingredient collateIngredients(Ingredient collateInto, Ingredient collateFrom) {
		if (collateFrom == null) return collateInto;
		if (!collateInto.getIngredientValue().equals(collateFrom.getIngredientValue())) return null;

		Double quantity1 = collateInto.getQuantity();
		Double quantity2 = collateFrom.getQuantity();

		if (quantity1 != null && quantity2 != null) {
			collateInto.setQuantity(quantity1 + quantity2);
		}

		return collateInto;
	}
}
