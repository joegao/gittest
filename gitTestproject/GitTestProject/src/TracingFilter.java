package com.example.tracing.filter;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.propagation.Propagator;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class TracingFilter implements Filter {

    private final Tracer tracer;
    private final Propagator propagator;

    public TracingFilter(Tracer tracer, Propagator propagator) {
        this.tracer = tracer;
        this.propagator = propagator;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Step 1: Extract traceId and spanId from incoming request headers
        String receivedTraceId = httpRequest.getHeader("x-b3-traceid");
        String receivedSpanId = httpRequest.getHeader("x-b3-spanid");

        // Step 2: Extract Micrometer's default trace information
        Propagator.Getter<HttpServletRequest> getter = HttpServletRequest::getHeader;
        Span extractedSpan = propagator.extract(httpRequest, getter).start();

        String extractedTraceId = extractedSpan.context().traceId();
        String extractedSpanId = extractedSpan.context().spanId();

        System.out.println("Extracted traceId from Micrometer: " + extractedTraceId);
        System.out.println("Received traceId from upstream: " + receivedTraceId);

        // Step 3: Use received traceId if available, otherwise use extracted one
        String finalTraceId = (receivedTraceId != null) ? receivedTraceId : extractedTraceId;

        // Step 4: Decide whether to generate a new spanId
        boolean generateNewSpanId = (receivedSpanId == null);
        String finalSpanId = generateNewSpanId ? tracer.nextSpan().context().spanId() : receivedSpanId;

        System.out.println("Final traceId: " + finalTraceId);
        System.out.println("Final spanId: " + finalSpanId + (generateNewSpanId ? " (New span generated)" : " (Using existing span)"));

        // Step 5: Create a new TraceContext with the final traceId and spanId
        TraceContext traceContext = extractedSpan.context().toBuilder()
                .traceId(finalTraceId)
                .spanId(finalSpanId)
                .build();

        // Step 6: Create a new span with the correct TraceContext
        Span finalSpan = tracer.nextSpan().name("incoming-request").setTraceContext(traceContext).start();

        // Step 7: Store the correct traceId and spanId in MDC for logging
        try (Tracer.SpanInScope ws = tracer.withSpan(finalSpan)) {
            MDC.put("b3-traceid", finalTraceId);
            MDC.put("b3-spanid", finalSpanId);

            chain.doFilter(request, response);
        } finally {
            MDC.clear();
            finalSpan.end();
        }
    }
}
