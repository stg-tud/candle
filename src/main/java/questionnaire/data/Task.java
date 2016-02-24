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

        public boolean isOpen; // no timeout?
        public boolean isBlind; // vs. clear
        public boolean isMeasured; // vs. fixed
        public boolean isGraded; // to show a warning?

        public int codeTime; // time the code is shown
        public int questionTime; // time the question is shown

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