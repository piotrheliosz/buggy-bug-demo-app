package app.controllers;

import app.application.MealService;
import app.domain.entities.Meal;
import app.domain.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@lombok.RequiredArgsConstructor
@RestController
@RequestMapping("/meals")
public class MealsController {

    private final MealService mealService;

    @GetMapping("/{mealId}")
    public ResponseEntity<Meal> getMeal(@PathVariable Long mealId) {
        return ResponseEntity.ok(mealService.getMealById(mealId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Meal>> getAllMeals() {
        return ResponseEntity.ok(mealService.getAllMeals());
    }

    @PostMapping("/{userId}")
    public ResponseEntity<User> addMealToUser(@PathVariable Long userId) {
        return ResponseEntity.ok(mealService.addMealToUser(userId));
    }

    @DeleteMapping("/{mealId}")
    public ResponseEntity<List<Long>> removeMeal(@PathVariable Long mealId) {
        return ResponseEntity.ok(mealService.deleteMeal(mealId));
    }

    @DeleteMapping("/{userId}/{mealId}")
    public ResponseEntity<User> removeMealFromUser(@PathVariable Long userId, @PathVariable Long mealId) {
        return ResponseEntity.ok(mealService.removeMealFromUser(userId, mealId));
    }
}
