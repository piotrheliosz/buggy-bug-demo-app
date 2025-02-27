package app.application;

import app.domain.entities.User;
import app.domain.repository.UserRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final Faker fake = new Faker();

    @Test
    void createUser() {
        // Given
        User user = new User(fake.name().firstName());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User createdUser = userService.createUser(user.getName());

        // Then
        assertNotNull(createdUser);
        assertEquals(user.getName(), createdUser.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUserById() {
        // Given
        User user = new User(fake.name().firstName());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When
        User foundUser = userService.getUserById(user.getId());

        // Then
        assertNotNull(foundUser);
        assertEquals(user.getName(), foundUser.getName());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void deleteUser() {
        // Given
        User user = new User(fake.name().firstName());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When
        userService.deleteUser(user.getId());

        // Then
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void getUserByIdThrowUserNotFound() {
        // When
        Exception exception = assertThrows(RuntimeException.class, () ->
                userService.getUserById(1L));

        // Then
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void deleteUserThrowUserNotFound() {
        // When
        Exception exception = assertThrows(RuntimeException.class, () ->
                userService.deleteUser(1L));

        // Then
        assertEquals("User not found", exception.getMessage());
    }
}
