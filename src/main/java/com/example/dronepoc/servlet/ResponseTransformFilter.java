package com.example.dronepoc.servlet;

import com.example.dronepoc.utils.ByteArrayPrinter;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.regex.Pattern;

@WebFilter(urlPatterns = "/*", dispatcherTypes = {DispatcherType.REQUEST})
public class ResponseTransformFilter implements Filter {
    private static final Pattern URL_PATTERN_DRONE_LIST = Pattern.compile("/api/drones/?");
    private static final Pattern URL_PATTERN_DRONE_DETAIL = Pattern.compile("/api/drones/\\d+/?");
    private static final Pattern URL_PATTERN_INSTRUCTIONS = Pattern.compile("/api/drones/instructions/?");
    private static final Pattern URL_PATTERN_INSTRUCTION_DETAIL = Pattern.compile("/api/drones/instructions/\\d+/?");
    private static final Pattern URL_PATTERN_DRONE_SORTIES = Pattern.compile("/api/drones/\\d+/sorties/?");

    private static final String RESPONSE_WITH_STATUS_AND_DATA = "{\"status\":\"%d\",\"data\":%s}";
    private static final String RESPONSE_WITH_DATA = "{\"data\":%s}";

    private String transformResponse(String requestUrl, String requestMethod, String responseData, int responseStatus) {
        URI uri = URI.create(requestUrl);
        String pathSegment = uri.getPath();
        if (pathSegment == null) {
            return responseData; // Do not transform
        } else if (requestMethod.equals("GET") && URL_PATTERN_DRONE_LIST.matcher(pathSegment).matches()) {
            return String.format(RESPONSE_WITH_STATUS_AND_DATA, responseStatus, responseData);
        } else if (requestMethod.equals("GET") && URL_PATTERN_DRONE_DETAIL.matcher(pathSegment).matches()) {
            return String.format(RESPONSE_WITH_DATA, responseData);
        } else if (requestMethod.equals("POST") && URL_PATTERN_INSTRUCTIONS.matcher(pathSegment).matches()) {
            return String.format(RESPONSE_WITH_STATUS_AND_DATA, responseStatus, responseData);
        } else if (requestMethod.equals("GET") && URL_PATTERN_INSTRUCTION_DETAIL.matcher(pathSegment).matches()) {
            return String.format(RESPONSE_WITH_DATA, responseData);
        } else if (requestMethod.equals("PUT") && URL_PATTERN_DRONE_SORTIES.matcher(pathSegment).matches()) {
            return String.format(RESPONSE_WITH_STATUS_AND_DATA, responseStatus, responseData);
        }
        // Do not transform
        return responseData;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        ByteArrayPrinter pw = new ByteArrayPrinter();
        HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper(response) {
            @Override
            public PrintWriter getWriter() throws IOException {
                return pw.getWriter();
            }

            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return pw.getStream();
            }
        };
        chain.doFilter(servletRequest, wrappedResponse);
        String responseData = new String(pw.toByteArray());
        int responseStatus = wrappedResponse.getStatus();

        if (responseStatus == HttpStatus.OK.value()) {
            // Only apply transformation on success responses
            responseData = transformResponse(request.getRequestURI(), request.getMethod(), responseData, responseStatus);
        }
        response.getWriter().write(responseData);
    }
}
