package lv.ctco.zephyr.beans.jira;

import java.util.List;
import java.util.Map;


public class ErrorResponse {
    private List<String> errorMessages;
    private Map<String, String> errors;

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
