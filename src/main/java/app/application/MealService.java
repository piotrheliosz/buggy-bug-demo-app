package app.application;

import app.infrastructure.MealsResponseDTO;
import app.domain.entities.Meal;
import app.domain.entities.User;
import app.domain.repository.MealRepository;
import app.domain.repository.UserRepository;
import app.infrastructure.MealClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@lombok.RequiredArgsConstructor
@Service
public class MealService {

    private final MealClient mealClient;
    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    @Transactional
    public User addMealToUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MealsResponseDTO.MealDTO mealDto = mealClient.fetchRandomMeal();
        if (mealDto == null) throw new RuntimeException("Failed to fetch meal");

        Meal meal = new Meal(mealDto.getStrMeal(), mealDto.getStrInstructions());
        user.getMeals().add(meal);
        mealRepository.save(meal);
        return userRepository.save(user);
    }

    @Transactional
    public User removeMealFromUser(Long userId, Long mealId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new RuntimeException("Meal not found"));

        user.getMeals().remove(meal);
        return userRepository.save(user);
    }

    public Meal getMealById(Long mealId) {
        return mealRepository.findById(mealId)
                .orElseThrow(() -> new RuntimeException("Meal not found"));
    }

    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }

    @Transactional
    public List<Long> deleteMeal(Long mealId) {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new RuntimeException("Meal not found"));

        List<Long> mealRemovedUsersIds = userRepository.findAll().stream()
                .map(user -> {
                    boolean removed = user.getMeals().removeIf(m -> m.getId().equals(meal.getId()));
                    if (removed) userRepository.save(user);
                    return user.getId();
                }).toList();

        mealRepository.delete(meal);

        return mealRemovedUsersIds;
    }
}
