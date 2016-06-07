package questionnaire.data;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Task {

    public TaskType taskType = new TaskType();
    public AnswerType answerType = AnswerType.MULTIPLE_CHOICE;

    public String id;
    public String title;
    public String description;
    public String code;

    public String question;
    public List<String> options;

    public String answer;

    public boolean hasAnswer() {
        return answer != null;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public static class TaskType {

        public boolean isOpen; // no timeout
        public boolean isBlind; // code is hidden after codeTime and only question can b seen then
        public boolean isMeasured; // button is always there, time to completion is measured, vs. auto-completion after fixed time
        public boolean isGraded; // shows a warning that the result is graded

        public int codeTime; // time the code is shown (ignored if code is never hidden)
        public int questionTime; // time the question is shown until auto-completion

        @Override
        public boolean equals(Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj);
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }
    }

    public enum AnswerType {
        MULTIPLE_CHOICE, FREE_TEXT
    }
}