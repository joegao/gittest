import org.springframework.stereotype.Component;

@Component
public class TraceContext {
    private static final ThreadLocal<String> traceId = new ThreadLocal<>();
    private static final ThreadLocal<String> spanId = new ThreadLocal<>();

    public static void setTraceId(String id) {
        traceId.set(id);
    }

    public static String getTraceId() {
        return traceId.get();
    }

    public static void setSpanId(String id) {
        spanId.set(id);
    }

    public static String getSpanId() {
        return spanId.get();
    }

    public static void clear() {
        traceId.remove();
        spanId.remove();
    }
}
