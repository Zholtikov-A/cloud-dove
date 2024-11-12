package com.zholtikov.cloud_dove.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.zholtikov.cloud_dove.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class S3ServiceImpl implements S3Service {

    Logger LOG = LogManager.getLogger(S3ServiceImpl.class);

    @Autowired
    AmazonS3 s3Client;

    @Value("${aws.bucket.name}")
    String bucketName;


    @Override
    public void putObjectIntoBucket(MultipartFile multipartFile) throws Exception {
        if (!validateBucket(bucketName)) {
            log.info("Invalid bucket No such bucket in storage!");
            throw new Exception("Invalid bucket");
        }
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(String.valueOf(multipartFile.getContentType()));
        try {
            s3Client.putObject(bucketName, multipartFile.getOriginalFilename(), multipartFile.getInputStream(), objectMetadata);
        } catch (AmazonServiceException e) {
            LOG.info(e.getErrorMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public S3Object downloadObjectFromBucket(String objectName) throws Exception {
        if (!validateBucket(bucketName)) {
            log.info("Invalid bucket No such bucket in storage!");
            throw new Exception("Invalid bucket");
        }
        return s3Client.getObject(bucketName, objectName);
    }

    @Override
    public byte[] convertS3ObjectToByteArray(S3Object s3Object) {
        byte[] content = null;
        final S3ObjectInputStream stream = s3Object.getObjectContent();
        try {
            content = IOUtils.toByteArray(stream);
            log.info("File downloaded successfully.");
            s3Object.close();
        } catch (final IOException ex) {
            log.info("IO Error Message = " + ex);
        }
        return content;
    }


    private boolean validateBucket(String bucketName) {
        List<Bucket> bucketList = s3Client.listBuckets();
        log.info("Bucket list:" + bucketList);
        return bucketList.stream().anyMatch(m -> bucketName.equals(m.getName()));
    }


}