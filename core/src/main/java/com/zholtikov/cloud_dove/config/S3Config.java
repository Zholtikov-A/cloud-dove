package com.zholtikov.cloud_dove.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${aws.access.key}")
    String awsAccessKey;

    @Value("${aws.secret.key}")
    String awsSecretKey;

    @Value("${aws.service.endpoint}")
    String awsServiceEndpoint;

    @Value("${aws.signing.region}")
    String awsSigningRegion;


    @Bean
    public AmazonS3 getAmazonS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                        new com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration(
                                awsServiceEndpoint,
                                awsSigningRegion
                        ))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}