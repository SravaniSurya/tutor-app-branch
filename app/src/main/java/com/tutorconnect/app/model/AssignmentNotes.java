package com.tutorconnect.app.model;

public class AssignmentNotes {
    private String id; // Assignment ID
    private String name;
    private String url;
    private String tutorId;
    private String studentId; // Optional field for student ID

    // Default constructor
    public AssignmentNotes() {
    }

    // Parameterized constructor
    public AssignmentNotes(String id, String name, String url, String tutorId) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.tutorId = tutorId;
    }

    // Parameterized constructor with studentId
    public AssignmentNotes(String id, String name, String url, String tutorId, String studentId) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.tutorId = tutorId;
        this.studentId = studentId;
    }

    // Getter for id
    public String getId() {
        return id;
    }

    // Setter for id
    public void setId(String id) {
        this.id = id;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for url
    public String getUrl() {
        return url;
    }

    // Setter for url
    public void setUrl(String url) {
        this.url = url;
    }

    // Getter for tutorId
    public String getTutorId() {
        return tutorId;
    }

    // Setter for tutorId
    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    // Getter for studentId (optional)
    public String getStudentId() {
        return studentId;
    }

    // Setter for studentId (optional)
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
