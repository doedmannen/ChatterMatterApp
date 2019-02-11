package Server.ServerApp;

import Common.User;

import java.util.concurrent.LinkedBlockingDeque;

public class Client {
    protected LinkedBlockingDeque<String> MESSAGES;
    private User user;

    public Client(String name) {
        this.user = new User(name);
        MESSAGES = new LinkedBlockingDeque<>();
    }

    protected String getUserName() {
        return user.getName();
    }
}
