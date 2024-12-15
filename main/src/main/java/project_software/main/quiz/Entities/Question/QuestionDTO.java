package project_software.main.quiz.Entities.Question;

import java.util.List;

public class QuestionDTO  {
    private String type;
    private String questionText;

    private List<String> options;
    private String correctOption;

    private Boolean correctAnswer;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public Boolean getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    @Override
    public String toString(){
        return type + "\n" + questionText + "\n" + options + "\n" + correctOption + "\n" + correctAnswer;
    }
}
