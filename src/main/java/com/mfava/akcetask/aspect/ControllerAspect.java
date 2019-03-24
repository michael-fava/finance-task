package com.mfava.akcetask.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfava.akcetask.model.AuditLog;
import com.mfava.akcetask.repo.AuditLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@Aspect
@Component
public class ControllerAspect {

    public ControllerAspect(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    private AuditLogRepository auditLogRepository;

    @Around("execution(* com.mfava.akcetask.controller..*(..))")
    public Object logAfterRequest(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Annotation mappingAnnotation = method.getAnnotation(RequestMapping.class);
        RequestMethod httpMethod = null;
        String url = null;



        if (mappingAnnotation == null) {
            mappingAnnotation = method.getAnnotation(GetMapping.class);
            if (mappingAnnotation == null) {
                mappingAnnotation = method.getAnnotation(PostMapping.class);
                if (mappingAnnotation == null) {
                    mappingAnnotation = method.getAnnotation(PutMapping.class);
                    if (mappingAnnotation == null) {
                        mappingAnnotation = method.getAnnotation(DeleteMapping.class);
                        if (mappingAnnotation == null) {
                            httpMethod = RequestMethod.PATCH;
                            url = method.getAnnotation(PatchMapping.class).value()[0];
                        }else {
                            httpMethod = RequestMethod.DELETE;
                            url = method.getAnnotation(DeleteMapping.class).value()[0];
                        }
                    }else{
                        httpMethod = RequestMethod.PUT;
                        url = method.getAnnotation(PutMapping.class).value()[0];
                    }
                }else{
                    httpMethod = RequestMethod.POST;
                    url = method.getAnnotation(PostMapping.class).value()[0];
                }
            }else{
                httpMethod = RequestMethod.GET;
                url = method.getAnnotation(GetMapping.class).value()[0];
            }
        }

        if(isBlank(url)){
            url  = method.getAnnotation(RequestMapping.class).value()[0];
        }
        String requestMethod = httpMethod == null ? method.getAnnotation(RequestMapping.class).method()[0].toString(): httpMethod.toString();

        String request = processRequestResponse(joinPoint.getArgs());
        String response;
        try {
            Object object = joinPoint.proceed();
            response = getResponse(object);
            auditLogRepository.save(AuditLog.builder()
                    .url(url)
                    .requestMethod(requestMethod)
                    .request(request)
                    .response(response)
                    .build());

            return object;
        }catch (Exception x){
            response = getResponse(x.getMessage());

            auditLogRepository.save(AuditLog.builder()
                    .url(url)
                    .requestMethod(requestMethod)
                    .request(request)
                    .response(response)
                    .build());
            throw  x;
        }
    }


    private String processRequestResponse(Object[] object) {
        StringBuilder sbRequest = new StringBuilder();
        if (object != null) {
            Arrays.asList(object).forEach(request -> {
                try {
                    sbRequest.append(objectMapper.writeValueAsString(request) + " ");
                } catch (JsonProcessingException e) {
                        log.equals(e.getMessage());
                }
            });
        }
        return sbRequest.toString();
    }

    private String getResponse(Object object) throws JsonProcessingException {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception ex) {
            return (ex.getMessage());
        }
    }

}
