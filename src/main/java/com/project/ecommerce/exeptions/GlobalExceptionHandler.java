package com.project.ecommerce.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.project.ecommerce.dto.ExceptionResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BindException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ExceptionResponseDTO handleBindException(BindException e) {
        // Trả về message của lỗi đầu tiên
        String errorMessage = "Request không hợp lệ";
        if (e.getBindingResult().hasErrors())
            errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new ExceptionResponseDTO("error", errorMessage);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ExceptionResponseDTO> resourceNotFoundException(ResourceNotFoundException e) {
		String message = e.getMessage();

		ExceptionResponseDTO res = new ExceptionResponseDTO("error", message);

		return new ResponseEntity<ExceptionResponseDTO>(res, HttpStatus.NOT_FOUND);
	}

    @ExceptionHandler(APIException.class)
	public ResponseEntity<ExceptionResponseDTO> apiException(APIException e) {
		String message = e.getMessage();

		ExceptionResponseDTO res = new ExceptionResponseDTO("error", message);

		return new ResponseEntity<ExceptionResponseDTO>(res, HttpStatus.BAD_REQUEST);
	}

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ExceptionResponseDTO missingServletRequestParamException(MissingServletRequestParameterException e) {
        String errorMessage = e.getMessage();
        return new ExceptionResponseDTO("error", errorMessage);
    }
}
