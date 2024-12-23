package org.example.lmsproject.course.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.lmsproject.userPart.model.Student;

@Entity
public class CourseEnrollRequest {

    @Getter @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long courseEnrollmentId;
    @Getter @Setter
    @ManyToOne
    Course course;
    @Setter @Getter
    @ManyToOne
    Student student;

    @Setter @Getter
    String status;

    public CourseEnrollRequest() {}


    @Override
    public String toString() {
        return "CourseEnrollRequest{" +
                "\n    courseEnrollId=" + courseEnrollmentId +
                ",\n    course=" + course.getCourseId() +
                ",\n    student=" +  student.getId() +
                ",\n    Status=" + status +
                "}";
    }
}
