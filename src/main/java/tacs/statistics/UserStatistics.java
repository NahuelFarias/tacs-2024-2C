package tacs.statistics;

import tacs.models.domain.users.NormalUser;

import java.util.List;

public class UserStatistics implements Statistics<NormalUser> {

    @Override
    public Integer generateStatistics(List<NormalUser> users) {
        return (int) users.stream().filter(u -> u.getLastLogin() != null).count();
    }

    @Override
    public String name() {
        return "Users";
    }
}
