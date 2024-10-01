package hrms.rnt.login.globelexception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import hrms.rnt.login.customeException.ApiError;
import hrms.rnt.login.customeException.CustomeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	
	@ExceptionHandler({CustomeException.class})
	public ResponseEntity<ApiError> customeExceptionMethod(Exception e){
		if(e.getCause() instanceof BadCredentialsException) {
		return ResponseEntity.ok(new ApiError(HttpStatus.NOT_FOUND,"you are not authorised",e.getMessage(),false));
		}
		return ResponseEntity.ok(new ApiError(HttpStatus.NOT_FOUND,"Something went wrong", e.getMessage(),false));
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		return ResponseEntity.ok(new ApiError(HttpStatus.METHOD_NOT_ALLOWED,"Methode Not Allowed",e.getMessage(),false));
    }
	
 }
