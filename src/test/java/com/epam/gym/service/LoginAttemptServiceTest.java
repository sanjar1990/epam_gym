package com.epam.gym.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginAttemptServiceTest {

    private LoginAttemptService service;

    @BeforeEach
    void setUp() {
        service = new LoginAttemptService();
    }

    @Test
    void loginFailed_shouldIncreaseAttempts_andNotBlockBeforeLimit() {
        String user = "john";

        service.loginFailed(user);
        service.loginFailed(user);

        assertFalse(service.isBlocked(user));
    }

    @Test
    void loginFailed_shouldBlockUser_afterMaxAttempts() {
        String user = "john";

        service.loginFailed(user);
        service.loginFailed(user);
        service.loginFailed(user); // 3rd attempt

        assertTrue(service.isBlocked(user));
    }

    @Test
    void loginSucceeded_shouldResetAttemptsAndUnblockUser() {
        String user = "john";

        service.loginFailed(user);
        service.loginFailed(user);
        service.loginFailed(user);

        assertTrue(service.isBlocked(user));

        service.loginSucceeded(user);

        assertFalse(service.isBlocked(user));
    }

    @Test
    void isBlocked_shouldReturnFalse_whenUserNeverBlocked() {
        assertFalse(service.isBlocked("unknown"));
    }

    @Test
    void isBlocked_shouldUnblockUser_afterBlockTimeExpires() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        String user = "john";

        // trigger block
        service.loginFailed(user);
        service.loginFailed(user);
        service.loginFailed(user);

        assertTrue(service.isBlocked(user));

        // simulate time passing (minimal sleep, still acceptable here)
        Thread.sleep(10);

        // 🔥 hack: we cannot change BLOCK_TIME_MS, so simulate expiration manually
        // call isBlocked multiple times until it resets (not ideal but works)

        // force expiration by manipulating internal state via reflection
        var blockTimeField = service.getClass().getDeclaredField("blockTimeCache");
        blockTimeField.setAccessible(true);

        var map = (java.util.Map<String, Long>) blockTimeField.get(service);
        map.put(user, System.currentTimeMillis() - (6 * 60 * 1000)); // older than 5 min

        assertFalse(service.isBlocked(user));
    }

    @Test
    void loginFailed_shouldTrackAttemptsIndependently_perUser() {
        String user1 = "john";
        String user2 = "alice";

        service.loginFailed(user1);
        service.loginFailed(user1);
        service.loginFailed(user1); // blocked

        service.loginFailed(user2); // only 1 attempt

        assertTrue(service.isBlocked(user1));
        assertFalse(service.isBlocked(user2));
    }

    @Test
    void loginSucceeded_shouldOnlyAffectSpecificUser() {
        String user1 = "john";
        String user2 = "alice";

        // block both
        for (int i = 0; i < 3; i++) {
            service.loginFailed(user1);
            service.loginFailed(user2);
        }

        assertTrue(service.isBlocked(user1));
        assertTrue(service.isBlocked(user2));

        service.loginSucceeded(user1);

        assertFalse(service.isBlocked(user1));
        assertTrue(service.isBlocked(user2));
    }

    @Test
    void loginFailed_shouldKeepUserBlocked_afterMoreThanMaxAttempts() {
        String user = "john";

        for (int i = 0; i < 5; i++) {
            service.loginFailed(user);
        }

        assertTrue(service.isBlocked(user));
    }
}