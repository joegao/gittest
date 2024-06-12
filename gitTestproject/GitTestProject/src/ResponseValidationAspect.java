import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.validation.Validator;
import javax.validation.ConstraintViolation;
import java.util.Set;

@Aspect
@Component
public class ResponseValidationAspect {

    @Autowired
    private Validator validator;

    @Pointcut("execution(* com.example.service.ExternalService.*(..))")
    public void externalServiceMethods() {}

    @Around("externalServiceMethods()")
    public Object validateExternalResponse(ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed(); // Proceed with the original method execution

        // Perform validation if result is not null
        if (result != null) {
            Set<ConstraintViolation<?>> violations = validator.validate(result);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException("Validation errors occurred", violations);
            }
        }

        return result;
    }
}
