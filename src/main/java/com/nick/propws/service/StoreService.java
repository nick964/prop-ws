package com.nick.propws.service;

import org.springframework.web.multipart.MultipartFile;

public interface StoreService {

     String uploadFile(MultipartFile file);
}
