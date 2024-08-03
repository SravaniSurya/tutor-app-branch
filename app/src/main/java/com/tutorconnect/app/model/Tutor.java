package com.tutorconnect.app.model;

public class Tutor {
    private String email;
    private String password;
    private String id;


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public String getId() {
        return id;
    }

    public Tutor() {
    }

    public Tutor(String email, String password, String id) {
        this.email = email;
        this.password = password;
        this.id = id;
    }

    public String toString() {
        return "Tutor{" +
                "name='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
