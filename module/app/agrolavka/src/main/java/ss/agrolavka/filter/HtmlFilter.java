package ss.agrolavka.filter;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
class HtmlFilter implements Filter {
    
    private final HtmlCompressor compressor = new HtmlCompressor();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        ServletResponse newResponse = response;

        if (request instanceof HttpServletRequest) {
            newResponse = new CharResponseWrapper((HttpServletResponse) response);
        }

        chain.doFilter(request, newResponse);

        if (newResponse instanceof CharResponseWrapper) {
            String text = newResponse.toString();
            if (text != null) {
                response.getWriter().write(compressor.compress(text));
            }
        }
    }
}
