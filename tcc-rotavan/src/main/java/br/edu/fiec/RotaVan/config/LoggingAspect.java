package br.edu.fiec.RotaVan.config; // Pacote ajustado

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // Pointcut ajustado para o pacote dos seus controllers
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) && " +
            "within(br.edu.fiec.RotaVan.features..*)")
    public void applicationControllerPointcut() {
        // Método vazio
    }

    @Around("applicationControllerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info(">>>> Controller INÍCIO: {}.{}()", className, methodName);
        log.info(">>>> Argumentos: {}", Arrays.toString(args));

        try {
            Object result = joinPoint.proceed();
            log.info("<<<< Controller FIM: {}.{}() - SUCESSO", className, methodName);
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Exception Ilegal: {} em {}.{}()", Arrays.toString(args), className, methodName);
            throw e;
        }
    }
}