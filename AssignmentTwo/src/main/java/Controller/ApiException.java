/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Andrew
 */
public class ApiException {
    private final String message;
     //private final Throwable throwable;
      private final HttpStatus httpStatus;
       private final ZonedDateTime timestamp;

    public ApiException(String message, HttpStatus httpStatus, ZonedDateTime timestamp) {
        this.message = message;
        //this.throwable = throwable;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }
//
//    public Throwable getThrowable() {
//        return throwable;
//    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
       
       
}
