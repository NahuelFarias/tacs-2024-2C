package tacs.statistics;

import tacs.models.domain.users.User;

import java.util.List;

public class UserStatistics implements Statistics<User> {

    @Override
    public Integer generateStatistics(List<User> users) {
        return (int) users.stream().filter(u -> u.getLastLogin() != null).count();
    }

    @Override
    public String name() {
        return "Users";
    }
}
