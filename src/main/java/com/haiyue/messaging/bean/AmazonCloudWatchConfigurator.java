package com.haiyue.messaging.bean;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonCloudWatchConfigurator {

    @Value("${cloud.aws.region.static}")
    private String region;

//    @Value("${cloud.aws.credentials.access-key}")
//    private String accessKey;
//
//    @Value("${cloud.aws.credentials.secret-key}")
//    private String secretKey;
//
//    @Bean
//    public AWSCredentialsProvider awsCredentialsProvider() {
//        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
//        return new AWSStaticCredentialsProvider(awsCredentials);
//    }
    @Autowired
    private AWSCredentialsProvider awsCredentialsProvider;

    @Bean
    public AmazonCloudWatch amazonCloudWatch() {
        return AmazonCloudWatchClientBuilder.standard()
                .withCredentials(this.awsCredentialsProvider)
                .withRegion(region)
                .build();
    }
}
