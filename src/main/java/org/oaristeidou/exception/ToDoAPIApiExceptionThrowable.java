package org.oaristeidou.exception;

import org.openapitools.model.ToDoApiException;
import org.springframework.http.HttpStatus;

/**
 * ToDoAPIApiExceptionThrowable is a custon throwable To-Do Service Application
 */
public class ToDoAPIApiExceptionThrowable extends Throwable {
  private ToDoApiException toDoApiException;
  private HttpStatus httpStatus;

  public ToDoAPIApiExceptionThrowable(ToDoApiException toDoApiException,
      HttpStatus httpStatus) {
    super();
    this.toDoApiException = toDoApiException;
    this.httpStatus = httpStatus;
  }

  public ToDoApiException getToDoApiException() {
    return toDoApiException;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
