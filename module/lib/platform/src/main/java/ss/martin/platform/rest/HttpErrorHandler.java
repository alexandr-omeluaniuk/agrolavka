package ss.martin.platform.rest;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ss.martin.security.model.RestResponse;

/**
 * HTTP errors interceptor.
 * @author ss
 */
@ControllerAdvice
class HttpErrorHandler {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(HttpErrorHandler.class);
    
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
    public RestResponse handleInternalError(final Exception ex) {
        LOG.error("Internal error", ex);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return new RestResponse(false, "Internal server error!", null, ex.getMessage(), sw.toString());
    }
}
