package com.zholtikov.cloud_dove.service;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

    void putObjectIntoBucket(MultipartFile multipartFile) throws Exception;

    S3Object downloadObjectFromBucket(String objectName) throws Exception;

    byte[] convertS3ObjectToByteArray(S3Object s3Object);

}