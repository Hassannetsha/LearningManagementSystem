package org.example.lmsproject.course.model;

import org.example.lmsproject.quiz.model.Quiz.QuizEntity;
import org.example.lmsproject.userPart.model.*;
import org.example.lmsproject.assignment.model.Assignment;
//import org.example.lmsproject.quiz.model.Quiz.QuizEntity;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;
    private String title;
    private String description;
    private int duration;
    private Boolean available;

    @ManyToOne
    Instructor instructor;
    @ManyToMany(mappedBy = "courses")
    List<Student> students;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Assignment> assignments;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    List <Lesson> lessons;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    List<QuizEntity> quizzes;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    List<CourseMaterial> courseMaterials;

    public Course(String title, String description, int duration, Boolean available,  Instructor instructor) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.available = available;
        this.instructor = instructor;
    }

    public Course() {

    }

    public Long getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
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

    public void removeMaterial(CourseMaterial material ) {
        this.students.remove(material);
    }

    public List<QuizEntity> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<QuizEntity> quizzes) {
        this.quizzes = quizzes;
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
    }

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
