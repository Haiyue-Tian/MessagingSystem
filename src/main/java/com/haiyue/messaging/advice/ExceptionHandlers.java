package com.haiyue.messaging.advice;

import com.haiyue.messaging.enums.Status;
import com.haiyue.messaging.exception.MessageServiceException;
import com.haiyue.messaging.response.CommonResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class ExceptionHandlers {

    /**
     * handle exception
     * @param messageServiceException
     * @return response body
     */
    @ExceptionHandler(MessageServiceException.class)
    public ResponseEntity<CommonResponse> handleMessageServiceException(MessageServiceException messageServiceException){
        log.info("Encountered an exception: ", messageServiceException);
        return new ResponseEntity<>(
                new CommonResponse(messageServiceException.getStatus()),
                messageServiceException.getStatus().getHttpStatus());
    }

    /**
     * handle exception
     * @param exception
     * @return response body
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> handleException(Exception exception){
        log.info("Encountered an exception: ", exception);
        return new ResponseEntity<>(
                new CommonResponse(Status.UNKNOWN_ERROR),
                Status.UNKNOWN_ERROR.getHttpStatus());
    }
}
