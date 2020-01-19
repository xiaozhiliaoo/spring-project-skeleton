package org.lili.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

public class CommonFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String filterName = filterConfig.getInitParameter("filterName");
        logger.info("filterName is:{}", filterName);
        return;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}
