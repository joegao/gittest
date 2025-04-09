package com.yourpackage.aspect;

import com.yourpackage.config.LoggingProperties;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final Set<String> sensitiveFields;

    public LoggingAspect(LoggingProperties loggingProperties) {
        this.sensitiveFields = loggingProperties.getSensitiveFieldSet();
    }

    @Pointcut("execution(* com.yourpackage..service..*(..))")
    public void serviceMethods() {}

    @Before("serviceMethods()")
    public void logMethodArguments(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();

        logger.info("Calling method: {}", methodName);
        for (int i = 0; i < args.length; i++) {
            String paramName = (paramNames != null && i < paramNames.length) ? paramNames[i] : "arg" + i;
            Object arg = args[i];
            logger.info("Arg[{}] ({}): {}", i, paramName, processValue(arg, paramName));
        }
    }

    private String processValue(Object value, String contextName) {
        if (value == null) return "null";

        Class<?> clazz = value.getClass();

        if (value instanceof String) {
            return shouldMask(contextName) ? maskValue((String) value) : value.toString();
        }

        if (isPrimitiveOrWrapper(clazz)) {
            return value.toString();
        }

        if (clazz.isArray()) {
            int length = Array.getLength(value);
            List<String> items = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                items.add(processValue(Array.get(value, i), contextName));
            }
            return items.toString();
        }

        if (value instanceof Collection<?>) {
            Collection<?> collection = (Collection<?>) value;
            List<String> items = new ArrayList<>();
            for (Object item : collection) {
                items.add(processValue(item, contextName));
            }
            return items.toString();
        }

        if (value instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) value;
            Map<String, String> maskedMap = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String keyStr = String.valueOf(entry.getKey());
                Object val = entry.getValue();
                String maskedVal = shouldMask(keyStr) ? maskValue(String.valueOf(val)) : processValue(val, keyStr);
                maskedMap.put(keyStr, maskedVal);
            }
            return maskedMap.toString();
        }

        return maskObjectFields(value);
    }

    private String maskObjectFields(Object obj) {
        Class<?> clazz = obj.getClass();
        StringBuilder sb = new StringBuilder(clazz.getSimpleName()).append(" { ");

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                String fieldName = field.getName();

                sb.append(fieldName)
                        .append("=")
                        .append(shouldMask(fieldName) && value instanceof String
                                ? maskValue((String) value)
                                : processValue(value, fieldName))
                        .append(", ");
            } catch (IllegalAccessException e) {
                sb.append(field.getName()).append("=ERROR, ");
            }
        }

        sb.append(" }");
        return sb.toString();
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

    private boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz == String.class ||
                Number.class.isAssignableFrom(clazz) ||
                clazz == Boolean.class ||
                clazz == Character.class;
    }
}
