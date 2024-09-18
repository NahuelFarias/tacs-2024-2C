package tacs.models.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tacs.models.domain.users.User;

public class UserTest {

    @Test
    public void generateUserTest() {
        String username = "Pepe Rodriguez";

        User user = new User(username);
        Assertions.assertEquals(user.username, username);
    }
}
