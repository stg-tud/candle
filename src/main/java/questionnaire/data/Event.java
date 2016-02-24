package questionnaire.data;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Event {

	public EventType type;

	public String auth;
	public String taskId;
	public String answer;

	public Date time = new Date();

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public enum EventType {
		PAGE_START, CODE_HIDDEN, SELECTION, SUBMISSION, TIMEOUT, RELOAD
	}
}