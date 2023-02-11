package microservices.examples.system;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "entity not found")
public class NotFoundException extends RuntimeException{

	private static final long serialVersionUID = -4169903183511341269L;

	public NotFoundException(String message) {
		super(message);
	}

}
