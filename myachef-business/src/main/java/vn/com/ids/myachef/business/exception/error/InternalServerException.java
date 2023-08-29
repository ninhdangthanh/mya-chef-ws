package vn.com.ids.myachef.business.exception.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    private final String message;

    private final String messageDescription;

    public InternalServerException(String message, String messageDescription) {
        super();
        this.message = message;
        this.messageDescription = messageDescription;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getMessageDescription() {
        return messageDescription;
    }
}
