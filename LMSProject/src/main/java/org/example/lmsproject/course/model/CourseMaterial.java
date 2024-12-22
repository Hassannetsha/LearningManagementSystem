package org.example.lmsproject.course.model;

import jakarta.persistence.*;
import lombok.Setter;

@Entity
public class CourseMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long courseMaterialId;
    @Setter
    @Column(nullable = false)
    private String filename;
    private String path;

    @Setter
    @ManyToOne
    private Course course;

    public CourseMaterial() {}

    public CourseMaterial(String filename, String path) {
        this.filename = filename;
        this.path = path;
    }

    public long getCourseMaterialId() {
        return courseMaterialId;
    }

    public String getFilename() {
        return filename;
    }


    public String getPath() {
        return path;
    }

    public Course getCourse() {
        return course;
    }

}
