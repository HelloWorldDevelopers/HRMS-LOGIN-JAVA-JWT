package hrms.rnt.login.customeException;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class ApiError {

    private HttpStatus status;
    private String message;
    private List<String> errors;
    private boolean success;

    public ApiError(HttpStatus status, String message, List<String> errors,boolean success) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.success=success;

    }

    public ApiError(HttpStatus status, String message, String error,boolean success) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
        this.success=success;

 
    }
}