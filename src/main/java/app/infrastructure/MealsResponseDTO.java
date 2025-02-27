package app.infrastructure;

import java.util.List;

@lombok.Data
public class MealsResponseDTO {

    private List<MealDTO> meals;

    @lombok.Data
    public static class MealDTO {
        private String idMeal;
        private String strMeal;
        private String strInstructions;
    }
}
