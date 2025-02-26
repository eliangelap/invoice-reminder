package br.dev.eliangela.invoice_reminder.web.error;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import br.dev.eliangela.invoice_reminder.config.EnvironmentConfiguration;
import br.dev.eliangela.invoice_reminder.core.model.schema.error.AppErrorSchema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@ControllerAdvice
@AllArgsConstructor
public class ExceptionInterceptor extends ResponseEntityExceptionHandler {

    private static final String NOT_FOUND = "Not Found";
    private static final String BAD_REQUEST = "Bad Request";
    private static final String GATEWAY_TIMEOUT = "Request Timeout";

    private final HttpServletRequest theRequest;
    private final EnvironmentConfiguration environment;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ResponseEntity<AppErrorSchema> handleAllExceptions(Exception ex, WebRequest request) {

        AppErrorSchema internalErrorSchema = handleError(ex, HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro não catalogado. Veja nos Logs.");

        printStackTrace(ex);

        return new ResponseEntity<>(internalErrorSchema, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            NotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ResponseEntity<AppErrorSchema> handleNotFound(Exception ex, WebRequest request) {

        return new ResponseEntity<>(handleError(ex, HttpStatus.NOT_FOUND, NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            BadRequestException.class,
            IOException.class,
            EnvironmentException.class,
            MultipartException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity<AppErrorSchema> handleBadRequest(Exception ex, WebRequest request) {

        return new ResponseEntity<>(handleError(ex, HttpStatus.BAD_REQUEST, BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            TokenExpiredException.class,
            JWTVerificationException.class,
            AuthenticationException.class,
            AccessDeniedException.class,
            BadCredentialsException.class
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<Object> handleNotLoggedIn(@NonNull BadCredentialsException ex, WebRequest request) {

        return new ResponseEntity<>(handleError(ex, HttpStatus.UNAUTHORIZED, "Você não está autenticado."),
                HttpStatus.UNAUTHORIZED);
    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(@NonNull AsyncRequestTimeoutException ex,
            @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {

        AppErrorSchema errorSchema = handleError(ex, HttpStatus.GATEWAY_TIMEOUT, GATEWAY_TIMEOUT);
        return super.handleExceptionInternal(ex, errorSchema, headers, HttpStatus.GATEWAY_TIMEOUT, request);
    }

    @Override
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handleNoHandlerFoundException(@NonNull NoHandlerFoundException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        AppErrorSchema notFoundSchema = handleError(ex, HttpStatus.NOT_FOUND, NOT_FOUND);

        return super.handleExceptionInternal(ex, notFoundSchema, headers, HttpStatus.NOT_FOUND, request);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleServletRequestBindingException(@NonNull ServletRequestBindingException ex,
            @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {

        return handleGenericInternalErrors(ex, headers, status, request);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex,
            @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {

        return handleGenericInternalErrors(ex, headers, status, request);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMissingServletRequestPart(@NonNull MissingServletRequestPartException ex,
            @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {

        return handleGenericInternalErrors(ex, headers, status, request);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            @NonNull MissingServletRequestParameterException ex,
            @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {

        return handleGenericInternalErrors(ex, headers, status, request);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMissingPathVariable(@NonNull MissingPathVariableException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status, @NonNull WebRequest request) {

        return handleGenericInternalErrors(ex, headers, status, request);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleTypeMismatch(@NonNull TypeMismatchException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status, @NonNull WebRequest request) {

        return handleGenericInternalErrors(ex, headers, status, request);
    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(@NonNull HttpMediaTypeNotSupportedException ex,
            @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {

        return handleGenericInternalErrors(ex, headers, status, request);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        List<FieldError> fieldErrors = ex.getFieldErrors();
        List<String> errorsList = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .filter(Objects::nonNull)
                .toList();

        AppErrorSchema appErrorSchema = getErrorSchema(ex, errorsList, HttpStatus.BAD_REQUEST, BAD_REQUEST);

        return super.handleExceptionInternal(ex, appErrorSchema, headers, status, request);
    }

    private ResponseEntity<Object> handleGenericInternalErrors(@NonNull Exception ex,
            @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        AppErrorSchema badRequestSchema = handleError(ex, HttpStatus.BAD_REQUEST, BAD_REQUEST);

        return super.handleExceptionInternal(ex, badRequestSchema, headers, status, request);
    }

    private AppErrorSchema getErrorSchema(Exception ex, List<String> messages, HttpStatus httpStatus,
            String errorTitle) {
        AppErrorSchema badRequestSchema = new AppErrorSchema();
        badRequestSchema.setTimestamp(new Date());
        badRequestSchema.setStatus(httpStatus.value());
        badRequestSchema.setError(errorTitle);

        badRequestSchema.setClassName(ex.getStackTrace()[0].getClassName());

        badRequestSchema.setMessages(messages);
        badRequestSchema.setPath(theRequest.getRequestURL().toString());

        return badRequestSchema;
    }

    private AppErrorSchema handleError(Exception ex, HttpStatus httpStatus, String errorTitle) {
        printStackTrace(ex);

        return getErrorSchema(
                ex,
                List.of(ex.getMessage() == null ? "" : ex.getMessage()),
                httpStatus, errorTitle);
    }

    private void printStackTrace(Exception ex) {
        if (environment.isDevelopment()) {
            ex.printStackTrace();
        }
    }
}
