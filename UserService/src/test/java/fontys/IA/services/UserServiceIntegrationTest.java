package fontys.IA.services;

import fontys.IA.domain.AESEncrypter;
import fontys.IA.domain.User;
import fontys.IA.domain.enums.UserRole;
import fontys.IA.repositories.IUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private IUserRepository userRepository;

    private User testUser;
    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_USERNAME = "testuser";
    private final UserRole TEST_ROLE = UserRole.USER;

    @BeforeEach
    public void setUp() {
        // Create a test user
        UUID userId = UUID.randomUUID();
        testUser = new User(userId, TEST_EMAIL, TEST_USERNAME, TEST_ROLE);
    }

    @AfterEach
    public void tearDown() {
        // Clean up the database after each test
        userRepository.deleteAll();
    }

    @Test
    public void addUser_SuccessTest() {
        // Act
        userService.addUser(testUser);
        Optional<User> savedUser = userRepository.findById(testUser.getId());

        // Assert
        assertTrue(savedUser.isPresent());
        assertNotEquals(TEST_EMAIL, savedUser.get().getEmail()); // is email encrypted
        assertEquals(TEST_EMAIL, AESEncrypter.decrypt(savedUser.get().getEmail())); // can email be decrypted correctly
        assertEquals(TEST_USERNAME, savedUser.get().getUsername());
        assertEquals(TEST_ROLE, savedUser.get().getRole());
    }

    @Test
    public void getUser_SuccessTest() {
        // Arrange
        String encryptedEmail = AESEncrypter.encrypt(TEST_EMAIL);
        User encryptedUser = new User(testUser.getId(), encryptedEmail, TEST_USERNAME, TEST_ROLE);
        userRepository.save(encryptedUser);

        // Act
        User retrievedUser = userService.getUser(testUser.getId().toString());

        // Assert
        assertNotNull(retrievedUser);
        assertEquals(testUser.getId(), retrievedUser.getId());
        assertEquals(TEST_EMAIL, retrievedUser.getEmail());
        assertEquals(TEST_USERNAME, retrievedUser.getUsername());
        assertEquals(TEST_ROLE, retrievedUser.getRole());
    }

    @Test
    public void getNonExistentUser_SuccessTest() {
        // Act
        User retrievedUser = userService.getUser(UUID.randomUUID().toString());

        // Assert
        assertNull(retrievedUser);
    }

    @Test
    public void addDuplicateEmail_FailureTest() {
        // Arrange
        userService.addUser(testUser);

        // Create another user with the same email
        User duplicateUser = new User(UUID.randomUUID(), TEST_EMAIL, "anotheruser", UserRole.ADMIN);

        // Assert
        assertThrows(DataIntegrityViolationException.class, () -> {
            userService.addUser(duplicateUser);
        });

        // Verify that the original user data is still in the database
        Optional<User> savedUser = userRepository.findById(testUser.getId());
        assertTrue(savedUser.isPresent());
    }
}
