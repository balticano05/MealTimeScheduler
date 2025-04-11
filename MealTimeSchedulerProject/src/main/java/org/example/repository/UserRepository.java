package org.example.repository;

import org.example.entity.User;
import org.example.utils.XmlUtils;

import java.io.IOException;

public class UserRepository {

    private static final String USER_FILE = "src/main/resources/user-data.xml";

    public void saveUser(User user) {
        try {
            XmlUtils.serializeToXml(user, USER_FILE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save user data", e);
        }
    }

    public User loadUser() {
        try {
            return XmlUtils.deserializeFromXml(USER_FILE, User.class);
        } catch (IOException e) {
            return new User(70, 175, 30, User.ActivityLevel.MEDIUM);
        }
    }

}
