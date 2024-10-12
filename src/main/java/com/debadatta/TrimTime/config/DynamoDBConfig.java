package com.debadatta.TrimTime.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;

@Configuration
public class DynamoDBConfig {

    @Value("${aws.dynamodb.endpoint}")
    private String dynamodbEndPoint;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.dynamodb.accesskey}")
    private String dynamodbAccessKey;

    @Value("${aws.dynamodb.secretkey}")
    private String dynamodbSecretKey;

    @Value("${aws.dynamodb.sessionkey}")
    private String dynamodbSessionKey;

    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(buildAmazonDynamoDB());
    }

    private AmazonDynamoDB buildAmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(dynamodbEndPoint, awsRegion))
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicSessionCredentials(dynamodbAccessKey, dynamodbSecretKey, dynamodbSessionKey)))
                .build();
    }
}
