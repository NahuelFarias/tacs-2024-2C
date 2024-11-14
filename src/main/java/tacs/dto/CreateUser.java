package tacs.dto;

import lombok.Data;

@Data
public class CreateUser {
    private String username;
    private String password;
    private String email;

    public CreateUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public CreateUser() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
