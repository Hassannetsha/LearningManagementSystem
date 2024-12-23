package org.example.lmsproject.assignment.model;

import lombok.Getter;
import lombok.Setter;
import org.example.lmsproject.userPart.model.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AssignmentSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @ManyToOne
    private Assignment assignment;

    @Setter
    @Getter
    @ManyToOne
    private User student;

    @Setter
    @Getter
    private String fileName;
    @Setter
    @Getter
    @Lob
    @Column(name = "file_content", columnDefinition = "LONGBLOB")
    private byte[] fileContent;

    @Setter
    @Getter
    private LocalDateTime submissiontime;

    @Setter
    @Getter
    private Integer grade;

    @Setter
    @Getter
    private String feedback;

    @Override
    public String toString() {
        return "AssignmentSubmission{" +
                "\n    filename" + fileName +
                ",\n    submissiontime" + submissiontime.toString() +
                ",\n    grade" + grade +
                ",\n    feedback" + feedback +
                ",\n    assignmentId" + assignment.getId();
    }
}

