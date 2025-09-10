package com.ntp.be.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileUploadService {

    @Autowired
    private Cloudinary cloudinary;

    public Map uploadFile(MultipartFile file, String fileName) {
        try {
            return cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("public_id", fileName, "folder", "uploads"));
        } catch (IOException e) {
            throw new RuntimeException("Upload file thất bại", e);
        }
    }
}