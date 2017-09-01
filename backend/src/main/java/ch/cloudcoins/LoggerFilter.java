package ch.cloudcoins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Servlet filter that logs all incoming requests and generates a correlation ID that will be used for all log outputs
 * to a specific request.
 */
public class LoggerFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(LoggerFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        long startTime = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();

        // set Mapped Diagnostic Context so all application log events get decorated with it
        logMdc(httpServletRequest);

        // let the application run
        chain.doFilter(httpServletRequest, httpServletResponse);

        // log the request itself
        sb.append(httpServletRequest.getMethod()).append(" ").append(httpServletRequest.getRequestURI());
        if (!StringUtil.isNullOrEmpty(httpServletRequest.getQueryString())) {
            sb.append("?");
            sb.append(httpServletRequest.getQueryString());
        }
        sb.append(" ").append(httpServletRequest.getProtocol());

        MDC.put("status", String.valueOf(httpServletResponse.getStatus()));
        MDC.put("duration", String.valueOf(System.currentTimeMillis() - startTime));

        LOG.info(sb.toString());
        clearMdc();
    }

    private static void logMdc(HttpServletRequest httpServletRequest) {
        String remoteAddr = httpServletRequest.getRemoteAddr();
        if (!StringUtil.isNullOrEmpty(remoteAddr)) {
            MDC.put("ip", remoteAddr);
        }

        // generate a unique transfer ID so we can trace individual requests even if they don't come from
        // the nevis environment
        MDC.put("correlationId", UUID.randomUUID().toString());
    }

    private static void clearMdc() {
        MDC.clear();
    }
}
