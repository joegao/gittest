package com.yourpackage.aspect;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.yourpackage.config.LoggingProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final Set<String> sensitiveFields;
    private final ObjectMapper objectMapper;

    public LoggingAspect(LoggingProperties loggingProperties) {
        this.sensitiveFields = loggingProperties.getSensitiveFieldSet();

        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty print
    }

    @Pointcut("execution(* com.yourpackage..service..*(..))")
    public void serviceMethods() {}

    @Around("serviceMethods()")
    public Object logMethodCallAndReturn(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();

        // Build and mask arguments
        StringBuilder logBuilder = new StringBuilder("Calling method: ").append(methodName).append(" | Args: [");

        for (int i = 0; i < args.length; i++) {
            String paramName = (paramNames != null && i < paramNames.length) ? paramNames[i] : "arg" + i;
            Object arg = args[i];
            String masked = serializeAndMask(arg, paramName);

            logBuilder.append(paramName).append("=").append(masked);
            if (i < args.length - 1) {
                logBuilder.append(", ");
            }
        }

        logBuilder.append("]");
        logger.info(logBuilder.toString());

        // Proceed with method execution
        Object result = joinPoint.proceed();

        // Log masked return value
        logger.info("Return from {}: {}", methodName, serializeAndMask(result, "return"));

        return result;
    }

    private String serializeAndMask(Object obj, String contextName) {
        if (obj == null) return "null";

        try {
            JsonNode node = objectMapper.valueToTree(obj);
            maskJson(node);
            return objectMapper.writeValueAsString(node);
        } catch (Exception e) {
            return "[ERROR serializing: " + e.getMessage() + "]";
        }
    }

    private void maskJson(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objNode = (ObjectNode) node;
            Iterator<Map.Entry<String, JsonNode>> fields = objNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String fieldName = entry.getKey();
                JsonNode childNode = entry.getValue();

                if (shouldMask(fieldName) && childNode.isTextual()) {
                    objNode.put(fieldName, maskValue(childNode.asText()));
                } else {
                    maskJson(childNode);
                }
            }
        } else if (node.isArray()) {
            for (JsonNode item : node) {
                maskJson(item);
            }
        }
        // primitives are left untouched
    }

    private boolean shouldMask(String name) {
        return name != null && sensitiveFields.contains(name.toLowerCase());
    }

    private String maskValue(String value) {
        if (value == null) return null;
        int visibleCount = 4;
        if (value.length() <= visibleCount) return "****";
        int maskedLength = value.length() - visibleCount;
        return "*".repeat(maskedLength) + value.substring(maskedLength);
    }
}
