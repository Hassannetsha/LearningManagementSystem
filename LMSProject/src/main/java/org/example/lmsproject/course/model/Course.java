package org.example.lmsproject.course.model;

import lombok.Getter;
import lombok.Setter;
import org.example.lmsproject.quiz.model.Quiz.QuizEntity;
import org.example.lmsproject.userPart.model.*;
import org.example.lmsproject.assignment.model.Assignment;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Course {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Setter
    @Getter
    @Column(unique = true, nullable = false)
    private String title;

    @Getter
    @Setter
    private String description;

    @Setter
    @Getter
    private int duration;

    @Getter
    @Setter
    private Boolean available;

    @Setter
    @Getter
    @ManyToOne
    Instructor instructor;

    @Setter
    @Getter
    @ManyToMany(mappedBy = "courses")
    List<Student> students;

    @Getter
    @Setter
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Assignment> assignments;

    @Setter
    @Getter
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    List <Lesson> lessons;

    @Setter
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    List<QuizEntity> quizzes;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    List<CourseMaterial> courseMaterials;

    @Setter
    @Getter
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    List<CourseEnrollRequest> courseEnrollRequests;

    public Course(String title, String description, int duration, Boolean available) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.available = available;
    }

    public Course() {

    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
    }

    public void addMaterial(CourseMaterial material ) {
        this.courseMaterials.add(material);
    }

    /*public void removeMaterial(CourseMaterial material ) {
        this.courseMaterials.remove(material);
    }

    public void addLesson(Lesson lesson) {
        this.lessons.add(lesson);
    }

    public void removeLesson(Lesson lesson) {
        this.lessons.remove(lesson);
    }

    public void addAssignment(Assignment assignment) {
        this.assignments.add(assignment);
    }

    public void removeAssignment(Assignment assignment) {
        this.assignments.remove(assignment);
    }*/

    @Override
    public String toString() {
        return "Course{" +
                "\n    courseId=" + courseId +
                ",\n    title='" + title + '\'' +
                ",\n    description='" + description + '\'' +
                ",\n    duration=" + duration +
                ",\n    available=" + available +
                ",\n    instructorId=" + instructor.getId() +
                "}";
    }
}
