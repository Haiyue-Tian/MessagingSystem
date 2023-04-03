package com.haiyue.messaging.scheduler;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import com.sun.management.OperatingSystemMXBean;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.List;

import static com.haiyue.messaging.aspect.LoginAspect.METRIC_NAMESPACE;

@Component
@Log4j2
public class ResourceUtilizationMonitor {
    @Autowired
    private AmazonCloudWatch amazonCloudWatch;
    @Scheduled(fixedDelay = 5000)
    public void monitor(){
        String className = this.getClass().getName();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        OperatingSystemMXBean operatingSystemMXBean =
                (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        double cpuLoad = operatingSystemMXBean.getProcessCpuLoad();
        double freeMemory = operatingSystemMXBean.getFreeMemorySize() / 1024.0 / 1024.0;

        log.info("Process cpu load: {}", cpuLoad);
        log.info("Free memory: {} MB", freeMemory);

        PutMetricDataRequest putMonitorMetricDataRequest = new PutMetricDataRequest();
        putMonitorMetricDataRequest.setNamespace(METRIC_NAMESPACE);

        MetricDatum cpuLoadMetricDatum = new MetricDatum();
        cpuLoadMetricDatum.setTimestamp(new Date());
        cpuLoadMetricDatum.setDimensions(List.of(
                new Dimension().withName("Module").withValue(className),
                new Dimension().withName("API").withValue(methodName)));
        cpuLoadMetricDatum.setMetricName("Process CPU Load");
        cpuLoadMetricDatum.setValue(cpuLoad);
        cpuLoadMetricDatum.setUnit(StandardUnit.Percent);

        MetricDatum freeMemoryMetricDatum = new MetricDatum();
        freeMemoryMetricDatum.setTimestamp(new Date());
        freeMemoryMetricDatum.setDimensions(List.of(
                new Dimension().withName("Module").withValue(className),
                new Dimension().withName("API").withValue(methodName)));
        freeMemoryMetricDatum.setMetricName("Free memory");
        freeMemoryMetricDatum.setValue(freeMemory);
        freeMemoryMetricDatum.setUnit(StandardUnit.Megabytes);

        putMonitorMetricDataRequest.setMetricData(List.of(cpuLoadMetricDatum, freeMemoryMetricDatum));

        this.amazonCloudWatch.putMetricData(putMonitorMetricDataRequest);
    }
}
