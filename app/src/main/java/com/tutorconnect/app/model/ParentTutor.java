package com.tutorconnect.app.model;

public class ParentTutor {
    private String name;
    private String id;
    private String studentId;
    private String tutorId;
    private String email;
    private String password;
    private Boolean requirePasswordChange;
    private String compositeKey; // used to filter the parent with student and tutor,
    // Eg: ejfbvjebrvfbev_dkjcbkjbkcjbd, as firebase doesn't support query with multiple field,
    // composite method can be used..!


    public ParentTutor() {
    }

    public ParentTutor(String name, String email, String password, String id,
                       String studentId, String tutorId, String compositeKey, boolean requirePasswordChange) {
        this.name = name;
        this.id = id;
        this.studentId = studentId;
        this.tutorId = tutorId;
        this.email = email;
        this.password = password;
        this.compositeKey = compositeKey;
        this.requirePasswordChange = requirePasswordChange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
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

    public String getCompositeKey() {
        return compositeKey;
    }

    public void setCompositeKey(String compositeKey) {
        this.compositeKey = compositeKey;
    }

    public Boolean getRequirePasswordChange() {
        return requirePasswordChange;
    }

    public void setRequirePasswordChange(Boolean requirePasswordChange) {
        this.requirePasswordChange = requirePasswordChange;
    }
}
