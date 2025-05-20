package com.ticketjo.ticketjo_backend.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileStorageService {
    String storeFile(MultipartFile file);
    Path loadFile(String fileName);
    Resource loadAsResource(String fileName);
}