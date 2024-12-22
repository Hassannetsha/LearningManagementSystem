package org.example.lmsproject.course.model;

import jakarta.persistence.*;

@Entity
public class CourseMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courseMaterialId;
    @Column(nullable = false)
    private String filename;
    private String path;

    @ManyToOne
    private Course course;

    public CourseMaterial() {}

    public CourseMaterial(String filename, String path) {
        this.filename = filename;
        this.path = path;
    }

    public int getCourseMaterialId() {
        return courseMaterialId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
