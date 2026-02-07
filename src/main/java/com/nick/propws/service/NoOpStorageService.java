package com.nick.propws.service;

import com.nick.propws.dto.UploadFileDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
@Profile("prod")
public class NoOpStorageService implements StoreService {

    @Override
    public UploadFileDto uploadFile(MultipartFile file) {
        log.warn("File upload is disabled in prod (no S3 configured). File: {}", file.getOriginalFilename());
        UploadFileDto dto = new UploadFileDto();
        dto.setObjectName("no-op");
        dto.setIconUrl("");
        return dto;
    }

    @Override
    public void deleteFile(String fileName) {
        log.warn("File deletion is disabled in prod (no S3 configured). File: {}", fileName);
    }
}
