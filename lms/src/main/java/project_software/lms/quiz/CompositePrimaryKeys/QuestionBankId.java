// package project_software.main.quiz.CompositePrimaryKeys;
package project_software.lms.quiz.CompositePrimaryKeys;

import java.io.Serializable;
import java.util.Objects;

public class QuestionBankId implements Serializable {
    private Long id;
    private Long courseId;

    public QuestionBankId() {}

    public QuestionBankId(Long id, Long courseId) {
        this.id = id;
        this.courseId = courseId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionBankId that = (QuestionBankId) o;
        return Objects.equals(id, that.id) && Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseId);
    }
}