package com.kostya.habittracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.kostya.habittracker.config.JpaConfig;
import com.kostya.habittracker.entity.User;
import com.kostya.habittracker.enums.UserRole;
import com.kostya.habittracker.enums.UserStatus;
import com.kostya.habittracker.repository.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(JpaConfig.class)
class UserTimezonePersistenceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void persistWithoutTimezoneFails() {
        User user = newUser("no-tz", "no-tz@example.com");
        user.setTimezone(null);

        assertThrows(Exception.class, () -> {
            userRepository.save(user);
            entityManager.flush();
        });
    }

    @Test
    void newUserDefaultsToUtc() {
        User user = newUser("utc-user", "utc-user@example.com");

        User saved = userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        User reloaded = userRepository.findByUsername("utc-user");
        assertEquals("UTC", saved.getTimezone());
        assertEquals("UTC", reloaded.getTimezone());
    }

    private User newUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("secret");
        user.setEmail(email);
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(UserRole.USER);
        return user;
    }
}
