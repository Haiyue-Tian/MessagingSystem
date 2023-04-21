package com.haiyue.messaging.bean;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfigurator {

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${sns.topicArn}")
    private static String snsTopicArn;

    @Autowired
    private AWSCredentialsProvider awsCredentialsProvider;

    @Bean
    public AmazonCloudWatch amazonCloudWatch() {
        return AmazonCloudWatchClientBuilder.standard()
                .withCredentials(this.awsCredentialsProvider)
                .withRegion(region)
                .build();
    }

    @Bean
    public AmazonSNS amazonSNS() {
        AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
                .withCredentials(this.awsCredentialsProvider)
                .withRegion(region)
                .build();
        return snsClient;
    }

    public static void createSnsSubscription(AmazonSNS snsClient, String protocol, String endpoint) {
        SubscribeRequest subscribeRequest = new SubscribeRequest()
                .withTopicArn(snsTopicArn)
                .withProtocol(protocol)
                .withEndpoint(endpoint);

        SubscribeResult subscribeResult = snsClient.subscribe(subscribeRequest);

        System.out.println("Subscription ARN: " + subscribeResult.getSubscriptionArn());
    }
}
