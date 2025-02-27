package app.application;

import app.infrastructure.MealsResponseDTO;
import app.domain.entities.Meal;
import app.domain.entities.User;
import app.domain.repository.MealRepository;
import app.domain.repository.UserRepository;
import app.infrastructure.MealClient;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MealClient mealClient;

    @InjectMocks
    private MealService mealService;

    private final Faker fake = new Faker();

    @Test
    void addMealToUser() {
        // Given
        User user = new User(fake.name().firstName());
        user.setId(fake.number().randomNumber());
        Meal meal = new Meal(fake.name().title(), fake.food().ingredient());
        user.getMeals().add(meal);

        MealsResponseDTO.MealDTO mealDto = new MealsResponseDTO.MealDTO();
        mealDto.setIdMeal(fake.name().name());
        mealDto.setStrMeal(meal.getName());
        mealDto.setStrInstructions(meal.getDescription());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mealClient.fetchRandomMeal()).thenReturn(mealDto);
        when(mealRepository.save(meal)).thenReturn(meal);
        when(userRepository.save(user)).thenReturn(user);

        // When
        User userWithMeals = mealService.addMealToUser(user.getId());

        // Then
        assertFalse(userWithMeals.getMeals().isEmpty());
        assertEquals(userWithMeals.getMeals().get(0), meal);
    }

    @Test
    void removeMealFromUser() {
        // Given
        User user = new User(fake.name().firstName());
        user.setId(fake.number().randomNumber());
        Meal meal = new Meal(fake.name().title(), fake.food().ingredient());
        meal.setId(fake.number().randomNumber());
        user.getMeals().add(meal);

        MealsResponseDTO.MealDTO mealDto = new MealsResponseDTO.MealDTO();
        mealDto.setIdMeal(fake.name().name());
        mealDto.setStrMeal(meal.getName());
        mealDto.setStrInstructions(meal.getDescription());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mealRepository.findById(meal.getId())).thenReturn(Optional.of(meal));
        when(userRepository.save(user)).thenReturn(user);

        // When
        User userWithMeals = mealService.removeMealFromUser(user.getId(), meal.getId());

        // Then
        assertTrue(userWithMeals.getMeals().isEmpty());
    }

    @Test
    void getMealById() {
        // Given
        Meal meal = new Meal(fake.name().title(), fake.food().ingredient());
        meal.setId(fake.number().randomNumber());

        when(mealRepository.findById(meal.getId())).thenReturn(Optional.of(meal));

        // When
        Meal foundMeal = mealService.getMealById(meal.getId());

        // Then
        assertNotNull(foundMeal);
        assertEquals(meal, foundMeal);
    }

    @Test
    void getAllMeals() {
        // Given
        Meal meal_0 = new Meal(fake.name().title(), fake.food().ingredient());
        Meal meal_1 = new Meal(fake.name().title(), fake.food().ingredient());
        meal_0.setId(fake.number().randomNumber());
        meal_1.setId(fake.number().randomNumber());
        List<Meal> meals = List.of(meal_0, meal_1);

        when(mealRepository.findAll()).thenReturn(meals);

        // When
        Iterable<Meal> foundMeal = mealService.getAllMeals();

        // Then
        assertNotNull(foundMeal);
        assertEquals(meals, foundMeal);
    }

    @Test
    void deleteMeal() {
        // Given
        Meal meal = new Meal(fake.name().title(), fake.food().ingredient());
        meal.setId(fake.number().randomNumber());
        User user = new User(fake.name().firstName());
        user.getMeals().add(meal);

        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(mealRepository.findById(meal.getId())).thenReturn(Optional.of(meal));

        // When
        List<Long> usersIdsWithoutThatMeal = mealService.deleteMeal(meal.getId());

        // Then
        assertEquals(user.getId(), usersIdsWithoutThatMeal.get(0));
    }

    @Test
    void removeMealFromUserThrowFailedToFetchMeal() {
        // Given
        User user = new User(fake.name().firstName());
        user.setId(fake.number().randomNumber());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When
        Exception exception = assertThrows(RuntimeException.class, () -> mealService.addMealToUser(user.getId()));

        // Then
        assertEquals("Failed to fetch meal", exception.getMessage());
    }

    @Test
    void addMealToUserThrowUserNotFound() {
        // When
        Exception exception = assertThrows(RuntimeException.class, () -> mealService.addMealToUser(1L));

        // Then
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void removeMealFromUserThrowUserNotFound() {
        // When
        Exception exception = assertThrows(RuntimeException.class, () -> mealService.removeMealFromUser(1L, 1L));

        // Then
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void removeMealFromUserThrowMealNotFound() {
        // Given
        User user = new User(fake.name().firstName());
        user.setId(fake.number().randomNumber());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When
        Exception exception = assertThrows(RuntimeException.class, () -> mealService.removeMealFromUser(user.getId(), 1L));

        // Then
        assertEquals("Meal not found", exception.getMessage());
    }

    @Test
    void getMealByIdThrowMealNotFound() {
        // When
        Exception exception = assertThrows(RuntimeException.class, () -> mealService.getMealById(1L));

        // Then
        assertEquals("Meal not found", exception.getMessage());
    }

    @Test
    void deleteThrowMealNotFound() {
        // When
        Exception exception = assertThrows(RuntimeException.class, () -> mealService.deleteMeal(1L));

        // Then
        assertEquals("Meal not found", exception.getMessage());
    }
}
