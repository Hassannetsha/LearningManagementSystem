package project_software.main.quiz.Entities.Question;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class QuestionBank {
    // @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionEntity> questions;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Id
    Long courseId;
}
