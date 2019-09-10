package io.github.viscent.mtia.ch6;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/memoryLeak")
public class ThreadLocalCleanupFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
        ThreadLocalMemoryLeak.counterHolder.remove();
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        // 什么也不做
    }

    @Override
    public void destroy() {
        // 什么也不做
    }
}
