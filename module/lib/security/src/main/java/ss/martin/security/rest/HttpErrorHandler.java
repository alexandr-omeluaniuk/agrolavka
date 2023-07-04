package ss.martin.security.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ss.martin.base.lang.ThrowingSupplier;
import ss.martin.security.api.AlertService;
import ss.martin.security.model.RestResponse;

/**
 * HTTP errors interceptor.
 * @author ss
 */
@ControllerAdvice
class HttpErrorHandler {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(HttpErrorHandler.class);
    
    @Autowired
    private AlertService alertService;
    
    /**
     * Handle access denied exception.
     * @param ex exception.
     * @return HTTP response data.
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public RestResponse handleAccessDenied(final AccessDeniedException ex) {
        LOG.warn("Access denied", ex);
        return new RestResponse(false, ex.getMessage());
    }
    
    /**
     * Handle internal error exception.
     * @param ex exception.
     * @return HTTP response data.
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RestResponse handleInternalError(final Exception ex, final HttpServletRequest request) {
        final var err = "HTTP request error!\n" + ex.getMessage() + "\n" + getRequestInfo(request);
        LOG.error(err, ex);
        alertService.sendAlert(err, ex);
        return new RestResponse(false, "Internal server error!", null, ex.getMessage(), "");
    }
    
    private String getRequestInfo(final HttpServletRequest request) {
        return ((ThrowingSupplier<String>) () -> {
            final var sb = new StringBuilder();
            sb.append("\n");
            sb.append(request.getMethod()).append(": ").append(request.getRequestURI()).append("\n");
            return sb.toString();
        }).get();
    }
}
