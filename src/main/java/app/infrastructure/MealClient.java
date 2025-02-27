package app.infrastructure;

import app.domain.services.MealFetcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@lombok.RequiredArgsConstructor
@Component
public class MealClient implements MealFetcher {

    @Value("${meal-db-api.url}")
    private String url;

    private final RestTemplate restTemplate;

    @Override
    public MealsResponseDTO.MealDTO fetchRandomMeal() {
        ResponseEntity<MealsResponseDTO> response = restTemplate
                .getForEntity(url + "/random.php", MealsResponseDTO.class);

        return response.getBody() != null ? response.getBody().getMeals().get(0) : null;
    }
}
