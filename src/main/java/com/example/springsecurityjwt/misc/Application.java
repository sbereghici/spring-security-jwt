package com.example.springsecurityjwt.misc;

public enum Application {

    AUTHENTICATOR(-1),
    TEAM_ANALYSIS(1);

    private final int id;

    Application(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
