package com.tutorconnect.app.model;

public class Student {
    private String name;
    private String subject;
    private String email;
    private String password;
    private String id;
    private String tutorId;
    private Boolean requirePasswordChange;
    private String parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

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

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public Boolean getRequirePasswordChange() {
        return requirePasswordChange;
    }

    public void setRequirePasswordChange(Boolean requirePasswordChange) {
        this.requirePasswordChange = requirePasswordChange;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Student() {
    }

    public Student(String name, String subject, String email,
                   String password,
                   String id,
                   String tutorId,
                   Boolean requirePasswordChange, String parentId) {
        this.name = name;
        this.subject = subject;
        this.email = email;
        this.password = password;
        this.id = id;
        this.tutorId = tutorId;
        this.requirePasswordChange = requirePasswordChange;
        this.parentId = parentId;
    }
}
