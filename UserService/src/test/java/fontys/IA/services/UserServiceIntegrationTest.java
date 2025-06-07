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
        // Create a test user with a random UUID
        UUID userId = UUID.randomUUID();
        testUser = new User(userId, TEST_EMAIL, TEST_USERNAME, TEST_ROLE);
    }

    @AfterEach
    public void tearDown() {
        // Clean up the database after each test
        userRepository.deleteAll();
    }

    @Test
    public void testAddUser() {
        // Act
        userService.addUser(testUser);

        // Assert
        Optional<User> savedUser = userRepository.findById(testUser.getId());
        assertTrue(savedUser.isPresent(), "User should be saved in the database");

        // Verify the email is encrypted
        assertNotEquals(TEST_EMAIL, savedUser.get().getEmail(), "Email should be encrypted");

        // Verify the email can be decrypted correctly
        assertEquals(TEST_EMAIL, AESEncrypter.decrypt(savedUser.get().getEmail()),
                "Decrypted email should match original");

        // Verify other fields
        assertEquals(TEST_USERNAME, savedUser.get().getUsername(), "Username should match");
        assertEquals(TEST_ROLE, savedUser.get().getRole(), "Role should match");
    }

    @Test
    public void testGetUser() {
        // Arrange
        String encryptedEmail = AESEncrypter.encrypt(TEST_EMAIL);
        User encryptedUser = new User(testUser.getId(), encryptedEmail, TEST_USERNAME, TEST_ROLE);
        userRepository.save(encryptedUser);

        // Act
        User retrievedUser = userService.getUser(testUser.getId().toString());

        // Assert
        assertNotNull(retrievedUser, "Retrieved user should not be null");
        assertEquals(testUser.getId(), retrievedUser.getId(), "User ID should match");
        assertEquals(TEST_EMAIL, retrievedUser.getEmail(), "Email should be decrypted correctly");
        assertEquals(TEST_USERNAME, retrievedUser.getUsername(), "Username should match");
        assertEquals(TEST_ROLE, retrievedUser.getRole(), "Role should match");
    }

    @Test
    public void testGetNonExistentUser() {
        // Act
        User retrievedUser = userService.getUser(UUID.randomUUID().toString());

        // Assert
        assertNull(retrievedUser, "Non-existent user should return null");
    }

    @Test
    public void testAddDuplicateUser() {
        // Arrange
        userService.addUser(testUser);

        // Create another user with the same ID but different details
        User duplicateUser = new User(
                testUser.getId(),
                "another@example.com",
                "anotheruser",
                UserRole.ADMIN
        );

        // Act & Assert
        // Currently, the service doesn't throw an exception for duplicate users
        // It just logs a TODO comment. We can test the current behavior:
        userService.addUser(duplicateUser);

        // Verify that the original user data is still in the database
        Optional<User> savedUser = userRepository.findById(testUser.getId());
        assertTrue(savedUser.isPresent(), "User should still exist in the database");

        // The current implementation would overwrite the existing user
        // If you implement the exception later, this test would need to be updated
    }
}
