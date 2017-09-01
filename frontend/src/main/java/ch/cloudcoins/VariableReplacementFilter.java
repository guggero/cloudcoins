package ch.cloudcoins;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableReplacementFilter implements Filter {

    private static final Pattern ENV_VARIABLE_PATTERN = Pattern.compile("##([A-Za-z0-9_]+)##");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // nothing to do here
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HtmlResponseWrapper capturingResponseWrapper = new HtmlResponseWrapper((HttpServletResponse) response);

        chain.doFilter(request, capturingResponseWrapper);
        String contentType = response.getContentType();

        if (contentType != null && (contentType.contains("text/html") || contentType.contains("text/plain"))) {

            String content = capturingResponseWrapper.getCaptureAsString();
            if (!content.trim().isEmpty()) {

                // replace variables here
                Matcher matcher = ENV_VARIABLE_PATTERN.matcher(content);
                while (matcher.find()) {
                    String value = System.getenv(matcher.group(1));
                    if (value == null) {
                        value = "";
                    }
                    Pattern replaceExpression = Pattern.compile(Pattern.quote(matcher.group(0)));
                    content = replaceExpression.matcher(content).replaceAll(value);
                }
            }

            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            ((HttpServletResponse) response).setHeader("content-length", String.valueOf(content.length()));
            response.getOutputStream().write(bytes);
        }

    }

    @Override
    public void destroy() {
        // nothing to do here
    }
}
