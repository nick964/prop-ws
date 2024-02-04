package com.nick.propws.service;

import com.nick.propws.dto.UploadFileDto;
import org.springframework.web.multipart.MultipartFile;

public interface StoreService {

     UploadFileDto uploadFile(MultipartFile file);

     void deleteFile(String fileName);
}
