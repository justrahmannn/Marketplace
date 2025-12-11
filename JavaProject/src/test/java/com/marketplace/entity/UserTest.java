import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

package com.marketplace.entity;




class UserTest {

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        User user = new User();

        // Assert
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getSurname());
        assertNull(user.getType());
        assertNull(user.getPassword());
        assertNull(user.getCreatedAt());
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        User user = new User();
        Long id = 1L;

        // Act
        user.setId(id);

        // Assert
        assertEquals(id, user.getId());
    }

    @Test
    void testSetAndGetName() {
        // Arrange
        User user = new User();
        String name = "John";

        // Act
        user.setName(name);

        // Assert
        assertEquals(name, user.getName());
    }

    @Test
    void testSetAndGetSurname() {
        // Arrange
        User user = new User();
        String surname = "Doe";

        // Act
        user.setSurname(surname);

        // Assert
        assertEquals(surname, user.getSurname());
    }

    @Test
    void testSetAndGetType() {
        // Arrange
        User user = new User();
        User.UserType type = User.UserType.CUSTOMER;

        // Act
        user.setType(type);

        // Assert
        assertEquals(type, user.getType());
    }

    @Test
    void testSetAndGetPassword() {
        // Arrange
        User user = new User();
        String password = "hashedPassword";

        // Act
        user.setPassword(password);

        // Assert
        assertEquals(password, user.getPassword());
    }

    @Test
    void testSetAndGetCreatedAt() {
        // Arrange
        User user = new User();
        LocalDateTime createdAt = LocalDateTime.now();

        // Act
        user.setCreatedAt(createdAt);

        // Assert
        assertEquals(createdAt, user.getCreatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(1L);

        User user3 = new User();
        user3.setId(2L);

        // Act & Assert
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setSurname("Doe");
        user.setType(User.UserType.MERCHANT);

        // Act
        String result = user.toString();

        // Assert
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("name=John"));
        assertTrue(result.contains("surname=Doe"));
        assertTrue(result.contains("type=MERCHANT"));
    }
}