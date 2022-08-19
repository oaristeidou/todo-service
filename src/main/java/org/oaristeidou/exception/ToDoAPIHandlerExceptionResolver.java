package org.oaristeidou.exception;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

@Component
public class ToDoAPIHandlerExceptionResolver extends AbstractHandlerExceptionResolver {

  private static final Logger LOGGER = LoggerFactory.getLogger(ToDoAPIHandlerExceptionResolver.class);

  public ToDoAPIHandlerExceptionResolver() {
    this.setOrder(2147483647);
    this.setWarnLogCategory(this.getClass().getName());
  }

  @Nullable
  protected ModelAndView doResolveException(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @Nullable Object handler, @NotNull Exception ex) {
    try {
      if (ex instanceof HttpRequestMethodNotSupportedException) {
        return this.handleHttpRequestMethodNotSupported((HttpRequestMethodNotSupportedException)ex, request, response, handler);
      }

      if (ex instanceof HttpMediaTypeNotSupportedException) {
        return this.handleHttpMediaTypeNotSupported((HttpMediaTypeNotSupportedException)ex, request, response, handler);
      }

      if (ex instanceof HttpMediaTypeNotAcceptableException) {
        return this.handleHttpMediaTypeNotAcceptable((HttpMediaTypeNotAcceptableException)ex, request, response, handler);
      }

      if (ex instanceof MissingPathVariableException) {
        return this.handleMissingPathVariable((MissingPathVariableException)ex, request, response, handler);
      }

      if (ex instanceof MissingServletRequestParameterException) {
        return this.handleMissingServletRequestParameter((MissingServletRequestParameterException)ex, request, response, handler);
      }

      if (ex instanceof ServletRequestBindingException) {
        return this.handleServletRequestBindingException((ServletRequestBindingException)ex, request, response, handler);
      }

      if (ex instanceof ConversionNotSupportedException) {
        return this.handleConversionNotSupported((ConversionNotSupportedException)ex, request, response, handler);
      }

      if (ex instanceof TypeMismatchException) {
        return this.handleTypeMismatch((TypeMismatchException)ex, request, response, handler);
      }

      if (ex instanceof HttpMessageNotReadableException) {
        return this.handleHttpMessageNotReadable((HttpMessageNotReadableException)ex, request, response, handler);
      }

      if (ex instanceof HttpMessageNotWritableException) {
        return this.handleHttpMessageNotWritable((HttpMessageNotWritableException)ex, request, response, handler);
      }

      if (ex instanceof MethodArgumentNotValidException) {
        return this.handleMethodArgumentNotValidException((MethodArgumentNotValidException)ex, request, response, handler);
      }

      if (ex instanceof MissingServletRequestPartException) {
        return this.handleMissingServletRequestPartException((MissingServletRequestPartException)ex, request, response, handler);
      }

      if (ex instanceof BindException) {
        return this.handleBindException((BindException)ex, request, response, handler);
      }

      if (ex instanceof NoHandlerFoundException) {
        return this.handleNoHandlerFoundException((NoHandlerFoundException)ex, request, response, handler);
      }

      if (ex instanceof AsyncRequestTimeoutException) {
        return this.handleAsyncRequestTimeoutException((AsyncRequestTimeoutException)ex, request, response, handler);
      }
    } catch (Exception var6) {
      if (this.logger.isWarnEnabled()) {
        this.logger.warn("Failure while trying to resolve exception [" + ex.getClass().getName() + "]", var6);
      }
    }

    return null;
  }

  protected ModelAndView handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
    String[] supportedMethods = ex.getSupportedMethods();
    if (supportedMethods != null) {
      response.setHeader("Allow", StringUtils.arrayToDelimitedString(supportedMethods, ", "));
    }

    LOGGER.warn(ex.getMessage());
    response.sendError(405, ex.getMessage());
    return new ModelAndView();
  }

  protected ModelAndView handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
    response.sendError(415);
    List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
    if (!CollectionUtils.isEmpty(mediaTypes)) {
      response.setHeader("Accept", MediaType.toString(mediaTypes));
    }

    LOGGER.warn(ex.getMessage());
    return new ModelAndView();
  }

  protected ModelAndView handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
    response.sendError(406);
    LOGGER.warn(ex.getMessage());
    return new ModelAndView();
  }

  protected ModelAndView handleMissingPathVariable(MissingPathVariableException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
    response.sendError(500, ex.getMessage());
    LOGGER.warn(ex.getMessage());
    return new ModelAndView();
  }

  protected ModelAndView handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
    response.sendError(400, ex.getMessage());
    LOGGER.warn(ex.getMessage());
    return new ModelAndView();
  }

  protected ModelAndView handleServletRequestBindingException(ServletRequestBindingException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
    response.sendError(400, ex.getMessage());
    LOGGER.warn(ex.getMessage());
    return new ModelAndView();
  }

  protected ModelAndView handleConversionNotSupported(ConversionNotSupportedException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
    this.sendServerError(ex, request, response);
    LOGGER.warn(ex.getMessage());
    return new ModelAndView();
  }

  protected ModelAndView handleTypeMismatch(TypeMismatchException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
    response.sendError(400);
    LOGGER.warn(ex.getMessage());
    return new ModelAndView();
  }

  protected ModelAndView handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
    response.sendError(400, ex.getCause().getCause().getMessage());
    LOGGER.warn(ex.getMessage());
    return new ModelAndView();
  }

  protected ModelAndView handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
    this.sendServerError(ex, request, response);
    LOGGER.warn(ex.getMessage());
    return new ModelAndView();
  }

  protected ModelAndView handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
    response.sendError(HttpServletResponse.SC_BAD_REQUEST, getExceptionMessage(ex));
    LOGGER.warn(ex.getMessage());
    return new ModelAndView();
  }

  protected ModelAndView handleMissingServletRequestPartException(MissingServletRequestPartException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
    response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
    LOGGER.warn(ex.getMessage());
    return new ModelAndView();
  }

  protected ModelAndView handleBindException(BindException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
    response.sendError(400);
    LOGGER.warn(ex.getMessage());
    return new ModelAndView();
  }

  protected ModelAndView handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
    response.sendError(404);
    LOGGER.warn(ex.getMessage());
    return new ModelAndView();
  }

  protected ModelAndView handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
    if (!response.isCommitted()) {
      response.sendError(503);
      LOGGER.warn(ex.getMessage());
    } else {
      LOGGER.warn("Async request timed out: " + ex.getMessage());
    }

    return new ModelAndView();
  }

  protected void sendServerError(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
    request.setAttribute("javax.servlet.error.exception", ex);
    response.sendError(500);
  }

  @NotNull
  private String getExceptionMessage(MethodArgumentNotValidException ex) {
    StringBuilder stringBuilder = new StringBuilder("MethodArgumentNotValidException error: ");
    Optional.ofNullable(ex)
        .map(MethodArgumentNotValidException::getBindingResult)
        .map(Errors::getAllErrors)
        .stream().flatMap(Collection::stream)
        .findFirst()
        .ifPresent(objectError -> {
          if (objectError.getCodes() != null && objectError.getCodes().length > 0)
            stringBuilder.append(objectError.getCodes()[0]);
          stringBuilder.append(" ");
          stringBuilder.append(objectError.getDefaultMessage());
        });
    return stringBuilder.toString();
  }
}
