package ss.agrolavka.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BlackListFilter implements Filter {

    private static final List<String> blacklist = new ArrayList<>();

    static {
        blacklist.add("POST::/api/route");
        blacklist.add("POST::/app");
        blacklist.add("POST::/_next/server");
        blacklist.add("POST::/_next");
        blacklist.add("GET::/shops/php_info.php");
        blacklist.add("GET::/shops/phptest.php3");
        blacklist.add("GET::/shops/info.php3");
        blacklist.add("GET::/shops/phpinfo.php3");
    }

    @Override
    public void doFilter(
        ServletRequest servletRequest,
        ServletResponse servletResponse,
        FilterChain filterChain
    ) throws IOException, ServletException {
        final var request = (HttpServletRequest) servletRequest;
        final var match = request.getMethod() + "::" + request.getServletPath();
        if (blacklist.contains(match)) {
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_BAD_GATEWAY);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
