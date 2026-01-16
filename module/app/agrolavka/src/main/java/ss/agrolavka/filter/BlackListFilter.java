package ss.agrolavka.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ss.agrolavka.AgrolavkaConfiguration;

import java.io.IOException;

@Component
public class BlackListFilter implements Filter {

    @Autowired
    protected AgrolavkaConfiguration agrolavkaConfiguration;

    @Override
    public void doFilter(
        ServletRequest servletRequest,
        ServletResponse servletResponse,
        FilterChain filterChain
    ) throws IOException, ServletException {
        final var blackList = agrolavkaConfiguration.blacklistIp();
        if (
            servletRequest instanceof HttpServletRequest &&
                servletResponse instanceof HttpServletResponse &&
                blackList != null && !blackList.isEmpty() &&
                blackList.contains(servletRequest.getRemoteAddr())
        ) {
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_BAD_GATEWAY);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
