package org.example.lmsproject.quiz.model.Question;

import org.example.lmsproject.course.model.Course;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(
        name = "question"
        
)
@AllArgsConstructor
@NoArgsConstructor
public abstract class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String question;
    @Column(nullable = false)
    private String type;
    // @Column(nullable = false)
    @ManyToOne
    private Course course;
    // private Long courseId;
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public abstract int calculateScore(Object userAnswer);

    // public Long getCourseId() {
    //     return courseId;
    // }

    // public void setCourseId(Long courseId) {
    //     this.courseId = courseId;
    // }

    // public Course getCourse() {
    //     return course;
    // }

    // public void setCourse(Course course) {
    //     this.course = course;
    // }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
