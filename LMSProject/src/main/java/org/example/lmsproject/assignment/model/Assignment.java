package org.example.lmsproject.assignment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.example.lmsproject.course.model.Course;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String title;
    @Setter
    @Column(length = 500)
    private String description;

    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    @Setter
    @ManyToOne
    private Course course;



    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL)
    private List<AssignmentSubmission> submissions;

    @Override
    public String toString() {
        return "Assignment{" +
                "\n    id=" + id +
                ",\n    title=" + title +
                ",\n    description=" + description +
                ",\n    deadline=" + deadline.toString() +
                ",\n    courseId=" + course.getCourseId() +
                "\n}";
    }
}
