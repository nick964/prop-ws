package com.nick.propws.service;


import com.nick.propws.dto.UploadFileDto;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.ServerSideEncryption;

import java.io.IOException;

@Log4j2
@Service
public class StorageServiceImpl implements StoreService{

    Logger logger = LoggerFactory.getLogger(StorageServiceImpl.class);

    @Value("${app.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final S3Client s3Client;

    public StorageServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }


    @Override
    public UploadFileDto uploadFile(MultipartFile file) {
        UploadFileDto fileDto = new UploadFileDto();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        log.info("Running uploadFile - " + fileName);
        fileDto.setObjectName(fileName);
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .serverSideEncryption(ServerSideEncryption.AES256)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            fileDto.setIconUrl("https://" + bucketName + ".s3.amazonaws.com/" + fileName);
            return fileDto;
        } catch (IOException e) {
            log.error("Error while uploading file - " + fileName, e);
            log.error(e.getMessage());
            throw new RuntimeException("Error during file upload", e);
        } catch (Exception e) {
            log.error("Error while uploading file - Unhandled - " + fileName, e);
            log.error(e.getMessage());
            throw new RuntimeException("Error during file upload", e);
        }
    }

    @Override
    public void deleteFile(String objectName) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectName).build();
            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            logger.error("Error deleting object with name: " + objectName);
            logger.error("Deletion object error: " + e.getMessage());
        }
    }

}
