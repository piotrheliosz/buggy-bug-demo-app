package app.controllers;

import app.domain.entities.User;
import app.domain.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    private User user;

    private final Faker fake = new Faker();

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port + "/api/users";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        user = new User(fake.name().fullName());
        user = userRepository.save(user);
    }

    @Test
    void shouldCreateUser() {
        String userName = fake.name().fullName();
        RequestSpecification specification =
                given().queryParam("name", userName);

        User newUser = specification.
                when().post().

                then()
                .statusCode(200)
                .extract()
                .as(User.class);

        assertEquals(userName, newUser.getName());
    }

    @Test
    void getUser() {
        RequestSpecification specification =
                given().queryParam("id", user.getId());

        User foundUser = specification.
                when().get().

                then()
                .statusCode(200)
                .extract()
                .as(User.class);

        assertEquals(user.toString(), foundUser.toString());
    }

    @Test
    void deleteUser() {
        when().delete("/{id}", user.getId()).
                then()
                .statusCode(204);
    }
}
