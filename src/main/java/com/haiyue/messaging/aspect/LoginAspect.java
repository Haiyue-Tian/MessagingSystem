package com.haiyue.messaging.aspect;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Aspect
@Log4j2
public class LoginAspect {

    // log controller invocations
    public static final String METRIC_NAMESPACE = "MessagingService";

    @Autowired
    private AmazonCloudWatch amazonCloudWatch;

    @Around("execution(* com.haiyue.messaging.controller.*.*(..))")
    public Object log(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //class.method exception latency
        Date startTime = new Date();
        boolean isExceptionThrown = false;
        String className = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        try {
            return proceedingJoinPoint.proceed();
        } catch (Throwable throwable){
            isExceptionThrown = true;
            throw throwable;
        } finally {
            long latencyInMs = new Date().getTime() - startTime.getTime();
            log.info("Executed {}.{}, latency: {} ms, exception thrown: {}",
                    className, methodName, latencyInMs, isExceptionThrown);

            PutMetricDataRequest putMetricDataRequest = new PutMetricDataRequest();
            putMetricDataRequest.setNamespace(METRIC_NAMESPACE);

            MetricDatum countMetricDatum = new MetricDatum();
            countMetricDatum.setTimestamp(new Date());
            countMetricDatum.setDimensions(List.of(
                    new Dimension().withName("Module").withValue(className),
                    new Dimension().withName("API").withValue(methodName)));
            countMetricDatum.setMetricName("Count");
            countMetricDatum.setValue(1.0);
            countMetricDatum.setUnit(StandardUnit.Count);

            MetricDatum timeMetricDatum = new MetricDatum();
            timeMetricDatum.setTimestamp(new Date());
            timeMetricDatum.setDimensions(List.of(
                    new Dimension().withName("Module").withValue(className),
                    new Dimension().withName("API").withValue(methodName)));
            timeMetricDatum.setMetricName("Time");
            timeMetricDatum.setValue(latencyInMs * 1.0);
            timeMetricDatum.setUnit(StandardUnit.Milliseconds);

            MetricDatum errorMetricDatum = new MetricDatum();
            errorMetricDatum.setTimestamp(new Date());
            errorMetricDatum.setDimensions(List.of(
                    new Dimension().withName("Module").withValue(className),
                    new Dimension().withName("API").withValue(methodName)));
            errorMetricDatum.setMetricName("Error");
            errorMetricDatum.setValue(isExceptionThrown? 1.0: 0.0);
            errorMetricDatum.setUnit(StandardUnit.Count);

            putMetricDataRequest.setMetricData(List.of(countMetricDatum, timeMetricDatum, errorMetricDatum));

            this.amazonCloudWatch.putMetricData(putMetricDataRequest);
        }
    }
}
