package com.tutorconnect.app.model;

public class Remark {
    private String studentName;
    private String studentSubject;
    private Boolean present;
    private String remarks;
    private String date;
    private String studentId;
    private String tutorId;
    private String parentId;
    private String review;

    public String getStudentName() {
        return studentName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentSubject() {
        return studentSubject;
    }

    public void setStudentSubject(String studentSubject) {
        this.studentSubject = studentSubject;
    }

    public Remark() {
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(Boolean present) {
        this.present = present;
    }

    public String getDate() {
        return date;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getParentId() {
        return parentId;
    }

    public String getReview() {
        return review;
    }

    public Remark(String studentName, String studentSubject, Boolean present, String date, String remarks,
                  String review, String studentId, String tutorId, String parentId) {
        this.studentName = studentName;
        this.studentSubject = studentSubject;
        this.present = present;
        this.date = date;
        this.remarks = remarks;
        this.studentId = studentId;
        this.tutorId = tutorId;
        this.parentId = parentId;
        this.review = review;
    }
}
