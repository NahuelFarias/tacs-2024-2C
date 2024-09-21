package tacs.models.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tacs.models.domain.users.NormalUser;

public class UserTest {

    @Test
    public void generateUserTest() {
        String username = "Pepe Rodriguez";

        NormalUser user = new NormalUser(username);
        Assertions.assertEquals(user.username, username);
    }
}
