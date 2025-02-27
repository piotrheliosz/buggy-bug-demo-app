package app.domain.services;

import app.infrastructure.MealsResponseDTO;

public interface MealFetcher {
    MealsResponseDTO.MealDTO fetchRandomMeal();
}
