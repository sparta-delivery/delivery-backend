package com.sparta.deliverybackend.api.s3.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final AmazonS3Client S3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String MAX_FILE_SIZE;

    private long maxFileSize;

    @PostConstruct
    public void init() {
        log.debug("MAX_FILE_SIZE: {}", MAX_FILE_SIZE);
        maxFileSize = parseMaxFileSize(MAX_FILE_SIZE);
    }


    public String uploadImage(MultipartFile file){

        validateFile(file);

        String keyName = "uploads/"+file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try{
            S3Client.putObject(bucket, keyName, file.getInputStream(), metadata);
        }catch(IOException | AmazonS3Exception e){
            log.error("S3 이미지 업로드 중 오류 발생: {}", e.getMessage(), e);
            throw new InternalError("S3 이미지 업로드 중 오류 발생");
        }
        return String.format("https://%s.s3.amazonaws.com/%s", bucket, keyName);
    }

    public void deleteImage(@RequestBody String imgPath) {
        try {
            String objectKey = extractObjectKeyFromUrl(imgPath);
            deleteObjectFromS3(bucket, objectKey);
            log.debug("디버그 : 버킷에서 이미지 삭제 완료");
        } catch (MalformedURLException e) {
            log.error("이미지 삭제 시도 실패: 이미지 URL 파싱 과정에서 오류 발생", e);
        } catch (AmazonServiceException e) {
            log.error("버킷에서 이미지 삭제 중 오류 발생", e);
            throw new InternalError("S3 이미지 업로드 중 오류 발생");
        }
    }

    private String extractObjectKeyFromUrl(String imgPath) throws MalformedURLException {
        URL url = new URL(imgPath);
        return url.getPath().substring(1);
    }

    private void deleteObjectFromS3(String bucket, String objectKey) {
        S3Client.deleteObject(bucket, objectKey);
    }
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.debug("S3 파일 업로드 실패: 파일이 없음");
            throw new NotFoundException("파일이 없음");
        }
        if (file.getSize() > maxFileSize) {
            log.debug("S3 파일 업로드 실패: 파일 크기 초과");
            throw new IllegalArgumentException("파일 크기 초과");
        }
    }

    private long parseMaxFileSize(String size) {
        if (size.toLowerCase().endsWith("mb")) {
            return Long.parseLong(size.replaceAll("[^0-9]", "")) * 1024 * 1024;
        } else if (size.toLowerCase().endsWith("kb")) {
            return Long.parseLong(size.replaceAll("[^0-9]", "")) * 1024;
        } else if (size.toLowerCase().endsWith("gb")) {
            return Long.parseLong(size.replaceAll("[^0-9]", "")) * 1024 * 1024 * 1024;
        } else {
            return Long.parseLong(size);
        }
    }
}
