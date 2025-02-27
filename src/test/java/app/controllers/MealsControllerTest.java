package app.controllers;

import app.infrastructure.MealsResponseDTO;
import app.domain.entities.Meal;
import app.domain.entities.User;
import app.domain.repository.MealRepository;
import app.domain.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import lombok.SneakyThrows;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MealsControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MealRepository mealRepository;

    private User user;
    private Meal meal_0;
    private Meal meal_1;

    private final Faker fake = new Faker();

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port + "/api/meals";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        meal_0 = new Meal(fake.food().dish(), fake.food().ingredient());
        mealRepository.save(meal_0);

        meal_1 = new Meal(fake.food().dish(), fake.food().ingredient());
        mealRepository.save(meal_1);

        user = new User(fake.name().fullName());
        user.getMeals().addAll(List.of(meal_0, meal_1));
        user = userRepository.save(user);
    }

    @Test
    void shouldGetMeal() {
        Meal foundMeal =
                when().get("/{id}", meal_1.getId())
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(Meal.class);

        assertEquals(foundMeal, meal_1);
    }


    @Test
    void shouldGetAllMeals() {
        List<Meal> actualMeals =
                when().get("/all")
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(new TypeRef<>() {
                        });

        assertTrue(actualMeals.containsAll(List.of(meal_0, meal_1)));
    }

    @Test
    @SneakyThrows
    void shouldAddMealToUser() {
        WireMock.configureFor("localhost", 8888);

        MealsResponseDTO.MealDTO mealDTO = new MealsResponseDTO.MealDTO();
        mealDTO.setIdMeal(fake.idNumber().valid());
        mealDTO.setStrMeal(fake.food().dish());
        mealDTO.setStrInstructions(fake.food().ingredient());

        MealsResponseDTO mealsResponseDTO = new MealsResponseDTO();
        mealsResponseDTO.setMeals(List.of(mealDTO));
        String dtoAsString = new ObjectMapper().writeValueAsString(mealsResponseDTO);

        stubFor(get(urlEqualTo("/random.php"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(dtoAsString)
                        .withHeader("Content-Type", "application/json")));

        User updatedUser =
                when().post("/{id}", user.getId())
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(User.class);

        Meal actualMeal = updatedUser.getMeals().stream()
                .filter(m -> m.getName().equals(mealDTO.getStrMeal()))
                .findAny()
                .orElseThrow(
                        () -> new AssertionError("Meal should be added to user")
                );

        assertEquals(mealDTO.getStrMeal(), actualMeal.getName());
        assertEquals(mealDTO.getStrInstructions(), actualMeal.getDescription());
    }

    @Test
    void shouldRemoveMeal() {
        List<Long> userIds =
                when().delete("/{id}", meal_0.getId())
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(new TypeRef<>() {
                        });

        assertTrue(userIds.contains(user.getId()));
    }

    @Test
    void shouldRemoveMealFromUser() {
        User updatedUser =
                when().delete("/{userId}/{mealId}", user.getId(), meal_0.getId())
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(User.class);

        assertFalse(updatedUser.getMeals().contains(meal_0));
        assertTrue(updatedUser.getMeals().contains(meal_1));
    }
}
