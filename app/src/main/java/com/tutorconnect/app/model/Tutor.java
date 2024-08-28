package com.tutorconnect.app.model;

public class Tutor {
    private String email;
    private String password;
    private String id;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Tutor() {
    }

    public Tutor(String email, String password, String id) {
        this.email = email;
        this.password = password;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Tutor{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
